package services;
import Exceptions.DatabaseException;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UserNotFoundException;
import daos.Database;
import models.User;
import requests.RegisterRequest;
import results.RegisterResult;

import java.util.UUID;

public class RegisterService extends Service{


    /**
     * Converts the given request into a user and adds it to the database
     * @return the result object of the correct
     */
    public RegisterResult registerUser(RegisterRequest request) throws UserAlreadyExistsException{
        User user = new User();
        RegisterResult result = new RegisterResult();

        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setPersonID(UUID.randomUUID().toString());

        db = new Database();
        try {
            db.getUdao().addUser(user);
            makeGenerations(user.getUserName(), 4);
            String authToken = createAuthToken(user);
            result.setSuccess(true);
            result.setAuthToken(authToken);
            result.setPersonID(user.getPersonID());
            result.setUserName(user.getUserName());
        }
        catch (UserAlreadyExistsException e) {
            throw e;
        }
        catch (UserNotFoundException e){
            e.printStackTrace();
        }
        finally{
            db.closeConnection(true);
        }


        return result;
    }


}
