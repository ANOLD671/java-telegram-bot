import java.util.HashMap;
import java.util.Map;

/**
 * Repository class responsible for storing and managing user profiles.
 * <p>
 * This class uses an in-memory {@link HashMap} to store user data, mapping
 * each user's unique Telegram ID to their corresponding {@link UserProfile}.
 * It acts as the data access layer, completely independent of the Telegram API
 * and the business logic.
 */
public class UserRepository {

    /** The internal storage mapping a user's Telegram ID to their profile. */
    private final Map<Long, UserProfile> users = new HashMap<>();

    /**
     * Saves or updates a user profile in the storage.
     * <p>
     * If a profile with the same Telegram ID already exists, it will be overwritten.
     * Null values are safely ignored.
     *
     * @param user The {@link UserProfile} object to be saved.
     */
    public void save(UserProfile user) {
        if (user != null) {
            users.put(user.getTelegramId(), user);
        }
    }

    /**
     * Retrieves a user profile by their unique Telegram ID.
     *
     * @param id The unique Telegram ID of the user.
     * @return The {@link UserProfile} associated with the given ID, or {@code null} if no profile exists.
     */
    public UserProfile findById(long id) {
        return users.get(id);
    }

    /**
     * Checks whether a user profile exists in the storage.
     *
     * @param id The unique Telegram ID of the user.
     * @return {@code true} if a profile with the given ID exists, {@code false} otherwise.
     */
    public boolean exists(long id) {
        return users.containsKey(id);
    }
}