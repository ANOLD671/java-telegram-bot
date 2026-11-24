/**
 * An abstraction for retrieving data on football matches.
 */
public interface FootballDataProvider {

    /**
     * Get the next past football league match.
     * @return A single past match, or null if none available.
     * @throws DataProviderException if data source unavailable
     */
    Match getPastMatch() throws DataProviderException;

    /**
     * Get the next upcoming football league match.
     * @return A single upcoming match, or null if none available.
     * @throws DataProviderException if data source unavailable
     */
    Match getUpcomingMatch() throws DataProviderException;

    /**
     * Get the next past football league match for the input team.
     * @param teamName input team
     * @return A single past match for the team, or null if none available.
     * @throws DataProviderException if data source unavailable
     */
    Match getPastMatchForFT(String teamName) throws DataProviderException;

    /**
     * Get the next upcoming football league match for the input team.
     * @param teamName input team
     * @return A single upcoming match for the team, or null if none available.
     * @throws DataProviderException if data source unavailable
     */
    Match getUpcomingMatchForFT(String teamName) throws DataProviderException;
}