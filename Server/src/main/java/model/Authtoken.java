package model;

import java.util.Objects;

/**
 * The authorization token model class.
 */
public class Authtoken {
    /**
     * The unique authtoken.
     */
    private String authtoken;
    /**
     * The username that is associated with this authtoken.
     */
    private String username;

    /**
     * Creates an authtoken with all variables set.
     *
     * @param authtoken the unique authtoken.
     * @param username the username that is associated with this authtoken.
     */
    public Authtoken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Overridden equals function that compares an inputted object and the current authtoken.
     *
     * @param o object that is being compared to current authtoken.
     * @return boolean about whether the authtoken and object being compared are the same.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Authtoken authtoken1 = (Authtoken) o;
        return Objects.equals(authtoken, authtoken1.authtoken) && Objects.equals(username, authtoken1.username);
    }

}
