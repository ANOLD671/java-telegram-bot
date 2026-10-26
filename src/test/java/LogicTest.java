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

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepository();
        logic = new Logic(userRepository);
    }

    @Test
    public void testStartCommand() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/start");
        assertTrue(response.contains("Welcome " + TEST_USER_NAME));
        assertFalse(userRepository.exists(TEST_USER_ID));
    }

    @Test
    public void testListOfTeamsCommand() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/listOfTeams");
        assertTrue(response.contains("Current Season Teams:"));
        assertTrue(response.contains("Зенит") || response.contains("Zenit"));
    }

    @Test
    public void testSetTeamSuccessAndAutoCreation() {
        assertFalse(userRepository.exists(TEST_USER_ID));
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/setteam Зенит");

        assertTrue(response.contains("Your favorite team is now: Зенит"));
        assertTrue(userRepository.exists(TEST_USER_ID));
        assertEquals("Зенит", userRepository.findById(TEST_USER_ID).getFavoriteTeam());
    }

    @Test
    public void testSetTeamCaseInsensitive() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/setteam зенит");
        assertTrue(response.contains("Your favorite team is now: Зенит"));
    }

    @Test
    public void testSetTeamInvalidName() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/setteam hfjsdh");
        assertTrue(response.contains("is not in the league list"));
        assertFalse(userRepository.exists(TEST_USER_ID));
    }

    @Test
    public void testProfileWithoutTeam() {
        String response = logic.handle(TEST_USER_ID, TEST_USER_NAME, "/profile");
        assertTrue(response.contains("None selected yet"));
    }
}