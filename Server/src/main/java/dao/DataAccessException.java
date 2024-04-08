package dao;

/**
 * Class that contains the error associated with accessing data from the server.
 */
public class DataAccessException extends Exception {
    /**
     * Error that can be thrown when trying to access data from the server, and it fails.
     */
    DataAccessException(String message) {
        super(message);
    }
}
