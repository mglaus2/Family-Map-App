package service;

import dao.Database;
import dao.EventDAO;
import dao.PersonDAO;
import dao.UserDAO;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import response.LoadResponse;

/**
 * The load service.
 */
public class LoadService {
    /**
     * Clears all data from the database and then load the user, person, and event data from the request body into the
     * database.
     *
     * @param r The load request that contains all the details regarding the load request.
     * @return A java object that has all the details about the results of the load.
     */
    public LoadResponse load(LoadRequest r) {
        Database db = new Database();
        try {
            db.openConnection();

            if(r.getUsers() == null || r.getPersons() == null || r.getEvents() == null) {
                db.closeConnection(false);
                return new LoadResponse("Error: Invalid input.");
            }

            User[] users = r.getUsers();
            Person[] persons = r.getPersons();
            Event[] events = r.getEvents();

            UserDAO tempUserDAO = new UserDAO(db.getConnection());
            PersonDAO tempPersonDAO = new PersonDAO(db.getConnection());
            EventDAO tempEventDAO = new EventDAO(db.getConnection());

            for(int i = 0; i < users.length; i++) {         // inserts the json data given into database
                tempUserDAO.insertUser(users[i]);
            }

            for(int i = 0; i < persons.length; i++) {
                tempPersonDAO.insertPerson(persons[i]);
            }

            for(int i = 0; i < events.length; i++) {
                tempEventDAO.insertEvent(events[i]);
            }

            db.closeConnection(true);
            return new LoadResponse(users.length, persons.length, events.length);
        } catch (Exception e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new LoadResponse("Error: Server error.");
        }
    }
}
