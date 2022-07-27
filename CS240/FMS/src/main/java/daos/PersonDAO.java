package daos;
import Exceptions.PersonAlreadyExistsException;
import Exceptions.PersonNotFoundException;
import Exceptions.EventNotFoundException;
import models.Person;
import models.Event;
import java.sql.*;
import Exceptions.DatabaseException;

public class PersonDAO {

    Connection conn;

    public PersonDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * adds a person to the database
     * @param person the person to add to the database
     * @throws PersonAlreadyExistsException when the passed person already exists within the database
     */
    public void addPerson(Person person) throws PersonAlreadyExistsException {
        try {
            Statement stmt = conn.createStatement();

            StringBuilder sql = new StringBuilder("INSERT INTO Persons (personID, firstName, lastName, fatherID, motherID, spouseID, gender, associatedUsername) VALUES (");
            sql.append("'");
            sql.append(person.getPersonID());
            sql.append("', '");
            sql.append(person.getFirstName().replace("'", "''"));
            sql.append("', '");
            sql.append(person.getLastName().replace("'", "''"));
            sql.append("', ");
            if (person.getFatherID() == null){
                sql.append("NULL, ");
            }
            else{
                sql.append("'");
                sql.append(person.getFatherID());
                sql.append("', ");
            }
            if (person.getMotherID() == null){
                sql.append("NULL, ");
            }
            else{
                sql.append("'");
                sql.append(person.getMotherID());
                sql.append("', ");
            }
            if (person.getSpouseID() == null){
                sql.append("NULL, ");
            }
            else{
                sql.append("'");
                sql.append(person.getSpouseID());
                sql.append("', ");
            }
            sql.append("'");
            sql.append(person.getGender());
            sql.append("', '");
            sql.append(person.getUsername().replace("'", "''"));
            sql.append("')");
            //System.out.println(sql.toString());
            stmt.executeUpdate(sql.toString());
            return;
        }
        catch(SQLException e){
            throw new PersonAlreadyExistsException();
        }
    }

    /**
     * checks to see if a person already exists within the database
     * @param person person to check against the database
     * @return true or false whether or not the person already exists within the database
     */
//    boolean personExists(Person person){
//        return true;
//    }

    /**
     *
     * @param personID matches this username to the person
     * @return the person with the given personID
     * @throws PersonNotFoundException when the passed personID does not have an associated person
     *                                 in the database
     */
    public Person getPerson(String personID) throws PersonNotFoundException {
        StringBuilder sql = new StringBuilder("SELECT * FROM Persons WHERE PersonID='");
        sql.append(personID);
        sql.append("';");
        Person person = new Person();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                person.setPersonID(rs.getString(1));
                person.setFirstName(rs.getString(2));
                person.setLastName(rs.getString(3));
                person.setFatherID(rs.getString(4));
                person.setMotherID(rs.getString(5));
                person.setSpouseID(rs.getString(6));
                person.setGender(rs.getString(7));
                person.setUsername(rs.getString(8));
            }
            if (person.getFirstName() == null){
                throw new PersonNotFoundException();
            }

        }
        catch(SQLException e){
            throw new PersonNotFoundException();
        }

        return person;
    }

    public ResultSet getPersons(String username){
        StringBuilder sql = new StringBuilder("SELECT * FROM Persons WHERE associatedUsername='");
        sql.append(username.replace("'", "''"));
        sql.append("';");
        Person person = new Person();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            ResultSet rs = stmt.executeQuery();
            return rs;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Person getPerson(Person person) throws PersonNotFoundException{
        return getPerson(person.getPersonID());
    }

    /**
     *
     * @param event event to match to a person
     * @return the person whose event was passed, otherwise returns null if the event does not occur in the database
     * @throws EventNotFoundException when the passed event does not exist in the database
     */
    public Person getPerson(Event event) throws PersonNotFoundException{
        return getPerson(event.getPersonID());
    }

    /**
     * removes a person from the database
     * @param person the person to remove from the database
     * @throws PersonNotFoundException when the passed Person object does not have
     */
    public void deletePerson(Person person) throws PersonNotFoundException{
        StringBuilder sql = new StringBuilder("DELETE FROM Persons WHERE PersonID = '");
        sql.append(person.getPersonID());
        sql.append("'");
        try{
            Statement stmt = conn.createStatement();
            StringBuilder checkIfExists = new StringBuilder("SELECT * FROM Persons WHERE PersonID = '");
            checkIfExists.append(person.getPersonID());
            checkIfExists.append("'");
            ResultSet checkHelper = stmt.executeQuery(checkIfExists.toString());
            if (!checkHelper.next()){
                throw new PersonNotFoundException();
            }
            else{
                stmt.executeUpdate(sql.toString());
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return;
    }

    public void clearPersons(){
        String sql = "DELETE FROM PERSONS";
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
