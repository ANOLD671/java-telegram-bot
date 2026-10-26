import io.github.cdimascio.dotenv.Dotenv;
import org.glassfish.jersey.message.internal.Token;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {
    public static void main(String[] args)
        throws Exception{
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("BOT_TOKEN");
        String username = dotenv.get("BOT_USERNAME");
        Logic logic = new Logic();


        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
         botsApi.registerBot(new TelegramBot(logic,token,username));
        System.out.println("Bot is running!");

    }
}
