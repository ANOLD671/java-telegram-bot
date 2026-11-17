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
public class ApiFootballProvider implements FootballDataProvider{
    private final String apiKey;
    private HttpClient httpClient;
    private static final String BASE_URL = "https://api.thestatsapi.com/api/football";
    private static final String RPL_COMP_ID = "comp_5824";

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
    * */
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
    * @param endpoint The URL part (e.g., "/matches").
    * @return The raw JSON text from the API.
    * @throws DataProviderException If the API fails or network is down.
    * */
    private String fetchJson(String endpoint) throws DataProviderException {
        try {
            String fullUrl = BASE_URL + endpoint;
            URI uri = URI.create(fullUrl);

            HttpRequest.Builder builder = HttpRequest.newBuilder();
            builder.uri(uri);
            builder.header("Authorization", "Bearer " + apiKey);
            builder.GET(); // Tell it this is a GET request
            HttpRequest request = builder.build();

            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
            HttpResponse<String> response = httpClient.send(request, handler);

            int statusCode = response.statusCode();
            if (statusCode != 200) {
                String errorMessage = "API returned status: " + statusCode;
                throw new DataProviderException(errorMessage);
            }
            String responseBody = response.body();
            return responseBody;
        } catch (DataProviderException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = "Network error while calling API";
            throw new DataProviderException(errorMessage, e);
        }
    }

/**
*takes one match from the API (a JSON object) and turns it into your Match Java object.
*gets the English name, looks it up in our dictionary (EN_TO_RU), and gets the Russian name
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

        Match match = new Match(homeRu, awayRu, date, homeScore, awayScore);
        return match;
    }

/**
* It opens the JSON text and finds the "data" array.
*It goes through that array one item at a time.
* It hands each item to the toMatch() method (which we simplified in the previous step), and puts the result into a normal Java List<Match>
* */
    private List<Match> parseMatches(String json) {

        JsonElement rootElement = JsonParser.parseString(json);
        JsonObject rootObject = rootElement.getAsJsonObject();

        JsonElement dataElement = rootObject.get("data");
        JsonArray dataArray = dataElement.getAsJsonArray();

        List<Match> matches = new ArrayList<>();

        for (int i = 0; i < dataArray.size(); i++) {

            JsonElement oneMatchElement = dataArray.get(i);

            JsonObject oneMatchObject = oneMatchElement.getAsJsonObject();

            Match match = toMatch(oneMatchObject);

            matches.add(match);
        }
        return matches;
    }


/**
* status=finished: Tells the API "give me matches that already happened."
* */
    @Override
    public List<Match> getPastMatches(Integer limit) throws DataProviderException {
        String json = fetchJson("/matches?competition_id=" + RPL_COMP_ID + "&status=finished&per_page=" + limit);
        List<Match> matches = parseMatches(json);
        Collections.reverse(matches); // Spec requires oldest first
        return matches;
    }
/**
* status=scheduled means "matches that haven't happened yet
* */
    @Override
    public List<Match> getUpcomingMatches(Integer limit) throws DataProviderException {
        String json = fetchJson("/matches?competition_id=" + RPL_COMP_ID + "&status=scheduled&per_page=" + limit);
        return parseMatches(json);
    }
/**
* per_page=50 ,we need to filter for one specific team .
*  So we ask for a large batch (50), find the ones where a team played, and take the first 4.
*It checks if the teamName,matches the home or away team in the match.
*
* */
    @Override
    public List<Match> getPastMatchesForFT(Integer limit, String teamName) throws DataProviderException {
        String json = fetchJson("/matches?competition_id=" + RPL_COMP_ID + "&status=finished&per_page=50");
        List<Match> allPast = parseMatches(json);
        Collections.reverse(allPast);

        List<Match> result = new ArrayList<>();
        for (Match m : allPast) {
            if (m.getHomeTeam().equals(teamName) || m.getAwayTeam().equals(teamName)) {
                result.add(m);
                if (result.size() == limit) break;
            }
        }
        return result;
    }
/**
* it fetches scheduled matches instead of finished ones
* */
    @Override
    public List<Match> getUpcomingMatchesForFT(Integer limit, String teamName) throws DataProviderException {
        String json = fetchJson("/matches?competition_id=" + RPL_COMP_ID + "&status=scheduled&per_page=50");
        List<Match> allUpcoming = parseMatches(json);

        List<Match> result = new ArrayList<>();
        for (Match m : allUpcoming) {
            if (m.getHomeTeam().equals(teamName) || m.getAwayTeam().equals(teamName)) {
                result.add(m);
                if (result.size() == limit) break;
            }
        }
        return result;
    }


}
