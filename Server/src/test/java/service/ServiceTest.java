package service;

import dao.DataAccessException;
import dao.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.*;
import model.*;
import response.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    private User user;
    private Person person;
    private Event bikingEvent;
    private Event runningEvent;
    private Authtoken authtoken;
    private Database db;
    private UserDAO uDao;
    private PersonDAO pDao;
    private EventDAO eDao;
    private AuthtokenDAO aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        user = new User("tim_J", "timIsTheMan21", "tim_J@gmail.com",
                "Tim", "Johnson", "m", "Tim3241J");

        person = new Person("Tim324", "tim_J", "Tim",
                "Johnson", "m", "Robert2551P", "Kim1943I", "Ryan3242");


        bikingEvent = new Event("Biking_123A", "tim_J", "Gale123A",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        runningEvent = new Event("Running_123A", "tim_J", "Tim123A",
                35.2f, 140.5f, "Japan", "Ushiku",
                "Running_Around", 2015);

        authtoken = new Authtoken("abc123", "tim_J");

        db = new Database();
        Connection conn = db.getConnection();

        uDao = new UserDAO(conn);
        pDao = new PersonDAO(conn);
        eDao = new EventDAO(conn);
        aDao = new AuthtokenDAO(conn);

        ClearService service = new ClearService();
        service.clear();
    }

    @Test
    public void clearServiceWithDataInsertedTest() throws DataAccessException {
        uDao.insertUser(user);
        pDao.insertPerson(person);
        eDao.insertEvent(bikingEvent);
        aDao.insertAuthtoken(authtoken);
        db.closeConnection(true);

        ClearService service = new ClearService();
        service.clear();

        assertThrows(DataAccessException.class, () -> uDao.getUserByUsername(user.getUsername()));
        assertThrows(DataAccessException.class, () -> pDao.getPersonByID(person.getPersonID()));
        assertThrows(DataAccessException.class, () -> eDao.getEventByEventID(bikingEvent.getEventID()));
        assertThrows(DataAccessException.class, () -> aDao.getAuthtokenByUsername(authtoken.getUsername()));
    }

    @Test
    public void clearServicePassWithDataNotInsertedTest() {
        db.closeConnection(true);
        ClearService service = new ClearService();
        service.clear();

        assertThrows(DataAccessException.class, () -> uDao.getUserByUsername(user.getUsername()));
        assertThrows(DataAccessException.class, () -> pDao.getPersonByID(person.getPersonID()));
        assertThrows(DataAccessException.class, () -> eDao.getEventByEventID(bikingEvent.getEventID()));
        assertThrows(DataAccessException.class, () -> aDao.getAuthtokenByUsername(authtoken.getUsername()));
    }

    @Test
    public void eventServicePass() throws DataAccessException {
        uDao.insertUser(user);
        eDao.insertEvent(bikingEvent);
        eDao.insertEvent(runningEvent);
        aDao.insertAuthtoken(authtoken);
        db.closeConnection(true);

        EventRequest request = new EventRequest(authtoken.getAuthtoken());
        EventService service = new EventService();
        EventResponse response = service.event(request);

        assertEquals(bikingEvent, response.getData()[0]);
        assertEquals(runningEvent, response.getData()[1]);
        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
    }

    @Test
    public void eventServiceFail() throws DataAccessException{
        uDao.insertUser(user);
        eDao.insertEvent(bikingEvent);
        eDao.insertEvent(runningEvent);
        db.closeConnection(true);

        EventRequest request = new EventRequest(authtoken.getAuthtoken());
        EventService service = new EventService();
        EventResponse response = service.event(request);

        assertNull(response.getData());
        assertFalse(response.isSuccess());
        assertEquals("Error: Invalid authtoken.", response.getMessage());
    }

    @Test
    public void eventWithEventIDPass() throws DataAccessException {
        aDao.insertAuthtoken(authtoken);
        eDao.insertEvent(bikingEvent);
        db.closeConnection(true);

        EventWithEventIDRequest request = new EventWithEventIDRequest(bikingEvent.getEventID(),
                authtoken.getAuthtoken());
        EventWithEventIDService service = new EventWithEventIDService();
        EventWithEventIDResponse response = service.event(request);

        assertEquals(bikingEvent.getAssociatedUsername(), response.getAssociatedUsername());
        assertEquals(bikingEvent.getEventID(), response.getEventID());
        assertEquals(bikingEvent.getPersonID(), response.getPersonID());
        assertEquals(bikingEvent.getLatitude(), response.getLatitude());
        assertEquals(bikingEvent.getLongitude(), response.getLongitude());
        assertEquals(bikingEvent.getCountry(), response.getCountry());
        assertEquals(bikingEvent.getCity(), response.getCity());
        assertEquals(bikingEvent.getEventType(), response.getEventType());
        assertEquals(bikingEvent.getYear(), response.getYear());
        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
    }

    @Test
    public void eventWithEventIDFail() throws DataAccessException {
        eDao.insertEvent(bikingEvent);
        db.closeConnection(true);

        EventWithEventIDRequest request = new EventWithEventIDRequest(bikingEvent.getEventID(),
                authtoken.getAuthtoken());
        EventWithEventIDService service = new EventWithEventIDService();
        EventWithEventIDResponse response = service.event(request);

        assertNull(response.getAssociatedUsername());
        assertNull(response.getEventID());
        assertNull(response.getPersonID());
        assertNull(response.getCountry());
        assertNull(response.getCity());
        assertNull(response.getEventType());
        assertFalse(response.isSuccess());
        assertEquals("Error: Requested event does not belong to this user.", response.getMessage());
    }

    @Test
    public void fillServicePass() throws DataAccessException {
        uDao.insertUser(user);
        db.closeConnection(true);

        FillRequest request = new FillRequest(user.getUsername(), 3);
        FillService service = new FillService();
        FillResponse response = service.fill(request);

        assertTrue(response.isSuccess());
        assertEquals("Successfully added 15 persons and 45 events to the database.", response.getMessage());
    }

    @Test
    public void fillServiceFail() throws DataAccessException {
        uDao.insertUser(user);
        db.closeConnection(true);

        FillRequest request = new FillRequest(user.getUsername(), -1);
        FillService service = new FillService();
        FillResponse response = service.fill(request);

        assertFalse(response.isSuccess());
        assertEquals("Error: Invalid input.", response.getMessage());
    }

    @Test
    public void loadServicePass() {
        db.closeConnection(true);

        User[] users = new User[1];
        users[0] = user;
        Person[] persons = new Person[]{person};
        Event[] events = new Event[]{bikingEvent};

        LoadRequest request = new LoadRequest(users, persons, events);
        LoadService service = new LoadService();
        LoadResponse response = service.load(request);

        assertTrue(response.isSuccess());
        assertEquals("Successfully added 1 users, 1 persons, and 1 events to the database.",
                response.getMessage());
    }

    @Test
    public void loadServiceFail() {
        db.closeConnection(true);

        LoadRequest request = new LoadRequest();
        LoadService service = new LoadService();
        LoadResponse response = service.load(request);

        assertFalse(response.isSuccess());
        assertEquals("Error: Invalid input.", response.getMessage());
    }

    @Test
    public void loginServicePass() throws DataAccessException {
        uDao.insertUser(user);
        db.closeConnection(true);

        LoginRequest request = new LoginRequest(user.getUsername(), user.getPassword());
        LoginService service = new LoginService();
        LoginResponse response = service.login(request);

        assertNotNull(response.getAuthtoken());
        assertEquals(user.getUsername(), response.getUsername());
        assertEquals(user.getPersonID(), response.getPersonID());
        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
    }

    @Test
    public void loginServiceFail() throws DataAccessException {
        uDao.insertUser(user);
        db.closeConnection(true);

        LoginRequest request = new LoginRequest(user.getUsername(), "password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request);

        assertNull(response.getAuthtoken());
        assertNull(response.getUsername());
        assertNull(response.getPersonID());
        assertFalse(response.isSuccess());
        assertEquals("Error: Username and password do not match.", response.getMessage());
    }

    @Test
    public void personServicePass() throws DataAccessException {
        Person userPerson = new Person(user.getPersonID(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getGender());
        uDao.insertUser(user);
        pDao.insertPerson(person);
        pDao.insertPerson(userPerson);
        aDao.insertAuthtoken(authtoken);
        db.closeConnection(true);

        PersonRequest request = new PersonRequest(authtoken.getAuthtoken());
        PersonService service = new PersonService();
        PersonResponse response = service.person(request);

        assertEquals(person, response.getData()[0]);
        assertEquals(userPerson, response.getData()[1]);
        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
    }

    @Test
    public void personServiceFail() throws DataAccessException {
        Person userPerson = new Person(user.getPersonID(), user.getUsername(), user.getFirstName(), user.getLastName(),
                user.getGender());
        uDao.insertUser(user);
        pDao.insertPerson(person);
        pDao.insertPerson(userPerson);
        db.closeConnection(true);

        PersonRequest request = new PersonRequest(authtoken.getAuthtoken());
        PersonService service = new PersonService();
        PersonResponse response = service.person(request);

        assertNull(response.getData());
        assertFalse(response.isSuccess());
        assertEquals("Error: Invalid authtoken.", response.getMessage());
    }

    @Test
    public void personWithPersonIDServicePass() throws DataAccessException {
        aDao.insertAuthtoken(authtoken);
        pDao.insertPerson(person);
        db.closeConnection(true);

        PersonWithPersonIDRequest request = new PersonWithPersonIDRequest(person.getPersonID(),
                authtoken.getAuthtoken());
        PersonWithPersonIDService service = new PersonWithPersonIDService();
        PersonWithPersonIDResponse response = service.person(request);

        assertEquals(person.getAssociatedUsername(), response.getAssociatedUsername());
        assertEquals(person.getPersonID(), response.getPersonID());
        assertEquals(person.getFirstName(), response.getFirstName());
        assertEquals(person.getLastName(), response.getLastName());
        assertEquals(person.getGender(), response.getGender());
        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
    }

    @Test
    public void personWithPersonIDServiceFail() throws DataAccessException {
        pDao.insertPerson(person);
        db.closeConnection(true);

        PersonWithPersonIDRequest request = new PersonWithPersonIDRequest(person.getPersonID(),
                authtoken.getAuthtoken());
        PersonWithPersonIDService service = new PersonWithPersonIDService();
        PersonWithPersonIDResponse response = service.person(request);

        assertNull(response.getAssociatedUsername());
        assertNull(response.getPersonID());
        assertNull(response.getFirstName());
        assertNull(response.getLastName());
        assertNull(response.getGender());
        assertFalse(response.isSuccess());
        assertEquals("Error: Requested person does not belong to this user.", response.getMessage());
    }

    @Test
    public void registerServicePass() {
        RegisterRequest request = new RegisterRequest(user);

        RegisterService service = new RegisterService();
        RegisterResponse response = service.register(request);

        assertNotNull(response.getAuthtoken());
        assertEquals(user.getUsername(), response.getUsername());
        assertNotNull(response.getPersonID());
        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
    }

    @Test
    public void registerServiceFail() {
        RegisterRequest request = new RegisterRequest(user);

        RegisterService service = new RegisterService();
        service.register(request);
        RegisterResponse response = service.register(request);

        assertNull(response.getAuthtoken());
        assertNull(response.getUsername());
        assertNull(response.getPersonID());
        assertFalse(response.isSuccess());
        assertEquals("Error: Username already exists.", response.getMessage());
    }
}
