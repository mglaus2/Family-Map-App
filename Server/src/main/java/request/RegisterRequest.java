package request;

import model.User;

/**
 * The register request.
 */
public class RegisterRequest {
    /**
     * The username of the user.
     */
    String username;
    /**
     * The password of the user.
     */
    String password;
    /**
     * The email of the user.
     */
    String email;
    /**
     * The first name of the user.
     */
    String firstName;
    /**
     * The last name of the user.
     */
    String lastName;
    /**
     * The gender of the user (m or f).
     */
    String gender;

    public RegisterRequest(User user) {
        username = user.getUsername();
        password = user.getPassword();
        email = user.getEmail();
        firstName = user.getFirstName();
        lastName = user.getLastName();
        gender = user.getGender();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}
