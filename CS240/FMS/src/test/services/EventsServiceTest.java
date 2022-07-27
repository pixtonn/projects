package services;

import daos.Database;
import models.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.EventsResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventsServiceTest {



    Database db;
    Event event1;
    Event event2;
    Event event3;
    EventsResult result;
    EventsService service;
    String username;
    String wrongUsername;

    @BeforeEach
    void setup() {
        db = new Database();
        service = new EventsService();
        result = new EventsResult();

        username = UUID.randomUUID().toString();
        wrongUsername = UUID.randomUUID().toString();

        event1 = new Event();
        event1.setEventID(UUID.randomUUID().toString());
        event1.setPersonID(UUID.randomUUID().toString());
        event1.setAssociatedUsername(username);
        event1.setLatitude(12.34);
        event1.setLongitude(12.34);
        event1.setCountry(UUID.randomUUID().toString());
        event1.setCity(UUID.randomUUID().toString());
        event1.setEventType(UUID.randomUUID().toString());
        event1.setYear(2000);
        event2 = new Event();
        event2.setEventID(UUID.randomUUID().toString());
        event2.setPersonID(UUID.randomUUID().toString());
        event2.setAssociatedUsername(username);
        event2.setLatitude(12.34);
        event2.setLongitude(12.34);
        event2.setCountry(UUID.randomUUID().toString());
        event2.setCity(UUID.randomUUID().toString());
        event2.setEventType(UUID.randomUUID().toString());
        event2.setYear(2000);
        event3 = new Event();
        event3.setEventID(UUID.randomUUID().toString());
        event3.setPersonID(UUID.randomUUID().toString());
        event3.setAssociatedUsername(username);
        event3.setLatitude(12.34);
        event3.setLongitude(12.34);
        event3.setCountry(UUID.randomUUID().toString());
        event3.setCity(UUID.randomUUID().toString());
        event3.setEventType(UUID.randomUUID().toString());
        event3.setYear(2000);

        result = new EventsResult();
        result.add(event1);
        result.add(event2);
        result.add(event3);
    }



    @Test
    void getEvents() {
        db.getEdao().clearEvents();
        assertDoesNotThrow(() -> db.getEdao().addEvent(event1));
        assertDoesNotThrow(() -> db.getEdao().addEvent(event2));
        assertDoesNotThrow(() -> db.getEdao().addEvent(event3));
        db.closeConnection(true);

        EventsResult r = service.getEvents(username);
        assertEquals(result, r);

        assertNotEquals(result, service.getEvents(wrongUsername));
    }
}