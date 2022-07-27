package services;

import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import daos.Database;
import models.User;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    LoginRequest request;
    LoginRequest badRequest;

    LoginService service;

    @Test
    void loginUser() {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        user.setGender("m");
        user.setFirstName(UUID.randomUUID().toString());
        user.setLastName(UUID.randomUUID().toString());
        user.setPersonID(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());

        Database db = new Database();
        assertDoesNotThrow(() -> db.getUdao().addUser(user));
        db.closeConnection(true);

        request = new LoginRequest();
        request.setUserName(username);
        request.setPassword(password);
        service = new LoginService();

        badRequest = new LoginRequest();
        badRequest.setUserName(UUID.randomUUID().toString());
        badRequest.setPassword(UUID.randomUUID().toString());

        assertDoesNotThrow(() -> service.loginUser(request));

        service = new LoginService();

        assertDoesNotThrow(() -> assertEquals("Error. No user by given username.", service.loginUser(badRequest).getMessage()));


    }
}