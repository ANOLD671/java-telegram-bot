import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;

/**
 * Unit tests for the ApiFootballProvider class.
 */
public class ApiFootballProviderTest {

    private final String PAST_MATCHES_JSON = """
            {
              "data": [
                {
                  "id": "101",
                  "utc_date": "2026-03-12T20:30:00Z",
                  "home_team": { "name": "Dynamo Moscow" },
                  "away_team": { "name": "Zenit St. Petersburg" },
                  "score": { "home": 2, "away": 1 }
                },
                {
                  "id": "102",
                  "utc_date": "2026-03-10T19:00:00Z",
                  "home_team": { "name": "CSKA Moscow" },
                  "away_team": { "name": "FC Spartak Moscow" },
                  "score": { "home": 1, "away": 1 }
                },
                {
                  "id": "103",
                  "utc_date": "2026-03-05T17:45:00Z",
                  "home_team": { "name": "Fakel Voronezh" },
                  "away_team": { "name": "Dynamo Moscow" },
                  "score": { "home": 0, "away": 3 }
                }
              ]
            }
            """;

    private final String UPCOMING_MATCHES_JSON = """
            {
              "data": [
                {
                  "id": "201",
                  "utc_date": "2026-10-01T17:45:00Z",
                  "home_team": { "name": "FK Krasnodar" },
                  "away_team": { "name": "Fakel Voronezh" },
                  "score": { "home": null, "away": null }
                },
                {
                  "id": "202",
                  "utc_date": "2026-10-05T20:30:00Z",
                  "home_team": { "name": "Lokomotiv Moscow" },
                  "away_team": { "name": "Dynamo Moscow" },
                  "score": { "home": null, "away": null }
                }
              ]
            }
            """;

    /**
     * Tests parsing, team name translation, chronological reversing, and queue caching for past matches.
     */
    @Test
    public void testGetPastMatch_ParsesTranslatesReversesAndCaches() throws DataProviderException {
        AtomicInteger apiCallsCount = new AtomicInteger(0);

        ApiFootballProvider provider = new ApiFootballProvider("fake-key") {
            @Override
            protected String fetchJson(String endpoint) {
                apiCallsCount.incrementAndGet();
                return PAST_MATCHES_JSON;
            }
        };

        Match first = provider.getPastMatch();
        Assertions.assertNotNull(first);
        Assertions.assertEquals("Факел", first.getHomeTeam());
        Assertions.assertEquals("Динамо", first.getAwayTeam());
        Assertions.assertEquals(LocalDateTime.of(2026, 3, 5, 17, 45), first.getDate());
        Assertions.assertEquals(0, first.getHomeScore());
        Assertions.assertEquals(3, first.getAwayScore());

        Match second = provider.getPastMatch();
        Assertions.assertNotNull(second);
        Assertions.assertEquals("ЦСКА", second.getHomeTeam());
        Assertions.assertEquals("Спартак", second.getAwayTeam());

        Match third = provider.getPastMatch();
        Assertions.assertNotNull(third);
        Assertions.assertEquals("Динамо", third.getHomeTeam());
        Assertions.assertEquals("Зенит", third.getAwayTeam());

        Assertions.assertEquals(1, apiCallsCount.get(), "Only one API request should be made");
    }

    /**
     * Tests parsing of null scores, order preservation, and queue caching for upcoming matches.
     */
    @Test
    public void testGetUpcomingMatch_ParsesNullScoresAndPreservesOrder() throws DataProviderException {
        AtomicInteger apiCallsCount = new AtomicInteger(0);

        ApiFootballProvider provider = new ApiFootballProvider("fake-key") {
            @Override
            protected String fetchJson(String endpoint) {
                apiCallsCount.incrementAndGet();
                return UPCOMING_MATCHES_JSON;
            }
        };

        Match first = provider.getUpcomingMatch();
        Assertions.assertNotNull(first);
        Assertions.assertEquals("Краснодар", first.getHomeTeam());
        Assertions.assertEquals("Факел", first.getAwayTeam());
        Assertions.assertNull(first.getHomeScore());
        Assertions.assertNull(first.getAwayScore());

        Match second = provider.getUpcomingMatch();
        Assertions.assertNotNull(second);
        Assertions.assertEquals("Локомотив", second.getHomeTeam());
        Assertions.assertEquals("Динамо", second.getAwayTeam());

        Assertions.assertEquals(1, apiCallsCount.get(), "Queue should prevent repeated API requests");
    }

    /**
     * Tests filtering past matches by the user's favorite team.
     */
    @Test
    public void testGetPastMatchForFT_FiltersOnlyFavoriteTeam() throws DataProviderException {
        ApiFootballProvider provider = new ApiFootballProvider("fake-key") {
            @Override
            protected String fetchJson(String endpoint) {
                return PAST_MATCHES_JSON;
            }
        };

        Match first = provider.getPastMatchForFT("Динамо");
        Assertions.assertNotNull(first);
        Assertions.assertEquals("Факел", first.getHomeTeam());
        Assertions.assertEquals("Динамо", first.getAwayTeam());

        Match second = provider.getPastMatchForFT("Динамо");
        Assertions.assertNotNull(second);
        Assertions.assertEquals("Динамо", second.getHomeTeam());
        Assertions.assertEquals("Зенит", second.getAwayTeam());
    }

    /**
     * Tests filtering upcoming matches by the user's favorite team.
     */
    @Test
    public void testGetUpcomingMatchForFT_FiltersOnlyFavoriteTeam() throws DataProviderException {
        ApiFootballProvider provider = new ApiFootballProvider("fake-key") {
            @Override
            protected String fetchJson(String endpoint) {
                return UPCOMING_MATCHES_JSON;
            }
        };

        Match match = provider.getUpcomingMatchForFT("Динамо");
        Assertions.assertNotNull(match);
        Assertions.assertEquals("Локомотив", match.getHomeTeam());
        Assertions.assertEquals("Динамо", match.getAwayTeam());
    }

    /**
     * Tests that DataProviderException is propagated when the API request fails.
     */
    @Test
    public void testFetchJsonFailure_ThrowsDataProviderException() {
        ApiFootballProvider brokenProvider = new ApiFootballProvider("fake-key") {
            @Override
            protected String fetchJson(String endpoint) throws DataProviderException {
                throw new DataProviderException("API returned status: 500");
            }
        };

        Assertions.assertThrows(DataProviderException.class, brokenProvider::getPastMatch);
        Assertions.assertThrows(DataProviderException.class, brokenProvider::getUpcomingMatch);
        Assertions.assertThrows(DataProviderException.class, () -> brokenProvider.getPastMatchForFT("Динамо"));
        Assertions.assertThrows(DataProviderException.class, () -> brokenProvider.getUpcomingMatchForFT("Динамо"));
    }
}