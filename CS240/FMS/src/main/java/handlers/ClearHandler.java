package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import services.ClearService;
import results.ClearResult;
import com.google.gson.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ClearHandler extends Handler implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange){

        boolean success = false;
        Gson gson = new Gson();

        if (exchange.getRequestMethod().toLowerCase().equals("post")){

            ClearService service = new ClearService();
            ClearResult result = service.clearDatabase();

            String respData = gson.toJson(result);

            try {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                writeString(respData, exchange.getResponseBody());
                exchange.getResponseBody().close();
                success = true;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
