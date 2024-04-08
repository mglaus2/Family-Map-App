package response;

/**
 * The clear result.
 */
public class ClearResponse {
    String message;
    /**
     * A boolean on whether the service worked or not.
     */
    boolean success;
    /**
     * A message to describe the error that happened.
     */

    public ClearResponse(String message) {
        this.message = message;
        success = false;
    }

    public ClearResponse() {
        message = "Clear succeeded.";
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }
}
