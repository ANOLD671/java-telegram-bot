import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;


/**
 * Fetches football match data from TheStatsAPI.
 * Translates English team names from the API into Russian names.
 */
public class ApiFootballProvider implements FootballDataProvider {
    private final String apiKey;
    private final HttpClient httpClient;
    private final String BASE_URL = "https://api.thestatsapi.com/api/football";
    private final String RPL_COMP_ID = "comp_5824";

    // Queues to hold matches in memory so we only make 1 API request
    private final Queue<Match> pastMatchesQueue = new LinkedList<>();
    private final Queue<Match> upcomingMatchesQueue = new LinkedList<>();
    private final Queue<Match> pastFTQueue = new LinkedList<>();
    private final Queue<Match> upcomingFTQueue = new LinkedList<>();

    /**
     * Creates the API provider.
     *
     * @param apiKey The secret API key.
     */
    public ApiFootballProvider(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public String getApiKey() {
        return apiKey;
    }

    /**
     * Translate API English names to Russian names
     */
    private final Map<String, String> EN_TO_RU = Map.ofEntries(
            Map.entry("Akhmat Grozny", "Ахмат"),
            Map.entry("Akhmat", "Ахмат"),

            Map.entry("Akron Togliatti", "Акрон"),
            Map.entry("Akron", "Акрон"),

            Map.entry("Baltika Kaliningrad", "Балтика"),
            Map.entry("Baltika", "Балтика"),

            Map.entry("CSKA Moscow", "ЦСКА"),
            Map.entry("CSKA", "ЦСКА"),

            Map.entry("Dinamo Makhachkala", "Динамо Махачкала"),

            Map.entry("Dynamo Moscow", "Динамо"),
            Map.entry("Dynamo", "Динамо"),

            Map.entry("FC Rostov", "Ростов"),
            Map.entry("Rostov", "Ростов"),

            Map.entry("FC Spartak Moscow", "Спартак"),
            Map.entry("Spartak", "Спартак"),

            Map.entry("FK Krasnodar", "Краснодар"),
            Map.entry("Krasnodar", "Краснодар"),

            Map.entry("Fakel Voronezh", "Факел"),
            Map.entry("Fakel", "Факел"),

            Map.entry("Krylya Sovetov Samara", "Крылья Советов"),
            Map.entry("Krylya Sovetov", "Крылья Советов"),

            Map.entry("Lokomotiv Moscow", "Локомотив"),
            Map.entry("Lokomotiv", "Локомотив"),

            Map.entry("Orenburg", "Оренбург"),

            Map.entry("Rodina Moscow", "Родина"),

            Map.entry("Rubin Kazan", "Рубин"),
            Map.entry("Rubin", "Рубин"),

            Map.entry("Zenit St. Petersburg", "Зенит"),
            Map.entry("Zenit", "Зенит")
    );

    /**
     * It sends the request. If the API says "Success" (200), it returns the raw JSON text.
     * If anything goes wrong (no internet, wrong key, API down), it throws DataProviderException
     * */
    private String fetchJson(String endpoint) throws DataProviderException {
        try {
            String fullUrl = BASE_URL + endpoint;
            URI uri = URI.create(fullUrl);

            HttpRequest.Builder builder = HttpRequest.newBuilder();
            builder.uri(uri);
            builder.header("Authorization", "Bearer " + apiKey);
            builder.GET();
            HttpRequest request = builder.build();

            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(request, handler);

            int statusCode = response.statusCode();
            if (statusCode != 200) {
                String errorMessage = "API returned status: " + statusCode;
                throw new DataProviderException(errorMessage);
            }
            return response.body(); // Simplified to fix redundant local variable warning
        } catch (DataProviderException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = "Network error while calling API";
            throw new DataProviderException(errorMessage, e);
        }
    }

    /**
     * takes one match from the API (a JSON object) and turns it into your Match Java object.
     * gets the English name, looks it up in our dictionary (EN_TO_RU), and gets the Russian name
     * */
    private Match toMatch(JsonObject node) {
        JsonElement idElement = node.get("id");
        String id = idElement.getAsString();

        JsonElement dateElement = node.get("utc_date");
        String dateStr = dateElement.getAsString();
        String cleanDateStr = dateStr.substring(0, 19);
        LocalDateTime date = LocalDateTime.parse(cleanDateStr);

        JsonObject homeTeamObj = node.getAsJsonObject("home_team");
        JsonElement homeNameElement = homeTeamObj.get("name");
        String homeEn = homeNameElement.getAsString();

        JsonObject awayTeamObj = node.getAsJsonObject("away_team");
        JsonElement awayNameElement = awayTeamObj.get("name");
        String awayEn = awayNameElement.getAsString();

        String homeRu = homeEn;
        if (EN_TO_RU.containsKey(homeEn)) {
            homeRu = EN_TO_RU.get(homeEn);
        }
        String awayRu = awayEn;
        if (EN_TO_RU.containsKey(awayEn)) {
            awayRu = EN_TO_RU.get(awayEn);
        }

        JsonObject score = node.getAsJsonObject("score");

        Integer homeScore = null;
        JsonElement homeScoreElement = score.get("home");
        if (homeScoreElement != null && !homeScoreElement.isJsonNull()) {
            homeScore = homeScoreElement.getAsInt();
        }

        Integer awayScore = null;
        JsonElement awayScoreElement = score.get("away");
        if (awayScoreElement != null && !awayScoreElement.isJsonNull()) {
            awayScore = awayScoreElement.getAsInt();
        }

        return new Match(id, homeRu, awayRu, date, homeScore, awayScore); // Simplified to fix redundant local variable warning
    }

    /**
     * It opens the JSON text and finds the "data" array.
     * It goes through that array one item at a time.
     * */
    private void loadMatchesToQueue(String json, Queue<Match> queue) {
        JsonElement rootElement = JsonParser.parseString(json);
        JsonObject rootObject = rootElement.getAsJsonObject();

        JsonElement dataElement = rootObject.get("data");
        JsonArray dataArray = dataElement.getAsJsonArray();

        for (int i = 0; i < dataArray.size(); i++) {
            JsonElement oneMatchElement = dataArray.get(i);
            JsonObject oneMatchObject = oneMatchElement.getAsJsonObject();
            Match match = toMatch(oneMatchObject);
            queue.add(match);
        }
    }

    /**
     * status=finished: Tells the API "give me matches that already happened."
     * */
    @Override
    public Match getPastMatch() throws DataProviderException {
        if (pastMatchesQueue.isEmpty()) {
            String json = fetchJson("/matches?competition_id=" + RPL_COMP_ID + "&status=finished&per_page=10");
            loadMatchesToQueue(json, pastMatchesQueue);

            // Spec requires oldest first
            List<Match> list = new ArrayList<>(pastMatchesQueue);
            Collections.reverse(list);
            pastMatchesQueue.clear();
            pastMatchesQueue.addAll(list);
        }
        return pastMatchesQueue.poll();
    }

    /**
     * status=scheduled means "matches that haven't happened yet
     * */
    @Override
    public Match getUpcomingMatch() throws DataProviderException {
        if (upcomingMatchesQueue.isEmpty()) {
            String json = fetchJson("/matches?competition_id=" + RPL_COMP_ID + "&status=scheduled&per_page=10");
            loadMatchesToQueue(json, upcomingMatchesQueue);
        }
        return upcomingMatchesQueue.poll();
    }

    /**
     * per_page=50 ,we need to filter for one specific team.
     * */
    @Override
    public Match getPastMatchForFT(String teamName) throws DataProviderException {
        if (pastFTQueue.isEmpty()) {
            String json = fetchJson("/matches?competition_id=" + RPL_COMP_ID + "&status=finished&per_page=50");
            loadMatchesToQueue(json, pastFTQueue);

            List<Match> list = new ArrayList<>(pastFTQueue);
            Collections.reverse(list);
            pastFTQueue.clear();
            pastFTQueue.addAll(list);
        }
        return pastFTQueue.poll();
    }

    /**
     * it fetches scheduled matches instead of finished ones
     * */
    @Override
    public Match getUpcomingMatchForFT(String teamName) throws DataProviderException {
        if (upcomingFTQueue.isEmpty()) {
            String json = fetchJson("/matches?competition_id=" + RPL_COMP_ID + "&status=scheduled&per_page=50");
            loadMatchesToQueue(json, upcomingFTQueue);
        }
        return upcomingFTQueue.poll();
    }
}