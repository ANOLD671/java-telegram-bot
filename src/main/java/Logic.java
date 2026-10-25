import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
/**
 * bot logic class implementing methods for handling user data
 **/

public class Logic {
    private final ConcurrentHashMap<Long, UserProfile> users = new ConcurrentHashMap<>();

    public String processMessage(Long chatId, String incomingText) {

        UserProfile currentUser;
        if (!users.containsKey(chatId)) {
            users.put(chatId, new UserProfile(chatId));}
        currentUser = users.get(chatId);

        if ("/help".equals(incomingText)) {
            return "/listOfTeams - show a list of teams for currnt season\n" +
                    "/profile - show Your current profile\n" +
                    "/setteam <team name> - set Your favorite team. (only teams from the list can be selected)\n";
        }
    return "Unknown command\n" +
            "Type /help to list existing commands";
    }
}
