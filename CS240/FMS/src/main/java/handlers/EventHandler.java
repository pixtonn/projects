package handlers;

import Exceptions.EventNotFoundException;
import Exceptions.PersonNotFoundException;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import daos.Database;
import results.EventResult;
import services.EventService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class EventHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        Gson gson = new Gson();

        String[] tokens = exchange.getRequestURI().toString().split("/");
        String eventID = tokens[2];

        EventResult result = new EventResult();

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

                        EventService service = new EventService();
                        result = service.getEvent(eventID);
                        result.setSuccess(true);
                        if (!result.getAssociatedUsername().equals(username)){
                            result = new EventResult();
                            result.setSuccess(false);
                            result.setMessage("Error. This event does not correspond to your current user.");
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
        catch(EventNotFoundException e){
            result.setMessage("Error. No event by given identification.");
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
