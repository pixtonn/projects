package services;

import daos.Database;
import models.Event;
import results.EventsResult;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EventsService extends Service{

    public EventsResult getEvents(String username){

        Database db = new Database();
        EventsResult result = new EventsResult();

        ResultSet rs = db.getEdao().getEvents(username);

        try {
            while (rs.next()) {
                Event event = new Event();
                event.setEventID(rs.getString(1));
                event.setPersonID(rs.getString(2));
                event.setAssociatedUsername(rs.getString(3));
                event.setLatitude(Double.parseDouble(rs.getString(4)));
                event.setLongitude(Double.parseDouble(rs.getString(5)));
                event.setCountry(rs.getString(6));
                event.setCity(rs.getString(7));
                event.setEventType(rs.getString(8));
                event.setYear(rs.getInt(9));
                result.add(event);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        db.closeConnection(false);
        return result;
    }



}
