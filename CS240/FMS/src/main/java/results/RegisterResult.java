package results;

import java.util.Objects;

public class RegisterResult extends Result {

    private String authToken;
    private String userName;
    private String personID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RegisterResult that = (RegisterResult) o;
        return authToken.equals(that.authToken) &&
                userName.equals(that.userName) &&
                personID.equals(that.personID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), authToken, userName, personID);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
