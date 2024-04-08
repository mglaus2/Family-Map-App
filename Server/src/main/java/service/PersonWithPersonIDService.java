package service;

import dao.AuthtokenDAO;
import dao.Database;
import dao.PersonDAO;
import model.Person;
import request.PersonWithPersonIDRequest;
import response.PersonWithPersonIDResponse;

/**
 * The person with personID service.
 */
public class PersonWithPersonIDService {
    /**
     * Returns the single Person object associated with the personID.
     *
     * @param r The person request that contains all the details regarding the person request.
     * @return A java object that has all the details about the results of the person.
     */
    public PersonWithPersonIDResponse person(PersonWithPersonIDRequest r) {
        Database db = new Database();
        try {
            db.openConnection();

            if(r.getAuthtoken() == null || r.getPersonID() == null) {
                db.closeConnection(false);
                return new PersonWithPersonIDResponse("Error: Invalid input.");
            }

            AuthtokenDAO tempAuthtokenDAO = new AuthtokenDAO(db.getConnection());
            String username = tempAuthtokenDAO.getUsernameByAuthtoken(r.getAuthtoken());

            PersonDAO tempPersonDAO = new PersonDAO(db.getConnection());
            Person person = tempPersonDAO.getPersonByPersonIDAndAssociatedUsername(r.getPersonID(), username);
            if(person == null) {
                db.closeConnection(false);
                return new PersonWithPersonIDResponse("Error: Requested person does not belong to this user.");
            }

            db.closeConnection(true);
            return new PersonWithPersonIDResponse(person);
        } catch (Exception e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new PersonWithPersonIDResponse("Error: Server error.");
        }
    }
}
