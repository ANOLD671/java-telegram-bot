import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Main {
    public static void main(String[] args)
        throws Exception{
        var botsApi = new

        TelegramBotsApi(DefaultBotSession.class);
         botsApi.registerBot(new Echo_class() );
        System.out.println("Bot is running,babay!");

    }
}
