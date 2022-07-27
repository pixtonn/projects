package handlers;

import Exceptions.EventAlreadyExistsException;
import Exceptions.PersonAlreadyExistsException;
import Exceptions.UserAlreadyExistsException;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import randomData.ReadInLoad;
import requests.LoadRequest;
import results.LoadResult;
import services.LoadService;

import java.io.IOException;
import java.net.HttpURLConnection;

public class LoadHandler extends Handler implements HttpHandler {


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;
        Gson gson = new Gson();

        LoadRequest request = new LoadRequest();
        LoadResult result = new LoadResult();

        String respData = "";

        if (exchange.getRequestMethod().toLowerCase().equals("post")){

            String reqBody = readString(exchange.getRequestBody());
            ReadInLoad data = gson.fromJson(reqBody, ReadInLoad.class);
            request.setData(data);

            LoadService service = new LoadService();

            try {
                result = service.loadData(request);
                success = true;
                result.setSuccess(true);

                respData = gson.toJson(result);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
            catch (UserAlreadyExistsException e) {
                result.setMessage("Error. Duplicate user in given data.");
            }
            catch (PersonAlreadyExistsException e) {
                result.setMessage("Error. Duplicate person in given data.");
            }
            catch (EventAlreadyExistsException e) {
                result.setMessage("Error. Duplicate event in given data.");
            }




        }

        if (!success){
            result.setSuccess(false);
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

        }

        respData = gson.toJson(result);
        writeString(respData, exchange.getResponseBody());

        exchange.getResponseBody().close();

    }
}
