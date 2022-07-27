package services;

import Exceptions.EventNotFoundException;
import Exceptions.PersonNotFoundException;
import Exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import models.*;

import java.util.UUID;
import daos.Database;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {

    final String aTusername = UUID.randomUUID().toString();
    final String aTtoken = UUID.randomUUID().toString();
    Event event = new Event();
    Person person = new Person();
    User user = new User();

    Database db = new Database();

    @Test
    void clearDatabase() {
        event.setEventID(UUID.randomUUID().toString());
        event.setPersonID(UUID.randomUUID().toString());
        event.setAssociatedUsername(UUID.randomUUID().toString());
        event.setLatitude(12.34);
        event.setLongitude(12.34);
        event.setCountry(UUID.randomUUID().toString());
        event.setCity(UUID.randomUUID().toString());
        event.setEventType(UUID.randomUUID().toString());
        event.setYear(2000);

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


        assertDoesNotThrow(() -> db.getAtdao().addToken(aTtoken, aTusername));
        assertTrue(db.getAtdao().verifyToken(aTtoken, aTusername));

        assertDoesNotThrow(() -> db.getEdao().addEvent(event));
        assertDoesNotThrow(() -> assertEquals(event, db.getEdao().getEvent(event.getEventID())));

        assertDoesNotThrow(() -> db.getPdao().addPerson(person));
        assertDoesNotThrow(() -> assertEquals(person, db.getPdao().getPerson(person)));

        assertDoesNotThrow(() -> db.getUdao().addUser(user));
        assertDoesNotThrow(() -> assertEquals(user, db.getUdao().getUser(user)));


        ClearService service = new ClearService();

        db.closeConnection(true);


        db = new Database();

        assertDoesNotThrow(() -> service.clearDatabase());


        assertFalse(db.getAtdao().verifyToken(aTtoken, aTusername));
        assertThrows(EventNotFoundException.class, () -> db.getEdao().deleteEvent(event.getEventID()));
        assertThrows(PersonNotFoundException.class, () -> db.getPdao().deletePerson(person));
        assertThrows(UserNotFoundException.class, () -> db.getUdao().deleteUser(user));

        assertDoesNotThrow(() -> db.closeConnection(true));

    }
}