import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Logic class.
 */
public class LogicTest {

    private Logic logic;
    private UserRepository userRepository;
    private final long TEST_USER_ID = 123456789L;
    private final String TEST_USER_NAME = "Arnold";

    /**
     * initialize Logic and UserRepository
     */

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();
        logic = new Logic(userRepository);
    }

    /**
     * testing start command. correct display of the "welcome text" + username
     */
    @Test
    public void testStartCommand() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/start");
        String expected = "Welcome " + TEST_USER_NAME + " to the Football Bot!\n" +
        "Use /listOfTeams to see available teams, then /setteam <name> to pick your club!";
        assertEquals(expected, response);
    }

    /**
     * testing list /ListOfTeams command
     */
    @Test
    public void testListOfTeamsCommand() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/listOfTeams");
        String expected = "🏆 Current Season Teams:\n" + "Зенит\n" +
                "Динамо Москва\n" +
                "ЦСКА\n" +
                "Краснодар\n" +
                "Спартак Москва\n" +
                "Оренбург\n" +
                "Ростов\n" +
                "Крылья Советов\n" +
                "Балтика\n" +
                "Динамо Махачкала\n" +
                "Рубин\n" +
                "Акрон\n" +
                "Локомотив Москва\n" +
                "Ахмат\n" +
                "Родина\n" +
                "Факел";
        assertEquals(expected,response);
    }

    /**
     * testing /setteam command. Testing creaing user profile. Testing the correctness of data saving
     */
    @Test
    public void testSetTeamSuccessAndAutoCreation() {
        assertFalse(userRepository.exists(TEST_USER_ID));
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/setteam Зенит");

        assertEquals("⭐ Your favorite team is now: Зенит", response);
        assertTrue(userRepository.exists(TEST_USER_ID));
        assertEquals("Зенит", userRepository.findById(TEST_USER_ID).getFavoriteTeam());
    }

    /**
     * testing /setteam command when entering the correct name using a different case
     */
    @Test
    public void testSetTeamCaseInsensitive() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/setteam зенит");
        assertEquals("⭐ Your favorite team is now: Зенит", response);
    }

    /**
     * testing /setteam win invalid team name input
     */
    @Test
    public void testSetTeamInvalidName() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/setteam hfjsdh");
        assertEquals("❌ *hfjsdh* is not in the league list! Check /listOfTeams.", response);
        assertFalse(userRepository.exists(TEST_USER_ID));
    }

    /**
     * testing favorite team of new profile
     */
    @Test
    public void testProfileWithoutTeam() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/profile");

        String expected = "👤 Profile Name: " + TEST_USER_NAME +
                "\n❤️ Favorite Team: None selected yet. Use /setteam to pick one!";
        assertEquals(expected, response);
    }
}