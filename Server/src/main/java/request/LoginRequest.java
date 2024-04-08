package request;

/**
 * The register request.
 */
public class LoginRequest {
    /**
     * The username of the user.
     */
    String username;

    /**
     * The password of the user.
     */
    String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
}
