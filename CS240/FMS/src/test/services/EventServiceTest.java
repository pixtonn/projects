package services;

import Exceptions.PersonNotFoundException;
import daos.Database;
import models.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.EventResult;
import Exceptions.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventServiceTest {


    Database db;
    Event event;
    EventResult result;
    EventService service;


    @BeforeEach
    void setup(){
        db = new Database();
        service = new EventService();

        event = new Event();
        event.setEventID(UUID.randomUUID().toString());
        event.setPersonID(UUID.randomUUID().toString());
        event.setAssociatedUsername(UUID.randomUUID().toString());
        event.setLatitude(12.34);
        event.setLongitude(12.34);
        event.setCountry(UUID.randomUUID().toString());
        event.setCity(UUID.randomUUID().toString());
        event.setEventType(UUID.randomUUID().toString());
        event.setYear(2000);


        result = new EventResult();
        result.setEventID(event.getEventID());
        result.setPersonID(event.getPersonID());
        result.setAssociatedUsername(event.getAssociatedUsername());
        result.setLatitude(event.getLatitude());
        result.setLongitude(event.getLongitude());
        result.setCountry(event.getCountry());
        result.setCity(event.getCity());
        result.setEventType(event.getEventType());
        result.setYear(event.getYear());

    }

    @AfterEach
    void end(){
        db.closeConnection(true);
    }


    @Test
    void getEvent() {
        assertDoesNotThrow(() -> db.getEdao().addEvent(event));
        db.closeConnection(true);

        assertDoesNotThrow(() -> assertEquals(result, service.getEvent(event.getEventID())));

        assertThrows(EventNotFoundException.class, () -> service.getEvent(UUID.randomUUID().toString()));


        db = new Database();
        assertDoesNotThrow(() -> db.getEdao().deleteEvent(event.getEventID()));

    }
}