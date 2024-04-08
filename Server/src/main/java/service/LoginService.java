package service;

import dao.AuthtokenDAO;
import dao.Database;
import dao.UserDAO;
import model.Authtoken;
import model.User;
import request.LoginRequest;
import response.LoginResponse;

import java.util.UUID;

/**
 * The login service.
 */
public class LoginService {
    /**
     * Logs the user in and returns an authtoken.
     *
     * @param r The login request that contains all the details regarding the logins request.
     * @return A java object that has all the details about the results of the login.
     */
    public LoginResponse login(LoginRequest r) {
        Database db = new Database();
        try {
            db.openConnection();

            if(r.getUsername() == null || r.getPassword() == null) {
                db.closeConnection(false);
                return new LoginResponse("Error: Invalid input.");
            }

            UserDAO tempUserDAO = new UserDAO(db.getConnection());
            if(!tempUserDAO.validate(r.getUsername(), r.getPassword())) {
                db.closeConnection(false);
                return new LoginResponse("Error: Username and password do not match.");
            }

            User tempUser = tempUserDAO.getUserByUsername(r.getUsername());

            UUID authtokenUUID = UUID.randomUUID();                     // generates random authtoken
            String authtokenID = authtokenUUID.toString();

            AuthtokenDAO authtokenDAO = new AuthtokenDAO(db.getConnection());
            Authtoken authtoken = new Authtoken(authtokenID, r.getUsername());
            authtokenDAO.insertAuthtoken(authtoken);

            db.closeConnection(true);
            return new LoginResponse(authtokenID, tempUser.getUsername(), tempUser.getPersonID());
        } catch (Exception e) {
            e.printStackTrace();
            db.closeConnection(false);
            return new LoginResponse("Error: Server error.");
        }
    }
}
