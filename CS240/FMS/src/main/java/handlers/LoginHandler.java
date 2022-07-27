package handlers;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import daos.Database;
import requests.LoginRequest;
import results.LoginResult;
import services.LoginService;
import com.google.gson.*;

public class LoginHandler extends Handler implements HttpHandler {



    @Override
    public void handle(HttpExchange exchange) throws IOException {


        LoginResult result = new LoginResult();


        Gson gson = new Gson();

        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                // Get the request body input stream
                InputStream reqBody = exchange.getRequestBody();
                // Read JSON string from the input stream
                String request = readString(reqBody);

                //String[] tokens = request.split("\"");

                LoginRequest logReq = new LoginRequest();

                logReq = gson.fromJson(request, LoginRequest.class);



                LoginService service = new LoginService();
                result = service.loginUser(logReq);


                String respData = gson.toJson(result);
                //System.out.println(respData);

                if (result.getSuccess()) {
                    // Start sending the HTTP response to the client, starting with
                    // the status code and any defined headers.
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    // Now that the status code and headers have been sent to the client,
                    // next we send the JSON data in the HTTP response body.

                    // Get the response body output stream.
                    // Write the JSON string to the output stream.
                    writeString(respData, exchange.getResponseBody());
                    // Close the output stream.  This is how Java knows we are done
                    // sending data and the response is complete
                    exchange.getResponseBody().close();
                    success = true;
                }

            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                result.setMessage("Error. Invalid username or password.");
                result.setSuccess(false);

                String respData = gson.toJson(result);
                writeString(respData, exchange.getResponseBody());
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
            exchange.getResponseBody().close();

            e.printStackTrace();
        }
    }

}
