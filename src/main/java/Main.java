import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Application entry point for the Telegram bot.
 *
 * This class is the composition root of the application: it is the
 * only place where all dependencies are wired together. It performs the
 * following steps:
 *
 *   -Loads configuration (bot token and username) from the .env file.
 *   -Creates the shared "Logic" instance.
 *   -Initializes the "TelegramBotApi" with the default long-polling session.
 *   -Registers a new "Telegram_bot" adapter with the given logic and credentials.
 *
 * Once "registerBot" returns, the bot session runs in a background
 * thread and keeps processing incoming updates until the JVM is terminated.
 *
 * @see Telegram_bot
 * @see Logic
 * @see TelegramBotsApi
 */

public class Main {
    public void main(String[] args)
        throws Exception {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("BOT_TOKEN");
        String username = dotenv.get("BOT_USERNAME");
        Logic logic = new Logic();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class); // Init bot API
        botsApi.registerBot(new Telegram_bot(logic, token, username));          // Bot registration
        System.out.println("Bot is running,babay!");

    }
}
