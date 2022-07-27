package services;
import Exceptions.EventNotFoundException;
import models.Event;
import requests.*;
import results.*;
import daos.Database;

public class EventService extends Service{


    /**
     * gets the event with the correlating eventID, stored in the request object
     * @return the event associated with the given eventID
     */
    public EventResult getEvent(String eventID) throws EventNotFoundException {
        EventResult result = new EventResult();
        Database db = new Database();
        Event event = new Event();

        try{
            event = db.getEdao().getEvent(eventID);
        }
        catch(EventNotFoundException e){
            db.closeConnection(false);
            throw e;
        }

        result.setAssociatedUsername(event.getAssociatedUsername());
        result.setCity(event.getCity());
        result.setCountry(event.getCountry());
        result.setEventID(event.getEventID());
        result.setEventType(event.getEventType());
        result.setLatitude(event.getLatitude());
        result.setLongitude(event.getLongitude());
        result.setPersonID(event.getPersonID());
        result.setYear(event.getYear());

        db.closeConnection(true);

        return result;
    }

    /**
     * gets all events of all persons related to the current user, defined using request's auth token
     * @return the result object with an arraylist of all events of all persons related to the current user
     */
    //Result getAllEvents(){
    //    return null;
    //}



}
