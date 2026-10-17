import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

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
        Assertions.assertEquals("Hello! I am echo-bot. I will repeat your messages\n" +
                        "Type /help to see all commands", reply);
    }

    /**
     * testing logic's answer for rusiian text
     */
    @Test
    void regularText_isEchoed() {
        String reply = bot.buildResponse("привет");
        Assertions.assertEquals ("You typed: привет", reply);
    }

    /**
     * testing logic's answer for english text
     */
    @Test
    void englishText_isEchoed() {
        String reply = bot.buildResponse("hello world");
        Assertions.assertEquals("You typed: hello world", reply);
    }

    /**
     * testing logic's answer with a lot of spaces
     */
    @Test
    void textWithSpaces_isEchoedExactly() {
        String reply = bot.buildResponse("  много   пробелов  ");
        Assertions.assertEquals("You typed:   много   пробелов  ", reply);
    }
}