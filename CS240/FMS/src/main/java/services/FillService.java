package services;
import Exceptions.DatabaseException;
import Exceptions.UserNotFoundException;
import daos.Database;
import requests.*;
import results.*;

public class FillService extends Service{


    /**
     * fills in the family tree for x generations, where x is the generations value on the request
     * @return the result object made by filling the family tree
     */
    public FillResult fillFamily(FillRequest request) throws UserNotFoundException {
        db = new Database();
        try {
            db.getUdao().getUser(request.getUsername());
        }
        catch(UserNotFoundException e){
            db.closeConnection(false);
            throw e;
        }

        try {
            db.clearTables(request.getUsername());
            db.closeConnection(true);
            db = new Database();
            makeGenerations(request.getUsername(), request.getNumGen());
        }
        catch (DatabaseException e) {
            e.printStackTrace();
        }

        FillResult result = new FillResult();
        result.setSuccess(true);

        result.setMessage("Successfully added " + (int)(Math.pow(2, request.getNumGen() + 1) - 1) + " persons and " +
                ((int)(Math.pow(2,request.getNumGen() + 1) - 1) * 3) + " events to the database.");

        db.closeConnection(true);
        return result;
    }


}
