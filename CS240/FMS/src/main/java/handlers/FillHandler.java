package handlers;

import Exceptions.UserNotFoundException;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import requests.FillRequest;
import results.FillResult;
import services.FillService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class FillHandler extends Handler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = true;
        Gson gson = new Gson();

        int numGen = 0;

        FillResult result = new FillResult();

        String[] tokens = exchange.getRequestURI().toString().split("/");
        if (tokens.length < 3){
            success = false;
            result.setMessage("Error. Invalid input. Please enter a current username and a number of generations to create.");
        }
        else if (tokens.length == 3){
            numGen = 4;
        }
        else if (Integer.parseInt(tokens[3]) < 1){
            success = false;
            result.setMessage("Error. Please enter a number of generations greater than 0.");
        }
        else{
            numGen = Integer.parseInt(tokens[3]);
        }


        if (exchange.getRequestMethod().toLowerCase().equals("post") && success){
            String username = tokens[2];

            FillRequest request = new FillRequest();
            request.setNumGen(numGen);
            request.setUsername(username);

            FillService service = new FillService();

            try {
                result = service.fillFamily(request);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                result.setSuccess(true);
            }
            catch(UserNotFoundException e){
                result.setMessage("Error. No user by given username.");
                success = false;
            }

        }
        else{
            success = false;
        }

        if (!success){
            result.setSuccess(false);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
        }
        String respData = gson.toJson(result);

        writeString(respData, exchange.getResponseBody());

        exchange.getResponseBody().close();
    }
}
