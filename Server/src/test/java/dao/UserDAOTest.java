package dao;

import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class UserDAOTest {
    private Database db;
    private User testUser;
    private UserDAO uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        testUser = new User("tim_J", "timIsTheMan21", "tim_J@gmail.com",
                "Tim", "Johnson", "m", "Tim3241J");

        Connection conn = db.getConnection();

        uDao = new UserDAO(conn);

        uDao.clearUsersTable();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertUserPass() throws DataAccessException {
        uDao.insertUser(testUser);
        User compareTest = uDao.getUserByUsername(testUser.getUsername());

        assertNotNull(compareTest);
        assertEquals(testUser, compareTest);
    }

    @Test
    public void insertUserFail() throws DataAccessException {
        uDao.insertUser(testUser);

        assertThrows(DataAccessException.class, () -> uDao.insertUser(testUser));
    }

    @Test
    public void getUserByUsernamePass() throws DataAccessException {
        User compareTest = uDao.getUserByUsername(testUser.getUsername());
        assertNull(compareTest);

        uDao.insertUser(testUser);
        compareTest = uDao.getUserByUsername(testUser.getUsername());

        assertNotNull(compareTest);
        assertEquals(testUser, compareTest);
    }

    @Test
    public void getUserByUsernameFail() throws DataAccessException {
        User compareTest = uDao.getUserByUsername(testUser.getUsername());
        assertNull(compareTest);
    }

    @Test
    public void validatePass() throws DataAccessException {
        uDao.insertUser(testUser);
        assertTrue(uDao.validate(testUser.getUsername(), testUser.getPassword()));
    }

    @Test
    public void validateFail() throws DataAccessException {
        assertFalse(uDao.validate(testUser.getUsername(), testUser.getPassword()));
    }

    @Test
    public void clearUsersTableWithUserInsertedTest() throws DataAccessException {
        uDao.insertUser(testUser);
        uDao.clearUsersTable();

        User compareTest = uDao.getUserByUsername(testUser.getUsername());
        assertNull(compareTest);
    }

    @Test
    public void clearUsersTableWithUserNotInsertedTest() throws DataAccessException {
        uDao.clearUsersTable();

        User compareTest = uDao.getUserByUsername(testUser.getUsername());
        assertNull(compareTest);
    }

}
