package com.example.matthewglausfamilymapclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.matthewglausfamilymapclient.model.DataCache;

import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Person;

public class DataCacheTest {
    private final DataCache dataCache = DataCache.getInstance();
    private Event bikingEvent;
    private Event runningEvent;
    private Event swimmingEvent;
    private Event marathonEvent;
    private Person testPersonTim;
    private Person testPersonAlex;
    private Person testPersonJack;
    private Person testPersonSusan;

    @Before
    public void setUp() {
        dataCache.setShowLifeStoryLines(true);
        dataCache.setShowFamilyTreeLines(true);
        dataCache.setShowSpouseLines(true);
        dataCache.setFilterFathersSide(true);
        dataCache.setFilterMothersSide(true);
        dataCache.setFilterMaleEvents(true);
        dataCache.setFilterFemaleEvents(true);

        bikingEvent = new Event("Biking_123A", "Tim", "Jack32",
                35.9f, 140.1f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        runningEvent = new Event("Running_123A", "Tim", "Jack32",
                35.2f, 140.5f, "Japan", "Ushiku",
                "Running_Around", 2015);

        swimmingEvent = new Event("Swimming_123A", "Tim", "Alex4132",
                35.1f, 140.3f, "Utah", "Provo",
                "Swimming_Around", 2017);

        marathonEvent = new Event("Marathon", "Tim", "Susan",
                35.1f, 140.3f, "Utah", "Provo",
                "Marathon", 2017);

        testPersonTim = new Person("Tim3241J", "Tim", "Tim",
                "Johnson", "m", "Susan2001E");

        testPersonAlex = new Person("Alex4132", "Alex", "Alex",
                "Smith", "m", "Jack32", "Susan", "Sally");

        testPersonSusan = new Person("Susan", "Susan", "Susan",
                "Smith", "f", "Jack32");

        testPersonJack = new Person("Jack32", "Alex", "Jack",
                "Brown", "m", "Marilyn");

        dataCache.addEvent(bikingEvent);
        dataCache.addEvent(runningEvent);
        dataCache.addEvent(swimmingEvent);
        dataCache.addEvent(marathonEvent);
        dataCache.addPerson(testPersonTim);
        dataCache.addPerson(testPersonAlex);
        dataCache.addPerson(testPersonSusan);
        dataCache.addPerson(testPersonJack);
        dataCache.setUser(testPersonAlex);
        dataCache.filterEvents();
    }

    @After
    public void tearDown() {
        dataCache.clearDataCache();
    }

    @Test
    public void getFamilyRelationshipsPass() {
        List<Person> alexsImmediateFamily = dataCache.getImmediateFamily(testPersonAlex);
        String alexsRelationToSusan = dataCache.getFamilyRelation(testPersonAlex, testPersonSusan);
        String alexsRelationToJack = dataCache.getFamilyRelation(testPersonAlex, testPersonJack);
        String alexsRelationToTim = dataCache.getFamilyRelation(testPersonAlex, testPersonTim);

        List<Person> expectedAlexsImmediateFamily = new ArrayList<>();
        expectedAlexsImmediateFamily.add(testPersonSusan);
        expectedAlexsImmediateFamily.add(testPersonJack);

        String expectedRelationToSusan = "Mother";
        String expectedRelationToJack = "Father";

        assertEquals(alexsImmediateFamily, expectedAlexsImmediateFamily);
        assertEquals(expectedRelationToSusan, alexsRelationToSusan);
        assertEquals(expectedRelationToJack, alexsRelationToJack);
        assertNull(alexsRelationToTim);
    }

    @Test
    public void getFamilyRelationshipsFail() {
        List<Person> timsImmediateFamily = dataCache.getImmediateFamily(testPersonTim);
        String timsRelationToSusan = dataCache.getFamilyRelation(testPersonTim, testPersonSusan);

        assertTrue(timsImmediateFamily.isEmpty());
        assertNull(timsRelationToSusan);
    }

    @Test
    public void filterEventsPass() {
        dataCache.setFilterFathersSide(false);
        List<Event> filteredEvents = dataCache.getFilteredEvents();

        List<Event> expectedEvents = new ArrayList<>();
        expectedEvents.add(swimmingEvent);
        expectedEvents.add(marathonEvent);

        assertEquals(filteredEvents, expectedEvents);
    }

    @Test
    public void filterEventsFail() {
        dataCache.setFilterMaleEvents(false);
        dataCache.setFilterFemaleEvents(false);
        List<Event> filteredEvents = dataCache.getFilteredEvents();

        assertTrue(filteredEvents.isEmpty());
    }

    @Test
    public void organizeLifeEventsPass() {
        List<Event> jacksOrganizedLifeEvents = dataCache.getPersonsLifeEvents(
                testPersonJack.getPersonID());
        List<Event> expectedJacksOrganizedLifeEvents = new ArrayList<>();
        expectedJacksOrganizedLifeEvents.add(runningEvent);
        expectedJacksOrganizedLifeEvents.add(bikingEvent);

        assertEquals(jacksOrganizedLifeEvents, expectedJacksOrganizedLifeEvents);
    }

    @Test
    public void organizeLifeEventsFail() {
        List<Event> timsOrganziedLifeEvents = dataCache.getPersonsLifeEvents(
                testPersonTim.getPersonID());

        assertTrue(timsOrganziedLifeEvents.isEmpty());
    }

    @Test
    public void getEventsAndPeopleBySearchPass() {
        String searchContent = "Ja";
        List<Event> expectedEventsBySearch = new ArrayList<>();
        List<Person> expectedPeopleBySearch = new ArrayList<>();
        expectedEventsBySearch.add(bikingEvent);
        expectedEventsBySearch.add(runningEvent);
        expectedPeopleBySearch.add(testPersonJack);

        List<Event> actualEventsBySearch = dataCache.getEventsBySearch(searchContent);
        List<Person> actualPeopleBySearch = dataCache.getPeopleBySearch(searchContent);

        assertEquals(actualEventsBySearch, expectedEventsBySearch);
        assertEquals(actualPeopleBySearch, expectedPeopleBySearch);
    }

    @Test
    public void getEventsAndPeopleBySearchFail() {
        String searchContent = "China";

        List<Event> actualEventsBySearch = dataCache.getEventsBySearch(searchContent);
        List<Person> actualPeopleBySearch = dataCache.getPeopleBySearch(searchContent);

        assertTrue(actualEventsBySearch.isEmpty());
        assertTrue(actualPeopleBySearch.isEmpty());
    }
}