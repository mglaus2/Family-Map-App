package response;

import model.Event;

/**
 * The event with eventID response.
 */
public class EventWithEventIDResponse {
    /**
     * The associated username of the current user.
     */
    String associatedUsername;

    /**
     * The eventID associated with the current user.
     */
    String eventID;

    /**
     * The personID of the current user.
     */
    String personID;

    /**
     * The latitude of the event associated with the current user.
     */
    float latitude;

    /**
     * The longitude of the event associated with the current user.
     */
    float longitude;

    /**
     * The country of the event associated with the current user.
     */
    String country;

    /**
     * The city of the event associated with the current user.
     */
    String city;

    /**
     * The eventType of the event associated with the current user.
     */
    String eventType;

    /**
     * The year of the event associated with the current user.
     */
    int year;

    /**
     * A boolean on whether the service worked or not.
     */
    boolean success;

    /**
     * A message to describe the error that happened.
     */
    String message;

    public EventWithEventIDResponse(String message) {
        this.message = message;
        success = false;
    }

    public EventWithEventIDResponse(Event e) {
        associatedUsername = e.getAssociatedUsername();
        eventID = e.getEventID();
        personID = e.getPersonID();
        latitude = e.getLatitude();
        longitude = e.getLongitude();
        country = e.getCountry();
        city = e.getCity();
        eventType = e.getEventType();
        year = e.getYear();
        success = true;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getEventID() {
        return eventID;
    }

    public String getPersonID() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
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

    public int getYear() {
        return year;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}
