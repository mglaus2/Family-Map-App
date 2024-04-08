package service;

import dao.AuthtokenDAO;
import dao.Database;
import dao.PersonDAO;
import model.Person;
import request.PersonRequest;
import response.PersonResponse;

/**
 * The person service.
 */
public class PersonService {
    /**
     * Returns all family members of the current user (determined by the authtoken provided).
     *
     * @return PersonResponse containing the family members of the current user.
     */
    public PersonResponse person(PersonRequest r) {
        Database db = new Database();
        try {
            db.openConnection();

            if(r.getAuthtoken() == null) {
                db.closeConnection(false);
                return new PersonResponse("Error: Invalid input.");
            }

            AuthtokenDAO tempAuthtokenDAO = new AuthtokenDAO(db.getConnection());
            String username = tempAuthtokenDAO.getUsernameByAuthtoken(r.getAuthtoken());

            PersonDAO tempPersonDAO = new PersonDAO(db.getConnection());
            Person[] personArray = tempPersonDAO.getPersonArrayByAssociatedUsername(username);

            if(personArray == null) {
                db.closeConnection(false);
                return new PersonResponse("Error: Invalid authtoken.");
            }

            db.closeConnection(true);
            return new PersonResponse(personArray);
        } catch (Exception e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new PersonResponse("Error: Server error.");
        }
    }
}
