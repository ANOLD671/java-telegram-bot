/**
 * Represents a user's profile in the system, storing their Telegram ID, name, and favorite team.
 */
public class UserProfile {

    /** The unique identifier for the user in Telegram. */
    private final long telegramId;

    /** The user's first name. */
    private final String name;

    /** The user's chosen favorite football team. */
    private String favoriteTeam;

    /**
     * Constructs a new UserProfile.
     *
     * @param telegramId The unique Telegram ID of the user.
     * @param name       The first name of the user.
     */
    public UserProfile(long telegramId, String name) {
        this.telegramId = telegramId;
        this.name = name;
    }

    /**
     * Gets the user's Telegram ID.
     *
     * @return The Telegram ID.
     */
    public long getTelegramId() {
        return telegramId;
    }

    /**
     * Gets the user's name.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's favorite team.
     *
     * @return The favorite team, or {@code null} if none is selected.
     */
    public String getFavoriteTeam() {
        return favoriteTeam;
    }

    /**
     * Sets the user's favorite team.
     *
     * @param favoriteTeam The name of the team to set.
     */
    public void setFavoriteTeam(String favoriteTeam) {
        this.favoriteTeam = favoriteTeam;
    }
}