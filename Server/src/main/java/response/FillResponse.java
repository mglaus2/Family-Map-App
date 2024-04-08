package response;

/**
 * The fill response.
 */
public class FillResponse {
    /**
     * A message to describe the number of people and events added to the database or the error that happened.
     */
    String message;
    /**
     * A boolean on whether the service worked or not.
     */
    boolean success;

    public FillResponse(String message) {
        this.message = message;
        success = false;
    }

    public FillResponse(int numOfPeople) {
        message = "Successfully added " + numOfPeople + " persons and " + (numOfPeople * 3) + " events to the database.";
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
