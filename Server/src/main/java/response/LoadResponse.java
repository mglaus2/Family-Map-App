package response;

/**
 * The load response.
 */
public class LoadResponse {
    /**
     * A message to describe the number of users, persons and events added to the database or the error that happened.
     */
    String message;
    /**
     * A boolean on whether the service worked or not.
     */
    boolean success;

    public LoadResponse(String message) {
        this.message = message;
        success = false;
    }

    public LoadResponse(int numOfUsers, int numOfPeople, int numOfEvents) {
        message = "Successfully added " + numOfUsers + " users, " + numOfPeople + " persons, and " + numOfEvents
                + " events to the database.";
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
