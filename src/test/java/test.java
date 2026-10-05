import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Echo_classTest {

    private final Echo_class bot = new Echo_class();

    @Test
    void start_test() {
        String reply = bot.buildResponse("/start");
        assertEquals(
    "Hello! I am echo-bot. I will repeat your messages\n" +
            "Type /help to see all commands",
            reply
        );
    }

    @Test
    void help_test() {
        String reply = bot.buildResponse("/help");
        assertEquals(
                "Commands:\n" +
                        "/start\n" +
                        "/help\n",
                reply
        );
    }
    @Test
    void regularText_isEchoed() {
        assertEquals("привет", bot.buildResponse("привет"));
    }

    @Test
    void englishText_isEchoed() {
        assertEquals("hello world", bot.buildResponse("hello world"));
    }

    @Test
    void textWithSpaces_isEchoedExactly() {
        assertEquals("  много   пробелов  ", bot.buildResponse("  много   пробелов  "));
    }
}