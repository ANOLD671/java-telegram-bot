import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Application entry point for the Telegram bot.
 */

public class Main {
    public void main(String[] args)
        throws Exception {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("BOT_TOKEN");
        String username = dotenv.get("BOT_USERNAME");
        Logic logic = new Logic();

        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class); // Init bot API
        botsApi.registerBot(new TelegramBot(logic, token, username));          // Bot registration
        System.out.println("Bot is running!");

    }
}
