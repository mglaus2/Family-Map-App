package request;

/**
 * The person request.
 */
public class PersonRequest {
    /**
     * The authtoken of the user.
     */
    String authtoken;

    public PersonRequest(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getAuthtoken() {
        return authtoken;
    }
}
