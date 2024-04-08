package dao;

import model.Authtoken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthtokenDAOTest {
    private Database db;
    private Authtoken authtoken;
    private AuthtokenDAO aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        authtoken = new Authtoken("abc123", "tim123");

        Connection conn = db.getConnection();
        aDao = new AuthtokenDAO(conn);
        aDao.clearAuthtokensTable();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertAuthtokenPass() throws DataAccessException {
        aDao.insertAuthtoken(authtoken);
        Authtoken compareTest = aDao.getAuthtokenByUsername(authtoken.getUsername());

        assertNotNull(compareTest);
        assertEquals(authtoken, compareTest);
    }

    @Test
    public void insertAuthtokenFail() throws DataAccessException {
        aDao.insertAuthtoken(authtoken);

        assertThrows(DataAccessException.class, () -> aDao.insertAuthtoken(authtoken));
    }

    @Test
    public void getUsernameByAuthtokenPass() throws DataAccessException {
        String compareTest = aDao.getUsernameByAuthtoken(authtoken.getAuthtoken());
        assertNull(compareTest);

        aDao.insertAuthtoken(authtoken);
        compareTest = aDao.getUsernameByAuthtoken(authtoken.getAuthtoken());

        assertNotNull(compareTest);
        assertEquals(authtoken.getUsername(), compareTest);
    }

    @Test
    public void getUsernameByAuthtokenFail() throws DataAccessException {
        String compareTest = aDao.getUsernameByAuthtoken(authtoken.getAuthtoken());
        assertNull(compareTest);
    }

    @Test
    public void getAuthtokenByUsernamePass() throws DataAccessException {
        Authtoken compareTest = aDao.getAuthtokenByUsername(authtoken.getUsername());
        assertNull(compareTest);

        aDao.insertAuthtoken(authtoken);
        compareTest = aDao.getAuthtokenByUsername(authtoken.getUsername());

        assertNotNull(compareTest);
        assertEquals(authtoken, compareTest);
    }

    @Test
    public void getAuthtokenByUsernameFail() throws DataAccessException {
        Authtoken compareTest = aDao.getAuthtokenByUsername(authtoken.getUsername());
        assertNull(compareTest);
    }

    @Test
    public void clearAuthtokensTableWithAuthtokensInsertedTest() throws DataAccessException {
        aDao.insertAuthtoken(authtoken);
        aDao.clearAuthtokensTable();

        Authtoken compareTest = aDao.getAuthtokenByUsername(authtoken.getUsername());
        assertNull(compareTest);
    }

    @Test
    public void clearAuthtokensTableWithAuthtokensNotInsertedTest() throws DataAccessException {
        aDao.clearAuthtokensTable();

        Authtoken compareTest = aDao.getAuthtokenByUsername(authtoken.getUsername());
        assertNull(compareTest);
    }
}
