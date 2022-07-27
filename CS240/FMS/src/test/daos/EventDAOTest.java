package daos;

import Exceptions.DatabaseException;
import Exceptions.EventAlreadyExistsException;
import Exceptions.EventNotFoundException;
import models.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EventDAOTest {

    Database db;
    Event event = new Event();
    Event nonExistentEvent = new Event();

    @BeforeEach
    void setup() {
        db = new Database();
        event.setEventID(UUID.randomUUID().toString());
        event.setPersonID(UUID.randomUUID().toString());
        event.setAssociatedUsername(UUID.randomUUID().toString());
        event.setLatitude(12.34);
        event.setLongitude(12.34);
        event.setCountry(UUID.randomUUID().toString());
        event.setCity(UUID.randomUUID().toString());
        event.setEventType(UUID.randomUUID().toString());
        event.setYear(2000);

        nonExistentEvent.setEventID(UUID.randomUUID().toString());
        nonExistentEvent.setPersonID(UUID.randomUUID().toString());
        nonExistentEvent.setAssociatedUsername(UUID.randomUUID().toString());
        nonExistentEvent.setLatitude(12.34);
        nonExistentEvent.setLongitude(12.34);
        nonExistentEvent.setCountry(UUID.randomUUID().toString());
        nonExistentEvent.setCity(UUID.randomUUID().toString());
        nonExistentEvent.setEventType(UUID.randomUUID().toString());
        nonExistentEvent.setYear(2000);
    }

    @AfterEach
    void end(){
        db.closeConnection(true);
    }

    @Test
    void addEvent() {
        assertDoesNotThrow(() -> db.getEdao().addEvent(event));
        assertDoesNotThrow(() -> assertEquals(event, db.getEdao().getEvent(event.getEventID())));

        assertThrows(EventAlreadyExistsException.class, () -> db.getEdao().addEvent(event));
        assertDoesNotThrow(() -> db.getEdao().deleteEvent(event.getEventID()));
    }

    @Test
    void deleteEvent() {
        assertDoesNotThrow(() -> db.getEdao().addEvent(event));
        assertDoesNotThrow(() -> assertEquals(event, db.getEdao().getEvent(event.getEventID())));

        assertThrows(EventNotFoundException.class, () -> db.getEdao().deleteEvent(nonExistentEvent.getEventID()));
        assertDoesNotThrow(() -> db.getEdao().deleteEvent(event.getEventID()));
    }

    @Test
    void getEvent() {
        assertDoesNotThrow(() -> db.getEdao().addEvent(event));
        assertDoesNotThrow(() -> assertEquals(event, db.getEdao().getEvent(event.getEventID())));

        assertThrows(EventNotFoundException.class, () -> db.getEdao().getEvent(nonExistentEvent.getEventID()));
        assertDoesNotThrow(() -> db.getEdao().deleteEvent(event.getEventID()));


    }

    @Test
    void clearEvents() {
        assertDoesNotThrow(() -> db.getEdao().clearEvents());
    }
}