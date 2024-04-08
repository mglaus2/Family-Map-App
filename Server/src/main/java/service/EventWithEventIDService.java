package service;

import dao.AuthtokenDAO;
import dao.Database;
import dao.EventDAO;
import model.Event;
import request.EventWithEventIDRequest;
import response.EventWithEventIDResponse;

/**
 * The event with an eventID service.
 */
public class EventWithEventIDService {
    /**
     * Returns the single Event object with the specified ID (only if the event is associated with the user).
     *
     * @param r The event request that contains all the details regarding the event request.
     * @return A java object that has all the details about the results of the event.
     */
    public EventWithEventIDResponse event(EventWithEventIDRequest r) {
        Database db = new Database();
        try {
            db.openConnection();

            if(r.getAuthtoken() == null || r.getEventID() == null) {
                db.closeConnection(false);
                return new EventWithEventIDResponse("Error: Invalid input.");
            }

            AuthtokenDAO tempAuthtokenDAO = new AuthtokenDAO(db.getConnection());
            String username = tempAuthtokenDAO.getUsernameByAuthtoken(r.getAuthtoken());

            EventDAO tempEventDAO = new EventDAO(db.getConnection());
            Event event = tempEventDAO.getEventByEventIDAndAssociatedUsername(r.getEventID(), username);

            if(event == null) {
                db.closeConnection(false);
                return new EventWithEventIDResponse("Error: Requested event does not belong to this user.");
            }

            db.closeConnection(true);
            return new EventWithEventIDResponse(event);
        } catch (Exception e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new EventWithEventIDResponse("Error: Server error.");
        }
    }
}
