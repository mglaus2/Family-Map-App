package service;

import dao.*;
import response.ClearResponse;

/**
 * The clear service.
 */
public class ClearService {
    /**
     * Deletes all data from database including data from all tables.
     *
     * @return A java object that has all the details about the results of the clear.
     */
    public ClearResponse clear() {
        Database db = new Database();
        try {
            db.openConnection();

            AuthtokenDAO tempAuthtokenDAO = new AuthtokenDAO(db.getConnection());
            EventDAO tempEventDAO = new EventDAO(db.getConnection());
            PersonDAO tempPersonDAO = new PersonDAO(db.getConnection());
            UserDAO tempUserDAO = new UserDAO(db.getConnection());

            tempAuthtokenDAO.clearAuthtokensTable();
            tempEventDAO.clearEventsTable();
            tempPersonDAO.clearPersonsTable();
            tempUserDAO.clearUsersTable();

            db.closeConnection(true);
            return new ClearResponse();
        } catch (Exception e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new ClearResponse("Error: Server error.");
        }
    }
}
