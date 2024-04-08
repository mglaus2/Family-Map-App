package service;

import dao.AuthtokenDAO;
import dao.Database;
import dao.EventDAO;
import model.Event;
import request.EventRequest;
import response.EventResponse;

/**
 * The event service.
 */
public class EventService {
    /**
     * Returns all events for all family members of the current user (determined by the provided authtoken).
     *
     * @return All the events for all family members and/or the results of the event.
     */
    public EventResponse event(EventRequest r) {
        Database db = new Database();
        try {
            db.openConnection();

            if(r.getAuthtoken() == null) {                          // makes sure authtoken has contents in it
                db.closeConnection(false);
                return new EventResponse("Error: Invalid input.");
            }

            AuthtokenDAO tempAuthtokenDAO = new AuthtokenDAO(db.getConnection());
            String username = tempAuthtokenDAO.getUsernameByAuthtoken(r.getAuthtoken());

            EventDAO tempEventDAO = new EventDAO(db.getConnection());
            Event[] eventArray = tempEventDAO.getEventArrayByAssociatedUsername(username);

            if(eventArray == null) {
                db.closeConnection(false);
                return new EventResponse("Error: Invalid authtoken.");
            }

            db.closeConnection(true);
            return new EventResponse(eventArray);
        } catch (Exception e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new EventResponse("Error: Server error.");
        }
    }
}
