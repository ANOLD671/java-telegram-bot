import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a match information. Participants teams in match, match date, score
 */

public class Match {
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

    public Match(String homeTeam, String awayTeam, LocalDateTime date, Integer homeTeamScore, Integer awayTeamScore) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.date = date;
        this.homeTeamScore = homeTeamScore;
        this.awayTeamScore = awayTeamScore;
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
    public Integer getAwayTeamScore() {return awayTeamScore;}
    /**
     * Home Team Score getter
     * @return {@link Integer} Home Team Score
     */
    public Integer getHomeTeamScore() {return homeTeamScore;}

    /**
     *
     * @return {@link String} formated match information with emoji
     */
    public String displayMatch() {
        if (homeTeamScore != null && awayTeamScore != null) {
            DateTimeFormatter pastFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return "📅 " + date.format(pastFormatter) + "\n" +
                    homeTeam + " " + homeTeamScore + " : " + awayTeamScore + " " + awayTeam;
        } else {
            DateTimeFormatter upcomingFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            return "📅 " + date.format(upcomingFormatter) + "\n" +
                    homeTeam + " - " + awayTeam;
        }
    }
}
