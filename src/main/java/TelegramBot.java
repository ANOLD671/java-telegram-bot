import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Telegram adapter for the bot.
 * This class is responsible for the Telegram-specific part of the bot:
 * receiving "Update" objects from Telegram, delegating the actual
 * response generation to "Logic", and sending the resulting text
 * back to the user via "SendMessage()".
 * All business logic lives in class "Logic";
 * this class only knows how to talk to Telegram.
 * Instances are created by the application bootstrap,
 * which supplies the shared "Logic" instance and the bot credentials.
 */
public class TelegramBot extends TelegramLongPollingBot {

    /**
     * Shared, platform-independent business logic.
     * Injected from outside so that the same instance can be reused
     * by other platform adapters.
     */
    private final Logic logic;
    private final String botToken;
    private final String botUsername;

    /**
     * Creates a new Telegram bot adapter.
     *
     * @param logic       shared business logic;
     * @param botToken    Telegram bot token issued by BotFather
     * @param botUsername Telegram bot username without the leading "@"
     */
    public TelegramBot(Logic logic, String botToken, String botUsername) {
        this.logic = logic;
        this.botToken = botToken;
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername(){
        return botUsername;
    }

    @Override
    public String getBotToken(){
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update){
      Message msg = update.getMessage();
      long chatId = msg.getChatId();
      String text = msg.getText();

      String reply = logic.processMessage(chatId, text);

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