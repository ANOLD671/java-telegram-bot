import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final Map<Long, UserProfile> users = new HashMap<>();

    public void save(UserProfile user) {
        if (user != null) {
            users.put(user.getTelegramId(), user); // Now this will work!
        }
    }

    public UserProfile findById(long id) {
        return users.get(id);
    }

    public boolean exists(long id) {
        return users.containsKey(id);
    }
}