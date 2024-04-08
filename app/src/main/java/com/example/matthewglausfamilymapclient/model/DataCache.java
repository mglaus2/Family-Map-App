package com.example.matthewglausfamilymapclient.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import model.Event;
import model.Person;

public class DataCache {
    private static DataCache instance = new DataCache();

    public static DataCache getInstance() {
        if(instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    private DataCache() {}

    private List<Person> allPeople = new ArrayList<>();
    private List<Event> allEvents = new ArrayList<>();
    private List<Event> filteredEvents = new ArrayList<>();

    private Person user;

    private Boolean showLifeStoryLines;
    private Boolean showFamilyTreeLines;
    private Boolean showSpouseLines;
    private Boolean filterFathersSide;
    private Boolean filterMothersSide;
    private Boolean filterMaleEvents;
    private Boolean filterFemaleEvents;

    public Person getPersonByID(String personID) {
        for(Person tempPerson : allPeople) {
            if(Objects.equals(tempPerson.getPersonID(), personID)) {
                return tempPerson;
            }
        }
        return null;
    }

    public String getSpouseID(String personID) {
        Person tempPerson = getPersonByID(personID);
        String spouseID = tempPerson.getSpouseID();
        return spouseID;
    }

    public Event getPersonsEarliestEvent(String personID) {
        Event personsEarliestEvent = null;
        Integer currYear = 3000;
        for(Event event : filteredEvents) {
            if(Objects.equals(event.getPersonID(), personID)) {
                if(event.getYear() <= currYear) {
                    personsEarliestEvent = event;
                    currYear = personsEarliestEvent.getYear();
                }
            }
        }
        return personsEarliestEvent;
    }

    public List<Event> getPersonsLifeEvents(String personID) {
        List<Event> personsLifeEvents = new ArrayList<>();
        for(Event event : filteredEvents) {
            if(Objects.equals(event.getPersonID(), personID)) {
                personsLifeEvents.add(event);
            }
        }
        List<Event> organizedLifeEvents = organizeLifeEvents(personsLifeEvents);
        return organizedLifeEvents;
    }

    private List<Event> organizeLifeEvents(List<Event> lifeEvents) {
        List<Event> organizedLifeEvents = new ArrayList<>();
        Event currEvent;

        while(!lifeEvents.isEmpty()) {
            currEvent = lifeEvents.get(0);
            for(int i = 0; i < lifeEvents.size(); i++) {
                if(lifeEvents.get(i).getYear() < currEvent.getYear()) {
                    currEvent = lifeEvents.get(i);
                }
                else if(Objects.equals(lifeEvents.get(i).getYear(), currEvent.getYear())) {
                    String currEventType = currEvent.getEventType();
                    if(currEventType.equals(currEventType.toUpperCase())) {
                        currEvent = lifeEvents.get(i);
                    }
                }
            }
            organizedLifeEvents.add(currEvent);
            lifeEvents.remove(currEvent);
        }
        return organizedLifeEvents;
    }

    public List<Person> getImmediateFamily(Person person) {
        List<Person> personsImmediateFamily = new ArrayList<>();
        for(Person tempPerson : allPeople) {
            if(Objects.equals(tempPerson.getPersonID(), person.getFatherID())) {
                personsImmediateFamily.add(tempPerson);
            }
            if(Objects.equals(tempPerson.getPersonID(), person.getMotherID())) {
                personsImmediateFamily.add(tempPerson);
            }
            if(Objects.equals(tempPerson.getPersonID(), person.getSpouseID())) {
                personsImmediateFamily.add(tempPerson);
            }
            if(Objects.equals(tempPerson.getFatherID(), person.getPersonID()) ||
                    Objects.equals(tempPerson.getMotherID(), person.getPersonID())) {
                personsImmediateFamily.add(tempPerson);
            }
        }
        return personsImmediateFamily;
    }

    public String getFamilyRelation(Person currPerson, Person relatedPerson) {
        String familyRelation = null;
        if(Objects.equals(relatedPerson.getPersonID(), currPerson.getFatherID())) {
            familyRelation = "Father";
        }
        if(Objects.equals(relatedPerson.getPersonID(), currPerson.getMotherID())) {
            familyRelation = "Mother";
        }
        if(Objects.equals(relatedPerson.getPersonID(), currPerson.getSpouseID())) {
            familyRelation = "Spouse";
        }
        if(Objects.equals(relatedPerson.getFatherID(), currPerson.getPersonID()) ||
                Objects.equals(relatedPerson.getMotherID(), currPerson.getPersonID())) {
            familyRelation = "Child";
        }
        return familyRelation;
    }

    public void filterEvents() {
        filteredEvents = new ArrayList<>(allEvents);
        List<Event> events = new ArrayList<>(allEvents);
        String personID;
        Person person;

        if(!filterFathersSide) {
            person = getPersonByID(user.getFatherID());
            for(Event event : events) {
                if(Objects.equals(person.getPersonID(), event.getPersonID())) {
                    filteredEvents.remove(event);               // removes all the fathers events
                }
            }                                   // removes all the fathers ancestors events
            filteredEvents = new ArrayList<>(filterParentsEvents(person, filteredEvents));
        }
        events = new ArrayList<>(filteredEvents);

        if(!filterMothersSide) {
            person = getPersonByID(user.getMotherID());
            for(Event event : events) {
                if(Objects.equals(person.getPersonID(), event.getPersonID())) {
                    filteredEvents.remove(event);       // removes all the mothers events
                }
            }                                           // removes all the mothers ancestors events
            filteredEvents = new ArrayList<>(filterParentsEvents(person, filteredEvents));
        }
        events = new ArrayList<>(filteredEvents);

        if(!filterMaleEvents) {
            for(Event event : events) {
                personID = event.getPersonID();
                person = getPersonByID(personID);
                if(Objects.equals(person.getGender(), "m")) {
                    filteredEvents.remove(event);           // removes all male events
                }
            }
        }
        events = new ArrayList<>(filteredEvents);

        if(!filterFemaleEvents) {
            for(Event event : events) {
                personID = event.getPersonID();
                person = getPersonByID(personID);
                if(Objects.equals(person.getGender(), "f")) {
                    filteredEvents.remove(event);           // removes all female events
                }
            }
        }
    }

    private List<Event> filterParentsEvents(Person person, List<Event> eventsBeingFiltered) {
        if(person.getFatherID() != null) {
            Person father = getPersonByID(person.getFatherID());
            eventsBeingFiltered.removeIf(event -> Objects.equals(father.getPersonID(),
                    event.getPersonID()));
            filterParentsEvents(father, eventsBeingFiltered);
        }

        if(person.getMotherID() != null) {
            Person mother = getPersonByID(person.getMotherID());
            eventsBeingFiltered.removeIf(event -> Objects.equals(mother.getPersonID(),
                    event.getPersonID()));
            filterParentsEvents(mother, eventsBeingFiltered);
        }
        return eventsBeingFiltered;
    }

    public List<Event> getEventsBySearch(String searchContent) {
        List<Event> eventsBySearch = new ArrayList<>();
        searchContent = searchContent.toLowerCase();
        if(!searchContent.isEmpty()) {
            for(Event event : filteredEvents) {
                if(event.getCity().toLowerCase().contains(searchContent)) {
                    eventsBySearch.add(event);
                }
                else if(event.getCountry().toLowerCase().contains(searchContent)) {
                    eventsBySearch.add(event);
                }
                else if(event.getEventType().toLowerCase().contains(searchContent)) {
                    eventsBySearch.add(event);
                }
                else if(Integer.toString(event.getYear()).toLowerCase().contains(searchContent)) {
                    eventsBySearch.add(event);
                }
            }
        }
        return eventsBySearch;
    }

    public List<Person> getPeopleBySearch(String searchContent) {
        List<Person> peopleBySearch = new ArrayList<>();
        searchContent = searchContent.toLowerCase();
        if(!searchContent.isEmpty()) {
            for(Person person : allPeople) {
                if(person.getFirstName().toLowerCase().contains(searchContent)) {
                    peopleBySearch.add(person);
                }
                else if(person.getLastName().toLowerCase().contains(searchContent)) {
                    peopleBySearch.add(person);
                }
            }
        }
        return peopleBySearch;
    }

    public List<Event> getFilteredEvents() {
        filterEvents();
        return filteredEvents;
    }

    public void setPeople(Person[] persons) {
        allPeople.addAll(Arrays.asList(persons));
    }

    public void setEvents(Event[] events) {
        allEvents.addAll(Arrays.asList(events));
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getFirstName() {
        return user.getFirstName();
    }

    public String getLastName() {
        return user.getLastName();
    }

    public Boolean getShowLifeStoryLines() {
        return showLifeStoryLines;
    }

    public void setShowLifeStoryLines(Boolean showLifeStoryLines) {
        this.showLifeStoryLines = showLifeStoryLines;
    }

    public Boolean getShowFamilyTreeLines() {
        return showFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(Boolean showFamilyTreeLines) {
        this.showFamilyTreeLines = showFamilyTreeLines;
    }

    public Boolean getShowSpouseLines() {
        return showSpouseLines;
    }

    public void setShowSpouseLines(Boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    public Boolean getFilterFathersSide() {
        return filterFathersSide;
    }

    public void setFilterFathersSide(Boolean filterFathersSide) {
        this.filterFathersSide = filterFathersSide;
    }

    public Boolean getFilterMothersSide() {
        return filterMothersSide;
    }

    public void setFilterMothersSide(Boolean filterMothersSide) {
        this.filterMothersSide = filterMothersSide;
    }

    public Boolean getFilterMaleEvents() {
        return filterMaleEvents;
    }

    public void setFilterMaleEvents(Boolean filterMaleEvents) {
        this.filterMaleEvents = filterMaleEvents;
    }

    public Boolean getFilterFemaleEvents() {
        return filterFemaleEvents;
    }

    public void setFilterFemaleEvents(Boolean filterFemaleEvents) {
        this.filterFemaleEvents = filterFemaleEvents;
    }

    public void addEvent(Event event) {
        allEvents.add(event);
    }

    public void addPerson(Person person) {
        allPeople.add(person);
    }

    public void clearDataCache() {
        allPeople = new ArrayList<>();
        allEvents = new ArrayList<>();
        filteredEvents = new ArrayList<>();
    }
}

