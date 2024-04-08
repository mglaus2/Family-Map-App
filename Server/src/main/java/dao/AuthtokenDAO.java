package dao;

import model.Authtoken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The AuthtokenDAO class to input, access, and manage data into the Authtoken table of the database.
 */
public class AuthtokenDAO {
    /**
     * The connection to the database.
     */
    private final Connection conn;

    /**
     * Constructs an AuthTokenDao by setting the connection to the database.
     *
     * @param conn the connection to the database.
     */
    public AuthtokenDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts an authtoken into the authtoken table of the database.
     *
     * @param authtoken the authtoken object that we will get the data from to put into the database.
     */
    public void insertAuthtoken(Authtoken authtoken) throws DataAccessException {
        String sql = "INSERT INTO Authtokens (Authtoken, Username) VALUES(?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authtoken.getAuthtoken());
            stmt.setString(2, authtoken.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an authtoken into the database");
        }

    }

    /**
     * Find authtoken in authtoken table of the database by their authtoken.
     *
     * @param authtoken the authtoken of the person being found.
     * @return the username that is associated with the authtoken.
     */
    public String getUsernameByAuthtoken(String authtoken) throws DataAccessException {
        String username;
        ResultSet rs;
        String sql = "SELECT * FROM Authtokens WHERE Authtoken = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authtoken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                username = rs.getString("Username");
                return username;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an authtoken in the database");
        }
    }

    public Authtoken getAuthtokenByUsername(String username) throws DataAccessException {
        Authtoken authtoken;
        ResultSet rs;
        String sql = "SELECT * FROM Authtokens Where Username = ?;";

        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if(rs.next()) {
                authtoken = new Authtoken(rs.getString("Authtoken"), rs.getString("Username"));
                return authtoken;
            }
            else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an authtoken in the database");
        }
    }

    /**
     * Deletes all the data from the authtoken table of the database.
     *
     * @throws DataAccessException if a SQL error occurs.
     */
    public void clearAuthtokensTable() throws DataAccessException {
        String sql = "DELETE FROM Authtokens";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the authtokens table");
        }
    }
}
