import io.github.cdimascio.dotenv.Dotenv;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
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