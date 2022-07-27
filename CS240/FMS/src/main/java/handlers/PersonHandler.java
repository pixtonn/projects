package handlers;


import Exceptions.PersonNotFoundException;
import com.sun.net.httpserver.*;
import com.google.gson.*;
import daos.*;
import results.PersonResult;
import services.PersonService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class PersonHandler extends Handler implements HttpHandler{


    @Override
    public void handle(HttpExchange exchange) throws IOException{

        boolean success = false;
        Gson gson = new Gson();

        String[] tokens = exchange.getRequestURI().toString().split("/");
        String personID = tokens[2];

        PersonResult result = new PersonResult();

        String username;

        try{

            if (exchange.getRequestMethod().toLowerCase().equals("get")){

                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")){

                    String authToken = reqHeaders.getFirst("Authorization");
                    Database db = new Database();
                    if (db.getAtdao().verifyToken(authToken)){
                        username = db.getAtdao().getUsername(authToken);
                        db.closeConnection(false);

                        PersonService service = new PersonService();
                        result = service.getPerson(personID);
                        if (!result.getAssociatedUsername().equals(username)){
                            result = new PersonResult();
                            result.setSuccess(false);
                            result.setMessage("Error. This person is not connected to your current user.");
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                        }
                        else {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        }

                        String respData = gson.toJson(result);

                        writeString(respData, exchange.getResponseBody());

                        exchange.getResponseBody().close();

                        success = true;

                    }
                    else{
                        db.closeConnection(false);
                        result.setMessage("Error. Incorrect authorization token.");

                    }

                }

            }

        }
        catch(PersonNotFoundException e){
            result.setMessage("Error. No person by given identification.");
        }

        if (!success){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            result.setSuccess(false);

            String respData = gson.toJson(result);
            writeString(respData, exchange.getResponseBody());

            exchange.getResponseBody().close();
        }

    }






}
