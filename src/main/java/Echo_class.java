import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Echo_class  extends TelegramLongPollingBot {

    @Override
    public String getBotUsername(){
        return "YOUR_BOT_NAME";
    }

    @Override
    public String getBotToken(){
        return "8799668869:AAEM9-5-c8UCEqlios3M093Lyk6aRkbEXdI";
    }

    @Override
    public void onUpdateReceived(Update update){
      var msg = update.getMessage();
      var user = msg.getFrom();
      var id = user.getId();


      SendMessage sm = SendMessage.builder()
        .chatId(id.toString())
        .text(msg.getText())
        .build();

      try{
          execute(sm);
      } catch (TelegramApiException e) {
          throw new RuntimeException(e);
      }

    }
}
