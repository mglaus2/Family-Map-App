package helper;

import com.google.gson.Gson;
import dao.DataAccessException;
import dao.EventDAO;
import dao.PersonDAO;
import json.*;
import model.Event;
import model.Person;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

public class GenerateFamilyTree {
    LocationData locData;
    MNamesData mNamesData;
    FNamesData fNamesData;
    SNamesData sNamesData;
    int defaultYear = 2000;
    int numOfPeopleCreated = 0;

    private void generateData() throws FileNotFoundException {
        try {
            Gson gson = new Gson();

            Reader locationReader = new FileReader("json/locations.json");
            locData = gson.fromJson(locationReader, LocationData.class);

            Reader mNamesReader = new FileReader("json/mnames.json");
            mNamesData = gson.fromJson(mNamesReader, MNamesData.class);

            Reader fNamesReader = new FileReader("json/fnames.json");
            fNamesData = gson.fromJson(fNamesReader, FNamesData.class);

            Reader sNamesReader = new FileReader("json/snames.json");
            sNamesData = gson.fromJson(sNamesReader, SNamesData.class);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new FileNotFoundException("Locations file was not found");
        }
    }

    public int generatePerson(Person tempPerson, int totalNumOfGenerations, int currNumOfGenerations, String username,
                              Connection conn) throws FileNotFoundException, DataAccessException {
        UUID motherUUID = UUID.randomUUID();
        String motherID = motherUUID.toString();            // generates random mother and father IDs

        UUID fatherUUID = UUID.randomUUID();
        String fatherID = fatherUUID.toString();

        if(totalNumOfGenerations != currNumOfGenerations) {         // sets motherID and fatherID of current person
            tempPerson.setMotherID(motherID);
            tempPerson.setFatherID(fatherID);
        }

        generateData();
        try {
            PersonDAO personDAO = new PersonDAO(conn);
            personDAO.insertPerson(tempPerson);           // inserts person passed into function and their birth event
            generateBirthEvent(tempPerson, currNumOfGenerations, conn);
            ++numOfPeopleCreated;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ((totalNumOfGenerations - currNumOfGenerations) > 0) {
            int fNameIndex = new Random().nextInt(fNamesData.getData().length);     // generates first and last names of
            String firstName = fNamesData.getData()[fNameIndex];                    // mother and father of current
                                                                                    // person
            int sNameIndex = new Random().nextInt(sNamesData.getData().length);
            String lastName = sNamesData.getData()[sNameIndex];
            Person mother = new Person(motherID, username, firstName, lastName, "f", fatherID);

            fNameIndex = new Random().nextInt(mNamesData.getData().length);
            firstName = mNamesData.getData()[fNameIndex];
            Person father = new Person(fatherID, username, firstName, lastName, "m", motherID);

            int locationIndex = new Random().nextInt(locData.getData().length);
            Location location = locData.getData()[locationIndex];           // generates location for marriage events

            generateMarriageEvent(mother, currNumOfGenerations, location, conn);
            generateMarriageEvent(father, currNumOfGenerations, location, conn);
            generateDeathEvent(mother, currNumOfGenerations, conn);     // generates marriage and death events of
            generateDeathEvent(father, currNumOfGenerations, conn);     // mother and father of current person

            // recursion to generate mother and father of current person
            generatePerson(mother, totalNumOfGenerations, currNumOfGenerations + 1, username, conn);
            generatePerson(father, totalNumOfGenerations, currNumOfGenerations + 1, username, conn);
        }
        return numOfPeopleCreated;
    }

    private void generateBirthEvent(Person tempPerson, int currNumOfGenerations, Connection conn) {
        UUID eventUUID = UUID.randomUUID();
        String eventID = eventUUID.toString();

        int locationIndex = new Random().nextInt(locData.getData().length);
        Location location = locData.getData()[locationIndex];

        Event birthEvent = new Event(eventID, tempPerson.getAssociatedUsername(), tempPerson.getPersonID(),
                location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(),
                "Birth", defaultYear - currNumOfGenerations * 30);

        try {
            EventDAO eventDAO = new EventDAO(conn);
            eventDAO.insertEvent(birthEvent);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void generateMarriageEvent(Person tempPerson, int currNumOfGenerations, Location location, Connection conn) {
        UUID eventUUID = UUID.randomUUID();
        String eventID = eventUUID.toString();

        Event marriageEvent = new Event(eventID, tempPerson.getAssociatedUsername(), tempPerson.getPersonID(),
                location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(),
                "Marriage", defaultYear - (currNumOfGenerations * 30) + 25);

        try {
            EventDAO eventDAO = new EventDAO(conn);
            eventDAO.insertEvent(marriageEvent);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void generateDeathEvent(Person tempPerson, int currNumOfGenerations, Connection conn) {
        UUID eventUUID = UUID.randomUUID();
        String eventID = eventUUID.toString();

        int locationIndex = new Random().nextInt(locData.getData().length);
        Location location = locData.getData()[locationIndex];

        Event deathEvent = new Event(eventID, tempPerson.getAssociatedUsername(), tempPerson.getPersonID(),
                location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(),
                "Death", defaultYear - (currNumOfGenerations * 30) + 80);

        try {
            EventDAO eventDAO = new EventDAO(conn);
            eventDAO.insertEvent(deathEvent);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
