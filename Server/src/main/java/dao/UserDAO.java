package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * The UserDAO class to input, access, and manage data into the user table of the database.
 */
public class UserDAO {
    /**
     * The connection to the database.
     */
    private final Connection conn;

    /**
     * Constructs an UserDao by setting the connection to the database.
     * @param conn the connection to the database.
     */
    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a user into the user table of the database.
     *
     * @param user the user object that we will get the data from to put into the database.
     */
    public void insertUser(User user) throws DataAccessException {
        String sql = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, " +
                "Gender, PersonID) VALUES(?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a user into the database");
        }
    }

    /**
     * Validates if there is a username with that password in the user table of the database.
     *
     * @param username the username of the user being found.
     * @param password the password of the user being found.
     * @return boolean on if the user is in the User table of the database.
     */
    public boolean validate(String username, String password) throws DataAccessException {
        ResultSet rs;
        String sql = "SELECT Password FROM Users WHERE Username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if(rs.next()) {
                if(Objects.equals(rs.getString("Password"), password)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while validating user in the database");
        }
        return false;
    }

    /**
     * Find user in user table of the database by their userID.
     *
     * @param username the username of the user being found.
     * @return the user object that is associated with the userID.
     */
    public User getUserByUsername(String username) throws DataAccessException {
        User user;
        ResultSet rs;
        String sql = "SELECT * FROM Users WHERE Username = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"),
                        rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("PersonID"));
                return user;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a user in the database");
        }
    }

    /**
     * Deletes all the data from the event table of the database.
     *
     * @throws DataAccessException if a SQL error occurs.
     */
    public void clearUsersTable() throws DataAccessException {
        String sql = "DELETE FROM Users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the user table");
        }
    }
}
