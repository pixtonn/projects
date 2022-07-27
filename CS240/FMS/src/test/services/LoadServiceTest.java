package services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import models.*;
import randomData.ReadInLoad;
import requests.LoadRequest;
import results.LoadResult;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {

    Person person;
    User user;
    Event event;
    LoadService service;
    LoadRequest request;
    LoadResult result;

    @BeforeEach
    void setup(){
        person = new Person();
        user = new User();
        event = new Event();
        service = new LoadService();
        request = new LoadRequest();
        result = new LoadResult();

        person.setFirstName(UUID.randomUUID().toString());
        person.setLastName(UUID.randomUUID().toString());
        person.setGender("m");
        person.setPersonID(UUID.randomUUID().toString());
        person.setUsername(UUID.randomUUID().toString());

        user.setUserName(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID().toString());
        user.setFirstName(UUID.randomUUID().toString());
        user.setLastName(UUID.randomUUID().toString());
        user.setGender(UUID.randomUUID().toString());
        user.setPersonID(UUID.randomUUID().toString());

        event.setEventID(UUID.randomUUID().toString());
        event.setPersonID(UUID.randomUUID().toString());
        event.setAssociatedUsername(UUID.randomUUID().toString());
        event.setLatitude(12.34);
        event.setLongitude(12.34);
        event.setCountry(UUID.randomUUID().toString());
        event.setCity(UUID.randomUUID().toString());
        event.setEventType(UUID.randomUUID().toString());
        event.setYear(2000);

        ArrayList<Person> persons = new ArrayList<Person>();
        persons.add(person);
        ArrayList<User> users = new ArrayList<User>();
        users.add(user);
        ArrayList<Event> events = new ArrayList<Event>();
        events.add(event);

        ReadInLoad data = new ReadInLoad();
        data.setPersons(persons);
        data.setUsers(users);
        data.setEvents(events);

        request.setData(data);

        result.setSuccess(true);
        result.setMessage("Successfully added 1 users, 1 persons, and 1 events to the database.");
    }





    @Test
    void loadData() {
        assertDoesNotThrow(() -> assertEquals(result.getMessage(), service.loadData(request).getMessage()));

    }
}