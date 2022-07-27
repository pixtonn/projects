package daos;
import Exceptions.TokenAlreadyExistsException;
import Exceptions.UserNotFoundException;
import models.User;
import java.sql.*;

public class AuthTokenDAO {


    Connection conn;

    AuthTokenDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * checks to see if the given auth token provided by a user is valid or not
     * @param authToken the current provided auth token
     * @param username the username of the user trying to access the database
     * @return true or false whether or not that user currently uses that auth token
     */
    public boolean verifyToken(String authToken, String username){
        StringBuilder sql = new StringBuilder("SELECT * FROM AuthTokens WHERE Username = '");
        sql.append(username.replace("'", "''"));
        sql.append("' AND AuthToken = '");
        sql.append(authToken);
        sql.append("'");
        //System.out.println(sql.toString());
        try{
            Statement stmt = conn.createStatement();
            if (stmt.executeQuery(sql.toString()).next()) {
                return true;
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean hasToken(String username){
        StringBuilder sql = new StringBuilder("SELECT * FROM AuthTokens WHERE Username = '");
        sql.append(username.replace("'", "''"));
        sql.append("'");
        try{
            Statement stmt = conn.createStatement();
            if (stmt.executeQuery(sql.toString()).next()){
                return true;
            }
            else{
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * adds the passed token to the user's list of valid tokens
     * @param token the token to add to database
     * @param username the username of the user to which to add the token
     * @throws TokenAlreadyExistsException when the passed token already exists associated
     *                                     with the user specified
     */
    public void addToken(String token, String username) throws TokenAlreadyExistsException{
        StringBuilder sql = new StringBuilder("INSERT INTO AuthTokens (Username, AuthToken) VALUES ('");
        sql.append(username);
        sql.append("', '");
        sql.append(token);
        sql.append("')");
        StringBuilder checkIfExists = new StringBuilder("SELECT * FROM AuthTokens WHERE AuthToken = '");
        checkIfExists.append(token);
        checkIfExists.append("'");
        try{
            Statement stmt = conn.createStatement();
            if (stmt.executeQuery(checkIfExists.toString()).next()){
                throw new TokenAlreadyExistsException();
            }
            stmt.executeUpdate(sql.toString());
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return;
    }

    public String getUsername(String authToken){
        StringBuilder sql = new StringBuilder("SELECT Username FROM AuthTokens WHERE AuthToken = \'");
        sql.append(authToken);
        sql.append("\'");
        try{
            Statement stmt = conn.createStatement();
            //System.out.println(sql.toString());
            ResultSet rs = stmt.executeQuery(sql.toString());

            return rs.getString(1);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public boolean verifyToken(String token){
        StringBuilder sql = new StringBuilder("SELECT * FROM AuthTokens WHERE AuthToken = \'");
        sql.append(token);
        sql.append("\'");
        try{
            Statement stmt = conn.createStatement();
            //System.out.println(sql.toString());
            ResultSet rs = stmt.executeQuery(sql.toString());
            if (!rs.next()){
                return false;
            }
            else{
                return true;
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * clears the tokens of the given user
     * @param user the user for whom to delete all auth tokens
     * @throws UserNotFoundException if the user passed does not exist in the database
     */
//    void clearTokens(User user) throws UserNotFoundException{
//        return;
//    }

    /**
     * removes all tokens from the user with the given username
     * @param username the username of the user for whom to delete all tokens
     * @throws UserNotFoundException if the username is not associated with any user
     */
    public void clearTokens(String username) throws UserNotFoundException{
        StringBuilder sql = new StringBuilder("DELETE FROM AuthTokens WHERE Username = '");
        sql.append(username);
        sql.append("'");
        try{
            Statement stmt = conn.createStatement();
            if (stmt.executeUpdate(sql.toString()) == 0){
                throw new UserNotFoundException();
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return;
    }

    public void clearTokens(){
        try {
            String sql = "DELETE FROM AuthTokens";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
