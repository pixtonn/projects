package services;
import Exceptions.PersonNotFoundException;
import requests.*;
import results.*;
import results.PersonResult;
import models.Person;
import daos.Database;

public class PersonService extends Service{


    /**
     * gets the person associated with the personID in the request object
     * @param personID the personID from the request object
     * @return the result of attempting to get the person from the database
     */
    public PersonResult getPerson(String personID) throws PersonNotFoundException {

        PersonResult result = new PersonResult();
        Database db = new Database();
        Person person;
        try {
            person = db.getPdao().getPerson(personID);
        }
        catch(PersonNotFoundException e){
            db.closeConnection(false);
            throw e;
        }

        result.setAssociatedUsername(person.getUsername());
        result.setFirstName(person.getFirstName());
        result.setLastName(person.getLastName());
        result.setFatherID(person.getFatherID());
        result.setMotherID(person.getMotherID());
        result.setSpouseID(person.getSpouseID());
        result.setPersonID(person.getPersonID());
        result.setGender(person.getGender());

        db.closeConnection(false);

        result.setSuccess(true);
        return result;
    }

    /**
     * gets the list of all people related to the current user, defined by request's auth token
     * @return the result object with an arraylist of persons who are related to the current user
     */
    //Result getFamily(){
    //    return null;
    //}



}
