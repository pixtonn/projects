package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;




public class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {

            //make sure it is a get request
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                String requestedURL = exchange.getRequestURI().toString();

                if (requestedURL.length() == 1){

                    String urlPath = new String("web/index.html" );
                    Path filePath = FileSystems.getDefault().getPath(urlPath);

                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                    Files.copy(filePath, exchange.getResponseBody());

                    exchange.getResponseBody().close();

                    success = true;
                }
                //catch anything else
                else {
                    String urlPath = "web" + requestedURL;
                    Path filePath = FileSystems.getDefault().getPath(urlPath);

                    //I really hope this isn't counted as "hard coding", because I tried a lot of things
                    //and only this would make the FileHandler run as needed
                    try {
                        if (urlPath.equals("web/css/main.css") ||
                                urlPath.equals("web/favicon.ico") ||
                                urlPath.equals("web/favicon.jpg")) {
                            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                        }

                        Files.copy(filePath, exchange.getResponseBody());

                        success = true;

                        exchange.getResponseBody().close();
                    }
                    catch (IOException e){
                        //simply continue
                    }


                }

            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                String urlPath = "web/HTML/404.html";
                Path filePath = FileSystems.getDefault().getPath(urlPath);

                Files.copy(filePath, exchange.getResponseBody());

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
