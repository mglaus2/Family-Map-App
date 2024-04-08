package service;

import dao.AuthtokenDAO;
import dao.Database;
import dao.UserDAO;
import helper.GenerateFamilyTree;
import model.Authtoken;
import model.Person;
import model.User;
import request.RegisterRequest;
import response.RegisterResponse;

import java.util.UUID;

/**
 * The register service.
 */
public class RegisterService {
    /**
     * This function creates a new user account, generates 4 generations of ancestor data for the new user, logs the
     * user in, and returns an authtoken.
     *
     * @param r The register request that contains all the details regarding the registers request.
     * @return A java object that has all the details about the results of the registration.
     */
    public RegisterResponse register(RegisterRequest r) {
        Database db = new Database();
        try {
            db.openConnection();

            if(r.getUsername() == null || r.getPassword() == null || r.getEmail() == null || r.getFirstName() == null
                    || r.getLastName() == null || r.getGender() == null) {
                db.closeConnection(false);
                return new RegisterResponse("Error: Invalid input.");
            }

            UserDAO tempUserDAO = new UserDAO(db.getConnection());
            if(tempUserDAO.getUserByUsername(r.getUsername()) != null) {
                db.closeConnection(false);
                return new RegisterResponse("Error: Username already exists.");
            }

            UUID personUUID = UUID.randomUUID();
            String personID = personUUID.toString();
            User tempUser = new User(r.getUsername(), r.getPassword(), r.getEmail(), r.getFirstName(), r.getLastName(),
                    r.getGender(), personID);
            tempUserDAO.insertUser(tempUser);

            Person userPerson = new Person(personID, r.getUsername(), r.getFirstName(), r.getLastName(), r.getGender());

            GenerateFamilyTree generateFamilyTree = new GenerateFamilyTree();
            generateFamilyTree.generatePerson(userPerson, 4, 0,
                    tempUser.getUsername(), db.getConnection());

            UUID authtokenUUID = UUID.randomUUID();
            String authtokenID = authtokenUUID.toString();

            AuthtokenDAO authtokenDAO = new AuthtokenDAO(db.getConnection());
            Authtoken authtoken = new Authtoken(authtokenID, r.getUsername());
            authtokenDAO.insertAuthtoken(authtoken);

            db.closeConnection(true);
            return new RegisterResponse(authtokenID, tempUser.getUsername(), tempUser.getPersonID());
        } catch (Exception e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new RegisterResponse("Error: Server error.");
        }
    }
}
