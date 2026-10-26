public class UserProfile {
    private final long telegramId;
    private final String name;
    private String favoriteTeam;

    public UserProfile(long telegramId, String name) {
        this.telegramId = telegramId;
        this.name = name;
    }

    public long getTelegramId() {
        return telegramId;
    }

    public String getName() {
        return name;
    }

    public String getFavoriteTeam() {
        return favoriteTeam;
    }

    public void setFavoriteTeam(String favoriteTeam) {
        this.favoriteTeam = favoriteTeam;
    }
}