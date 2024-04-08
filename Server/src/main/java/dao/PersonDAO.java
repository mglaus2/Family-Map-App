package dao;

import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The PersonDAO class to input, access, and manage data into the person table of the database.
 */
public class PersonDAO {
    /**
     * The connection to the database.
     */
    private final Connection conn;

    /**
     * Constructs an PersonDao by setting the connection to the database.
     *
     * @param conn the connection to the database.
     */
    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a person into the person table of the database.
     *
     * @param person the person object that we will get the data from to put into the database.
     */
    public void insertPerson(Person person) throws DataAccessException {
        String sql = "INSERT INTO Persons (PersonID, AssociatedUsername, FirstName, LastName, Gender, " +
                "FatherID, MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a person into the database");
        }
    }

    /**
     * Find person in person table of the database by their personID.
     *
     * @param personID the personID of the person being found.
     * @return the person object that is associated with the personID.
     */
    public Person getPersonByID(String personID) throws DataAccessException {
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    public Person[] getPersonArrayByAssociatedUsername(String username) throws DataAccessException {
        Person person;
        ArrayList<Person> arrayList = new ArrayList<>();       // creates an array list that will store the elements of
        Person[] personArray = new Person[0];                  // the array which will be copied into eventArray
        ResultSet rs;
        String sql = "SELECT * FROM Persons WHERE AssociatedUsername = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while(rs.next()) {                              // iterates throughout all the elements stored in database
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));
                arrayList.add(person);
            }
            if(arrayList.isEmpty()) {
                return null;
            }

            personArray = arrayList.toArray(personArray);     // copies the elements in array list into eventArray
            return personArray;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    public Person getPersonByPersonIDAndAssociatedUsername(String personID, String username) throws DataAccessException {
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Persons WHERE PersonID = ? AND AssociatedUsername = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            stmt.setString(2, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"), rs.getString("AssociatedUsername"),
                        rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("FatherID"),
                        rs.getString("MotherID"), rs.getString("SpouseID"));
                return person;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    /**
     * Deletes all the data from the person table of the database.
     *
     * @throws DataAccessException if a SQL error occurs.
     */
    public void clearPersonsTable() throws DataAccessException {
        String sql = "DELETE FROM Persons";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table");
        }
    }

    public void clearPersonsTableAssociatedWithUser(String associatedUsername) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE AssociatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, associatedUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table associated with user "
                    + associatedUsername);
        }
    }
}
