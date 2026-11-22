import java.util.Date;
import java.util.List;

/**
 * An abstraction for retrieving data on football matches.
 */
public interface FootballDataProvider {

    /**
     * Get list of entered quantity last football league's matches
     * @param limit number of matches to display
     * @return List of matches played
     * @throws DataProviderException if data source unavailable
     */
    List<Match> getPastMatches(Integer limit) throws DataProviderException;

    /**
     * Get list of entered quantity upcoming football league's matches
     * @param limit number of matches to display
     * @return List of upcoming matches
     * @throws DataProviderException if data source unavailable
     */
    List<Match> getUpcomingMatches(Integer limit) throws DataProviderException;

    /**
     * Get list of last entered quantity football league matches for input team
     * @param limit number of matches to display
     * @param teamName input team
     * @return List of matches played for input team
     * @throws DataProviderException if data source unavailable
     */
    List<Match> getPastMatchesForFT(Integer limit, String teamName) throws DataProviderException;

    /**
     * Get list of entered quantity upcoming football league matches for current team
     * @param limit number of matches to display
     * @param teamName input team
     * @return List of upcoming matches for input team
     * @throws DataProviderException if data source unavailable
     */
    List<Match> getUpcomingMatchesForFT(Integer limit, String teamName) throws DataProviderException;
}