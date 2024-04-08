package model;

import java.util.Objects;

/**
 * The event model class.
 */
public class Event {

    /**
     * The unique identifier for this event.
     */
    private String eventID;
    /**
     * The username of the user that this event belongs to.
     */
    private String associatedUsername;
    /**
     * The ID of the person that this event belongs to.
     */
    private String personID;
    /**
     * The latitude of this event's location.
     */
    private Float latitude;
    /**
     * The longitude of this event's location.
     */
    private Float longitude;
    /**
     * The country where this event occurred.
     */
    private String country;
    /**
     * The city where this event occurred.
     */
    private String city;
    /**
     * The type of event.
     */
    private String eventType;
    /**
     * The year this event occurred.
     */
    private Integer year;

    /**
     * Creates an event with all variables set.
     *
     * @param eventID an identifier for this event.
     * @param username the username of the user that this event belongs to.
     * @param personID the ID of the user that this event belongs to.
     * @param latitude the latitude of this event's location.
     * @param longitude the longitude of this event's location.
     * @param country the country this event occurred in.
     * @param city the city this event occurred in.
     * @param eventType the type of event this event is.
     * @param year the year this event occurred in.
     */
    public Event(String eventID, String username, String personID, Float latitude, Float longitude,
                 String country, String city, String eventType, Integer year) {
        this.eventID = eventID;
        this.associatedUsername = username;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public String getEventID() {
        return eventID;
    }


    public String getAssociatedUsername() {
        return associatedUsername;
    }


    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public Float getLatitude() {
        return latitude;
    }


    public Float getLongitude() {
        return longitude;
    }


    public String getCountry() {
        return country;
    }


    public String getCity() {
        return city;
    }


    public String getEventType() {
        return eventType;
    }


    public Integer getYear() {
        return year;
    }


    /**
     * Overridden equals function that compares an inputted object and the current event.
     *
     * @param o object that is being compared to current event.
     * @return boolean about whether the event and object being compared are the same.
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(eventID, event.eventID) && Objects.equals(associatedUsername, event.associatedUsername)
                && Objects.equals(personID, event.personID) && Objects.equals(latitude, event.latitude) &&
                Objects.equals(longitude, event.longitude) && Objects.equals(country, event.country) &&
                Objects.equals(city, event.city) && Objects.equals(eventType, event.eventType) &&
                Objects.equals(year, event.year);
    }

}
