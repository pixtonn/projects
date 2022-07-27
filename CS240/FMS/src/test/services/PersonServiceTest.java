package services;

import Exceptions.PersonNotFoundException;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import daos.Database;
import models.Person;
import results.PersonResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    Database db;
    Person person;
    PersonResult result;
    PersonService service;


    @BeforeEach
    void setup(){
        db = new Database();
        service = new PersonService();

        person = new Person();
        person.setFirstName(UUID.randomUUID().toString());
        person.setLastName(UUID.randomUUID().toString());
        person.setGender("m");
        person.setPersonID(UUID.randomUUID().toString());
        person.setUsername(UUID.randomUUID().toString());

        result = new PersonResult();
        result.setAssociatedUsername(person.getUsername());
        result.setFirstName(person.getFirstName());
        result.setLastName(person.getLastName());
        result.setPersonID(person.getPersonID());
        result.setGender(person.getGender());
    }


    @AfterEach
    void end(){
        db.closeConnection(true);
    }

    @Test
    void getPerson(){
        assertDoesNotThrow(() -> db.getPdao().addPerson(person));
        db.closeConnection(true);

        assertDoesNotThrow(() -> assertEquals(result, service.getPerson(person.getPersonID())));

        assertThrows(PersonNotFoundException.class, () -> service.getPerson(UUID.randomUUID().toString()));


        db = new Database();
        assertDoesNotThrow(() -> db.getPdao().deletePerson(person));
    }

}