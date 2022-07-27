package services;
import Exceptions.*;
import daos.Database;
import models.*;
import randomData.*;

import java.util.UUID;

public class Service {

    protected Database db;
    MakeRandomInfo data;

    public Service(){
        db = new Database();
    }

    protected String createAuthToken(User user){
        String token = UUID.randomUUID().toString();
        try{
            db.getAtdao().addToken(token, user.getUserName());
        }
        catch(TokenAlreadyExistsException e){
            e.printStackTrace();
        }
        return token;
    }
/*
    It is assumed that the passed username has nothing associated with it in the database.
    This and the helper function below are quite a lot of code and somewhat repetitive. However,
    I'm not sure it can be condensed easily into functions, due to minute details being different
    in each repetitive part. This was the easiest and most effective method for me to write it.
 */
    protected void makeGenerations(String username, int numGen) throws UserNotFoundException {
        User user = new User();
        data = new MakeRandomInfo();

        user = db.getUdao().getUser(username);

        Person me = new Person();
        me.setUsername(user.getUserName());
        me.setPersonID(user.getPersonID());
        me.setFirstName(user.getFirstName());
        me.setLastName(user.getLastName());
        me.setGender(user.getGender());
        me.setFatherID(UUID.randomUUID().toString());
        me.setMotherID(UUID.randomUUID().toString());

        Event birth = new Event();
        location loc = data.getLocation();
        birth.setCountry(loc.getCountry());
        birth.setCity(loc.getCity());
        birth.setAssociatedUsername(user.getUserName());
        birth.setLatitude(loc.getLatitude());
        birth.setLongitude(loc.getLongitude());
        birth.setYear(2005 - (int)(Math.random()*70));//seems like a reasonable guess for the person's birth year, between 1935 and 2005
        birth.setPersonID(me.getPersonID());
        birth.setEventType("birth");
        birth.setEventID(UUID.randomUUID().toString());

        Event caughtAFrog = new Event();
        loc = data.getLocation();
        caughtAFrog.setCountry(loc.getCountry());
        caughtAFrog.setCity(loc.getCity());
        caughtAFrog.setAssociatedUsername(user.getUserName());
        caughtAFrog.setLatitude(loc.getLatitude());
        caughtAFrog.setLongitude(loc.getLongitude());
        caughtAFrog.setYear(birth.getYear() + 6);
        caughtAFrog.setPersonID(me.getPersonID());
        caughtAFrog.setEventType("caught a frog");
        caughtAFrog.setEventID(UUID.randomUUID().toString());

        Event caughtAToad = new Event();
        loc = data.getLocation();
        caughtAToad.setCountry(loc.getCountry());
        caughtAToad.setCity(loc.getCity());
        caughtAToad.setAssociatedUsername(user.getUserName());
        caughtAToad.setLatitude(loc.getLatitude());
        caughtAToad.setLongitude(loc.getLongitude());
        caughtAToad.setYear(birth.getYear() + 10);
        caughtAToad.setPersonID(me.getPersonID());
        caughtAToad.setEventType("caught a toad");
        caughtAToad.setEventID(UUID.randomUUID().toString());

        try {
            db.getPdao().addPerson(me);
            db.getEdao().addEvent(birth);
            db.getEdao().addEvent(caughtAFrog);
            db.getEdao().addEvent(caughtAToad);
        }
        catch (EventAlreadyExistsException e) {
            e.printStackTrace();
        }
        catch (PersonAlreadyExistsException e) {
            e.printStackTrace();
        }

        //begin recursion
        try {
            makeGenHelper(me, numGen, birth.getYear());
        }
        catch (PersonAlreadyExistsException | EventAlreadyExistsException e) {
            e.printStackTrace();
        }


    }


    private void makeGenHelper(Person curr, int genNum, int currBirthYear) throws PersonAlreadyExistsException, EventAlreadyExistsException {
        //make parents
        Person mother = new Person();
        mother.setUsername(curr.getUsername());
        mother.setGender("f");
        mother.setFirstName(data.getFName());
        mother.setLastName(data.getSName());
        mother.setPersonID(UUID.randomUUID().toString());
        mother.setSpouseID(UUID.randomUUID().toString());
        curr.setMotherID(mother.getPersonID());

        Person father = new Person();
        father.setUsername(curr.getUsername());
        father.setGender("m");
        father.setFirstName(data.getMName());
        father.setLastName(curr.getLastName());
        father.setPersonID(mother.getSpouseID());
        father.setSpouseID(mother.getPersonID());
        curr.setFatherID(father.getPersonID());

        try {
            db.getPdao().deletePerson(curr);
        }
        catch (PersonNotFoundException e) {
            e.printStackTrace();
        }
        db.getPdao().addPerson(curr);
        db.getPdao().addPerson(mother);
        db.getPdao().addPerson(father);


        Event mBirth = new Event();
        location loc = data.getLocation();
        mBirth.setCountry(loc.getCountry());
        mBirth.setCity(loc.getCity());
        mBirth.setAssociatedUsername(curr.getUsername());
        mBirth.setLatitude(loc.getLatitude());
        mBirth.setLongitude(loc.getLongitude());
        mBirth.setYear(currBirthYear - data.childBearingAge());
        mBirth.setPersonID(mother.getPersonID());
        mBirth.setEventType("birth");
        mBirth.setEventID(UUID.randomUUID().toString());

        Event fBirth = new Event();
        loc = data.getLocation();
        fBirth.setCountry(loc.getCountry());
        fBirth.setCity(loc.getCity());
        fBirth.setAssociatedUsername(curr.getUsername());
        fBirth.setLatitude(loc.getLatitude());
        fBirth.setLongitude(loc.getLongitude());
        fBirth.setYear(mBirth.getYear());
        fBirth.setPersonID(father.getPersonID());
        fBirth.setEventType("birth");
        fBirth.setEventID(UUID.randomUUID().toString());

        Event marriage = new Event();
        loc = data.getLocation();
        marriage.setCountry(loc.getCountry());
        marriage.setCity(loc.getCity());
        marriage.setAssociatedUsername(curr.getUsername());
        marriage.setLatitude(loc.getLatitude());
        marriage.setLongitude(loc.getLongitude());
        marriage.setYear(mBirth.getYear()+data.childBearingAge());
        marriage.setPersonID(mother.getPersonID());
        marriage.setEventType("marriage");
        marriage.setEventID(UUID.randomUUID().toString());

        db.getEdao().addEvent(mBirth);
        db.getEdao().addEvent(fBirth);
        db.getEdao().addEvent(marriage);

        marriage.setPersonID(father.getPersonID());
        marriage.setEventID(UUID.randomUUID().toString());

        db.getEdao().addEvent(marriage);

        Event mDeath = new Event();
        loc = data.getLocation();
        mDeath.setCountry(loc.getCountry());
        mDeath.setCity(loc.getCity());
        mDeath.setAssociatedUsername(curr.getUsername());
        mDeath.setLatitude(loc.getLatitude());
        mDeath.setLongitude(loc.getLongitude());
        mDeath.setYear(currBirthYear-data.deathAge());
        mDeath.setPersonID(mother.getPersonID());
        mDeath.setEventType("death");
        mDeath.setEventID(UUID.randomUUID().toString());

        Event fDeath = new Event();
        loc = data.getLocation();
        fDeath.setCountry(loc.getCountry());
        fDeath.setCity(loc.getCity());
        fDeath.setAssociatedUsername(curr.getUsername());
        fDeath.setLatitude(loc.getLatitude());
        fDeath.setLongitude(loc.getLongitude());
        fDeath.setYear(currBirthYear-data.deathAge());
        fDeath.setPersonID(father.getPersonID());
        fDeath.setEventType("death");
        fDeath.setEventID(UUID.randomUUID().toString());

        db.getEdao().addEvent(mDeath);
        db.getEdao().addEvent(fDeath);

        if(genNum == 1){
            return;
        }

        makeGenHelper(father, genNum-1, fBirth.getYear());
        makeGenHelper(mother, genNum-1, mBirth.getYear());

        return;
    }




}