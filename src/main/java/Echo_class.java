import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.HashMap;
import java.util.Map;

/**
 * Default telegram bot class
 **/

public class Echo_class extends
        TelegramLongPollingBot {
    private final Map<Long, CityGame> userGames
            = new HashMap<>();
    private final Dotenv dotenv = Dotenv. load();


    public String getBotUsername() { return
            dotenv.get("BOT_USERNAME", "YOUR_BOT_NAME"); }

    @Override
    public String getBotToken() { return
            dotenv.get("BOT_TOKEN"); }

    @Override
    public void onUpdateReceived(Update update){
        if (update.hasMessage() && update.getMessage().hasText()){
            long userId = update.getMessage().getFrom().getId();
            String text = update.getMessage().getText();
            userGames.putIfAbsent(userId, new CityGame());
            String reply = userGames.get(userId).handleInput(text);
            sendText(update.getMessage().getChatId(),reply);

        }
    }

    private void sendText(long chatId, String text){
        SendMessage sm = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(text).build();
        try{execute(sm);}
        catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
}

