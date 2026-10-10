/**
 * bot logic class implementing methods for handling user data
 **/

public class Logic {
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
}
