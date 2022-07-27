package results;

public class LoginResult extends  Result {

    private String authToken;
    private String personID;
    private String userName;


    public LoginResult() { }

    public LoginResult(boolean success) {
        super(success);
    }

    public LoginResult(boolean success, String message) {
        super(success, message);
    }



    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getPersonID() {
        return personID;
    }

    public String getUsername() {
        return userName;
    }

    public String getToken() {
        return authToken;
    }

    public void setToken(String token) {
        this.authToken = token;
    }

}
