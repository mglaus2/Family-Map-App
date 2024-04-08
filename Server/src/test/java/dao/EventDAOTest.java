package dao;

import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    private Database db;
    private Event bikingEvent;
    private Event runningEvent;
    private Event swimmingEvent;
    private EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        bikingEvent = new Event("Biking_123A", "Gale", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        runningEvent = new Event("Running_123A", "Tim", "Tim123A",
                35.2f, 140.5f, "Japan", "Ushiku",
                "Running_Around", 2015);

        swimmingEvent = new Event("Swimming_123A", "Tim", "Jim123A",
                35.1f, 140.3f, "Utah", "Provo",
                "Swimming_Around", 2017);

        Connection conn = db.getConnection();
        eDao = new EventDAO(conn);
        eDao.clearEventsTable();
    }

    @AfterEach
    public void tearDown() {
        db.closeConnection(false);
    }

    @Test
    public void insertEventPass() throws DataAccessException {
        eDao.insertEvent(bikingEvent);
        Event compareTest = eDao.getEventByEventID(bikingEvent.getEventID());

        assertNotNull(compareTest);
        assertEquals(bikingEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eDao.insertEvent(bikingEvent);

        assertThrows(DataAccessException.class, () -> eDao.insertEvent(bikingEvent));
    }

    @Test
    public void getEventByEventIDPass() throws DataAccessException {
        Event compareTest = eDao.getEventByEventID(bikingEvent.getEventID());
        assertNull(compareTest);

        eDao.insertEvent(bikingEvent);
        compareTest = eDao.getEventByEventID(bikingEvent.getEventID());

        assertNotNull(compareTest);
        assertEquals(bikingEvent, compareTest);
    }

    @Test
    public void getEventByEventIDFail() throws DataAccessException {
        Event compareTest = eDao.getEventByEventID(bikingEvent.getEventID());
        assertNull(compareTest);
    }

    @Test
    public void getEventByEventIDAndAssociatedUsernamePass() throws DataAccessException {
        Event compareTest = eDao.getEventByEventIDAndAssociatedUsername(bikingEvent.getEventID(),
                bikingEvent.getAssociatedUsername());
        assertNull(compareTest);

        eDao.insertEvent(bikingEvent);
        compareTest = eDao.getEventByEventIDAndAssociatedUsername(bikingEvent.getEventID(),
                bikingEvent.getAssociatedUsername());

        assertNotNull(compareTest);
        assertEquals(bikingEvent, compareTest);
    }

    @Test
    public void getEventByEventIDAndAssociatedUsernameFail() throws DataAccessException {
        Event compareTest = eDao.getEventByEventIDAndAssociatedUsername(bikingEvent.getEventID(),
                bikingEvent.getAssociatedUsername());
        assertNull(compareTest);
    }

    @Test
    public void getEventArrayByAssociatedUsernamePass() throws DataAccessException {
        Event[] eventArrayTim = new Event[2];
        eventArrayTim[0] = runningEvent;
        eventArrayTim[1] = swimmingEvent;

        Event[] eventArrayGale = new Event[1];
        eventArrayGale[0] = bikingEvent;

        eDao.insertEvent(bikingEvent);
        eDao.insertEvent(runningEvent);
        eDao.insertEvent(swimmingEvent);

        Event[] compareTestTim = eDao.getEventArrayByAssociatedUsername(runningEvent.getAssociatedUsername());
        Event[] compareTestGale = eDao.getEventArrayByAssociatedUsername(bikingEvent.getAssociatedUsername());

        assertNotNull(compareTestTim);
        assertEquals(eventArrayTim[0], compareTestTim[0]);
        assertEquals(eventArrayTim[1], compareTestTim[1]);

        assertNotNull(compareTestGale);
        assertEquals(eventArrayGale[0], compareTestGale[0]);
    }

    @Test
    public void getEventArrayByAssociatedUsernameFail() throws  DataAccessException {
        Event[] compareTest = eDao.getEventArrayByAssociatedUsername(runningEvent.getAssociatedUsername());
        assertNull(compareTest);
    }

    @Test
    public void clearEventsTableWithEventsInsertedTest() throws DataAccessException {
        eDao.insertEvent(bikingEvent);
        eDao.insertEvent(runningEvent);
        eDao.clearEventsTable();

        Event compareTestBiking = eDao.getEventByEventID(bikingEvent.getEventID());
        Event compareTestRunning = eDao.getEventByEventID(runningEvent.getEventID());

        assertNull(compareTestBiking);
        assertNull(compareTestRunning);
    }

    @Test
    public void clearEventsTableWithEventsNotInsertedTest() throws DataAccessException {
        eDao.clearEventsTable();

        Event compareTestBiking = eDao.getEventByEventID(bikingEvent.getEventID());
        Event compareTestRunning = eDao.getEventByEventID(runningEvent.getEventID());

        assertNull(compareTestBiking);
        assertNull(compareTestRunning);
    }

    @Test
    public void clearEventsTableAssociatedWithUserWithEventsInsertedTest() throws DataAccessException {
        eDao.insertEvent(bikingEvent);
        eDao.insertEvent(runningEvent);
        eDao.clearEventsTableAssociatedWithUser(bikingEvent.getAssociatedUsername());

        Event compareTestBiking = eDao.getEventByEventID(bikingEvent.getEventID());
        Event compareTestRunning = eDao.getEventByEventID(runningEvent.getEventID());

        assertNull(compareTestBiking);
        assertNotNull(compareTestRunning);
        assertEquals(runningEvent, compareTestRunning);
    }

    @Test
    public void clearEventsTableAssociatedWithUserWithEventsNotInsertedTest() throws DataAccessException {
        eDao.clearEventsTableAssociatedWithUser(bikingEvent.getAssociatedUsername());

        Event compareTestBiking = eDao.getEventByEventID(bikingEvent.getEventID());
        Event compareTestRunning = eDao.getEventByEventID(runningEvent.getEventID());

        assertNull(compareTestBiking);
        assertNull(compareTestRunning);
    }
}
