package services;
import requests.*;
import results.*;

import javax.xml.crypto.Data;
import daos.Database;

public class ClearService extends Service{

    /**
     * clears all data in the database
     * @return the result object with the results of clearing the database
     */
    public ClearResult clearDatabase(){
        ClearResult result = new ClearResult();
        Database db = new Database();

        db.getAtdao().clearTokens();
        db.getEdao().clearEvents();
        db.getPdao().clearPersons();
        db.getUdao().clearUsers();

        result.setMessage("Clear succeeded.");
        result.setSuccess(true);

        db.closeConnection(true);

        return result;
    }


}
