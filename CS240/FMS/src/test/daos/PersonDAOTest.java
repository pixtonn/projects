package daos;

import Exceptions.DatabaseException;
import Exceptions.PersonAlreadyExistsException;
import Exceptions.PersonNotFoundException;
import models.Person;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

class PersonDAOTest {

    Database db;
    Person person = new Person();
    Person nonExistentPerson = new Person();

    @BeforeEach
    void setup(){
        db = new Database();
        person.setFirstName(UUID.randomUUID().toString());
        person.setLastName(UUID.randomUUID().toString());
        person.setGender("m");
        person.setPersonID(UUID.randomUUID().toString());
        person.setUsername(UUID.randomUUID().toString());
        nonExistentPerson.setFirstName(UUID.randomUUID().toString());
        nonExistentPerson.setLastName(UUID.randomUUID().toString());
        nonExistentPerson.setGender("m");
        nonExistentPerson.setPersonID(UUID.randomUUID().toString());
        nonExistentPerson.setUsername(UUID.randomUUID().toString());
    }

    @AfterEach
    void end(){
        db.closeConnection(true);

    }


    @Test
    void testAddPerson() {
        assertDoesNotThrow(() -> db.getPdao().addPerson(person));
        assertDoesNotThrow(() -> assertEquals(person, db.getPdao().getPerson(person)));

        assertThrows(PersonAlreadyExistsException.class, () -> db.getPdao().addPerson(person));

        assertDoesNotThrow(() -> db.getPdao().deletePerson(person));
    }

    @Test
    void testGetPerson() {
        assertDoesNotThrow(() -> db.getPdao().addPerson(person));
        assertDoesNotThrow(() -> assertEquals(person, db.getPdao().getPerson(person)));

        assertThrows(PersonNotFoundException.class, () -> db.getPdao().getPerson(nonExistentPerson));
        assertDoesNotThrow(() -> db.getPdao().deletePerson(person));
    }

    @Test
    void testDeletePerson(){
        assertDoesNotThrow(() -> db.getPdao().addPerson(person));
        assertDoesNotThrow(() -> assertEquals(person, db.getPdao().getPerson(person)));

        assertThrows(PersonNotFoundException.class, () -> db.getPdao().deletePerson(nonExistentPerson));
        assertDoesNotThrow(() -> db.getPdao().deletePerson(person));
    }

    @Test
    void testClearPersons(){
        assertDoesNotThrow(() -> db.getPdao().addPerson(person));
        assertDoesNotThrow(() -> db.getPdao().addPerson(nonExistentPerson));

        assertDoesNotThrow(() -> assertEquals(person, db.getPdao().getPerson(person)));
        assertDoesNotThrow(() -> assertEquals(nonExistentPerson, db.getPdao().getPerson(nonExistentPerson)));

        assertDoesNotThrow(() -> db.getPdao().clearPersons());

        assertThrows(PersonNotFoundException.class, () -> db.getPdao().getPerson(nonExistentPerson));
        assertThrows(PersonNotFoundException.class, () -> db.getPdao().getPerson(person));
    }
}