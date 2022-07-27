package services;

import Exceptions.UserNotFoundException;
import results.*;
import requests.*;
import models.User;

public class LoginService extends Service {

    LoginResult result;

    public LoginService(){
        super();
    }

    /**
     * logs in the user given the information in the request passed to the constructor
     * @return the result object with the information of whether the login
     *          was successful or not
     */
    public LoginResult loginUser(LoginRequest request){
        result = new LoginResult();
        try {
            User user = db.getUdao().getUser(request.getUserName());
            if (!user.getPassword().equals(request.getPassword())){
                result.setMessage("Error. Incorrect password for given username.");
                throw new UserNotFoundException();
            }
            //success if reaching this point
            result.setSuccess(true);
            result.setPersonID(user.getPersonID());
            result.setUsername(request.getUserName());

            //now to set the auth token
            String token = createAuthToken(user);
            result.setToken(token);
        }
        catch (UserNotFoundException e) {
            if (result.getMessage() ==  null){
                result.setMessage("Error. No user by given username.");
            }
            result.setSuccess(false);
        }
        finally{
            db.closeConnection(true);
        }
        return result;
    }




}
