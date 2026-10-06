import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * Default telegram bot class
 **/

public class Echo_class extends TelegramLongPollingBot {

    private final Dotenv dotenv = Dotenv.load();
    /**
     * @return bot username
     */
    @Override
    public String getBotUsername(){
        return dotenv.get("BOT_USERNAME", "YOUR_BOT_NAME");
    }

    /**
     * @return bot`s telegram token
     */
    @Override
    public String getBotToken(){
        return dotenv.get("BOT_TOKEN");
    }

    /**
     * This method is called automatically
     * when Telegram sends a new event to the bot.
     * He processes it and sends echo-response message.
     * @param "update" java object
     */

    public String buildResponse(String incomingText) {
        if ("/start".equals(incomingText)) {
            return "Hello! I am echo-bot. I will repeat your messages\n" +
                    "Type /help to see all commands";
        } else if ("/help".equals(incomingText)) {
            return "Commands:\n" +
                    "/start\n" +
                    "/help\n";
        } else {return "You typed: " + incomingText;}
    }
    @Override
    public void onUpdateReceived(Update update){
      Message msg = update.getMessage();
      long chatId = msg.getChatId();
      String text = msg.getText();

      String reply = buildResponse(text);

      SendMessage sm = SendMessage.builder()
        .chatId(Long.toString(chatId))
        .text(reply)
        .build();

      try{
          execute(sm);
      } catch (TelegramApiException e) {
          throw new RuntimeException(e);
      }

    }
}
