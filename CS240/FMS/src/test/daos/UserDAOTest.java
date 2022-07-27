package daos;
import Exceptions.DatabaseException;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UserNotFoundException;
import models.User;
import java.util.UUID;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    Database db;
    User user = new User();
    User nonExistentUser = new User();

    @BeforeEach
    void setup(){
        db = new Database();
        user.setUserName(UUID.randomUUID().toString());
        user.setEmail(UUID.randomUUID().toString());
        user.setPassword(UUID.randomUUID().toString());
        user.setFirstName(UUID.randomUUID().toString());
        user.setLastName(UUID.randomUUID().toString());
        user.setGender(UUID.randomUUID().toString());
        user.setPersonID(UUID.randomUUID().toString());
        nonExistentUser.setUserName(UUID.randomUUID().toString());
        nonExistentUser.setEmail(UUID.randomUUID().toString());
        nonExistentUser.setPassword(UUID.randomUUID().toString());
        nonExistentUser.setFirstName(UUID.randomUUID().toString());
        nonExistentUser.setLastName(UUID.randomUUID().toString());
        nonExistentUser.setGender(UUID.randomUUID().toString());
        nonExistentUser.setPersonID(UUID.randomUUID().toString());
    }

    @AfterEach
    void end(){
        db.closeConnection(true);
    }
    @Test
    void addUser() {
        assertDoesNotThrow(() -> db.getUdao().addUser(user));
        assertDoesNotThrow(() -> assertEquals(user, db.getUdao().getUser(user)));

        assertThrows(UserAlreadyExistsException.class, () -> db.getUdao().addUser(user));
        assertDoesNotThrow(() -> db.getUdao().deleteUser(user));
    }

    @Test
    void getUser() {
        assertDoesNotThrow(() -> db.getUdao().addUser(user));
        assertDoesNotThrow(() -> assertEquals(user, db.getUdao().getUser(user)));

        assertThrows(UserNotFoundException.class, () -> db.getUdao().getUser(nonExistentUser));
        assertDoesNotThrow(() -> db.getUdao().deleteUser(user));
    }

    @Test
    void deleteUser() {
        assertDoesNotThrow(() -> db.getUdao().addUser(user));
        assertDoesNotThrow(() -> assertEquals(user, db.getUdao().getUser(user)));

        assertThrows(UserNotFoundException.class, () -> db.getUdao().deleteUser(nonExistentUser));
        assertDoesNotThrow(() -> db.getUdao().deleteUser(user));
    }


    @Test
    void testClearPersons(){
        assertDoesNotThrow(() -> db.getUdao().addUser(user));
        assertDoesNotThrow(() -> db.getUdao().addUser(nonExistentUser));

        assertDoesNotThrow(() -> assertEquals(user, db.getUdao().getUser(user)));
        assertDoesNotThrow(() -> assertEquals(nonExistentUser, db.getUdao().getUser(nonExistentUser)));

        assertDoesNotThrow(() -> db.getUdao().clearUsers());
    }

}