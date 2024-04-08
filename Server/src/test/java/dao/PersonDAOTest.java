package dao;

import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {
    private Database db;
    private Person testPersonTim;
    private Person testPersonAlex;
    private Person testPersonJohn;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        testPersonTim = new Person("Tim3241J", "Tim", "Tim",
                "Johnson", "m", "Robert2551P", "Kim1943I",
                "Susan2001E");

        testPersonJohn = new Person("Alex4132", "Alex", "Alex", "Smith", "m",
                "Smith32451", "Susan3424", "Kelly3332");

        testPersonAlex = new Person("John32", "Alex", "John", "Gabe", "m");

        Connection conn = db.getConnection();

        pDao = new PersonDAO(conn);

        pDao.clearPersonsTable();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertPersonPass() throws DataAccessException {
        pDao.insertPerson(testPersonTim);
        Person compareTest = pDao.getPersonByID(testPersonTim.getPersonID());

        assertNotNull(compareTest);
        assertEquals(testPersonTim, compareTest);
    }

    @Test
    public void insertPersonFail() throws DataAccessException {
        pDao.insertPerson(testPersonTim);

        assertThrows(DataAccessException.class, () -> pDao.insertPerson(testPersonTim));
    }

    @Test
    public void getPersonByPersonIDPass() throws DataAccessException {
        Person compareTest = pDao.getPersonByID(testPersonTim.getPersonID());
        assertNull(compareTest);

        pDao.insertPerson(testPersonTim);
        compareTest = pDao.getPersonByID(testPersonTim.getPersonID());

        assertNotNull(compareTest);
        assertEquals(testPersonTim, compareTest);
    }

    @Test
    public void getPersonByPersonIDFail() throws DataAccessException {
        Person compareTest = pDao.getPersonByID(testPersonTim.getPersonID());
        assertNull(compareTest);
    }

    @Test
    public void getPersonArrayByAssociatedUsernamePass() throws DataAccessException {
        Person[] personArrayJohn = new Person[2];
        personArrayJohn[0] = testPersonJohn;
        personArrayJohn[1] = testPersonAlex;

        Person[] personArrayTim = new Person[1];
        personArrayTim[0] = testPersonTim;

        pDao.insertPerson(testPersonTim);
        pDao.insertPerson(testPersonJohn);
        pDao.insertPerson(testPersonAlex);

        Person[] compareTestArrayJohn = pDao.getPersonArrayByAssociatedUsername(testPersonJohn.getAssociatedUsername());
        Person[] compareTestArrayTim = pDao.getPersonArrayByAssociatedUsername(testPersonTim.getAssociatedUsername());

        assertNotNull(compareTestArrayJohn);
        assertEquals(personArrayJohn[0], compareTestArrayJohn[0]);
        assertEquals(personArrayJohn[1], compareTestArrayJohn[1]);

        assertNotNull(compareTestArrayTim);
        assertEquals(personArrayTim[0], compareTestArrayTim[0]);
    }

    @Test
    public void getPersonArrayByAssociatedUsernameFail() throws DataAccessException {
        Person[] compareTest = pDao.getPersonArrayByAssociatedUsername(testPersonTim.getAssociatedUsername());
        assertNull(compareTest);
    }

    @Test
    public void getPersonByPersonIDAndAssociatedUsernamePass() throws DataAccessException {
        Person compareTest = pDao.getPersonByPersonIDAndAssociatedUsername(testPersonTim.getPersonID(),
                testPersonTim.getAssociatedUsername());
        assertNull(compareTest);

        pDao.insertPerson(testPersonTim);
        compareTest = pDao.getPersonByPersonIDAndAssociatedUsername(testPersonTim.getPersonID(),
                testPersonTim.getAssociatedUsername());

        assertNotNull(compareTest);
        assertEquals(testPersonTim, compareTest);
    }

    @Test
    public void getPersonByPersonIDAndAssociatedUsernameFail() throws DataAccessException {
        Person compareTest = pDao.getPersonByPersonIDAndAssociatedUsername(testPersonTim.getPersonID(),
                testPersonTim.getAssociatedUsername());
        assertNull(compareTest);
    }

    @Test
    public void clearPersonsTableWithPeopleInsertedTest() throws DataAccessException {
        pDao.insertPerson(testPersonTim);
        pDao.insertPerson(testPersonAlex);
        pDao.clearPersonsTable();

        Person compareTestTim = pDao.getPersonByID(testPersonTim.getPersonID());
        Person compareTestAlex = pDao.getPersonByID(testPersonAlex.getPersonID());

        assertNull(compareTestTim);
        assertNull(compareTestAlex);
    }

    @Test
    public void clearPersonsTableWithPeopleNotInsertedTest() throws DataAccessException {
        pDao.clearPersonsTable();

        Person compareTestTim = pDao.getPersonByID(testPersonTim.getPersonID());
        Person compareTestAlex = pDao.getPersonByID(testPersonAlex.getPersonID());

        assertNull(compareTestTim);
        assertNull(compareTestAlex);
    }

    @Test
    public void clearPersonsTableAssociatedWithUsersWithPeopleInsertedTest() throws DataAccessException {
        pDao.insertPerson(testPersonTim);
        pDao.insertPerson(testPersonAlex);
        pDao.clearPersonsTableAssociatedWithUser(testPersonTim.getAssociatedUsername());

        Person compareTestTim = pDao.getPersonByID(testPersonTim.getPersonID());
        Person compareTestAlex = pDao.getPersonByID(testPersonAlex.getPersonID());

        assertNull(compareTestTim);
        assertNotNull(compareTestAlex);
        assertEquals(testPersonAlex, compareTestAlex);
    }

    @Test
    public void clearPersonsTableAssociatedWithUsersWithPeopleNotInsertedTest() throws DataAccessException {
        pDao.clearPersonsTableAssociatedWithUser(testPersonTim.getAssociatedUsername());

        Person compareTestTim = pDao.getPersonByID(testPersonTim.getPersonID());
        Person compareTestAlex = pDao.getPersonByID(testPersonAlex.getPersonID());

        assertNull(compareTestTim);
        assertNull(compareTestAlex);
    }

}
