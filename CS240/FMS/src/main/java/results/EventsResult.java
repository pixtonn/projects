package results;

import models.Event;

import java.util.ArrayList;
import java.util.Objects;

public class EventsResult extends Result{
    ArrayList<Event> data;

    public EventsResult(){
        data = new ArrayList<Event>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventsResult that = (EventsResult) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    public void add(Event event){
        data.add(event);
    }

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }
}
