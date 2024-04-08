package dao;

import model.Event;
import model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The EventDAO class to input, access, and manage data into the event table of the database.
 */
public class EventDAO {
    /**
     * The connection to the database.
     */
    private final Connection conn;

    /**
     * Constructs an EventDao by setting the connection to the database.
     * @param conn the connection to the database.
     */
    public EventDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a user into the user table of the database.
     *
     * @param event the event object that we will get the data from to put into the database.
     * @throws DataAccessException if a SQL error occurs.
     */

    public void insertEvent(Event event) throws DataAccessException {
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an event into the database");
        }
    }

    /**
     * Find event in event table of the database by its eventID.
     *
     * @param eventID the eventID of the event being found.
     * @return the event object that is associated with the eventID.
     * @throws DataAccessException if a SQL error occurs.
     */
    public Event getEventByEventID(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"),
                        rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    public Event getEventByEventIDAndAssociatedUsername(String eventID, String username) throws DataAccessException {
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Events WHERE EventID = ? AND AssociatedUsername = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            stmt.setString(2, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"),
                        rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    public Event[] getEventArrayByAssociatedUsername(String username) throws DataAccessException {
        Event event;
        ArrayList<Event> arrayList = new ArrayList<>();       // creates an array list that will store the elements of
        Event[] eventArray = new Event[0];                    // the array which will be copied into eventArray
        ResultSet rs;
        String sql = "SELECT * FROM Events WHERE AssociatedUsername = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while(rs.next()) {                               // iterates throughout all the elements stored in database
                event = new Event(rs.getString("EventID"), rs.getString("AssociatedUsername"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"),
                        rs.getFloat("Longitude"), rs.getString("Country"),
                        rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                arrayList.add(event);
            }
            if(arrayList.isEmpty()) {
                return null;
            }
            eventArray = arrayList.toArray(eventArray);         // copies the elements in array list into eventArray
            return eventArray;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    /**
     * Deletes all the data from the event table of the database.
     *
     * @throws DataAccessException if a SQL error occurs.
     */
    public void clearEventsTable() throws DataAccessException {
        String sql = "DELETE FROM Events";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table");
        }
    }

    public void clearEventsTableAssociatedWithUser(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE AssociatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the event table associated with user "
                    + associatedUsername);
        }
    }
}

