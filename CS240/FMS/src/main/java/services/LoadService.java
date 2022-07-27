package services;
import Exceptions.DatabaseException;
import Exceptions.EventAlreadyExistsException;
import Exceptions.PersonAlreadyExistsException;
import Exceptions.UserAlreadyExistsException;
import daos.Database;
import requests.*;
import results.*;

public class LoadService extends Service{


    /**
     * clears the database and then loads data from the request object to fill it
     * @return the result object filled by success or failure of the load
     */
    public LoadResult loadData(LoadRequest request) throws UserAlreadyExistsException, PersonAlreadyExistsException, EventAlreadyExistsException{
        Database db = new Database();

        try {
            db.clearTables();
            for (int i = 0; i < request.getData().getUsers().size(); i++){
                db.getUdao().addUser(request.getData().getUsers().get(i));
            }
            for (int i = 0; i < request.getData().getPersons().size(); i++){
                db.getPdao().addPerson(request.getData().getPersons().get(i));
            }
            for (int i = 0; i < request.getData().getEvents().size(); i++){
                db.getEdao().addEvent(request.getData().getEvents().get(i));
            }

        }
        catch (DatabaseException e) {
            e.printStackTrace();
        }
        StringBuilder message = new StringBuilder("Successfully added ");
        message.append(request.getData().getUsers().size());
        message.append(" users, ");
        message.append(request.getData().getPersons().size());
        message.append(" persons, and ");
        message.append(request.getData().getEvents().size());
        message.append(" events to the database.");

        LoadResult result = new LoadResult();
        result.setSuccess(true);
        result.setMessage(message.toString());

        db.closeConnection(true);

        return result;
    }
}
