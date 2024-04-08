package response;

import model.Event;

/**
 * The event response.
 */
public class EventResponse {
    /**
     * An array of all events for all family members of the current user.
     */
    Event[] data;

    /**
     * A boolean on whether the service worked or not.
     */
    boolean success;

    /**
     * A message to describe the error that happened.
     */
    String message;

    public EventResponse(String message) {
        this.message = message;
        success = false;
    }

    public EventResponse(Event[] data) {
        this.data = data;
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public Event[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
