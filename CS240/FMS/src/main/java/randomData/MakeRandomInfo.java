package randomData;
import java.io.*;
import java.util.UUID;

import models.*;

import handlers.Handler;
import com.google.gson.*;

public class MakeRandomInfo {
    names fnames, mnames, snames;
    locations locations;
    Gson gson = new Gson();

    public MakeRandomInfo(){
        fill();
    }

    private void fill(){
        try {
            Handler forReading = new Handler();
            File file = new File("json/fnames.json");
            InputStream in = new FileInputStream(file);
            String json = forReading.readString(in);
            fnames = gson.fromJson(json, names.class);

            file = new File("json/mnames.json");
            in = new FileInputStream(file);
            json = forReading.readString(in);
            mnames = gson.fromJson(json, names.class);

            file = new File("json/snames.json");
            in = new FileInputStream(file);
            json = forReading.readString(in);
            snames = gson.fromJson(json, names.class);

            file = new File("json/locations.json");
            in = new FileInputStream(file);
            json = forReading.readString(in);
            locations = gson.fromJson(json, locations.class);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFName(){
        int size = fnames.getData().size();
        int index = (int)(Math.random()*size);
        return fnames.getData().get(index);
    }

    public String getMName(){
        int size = mnames.getData().size();
        int index = (int)(Math.random()*size);
        return mnames.getData().get(index);
    }

    public String getSName(){
        int size = snames.getData().size();
        int index = (int)(Math.random()*size);
        return snames.getData().get(index);
    }

    public location getLocation(){
        int size = locations.getData().size();
        int index = (int)(Math.random()*size);
        return locations.getData().get(index);
    }

    public int childBearingAge(){//also marrying age
        return (int)(Math.random()*20) + 20;
    }

    public int deathAge(){
        return (int)(Math.random()*40) + 40;
    }

    //for testing purposes only
    public static void main(String[] args){
        MakeRandomInfo m = new MakeRandomInfo();
        m.fill();

        for (int i = 0; i < 20; i++){
            System.out.println("Random female name: " + m.getFName());
            System.out.println("Random male name: " + m.getMName());
            System.out.println("Random surname: " + m.getSName());
            System.out.println("A child bearing age: " + m.childBearingAge());
        }
    }


}
