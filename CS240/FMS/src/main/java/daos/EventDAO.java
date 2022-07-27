package daos;
import Exceptions.EventAlreadyExistsException;
import Exceptions.EventNotFoundException;
import Exceptions.PersonNotFoundException;
import models.Person;
import models.Event;
import java.util.ArrayList;
import java.sql.*;
import java.util.UUID;

public class EventDAO {

    Connection conn;

    EventDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * adds an event to a person
     * @param event the event to add to that person within the database
     * @throws EventAlreadyExistsException when the passed event already exists associated with the passed person
     */
    public void addEvent(Event event) throws EventAlreadyExistsException {
        try {
            Statement stmt = conn.createStatement();
            StringBuilder sql = new StringBuilder("INSERT INTO Events (EventID, PersonID, AssociatedUsername, Latitude, Longitude, Country, City, EventType, Year) VALUES (");
            sql.append("'");
            sql.append(event.getEventID());
            sql.append("', '");
            sql.append(event.getPersonID());
            if (event.getAssociatedUsername() == null){
                sql.append("', NULL, '");
            }
            else{
                sql.append("', '");
                sql.append(event.getAssociatedUsername().replace("'", "''"));
                sql.append("', '");
            }
            sql.append(event.getLatitude());
            sql.append("', '");
            sql.append(event.getLongitude());
            sql.append("', '");
            sql.append(event.getCountry().replace("'", "''"));
            sql.append("', '");
            sql.append(event.getCity().replace("'", "''"));
            sql.append("', '");
            sql.append(event.getEventType().replace("'", "''"));
            sql.append("', ");
            sql.append(event.getYear());
            sql.append(")");

            //fixme remove below
            //System.out.println(sql.toString());
            //fixme remove above

            stmt.executeUpdate(sql.toString());
        }
        catch (SQLException e) {
            throw new EventAlreadyExistsException();
        }

        return;
    }

    public ResultSet getEvents(String username){
        StringBuilder sql = new StringBuilder("SELECT * FROM Events WHERE associatedUsername='");
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

    /**
     * removes an event from the database from the given person
     * @param eventID the event to delete from the database
     * @throws EventNotFoundException when the passed event does not exist in the database
     */
    public void deleteEvent(String eventID) throws EventNotFoundException {
        StringBuilder sql = new StringBuilder("DELETE FROM Events WHERE EventID = '");
        sql.append(eventID);
        sql.append("'");

        try{
            Statement stmt = conn.createStatement();
            StringBuilder checkIfExists = new StringBuilder("SELECT * FROM Events WHERE EventID = '");
            checkIfExists.append(eventID);
            checkIfExists.append("'");

            ResultSet checkHelper = stmt.executeQuery(checkIfExists.toString());
            if (!checkHelper.next()){
                throw new EventNotFoundException();
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

    /**
     * gets the event by using the eventID
     * @param eventID the string identification for the event to return
     * @return the event with the passed eventID
     * @throws EventNotFoundException when the passed eventID does not exist in the database
     */
    public Event getEvent(String eventID) throws EventNotFoundException{
        StringBuilder sql = new StringBuilder("SELECT * FROM Events WHERE EventID='");
        sql.append(eventID);
        sql.append("'");
        Event event = new Event();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql.toString());
            //System.out.println(sql.toString());
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                event.setEventID(rs.getString(1));
                event.setPersonID(rs.getString(2));
                event.setAssociatedUsername(rs.getString(3));
                event.setLatitude(Double.parseDouble(rs.getString(4)));
                event.setLongitude(Double.parseDouble(rs.getString(5)));
                event.setCountry(rs.getString(6));
                event.setCity(rs.getString(7));
                event.setEventType(rs.getString(8));
                event.setYear(rs.getInt(9));

            }
            if (event.getEventID() == null){
                throw new EventNotFoundException();
            }

        }
        catch(SQLException e){
            throw new EventNotFoundException();
        }

        return event;

    }

    public void clearEvents(){
        String sql = "DELETE FROM EVENTS";
        try{
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * returns all events of the given person
     * @param person the person whose events will be returned
     * @return the arraylist of all events recorded in the database for the given person
     * @throws PersonNotFoundException when the passed person does not exist within the database
     */
//    ArrayList<Event> getEvents(Person person) throws PersonNotFoundException{
//        return null;
//    }


}
