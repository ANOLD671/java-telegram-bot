import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a match information. Participants teams in match, match date, score
 */

public class Match {
    /**
     * Match ID
     */
    private final String id;
    /**
     * Home team's name
     */
    private final String homeTeam;
    /**
     * Away team's name
     */
    private final String awayTeam;
    /**
     * Match date
     */
    private final LocalDateTime date;
    /**
     * Home teams score
     */
    private final Integer homeTeamScore;
    /**
     * Away team score
     */
    private final Integer awayTeamScore;

    public Match(String id, String homeTeam, String awayTeam, LocalDateTime date, Integer homeTeamScore, Integer awayTeamScore) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = date;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
        this.id = id;
    }

    /**
     * Home Team getter
     * @return {@link String} Home Team
     */
    public String getHomeTeam() {return homeTeam;}
    /**
     * Away Team getter
     * @return {@link String} Away Team
     */
    public String getAwayTeam() {return awayTeam;}
    /**
     * Match date getter. Format
     * @return {@link String} YYYY-MM-DD'T'HH:MM
     */
    public LocalDateTime getDate() {return date;}
    /**
     * Away Team Score getter
     * @return {@link Integer} Away Tean Score
     */
    public Integer getAwayScore() {return awayTeamScore;}
    /**
     * Home Team Score getter
     * @return {@link Integer} Home Team Score
     */
    public Integer getHomeScore() {return homeTeamScore;}
    /**
     * Match ID getter
     * @return {@link String} Match ID
     */
    public String getId() {return id;}

    /**
     *
     * @return {@link String} formated match information with emoji
     */
    public String displayMatch() {
        if (homeTeamScore != null && awayTeamScore != null) {
            return "📅 " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + "\n" +
                    homeTeam + " " + homeTeamScore + " : " + awayTeamScore + " " + awayTeam;
        } else {
            return "📅 " + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) + "\n" +
                    homeTeam + " - " + awayTeam;
        }
    }
}
