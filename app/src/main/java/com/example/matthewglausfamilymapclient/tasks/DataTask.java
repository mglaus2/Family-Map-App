package com.example.matthewglausfamilymapclient.tasks;

import android.os.Handler;

import com.example.matthewglausfamilymapclient.model.DataCache;
import com.example.matthewglausfamilymapclient.model.ServerProxy;

import java.io.IOException;

import model.Person;
import request.EventRequest;
import request.PersonRequest;
import response.EventResponse;
import response.PersonResponse;

public class DataTask implements Runnable {

    private final String serverHost;
    private final String serverPort;
    private final String authToken;
    private final String personID;
    private final Handler messageHandler;
    private PersonResponse personResponse;
    private EventResponse eventResponse;

    public DataTask(Handler messageHandler, String serverHost, String serverPort, String authToken,
                    String personID) {
        this.messageHandler = messageHandler;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.authToken = authToken;
        this.personID = personID;
    }

    @Override
    public void run() {
        PersonRequest personRequest = new PersonRequest(authToken);
        EventRequest eventRequest = new EventRequest(authToken);

        ServerProxy serverProxy = ServerProxy.getInstance();
        DataCache dataCache = DataCache.getInstance();
        try {
            personResponse = serverProxy.getPeople(personRequest, serverHost, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            eventResponse = serverProxy.getEvents(eventRequest, serverHost, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataCache.setPeople(personResponse.getData());
        dataCache.setEvents(eventResponse.getData());

        Person user = dataCache.getPersonByID(personID);
        dataCache.setUser(user);

        String name = dataCache.getFirstName() + " " + dataCache.getLastName();
        MessageHelper messageHelper = new MessageHelper(name, messageHandler);
        messageHelper.sendMessage();
    }
}

