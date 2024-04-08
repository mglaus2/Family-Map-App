package request;

/**
 * The event request.
 */
public class EventRequest {
    /**
     * The authtoken of the user.
     */
    String authtoken;

    public EventRequest(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getAuthtoken() {
        return authtoken;
    }
}
