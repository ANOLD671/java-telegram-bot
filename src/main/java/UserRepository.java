import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final Map<Long,User> users = new HashMap<>();

    public void save(User user){
        users.put(user.getTelegramId(), user);
    }

    public User findById(long id){
        return users.get(id);
    }
    public boolean exists(long id){
        return users.containsKey(id);
    }

}
