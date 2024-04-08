package com.example.matthewglausfamilymapclient;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.matthewglausfamilymapclient.model.ServerProxy;

import java.io.IOException;
import java.util.UUID;

import model.User;
import request.EventRequest;
import request.LoginRequest;
import request.PersonRequest;
import request.RegisterRequest;
import response.EventResponse;
import response.LoginResponse;
import response.PersonResponse;
import response.RegisterResponse;

public class ServerProxyTest {
    private final ServerProxy serverProxy = ServerProxy.getInstance();
    private static final String SERVER_HOST = "localhost";
    private static final String SERVER_PORT = "8080";
    private static final String ERROR_MESSAGE = "Error: Bad Request";

    private LoginRequest loginRequest;
    private User sheilaParker;

    @Before
    public void setUp() {
        loginRequest = new LoginRequest("sheila", "parker");
        sheilaParker = new User("sheila", "parker", "sheila@parker.com",
                "Sheila", "Parker", "f", "Sheila_Parker");
    }

    @Test
    public void loginUserPass() throws IOException {
        LoginResponse loginResponse = serverProxy.login(loginRequest, SERVER_HOST, SERVER_PORT);

        assertTrue(loginResponse.isSuccess());
        assertEquals(sheilaParker.getPersonID(), loginResponse.getPersonID());
        assertEquals(sheilaParker.getUsername(), loginResponse.getUsername());
        assertNotNull(loginResponse.getAuthtoken());
        assertNull(loginResponse.getMessage());
    }

    @Test
    public void loginUserFail() throws IOException {
        LoginRequest loginRequestFail = new LoginRequest("fail", "fail");
        LoginResponse loginResponse = serverProxy.login(loginRequestFail, SERVER_HOST, SERVER_PORT);

        assertFalse(loginResponse.isSuccess());
        assertNull(loginResponse.getAuthtoken());
        assertNull(loginResponse.getPersonID());
        assertNull(loginResponse.getUsername());
        assertEquals(ERROR_MESSAGE, loginResponse.getMessage());
    }

    @Test
    public void registerUserPass() throws IOException {
        UUID usernameUUID = UUID.randomUUID();
        String username = usernameUUID.toString();
        User user = new User(username, "password", "email", "user",
                "user", "m", null);

        RegisterRequest registerRequest = new RegisterRequest(user);
        RegisterResponse registerResponse = serverProxy.register(registerRequest, SERVER_HOST,
                SERVER_PORT);

        assertTrue(registerResponse.isSuccess());
        assertEquals(user.getUsername(), registerResponse.getUsername());
        assertNotNull(registerResponse.getAuthtoken());
        assertNotNull(registerResponse.getAuthtoken());
        assertNull(registerResponse.getMessage());
    }

    @Test
    public void registerUserFail() throws IOException {
        RegisterRequest registerRequest = new RegisterRequest(sheilaParker);
        RegisterResponse registerResponse = serverProxy.register(registerRequest, SERVER_HOST,
                SERVER_PORT);

        assertFalse(registerResponse.isSuccess());
        assertNull(registerResponse.getAuthtoken());
        assertNull(registerResponse.getPersonID());
        assertEquals(ERROR_MESSAGE, registerResponse.getMessage());
    }

    @Test
    public void getPeopleRelatedToUserPass() throws IOException {
        LoginResponse loginResponse = serverProxy.login(loginRequest, SERVER_HOST, SERVER_PORT);
        PersonRequest personRequest = new PersonRequest(loginResponse.getAuthtoken());
        PersonResponse personResponse = serverProxy.getPeople(personRequest, SERVER_HOST,
                SERVER_PORT);

        assertTrue(personResponse.isSuccess());
        assertNotNull(personResponse.getData());
        assertNull(personResponse.getMessage());
    }

    @Test
    public void getPeopleRelatedToUserFail() throws IOException {
        PersonRequest personRequest = new PersonRequest("fail");
        PersonResponse personResponse = serverProxy.getPeople(personRequest, SERVER_HOST,
                SERVER_PORT);

        assertFalse(personResponse.isSuccess());
        assertNull(personResponse.getData());
        assertEquals(ERROR_MESSAGE, personResponse.getMessage());
    }

    @Test
    public void getEventsRelatedToUserPass() throws IOException {
        LoginResponse loginResponse = serverProxy.login(loginRequest, SERVER_HOST, SERVER_PORT);
        EventRequest eventRequest = new EventRequest(loginResponse.getAuthtoken());
        EventResponse eventResponse = serverProxy.getEvents(eventRequest, SERVER_HOST, SERVER_PORT);

        assertTrue(eventResponse.isSuccess());
        assertNotNull(eventResponse.getData());
        assertNull(eventResponse.getMessage());
    }

    @Test
    public void getEventsRelatedToUserFail() throws IOException {
        EventRequest eventRequest = new EventRequest("fail");
        EventResponse eventResponse = serverProxy.getEvents(eventRequest, SERVER_HOST, SERVER_PORT);

        assertFalse(eventResponse.isSuccess());
        assertNull(eventResponse.getData());
        assertEquals(ERROR_MESSAGE, eventResponse.getMessage());
    }
}