import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * The Telegram interface for the bot. Handles network communication.
 */
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final Logic logic;

    public TelegramBot(String botToken, String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
        this.logic = new Logic(new UserRepository()); // Initialize your new Logic!
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long userId = update.getMessage().getFrom().getId();
            String userName = update.getMessage().getFrom().getFirstName();
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            // Call your Logic class instead of ProfileService
            String reply = logic.handle(userId, userName, text);

            sendText(chatId, reply);
        }
    }

    private void sendText(long chatId, String text) {
        SendMessage sm = SendMessage.builder()
                .chatId(Long.toString(chatId))
                .text(text).build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            // Using printStackTrace is okay for now, but we use System.err for standard practice
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }
}