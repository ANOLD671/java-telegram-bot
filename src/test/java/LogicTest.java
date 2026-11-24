import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Unit tests for Logic and ApiFootballProvider.
 */
public class LogicTest {

    private Logic logic;
    private UserRepository userRepository;
    private final long TEST_USER_ID = 123456789L;
    private final String TEST_USER_NAME = "Arnold";

    private final String PAST_MATCHES_JSON = """
            {
              "data": [
                {
                  "id": "106",
                  "utc_date": "2026-03-20T19:00:00Z",
                  "home_team": { "name": "Dynamo Moscow" },
                  "away_team": { "name": "Rubin Kazan" },
                  "score": { "home": 4, "away": 0 }
                },
                {
                  "id": "105",
                  "utc_date": "2026-03-15T18:00:00Z",
                  "home_team": { "name": "FC Rostov" },
                  "away_team": { "name": "Dynamo Moscow" },
                  "score": { "home": 1, "away": 2 }
                },
                {
                  "id": "104",
                  "utc_date": "2026-03-12T20:30:00Z",
                  "home_team": { "name": "Dynamo Moscow" },
                  "away_team": { "name": "Zenit St. Petersburg" },
                  "score": { "home": 2, "away": 1 }
                },
                {
                  "id": "103",
                  "utc_date": "2026-03-10T19:00:00Z",
                  "home_team": { "name": "CSKA Moscow" },
                  "away_team": { "name": "FC Spartak Moscow" },
                  "score": { "home": 1, "away": 1 }
                },
                {
                  "id": "102",
                  "utc_date": "2026-03-05T17:45:00Z",
                  "home_team": { "name": "Fakel Voronezh" },
                  "away_team": { "name": "Dynamo Moscow" },
                  "score": { "home": 0, "away": 3 }
                },
                {
                  "id": "101",
                  "utc_date": "2026-03-01T16:00:00Z",
                  "home_team": { "name": "Dynamo Moscow" },
                  "away_team": { "name": "FC Spartak Moscow" },
                  "score": { "home": 0, "away": 0 }
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
                  "home_team": { "name": "Dynamo Moscow" },
                  "away_team": { "name": "FC Spartak Moscow" },
                  "score": { "home": null, "away": null }
                },
                {
                  "id": "203",
                  "utc_date": "2026-10-10T19:00:00Z",
                  "home_team": { "name": "Zenit St. Petersburg" },
                  "away_team": { "name": "Dynamo Moscow" },
                  "score": { "home": null, "away": null }
                },
                {
                  "id": "204",
                  "utc_date": "2026-10-12T18:00:00Z",
                  "home_team": { "name": "CSKA Moscow" },
                  "away_team": { "name": "Lokomotiv Moscow" },
                  "score": { "home": null, "away": null }
                },
                {
                  "id": "205",
                  "utc_date": "2026-10-15T17:45:00Z",
                  "home_team": { "name": "Dynamo Moscow" },
                  "away_team": { "name": "Fakel Voronezh" },
                  "score": { "home": null, "away": null }
                },
                {
                  "id": "206",
                  "utc_date": "2026-10-20T19:00:00Z",
                  "home_team": { "name": "Rubin Kazan" },
                  "away_team": { "name": "Dynamo Moscow" },
                  "score": { "home": null, "away": null }
                },
                {
                  "id": "207",
                  "utc_date": "2026-10-25T16:00:00Z",
                  "home_team": { "name": "Dynamo Moscow" },
                  "away_team": { "name": "FC Rostov" },
                  "score": { "home": null, "away": null }
                }
              ]
            }
            """;

    /**
     * Initializes UserRepository, stubbed ApiFootballProvider, and Logic before each test.
     */
    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();
        ApiFootballProvider stubProvider = new ApiFootballProvider("fake-key") {
            @Override
            protected String fetchJson(String endpoint) {
                if (endpoint.contains("status=finished")) {
                    return PAST_MATCHES_JSON;
                }
                return UPCOMING_MATCHES_JSON;
            }
        };
        logic = new Logic(userRepository, stubProvider);
    }

    /**
     * Tests parsing, team name translation, chronological reversing, and queue caching for past matches.
     */
    @Test
    public void testGetPastMatch_ParsesTranslatesReversesAndCaches() {
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
        Assertions.assertEquals("Динамо Москва", first.getHomeTeam());
        Assertions.assertEquals("Спартак Москва", first.getAwayTeam());
        Assertions.assertEquals(LocalDateTime.of(2026, 3, 1, 16, 0), first.getDate());
        Assertions.assertEquals(0, first.getHomeScore());
        Assertions.assertEquals(0, first.getAwayScore());

        Match second = provider.getPastMatch();
        Assertions.assertNotNull(second);
        Assertions.assertEquals("Факел", second.getHomeTeam());
        Assertions.assertEquals("Динамо Москва", second.getAwayTeam());

        Match third = provider.getPastMatch();
        Assertions.assertNotNull(third);
        Assertions.assertEquals("ЦСКА", third.getHomeTeam());
        Assertions.assertEquals("Спартак Москва", third.getAwayTeam());

        Assertions.assertEquals(1, apiCallsCount.get(), "Only one API request should be made");
    }

    /**
     * Tests parsing of null scores, order preservation, and queue caching for upcoming matches.
     */
    @Test
    public void testGetUpcomingMatch_ParsesNullScoresAndPreservesOrder() {
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
        Assertions.assertEquals("Динамо Москва", second.getHomeTeam());
        Assertions.assertEquals("Спартак Москва", second.getAwayTeam());

        Assertions.assertEquals(1, apiCallsCount.get(), "Queue should prevent repeated API requests");
    }

    /**
     * Tests filtering and formatting of the 4 past matches for the user's favorite team.
     */
    @Test
    public void testPastMatchesForFTCommand_WithFavoriteTeam() {
        logic.handle(TEST_USER_ID, TEST_USER_NAME, "/setteam Динамо Москва");

        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/pastMatchesForFT");

        String expectedResponse =
                "📅 01.03.2026\nДинамо Москва 0 : 0 Спартак Москва\n\n" +
                        "📅 05.03.2026\nФакел 0 : 3 Динамо Москва\n\n" +
                        "📅 12.03.2026\nДинамо Москва 2 : 1 Зенит\n\n" +
                        "📅 15.03.2026\nРостов 1 : 2 Динамо Москва";

        Assertions.assertEquals(expectedResponse, response, "Incorrect filtered output for /pastMatchesForFT");
    }

    /**
     * Tests filtering and formatting of the 4 upcoming matches for the user's favorite team.
     */
    @Test
    public void testUpcomingMatchesForFTCommand_WithFavoriteTeam() {
        logic.handle(TEST_USER_ID, TEST_USER_NAME, "/setteam Динамо Москва");

        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/upcomingMatchesForFT");

        String expectedResponse =
                "📅 05.10.2026 20:30\nДинамо Москва - Спартак Москва\n\n" +
                        "📅 10.10.2026 19:00\nЗенит - Динамо Москва\n\n" +
                        "📅 15.10.2026 17:45\nДинамо Москва - Факел\n\n" +
                        "📅 20.10.2026 19:00\nРубин - Динамо Москва";

        Assertions.assertEquals(expectedResponse, response, "Incorrect filtered output for /upcomingMatchesForFT");
    }

    /**
     * Tests requesting favorite team matches when no favorite team is selected.
     */
    @Test
    public void testMatchesForFTCommand_WithoutFavoriteTeam() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/upcomingMatchesForFT");

        String expectedResponse = "You have not selected a favorite team.\nType /setteam <TeamName> to select favorite team";

        Assertions.assertEquals(expectedResponse, response, "The bot must require a team to be selected");
    }

    /**
     * Tests that DataProviderException is thrown by provider and handled gracefully by Logic.
     */
    @Test
    public void testApiFailureExceptionHandling() {
        ApiFootballProvider brokenProvider = new ApiFootballProvider("fake-key") {
            @Override
            protected String fetchJson(String endpoint) {
                throw new DataProviderException("Network Error");
            }
        };

        Assertions.assertThrows(DataProviderException.class, brokenProvider::getPastMatch);
        Assertions.assertThrows(DataProviderException.class, brokenProvider::getUpcomingMatch);

        Logic brokenLogic = new Logic(userRepository, brokenProvider);
        String response = brokenLogic.handle(TEST_USER_ID, TEST_USER_NAME, "/pastMatches");

        Assertions.assertEquals("The service is temporarily unavailable; please try again later.", response);
    }
}