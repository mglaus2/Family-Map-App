package model;

import java.util.Objects;

/**
 * The person model class.
 */
public class Person {

    /**
     * The unique identifier for this person.
     */
    private String personID;
    /**
     * The username of the user that this person belongs to.
     */
    private String associatedUsername;
    /**
     * The person's first name.
     */
    private String firstName;
    /**
     * The person's last name.
     */
    private String lastName;
    /**
     * The person's gender (m or f).
     */
    private String gender;
    /**
     * The person ID of this person's father (may be null).
     */
    private String fatherID;
    /**
     * The person ID of this person's mother (may be null).
     */
    private String motherID;
    /**
     * The person ID of this person's spouse (may be null).
     */
    private String spouseID;

    /**
     * Creates a person with all variables set.
     *
     * @param personID an identifier for this person.
     * @param username the username of the user that this person belongs to.
     * @param firstName the person's first name.
     * @param lastName the person's last name.
     * @param gender the person's gender.
     * @param fatherID the person ID of this person's father.
     * @param motherID the person ID of this person's mother.
     * @param spouseID the person ID of this person's spouse.
     */

    public Person(String personID, String username, String firstName, String lastName, String gender,
                 String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    /**
     * Creates a person with all variables except fatherID, motherID, and spouseID set because they can be null.
     *
     * @param personID an identifier for this person.
     * @param username the username of the user that this person belongs to.
     * @param firstName the person's first name.
     * @param lastName the person's last name.
     * @param gender the person's gender.
     */
    public Person(String personID, String username, String firstName, String lastName, String gender) {
        this.personID = personID;
        this.associatedUsername = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public Person(String personID, String username, String firstName, String lastName, String gender, String spouseID) {
        this.personID = personID;
        this.associatedUsername = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.spouseID = spouseID;
    }


    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }


    /**
     * Overridden toString function to print out the current person information.
     *
     * @return string containing the current person information.
     */

    @Override
    public String toString() {
        return null;
    }

    /**
     * Overridden equals function that compares an inputted object and the current person.
     *
     * @param o object that is being compared to current person.
     * @return boolean about whether the person and object being compared are the same.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(personID, person.personID) && Objects.equals(associatedUsername,
                person.associatedUsername) && Objects.equals(firstName, person.firstName) &&
                Objects.equals(lastName, person.lastName) && Objects.equals(gender, person.gender) &&
                Objects.equals(fatherID, person.fatherID) && Objects.equals(motherID, person.motherID) &&
                Objects.equals(spouseID, person.spouseID);
    }

}

