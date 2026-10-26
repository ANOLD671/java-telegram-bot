public class User {
    private final long telegramId;
    private String favoriteTeam;
    private int winStreak;
    private final String name;



    public User(long telegramId,String name, long balance, String favoriteTeam, int winStreak) {
        this.telegramId = telegramId;
        this.favoriteTeam = "None";
        this.winStreak = 0;
        this.name = name;
    }

    public User(long id,String name){
        this(id,name,"None",0);
    }
    public long getTelegramId() {
        return telegramId;
    }
    public String getFavoriteTeam() {
        return favoriteTeam;
    }

    public String getName() {
        return name;
    }

    public void setFavoriteTeam(String favoriteTeam) {
        this.favoriteTeam = favoriteTeam;
    }

    public int getWinStreak() {
        return winStreak;
    }

    public void setWinStreak(int winStreak) {
        this.winStreak = winStreak;
    }
}
