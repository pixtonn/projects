package daos;
import Exceptions.PersonAlreadyExistsException;
import Exceptions.PersonNotFoundException;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UserNotFoundException;
import models.User;
import models.Person;

import java.sql.*;


public class UserDAO {

    Connection conn;

    public UserDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * adds a user to the database
     * @param user the user to add to the database
     * @throws UserAlreadyExistsException when the passed user already exists within the database
     */
    public void addUser(User user)throws UserAlreadyExistsException {
        try {
            Statement stmt = conn.createStatement();

            StringBuilder sql = new StringBuilder("INSERT INTO Users (Username, Password, Email, FirstName, LastName, Gender, PersonID) VALUES (");
            sql.append("'");
            sql.append(user.getUserName().replace("'", "''"));
            sql.append("', '");
            sql.append(user.getPassword().replace("'", "''"));
            sql.append("', '");
            sql.append(user.getEmail().replace("'", "''"));
            sql.append("', '");
            sql.append(user.getFirstName().replace("'", "''"));
            sql.append("', '");
            sql.append(user.getLastName().replace("'", "''"));
            sql.append("', '");
            sql.append(user.getGender());
            sql.append("', '");
            sql.append(user.getPersonID());
            sql.append("')");

            //System.out.println(sql.toString());
            stmt.executeUpdate(sql.toString());
            return;
        }
        catch(SQLException e){
            throw new UserAlreadyExistsException();
        }
    }

    /**
     * removes the passed used from the database
     * @param user the user to remove from the database
     * @throws UserNotFoundException when the passed user does not exist in the database
     */
    public void deleteUser(User user) throws UserNotFoundException{
        try{
            deleteUser(user.getUserName());
        }
        catch(UserNotFoundException e){
            throw e;
        }
        return;
    }

    /**
     * removes the user from the database given their username
     * @param username the username to search for and delete the associated user
     * @throws UserNotFoundException when the given username does not have an associated
     *                               user in the database
     */
    public void deleteUser(String username) throws UserNotFoundException{
        StringBuilder sql = new StringBuilder("DELETE FROM Users WHERE Username = '");
        sql.append(username.replace("'", "''"));
        sql.append("'");
        try{
            Statement stmt = conn.createStatement();
            if (stmt.executeUpdate(sql.toString()) == 0){
                throw new UserNotFoundException();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return;
    }

    /**
     * gets a user given the person object related to that user
     * @param person the person who user is to be found
     * @return the user whose person object is passed
     * @throws UserNotFoundException when the person passed does not have an associated user
     */
//    User getUser(Person person) throws UserNotFoundException {
//        return null;
//    }//UNIMPLEMENTED!!!

    /**
     * given a username, returns the User model object
     * @param username username to search for
     * @return the user with the given username
     * @throws UserNotFoundException if the given username does not have an associated user
     */
    public User getUser(String username) throws UserNotFoundException{
        StringBuilder sql = new StringBuilder("SELECT * FROM Users WHERE Username = '");
        sql.append(username.replace("'", "''"));
        sql.append("';");
        User user = new User();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                user.setUserName(rs.getString(1));
                user.setPassword(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setFirstName(rs.getString(4));
                user.setLastName(rs.getString(5));
                user.setGender(rs.getString(6));
                user.setPersonID(rs.getString(7));
            }
            if (user.getUserName() == null){
                throw new UserNotFoundException();
            }

        }
        catch(SQLException e){
            throw new UserNotFoundException();
        }

        return user;
    }

    public User getUser(User user) throws UserNotFoundException{
        return getUser(user.getUserName());
    }

    public void clearUsers(){
        String sql = "DELETE FROM USERS";
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
