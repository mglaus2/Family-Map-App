package request;

import model.Event;
import model.Person;
import model.User;

/**
 * The load request.
 */
public class LoadRequest {
    /**
     * An array of users from the request body.
     */
    User[] users;
    /**
     * An array of persons from the request body.
     */
    Person[] persons;
    /**
     * An array of events from the request body.
     */
    Event[] events;

    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public LoadRequest() {
        users = null;
        persons = null;
        events = null;
    }
}
