package response;

import model.Person;

/**
 * The person with personID response.
 */
public class PersonWithPersonIDResponse {
    /**
     * The associated username of the user.
     */
    String associatedUsername;

    /**
     * The personID of the user.
     */
    String personID;

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

    /**
     * The fatherID of the user.
     */
    String fatherID;

    /**
     * The motherID of the user.
     */
    String motherID;

    /**
     * The spouseID of the user.
     */
    String spouseID;

    /**
     * A boolean on whether the service worked or not.
     */
    boolean success;

    /**
     * A message to describe the error that happened.
     */
    String message;

    public PersonWithPersonIDResponse(String message) {
        this.message = message;
        success = false;
    }

    public PersonWithPersonIDResponse(Person p) {
        associatedUsername = p.getAssociatedUsername();
        personID = p.getPersonID();
        firstName = p.getFirstName();
        lastName = p.getLastName();
        gender = p.getGender();
        fatherID = p.getFatherID();
        motherID = p.getMotherID();
        spouseID = p.getSpouseID();
        success = true;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
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

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
