/**
 * Exception thrown when an error occurs while fetching or processing football match data
 * from a {@link FootballDataProvider} (e.g., network failure or API error).
 */
public class DataProviderException extends RuntimeException{
    /**
     * Constructs a new DataProviderException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the failure
     */
    public DataProviderException(String message) {super(message);}

    /**
     * Constructs a new DataProviderException with the specified detail message and cause.
     *
     * @param message the detail message explaining the reason for the failure
     * @param cause   the underlying cause of the exception
     */
    public DataProviderException(String message, Throwable cause) {super(message, cause);}
}
