package service;

import dao.*;
import helper.GenerateFamilyTree;
import model.Person;
import model.User;
import request.FillRequest;
import response.FillResponse;

import java.io.FileNotFoundException;

/**
 * The fill service.
 */
public class FillService {
    /**
     * Populates the server's database with generated data for the specified username (has to be an exsisting username
     * and deletes data in database that is already associated with the given username). The generations parameter
     * allows you to change the amount of generations created (default is 4).
     *
     * @param r The fill request that contains all the details regarding the fill request.
     * @return A java object that has all the details about the results of the fill.
     */
    public FillResponse fill(FillRequest r) {
        Database db = new Database();
        try {
            db.openConnection();
            int numOfGenerations = r.getNumOfGenerations();

            if(r.getUsername() == null || numOfGenerations < 0) {    // makes suer the request contains a username and
                db.closeConnection(false);                   // that numOfGenerations is not negative
                return new FillResponse("Error: Invalid input.");
            }

            UserDAO tempUserDAO = new UserDAO(db.getConnection());
            User tempUser = tempUserDAO.getUserByUsername(r.getUsername());
            if(tempUser == null) {                              // makes sure there is a user with username in request
                db.closeConnection(false);
                return new FillResponse("Error: Username does not exist.");
            }

            PersonDAO tempPersonDAO = new PersonDAO(db.getConnection());
            EventDAO tempEventDAO = new EventDAO(db.getConnection());
            tempPersonDAO.clearPersonsTableAssociatedWithUser(r.getUsername());   // clears tables associated with user
            tempEventDAO.clearEventsTableAssociatedWithUser(r.getUsername());

            Person userPerson = new Person(tempUser.getPersonID(), tempUser.getUsername(), tempUser.getFirstName(),
                    tempUser.getLastName(), tempUser.getGender());

            GenerateFamilyTree generateFamilyTree = new GenerateFamilyTree();
            int numOfPeopleCreated = generateFamilyTree.generatePerson(userPerson, numOfGenerations,
                    0, tempUser.getUsername(), db.getConnection());   // generates users family tree

            db.closeConnection(true);
            return new FillResponse(numOfPeopleCreated);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new FillResponse("Error: Server error.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new FillResponse("Error: Error generating people and events.");
        }
    }
}
