package services;

import Exceptions.UserAlreadyExistsException;
import daos.Database;
import models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import results.RegisterResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    RegisterRequest request;
    RegisterResult result;
    RegisterService service;
    RegisterResult wrongResult;

    @BeforeEach
    void setup(){
        request = new RegisterRequest();
        result = new RegisterResult();
        service = new RegisterService();
        wrongResult = new RegisterResult();

        request.setUserName(UUID.randomUUID().toString());
        request.setFirstName(UUID.randomUUID().toString());
        request.setLastName(UUID.randomUUID().toString());
        request.setEmail(UUID.randomUUID().toString());
        request.setPassword(UUID.randomUUID().toString());
        request.setGender("m");

        result.setUserName(request.getUserName());
        result.setSuccess(true);

        wrongResult.setSuccess(false);
        wrongResult.setMessage("Error. Username taken. Please use a different username.");
    }



    @Test
    void registerUser() {
        RegisterResult r = new RegisterResult();
        try {
            r = service.registerUser(request);
        }
        catch (UserAlreadyExistsException e) {
            e.printStackTrace();
        }
        result.setPersonID(r.getPersonID());
        result.setAuthToken(r.getAuthToken());


        assertEquals(r, result);

        assertThrows(UserAlreadyExistsException.class, () -> assertEquals(wrongResult.getMessage(), service.registerUser(request).getMessage()));

    }
}