import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Logic class.
 */
public class LogicTest {

    private Logic logic;
    private UserRepository userRepository;
    private final long TEST_USER_ID = 123456789L;
    private final String TEST_USER_NAME = "Arnold";
    private MockFootballProvider matchProvider;

    /**
     * initialize Logic and UserRepository
     */

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();
        matchProvider = new MockFootballProvider();
        logic = new Logic(userRepository, matchProvider);
    }

    /**
     * Testing /pastMatches
     */
    @Test
    public void testPastMatchesCommand() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/pastMatches");

        String expectedResponse =
                "📅 25.04.2025\nЦСКА 2 : 2 Спартак Москва\n\n" +
                        "📅 12.03.2026\nЛокомотив Москва 0 : 0 Зенит\n\n" +
                        "📅 01.10.2026\nКраснодар 3 : 0 Факел\n\n" +
                        "📅 25.04.2025\nЦСКА 2 : 2 Спартак Москва\n\n" +
                        "📅 12.03.2026\nЛокомотив Москва 0 : 0 Зенит\n\n" +
                        "📅 01.10.2026\nКраснодар 3 : 0 Факел\n\n" +
                        "📅 01.10.2026\nКраснодар 3 : 0 Факел";

        assertEquals(expectedResponse, response, "Incorrect output for /pastMatches");
    }

    /**
     * Testing /upcomingMatches
     */
    @Test
    public void testUpcomingMatchesCommand() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/upcomingMatches");

        String expectedResponse =
                "📅 25.04.2025 19:00\nДинамо Москва - Спартак Москва\n\n" +
                        "📅 12.03.2026 20:30\nЛокомотив Москва - Зенит\n\n" +
                        "📅 01.10.2026 17:45\nКраснодар - Факел\n\n" +
                        "📅 25.04.2025 19:00\nЦСКА - Спартак Москва\n\n" +
                        "📅 12.03.2026 20:30\nЛокомотив Москва - Зенит\n\n" +
                        "📅 01.10.2026 17:45\nКраснодар - Факел\n\n" +
                        "📅 01.10.2026 17:45\nКраснодар - Факел";

        assertEquals(expectedResponse, response, "Incorrect output for /upcomingMatches");
    }

    /**
     * Testing /pastMatchesForFT
     */
    @Test
    public void testPastMatchesForFTCommand_WithFavoriteTeam() {
        logic.handle(TEST_USER_ID, TEST_USER_NAME, "/setteam Динамо Москва");

        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/pastMatchesForFT");

        String expectedResponse =
                "📅 25.04.2025\nДинамо Москва 0 : 0 Спартак Москва\n\n" +
                        "📅 12.03.2026\nДинамо Москва 1 : 1 Зенит\n\n" +
                        "📅 01.10.2026\nДинамо Москва 2 : 2 Факел\n\n" +
                        "📅 25.04.2025\nЦСКА 2 : 2 Спартак Москва";

        assertEquals(expectedResponse, response, "Incorrect result for /pastMatchesForFT");
    }

    /**
     * Testing /upcomingMatchesForFT
     */
    @Test
    public void testUpcomingMatchesForFTCommand_WithFavoriteTeam() {
        logic.handle(TEST_USER_ID, TEST_USER_NAME, "/setteam Динамо Москва");

        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/upcomingMatchesForFT");

        String expectedResponse =
                "📅 25.04.2025 19:00\nДинамо Москва - Спартак Москва\n\n" +
                        "📅 12.03.2026 20:30\nДинамо Москва - Зенит\n\n" +
                        "📅 01.10.2026 17:45\nДинамо Москва - Факел\n\n" +
                        "📅 25.04.2025 19:00\nЦСКА - Спартак Москва";

        assertEquals(expectedResponse, response, "Incorrect output for /upcomingMatchesForFT");
    }

    /**
     * Testing /upcomingMatchesForFT with not selected favorite team
     */
    @Test
    public void testMatchesForFTCommand_WithoutFavoriteTeam() {
        // The user has NOT selected a team; we are requesting matches immediately.
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/upcomingMatchesForFT");

        String expectedResponse = "You have not selected a favorite team.\nType /setFavoriteTeam <TeamName> no select favorite team";

        assertEquals(expectedResponse, response, "The bot must require a team to be selected.");
    }

    /**
     * Testing simulated network error situation
     */
    @Test
    public void testApiFailureExceptionHandling() {
        // Network error imitation
        FootballDataProvider brokenProvider = new FootballDataProvider() {
            @Override public List<Match> getPastMatches(Integer limit) { throw new DataProviderException("Network Error"); }
            @Override public List<Match> getUpcomingMatches(Integer limit) { throw new DataProviderException("Network Error"); }
            @Override public List<Match> getPastMatchesForFT(Integer limit, String teamName) { throw new DataProviderException("Network Error"); }
            @Override public List<Match> getUpcomingMatchesForFT(Integer limit, String teamName) { throw new DataProviderException("Network Error"); }
        };

        Logic brokenLogic = new Logic(userRepository, brokenProvider);

        String response = brokenLogic.handle(TEST_USER_ID, TEST_USER_NAME, "/pastMatches");

        assertEquals("The service is temporarily unavailable; please try again later.", response, "Provider error is not handled correctly");
    }
}