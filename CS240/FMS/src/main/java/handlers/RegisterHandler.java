package handlers;

import Exceptions.UserAlreadyExistsException;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import requests.RegisterRequest;
import results.RegisterResult;
import services.RegisterService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class RegisterHandler extends Handler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        Gson gson = new Gson();

        RegisterService service = new RegisterService();
        RegisterRequest request = new RegisterRequest();
        RegisterResult result = new RegisterResult();


        if (exchange.getRequestMethod().toLowerCase().equals("post")){

            String reqBody = readString(exchange.getRequestBody());
            request = gson.fromJson(reqBody, RegisterRequest.class);

            try {
                result = service.registerUser(request);

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
            catch(UserAlreadyExistsException e){
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                result.setMessage("Error. Username taken. Please use a different username.");
                result.setSuccess(false);
            }
            String respData = gson.toJson(result);

            writeString(respData, exchange.getResponseBody());
        }
        else{
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            result.setSuccess(false);
            result.setMessage("Error. Invalid input.");
            String respData = gson.toJson(result);

            writeString(respData, exchange.getResponseBody());
        }
        exchange.getResponseBody().close();


    }
}
