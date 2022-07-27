package services;

import daos.Database;
import models.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.PersonResult;
import results.PersonsResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PersonsServiceTest {


    Database db;
    Person person1;
    Person person2;
    Person person3;
    PersonsResult result;
    PersonsService service;
    String username;
    String wrongUsername;

    @BeforeEach
    void setup() {
        db = new Database();
        service = new PersonsService();

        username = UUID.randomUUID().toString();
        wrongUsername = UUID.randomUUID().toString();

        person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("YaBoi");
        person1.setGender("m");
        person1.setPersonID(UUID.randomUUID().toString());
        person1.setUsername(username);
        person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("YaGirl");
        person2.setGender("m");
        person2.setPersonID(UUID.randomUUID().toString());
        person2.setUsername(username);
        person3 = new Person();
        person3.setFirstName("Jason");
        person3.setLastName("TheMan");
        person3.setGender("m");
        person3.setPersonID(UUID.randomUUID().toString());
        person3.setUsername(username);

        result = new PersonsResult();
        result.add(person1);
        result.add(person2);
        result.add(person3);
    }



    @Test
    void getPersons() {
        db.getPdao().clearPersons();
        assertDoesNotThrow(() -> db.getPdao().addPerson(person1));
        assertDoesNotThrow(() -> db.getPdao().addPerson(person2));
        assertDoesNotThrow(() -> db.getPdao().addPerson(person3));
        db.closeConnection(true);

        PersonsResult r = service.getPersons(username);
        assertEquals(result, r);

        assertNotEquals(result, service.getPersons(wrongUsername));
    }
}