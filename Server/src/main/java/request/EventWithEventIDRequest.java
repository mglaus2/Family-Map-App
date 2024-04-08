package request;

/**
 * The event with an eventID request.
 */
public class EventWithEventIDRequest {
    /**
     * The eventID if the event is associated with the current user.
     */
    String eventID;

    String authtoken;

    public String getEventID() {
        return eventID;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public EventWithEventIDRequest(String eventID, String authtoken) {
        this.eventID = eventID;
        this.authtoken = authtoken;
    }
}
