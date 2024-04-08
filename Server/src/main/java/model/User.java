package model;

import java.util.Objects;

/**
 * The user model class.
 */
public class User {
    /**
     * The unique username for the user.
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;
    /**
     * The user's email address.
     */
    private String email;
    /**
     * The user's first name.
     */
    private String firstName;
    /**
     * The user's last name.
     */
    private String lastName;
    /**
     * The user's gender (m or f).
     */
    private String gender;
    /**
     * The Unique Person ID assigned to this user’s generated Person.
     */
    private String personID;

    /**
     * Creates a user with all variables set.
     *
     * @param username the username of the user
     * @param password the user's password
     * @param email the user's email address.
     * @param firstName the user's first name.
     * @param lastName the user's last name.
     * @param gender the user's gender (male(m) or female(f) only
     * @param personID an identifier for the user
     */
    public User(String username, String password, String email,
                 String firstName, String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * Overridden toString function to print out the current user information.
     *
     * @return string containing the current user information.
     */

    @Override
    public String toString() {
        return null;
    }

    /**
     * Overridden equals function that compares an inputted object and the current user.
     *
     * @param o object that is being compared to current user.
     * @return boolean about whether the user and object being compared are the same.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) &&
                Objects.equals(email, user.email) && Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) && Objects.equals(gender, user.gender) &&
                Objects.equals(personID, user.personID);
    }

}

