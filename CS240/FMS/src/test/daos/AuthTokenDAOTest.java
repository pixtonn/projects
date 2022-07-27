package daos;

import Exceptions.DatabaseException;
import Exceptions.TokenAlreadyExistsException;
import Exceptions.UserNotFoundException;
import org.junit.jupiter.api.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicStampedReference;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenDAOTest {


    Database db;
    @BeforeEach
    void setup(){
        db = new Database();
    }

    @AfterEach
    void end(){
        db.closeConnection(true);
    }


    final String username = UUID.randomUUID().toString();
    final String token = UUID.randomUUID().toString();
    final String nonExistantUsername = UUID.randomUUID().toString();


    @Test
    void addToken() {
        assertDoesNotThrow(() -> db.getAtdao().addToken(token, username));
        assertTrue(db.getAtdao().verifyToken(token, username));

        assertThrows(TokenAlreadyExistsException.class, () -> db.getAtdao().addToken(token, username));
        assertDoesNotThrow(() -> db.getAtdao().clearTokens(username));
    }

    @Test
    void verifyToken() {
        assertDoesNotThrow(() -> db.getAtdao().addToken(token, username));
        assertTrue( db.getAtdao().verifyToken(token, username));

        assertEquals(false, db.getAtdao().verifyToken(token, nonExistantUsername));
        assertDoesNotThrow(() -> db.getAtdao().clearTokens(username));
    }

    @Test
    void clearTokens() {
        assertDoesNotThrow(() -> db.getAtdao().addToken(token, username));
        assertTrue(db.getAtdao().verifyToken(token, username));
        assertDoesNotThrow(() -> db.getAtdao().clearTokens(username));

        assertThrows(UserNotFoundException.class, () -> db.getAtdao().clearTokens(nonExistantUsername));
    }
}