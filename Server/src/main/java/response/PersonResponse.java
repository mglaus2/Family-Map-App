package response;

import model.Person;

/**
 * The person response.
 */
public class PersonResponse {
    /**
     * An array of all the family members of the current user.
     */
    Person[] data;

    /**
     * A boolean on whether the service worked or not.
     */
    boolean success;

    /**
     * A message to describe the error that happened.
     */
    String message;

    public PersonResponse(String message) {
        this.message = message;
        success = false;
    }

    public PersonResponse(Person[] data) {
        this.data = data;
        success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public Person[] getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
