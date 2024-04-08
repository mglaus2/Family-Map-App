package response;

/**
 * The register response.
 */
public class RegisterResponse {
    /**
     * The authtoken of the user.
     */
    String authtoken;
    /**
     * The username of the user.
     */
    String username;
    /**
     * The personID of the user.
     */
    String personID;
    /**
     * A boolean on whether the service worked or not.
     */
    boolean success;
    /**
     * A message to describe the error that happened.
     */
    String message;

    public RegisterResponse(String authtoken, String username, String personID) {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        success = true;
    }

    public RegisterResponse(String message) {
        this.message = message;
        success = false;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }

    public String getMessage() {
        return message;
    }
}
