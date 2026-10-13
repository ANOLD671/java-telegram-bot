import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * testing bot logic
 */
class TelegramBotTest {

    private final Logic bot = new Logic();

    /**
     * testing "/start" command
     */
    @Test
    void start_test() {
        String reply = bot.buildResponse("/start");
        assertEquals(
                "Hello! I am echo-bot. I will repeat your messages\n" +
                        "Type /help to see all commands",
                reply
        );
    }

    /**
     * testing "echo" command
     */
    @Test
    void regularText_isEchoed() {
        assertEquals("You typed: привет", bot.buildResponse("привет"));
    }

    @Test
    void englishText_isEchoed() {
        assertEquals("You typed: hello world", bot.buildResponse("hello world"));
    }

    @Test
    void textWithSpaces_isEchoedExactly() {
        assertEquals("You typed:   много   пробелов  ", bot.buildResponse("  много   пробелов  "));
    }
}