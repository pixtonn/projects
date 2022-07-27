package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import daos.Database;
import results.EventsResult;
import services.EventsService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class EventsHandler extends Handler implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        Gson gson = new Gson();

        EventsResult result = new EventsResult();

        if (exchange.getRequestMethod().toLowerCase().equals("get")){

            Headers reqHeaders = exchange.getRequestHeaders();
            if (reqHeaders.containsKey("Authorization")){

                String authToken = reqHeaders.getFirst("Authorization");
                Database db = new Database();
                if (db.getAtdao().verifyToken(authToken)){

                    String username = db.getAtdao().getUsername(authToken);
                    db.closeConnection(false);

                    EventsService service = new EventsService();
                    result = service.getEvents(username);
                    result.setSuccess(true);

                    String respData = gson.toJson(result);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    writeString(respData, exchange.getResponseBody());

                    exchange.getResponseBody().close();

                    success = true;


                }
                else{
                    db.closeConnection(false);
                    result.setMessage("Error. Incorrect authorization token.");
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    result.setSuccess(false);

                    result.setData(null);

                    String respData = gson.toJson(result);
                    writeString(respData, exchange.getResponseBody());

                    exchange.getResponseBody().close();
                }



            }

        }









    }

}
