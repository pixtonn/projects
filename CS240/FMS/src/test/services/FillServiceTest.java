package services;

import Exceptions.UserAlreadyExistsException;
import Exceptions.UserNotFoundException;
import daos.Database;
import models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.FillRequest;
import results.FillResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FillServiceTest {

    Database db = null;
    FillService service;
    User user;
    String username;
    FillRequest request;
    FillResult result;
    int numGen;

    @BeforeEach
    void setup(){
        user = new User();

        username = UUID.randomUUID().toString();
        db = new Database();
        numGen = 4;

        user.setUserName(username);
        user.setEmail(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID().toString());
        user.setFirstName(UUID.randomUUID().toString());
        user.setLastName(UUID.randomUUID().toString());
        user.setGender(UUID.randomUUID().toString());
        user.setPersonID(UUID.randomUUID().toString());

        service = new FillService();
        request = new FillRequest();
        result = new FillResult();

        request.setUsername(username);
        request.setNumGen(numGen);

        result.setSuccess(true);
        result.setMessage("Successfully added " + (int)(Math.pow(2, request.getNumGen() + 1) - 1) + " persons and " +
                ((int)(Math.pow(2,request.getNumGen() + 1) - 1) * 3) + " events to the database.");

    }

    @Test
    void fillFamily() {
        assertDoesNotThrow(() -> db.getUdao().addUser(user));
        db.closeConnection(true);

        assertDoesNotThrow(() -> assertEquals(result, service.fillFamily(request)));

        request.setUsername("non-existent");

        assertThrows(UserNotFoundException.class, () -> service.fillFamily(request));

        db = new Database();
        assertDoesNotThrow(() -> db.clearTables());
        db.closeConnection(true);
    }
}