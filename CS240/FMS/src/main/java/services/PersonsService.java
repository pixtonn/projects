package services;

import daos.Database;
import models.Person;
import results.PersonsResult;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonsService extends Service {

    public PersonsResult getPersons(String username){

        Database db = new Database();
        PersonsResult result = new PersonsResult();

        ResultSet rs = db.getPdao().getPersons(username);

        try {
            while (rs.next()) {
                Person person = new Person();
                person.setPersonID(rs.getString(1));
                person.setFirstName(rs.getString(2));
                person.setLastName(rs.getString(3));
                person.setFatherID(rs.getString(4));
                person.setMotherID(rs.getString(5));
                person.setSpouseID(rs.getString(6));
                person.setGender(rs.getString(7));
                person.setUsername(rs.getString(8));
                result.add(person);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        db.closeConnection(false);
        return result;
    }

}
