import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * The application entry point responsible for initializing the Telegram Bot.
 */
public class Main {

    /**
     * The main method that serves as the starting point of the application.
     * Loads configuration from the .env file and registers the bot.
     *
     * @param args Command-line arguments (not used).
     * @throws Exception If the bot fails to initialize or register with the Telegram API.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("🔍 Initializing configuration...");

        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String token = dotenv.get("BOT_TOKEN");
        String username = dotenv.get("BOT_USERNAME");

        if (token == null || token.trim().isEmpty() || username == null || username.trim().isEmpty()) {
            System.out.println("⚠️ [WARNING] BOT_TOKEN or BOT_USERNAME is missing. Exiting gracefully.");
            return;
        }

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(new TelegramBot(token, username));
        System.out.println("🚀 Football Bot is successfully up and running!");
    }
}