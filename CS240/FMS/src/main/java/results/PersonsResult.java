package results;

import models.Person;

import java.util.ArrayList;
import java.util.Objects;

public class PersonsResult extends Result{
    ArrayList<Person> data;
    transient int index;

    public PersonsResult(){
        data = new ArrayList<Person>();
        index = 0;
    }

    public PersonsResult(ArrayList<Person> data) {
        this.data = data;
        index = 0;
    }

    public void add(Person person){
        data.add(index, person);
        index++;
    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonsResult that = (PersonsResult) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}
