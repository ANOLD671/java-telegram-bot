import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
/** Start program **/

public class Main {
    public void main(String[] args)
        throws Exception{
        var botsApi = new TelegramBotsApi(DefaultBotSession.class); // Init bot API
         botsApi.registerBot(new Echo_class() );                    // Bot registration
        System.out.println("Bot is running,babay!");

    }
}
