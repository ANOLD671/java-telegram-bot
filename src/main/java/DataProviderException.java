import javax.xml.crypto.Data;

public class DataProviderException extends RuntimeException{
    public DataProviderException(String message) {super(message);}

    public DataProviderException(String message, Throwable cause) {super(message, cause);}
}
