import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Default telegram bot class
 **/

public class Telegram_bot extends TelegramLongPollingBot {

    private final Logic logic;
    private final String botToken;
    private final String botUsername;


    public Telegram_bot(Logic logic, String botToken, String botUsername) {
        this.logic = logic;
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    /**
     * @return bot username
     */
    @Override
    public String getBotUsername(){
        return botUsername;
    }

    /**
     * @return bot`s telegram token
     */
    @Override
    public String getBotToken(){
        return botToken;
    }

    /**
     * This method is called automatically
     * when Telegram sends a new event to the bot.
     * He processes it and sends echo-response message.
     * @param "update" java object
     */


    @Override
    public void onUpdateReceived(Update update){
      Message msg = update.getMessage();
      long chatId = msg.getChatId();
      String text = msg.getText();

      String reply = logic.buildResponse(text);

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