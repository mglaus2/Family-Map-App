package request;

/**
 * The person with personID request.
 */
public class PersonWithPersonIDRequest {
    /**
     * The personID if the person is associated with the current user.
     */
    String personID;
    String authtoken;

    public PersonWithPersonIDRequest(String personID, String authtoken) {
        this.personID = personID;
        this.authtoken = authtoken;
    }

    public String getPersonID() {
        return personID;
    }

    public String getAuthtoken() {
        return authtoken;
    }
}
