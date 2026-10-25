/**
 * single-user data class
 */

public class UserProfile {
    private final long id;
    private String favoriteTeam;

    public UserProfile(long id) {
        this.id = id;
        this.favoriteTeam = "Not selected";
    }
    /**
     * @return long id
     */
    public long getId() {return id;};

    /**
     * favorite team getter
     * @return String favoriteTeam
     */
    public String getFavoriteTeam() {return favoriteTeam;};

    /** favorite team setter
     * @param favoriteTeam;
     */
    public void setFavoriteTeam(String favoriteTeam) {
        this.favoriteTeam = favoriteTeam;
    }
}
