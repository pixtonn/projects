package server;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;
import handlers.*;

public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;


    private HttpServer server;


    private void run(String portNumber) {

        System.out.println("Initializing HTTP Server");

        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        server.setExecutor(null);


        System.out.println("Creating contexts");


        server.createContext("/user/login", new LoginHandler());


        server.createContext("/user/register", new RegisterHandler());


        server.createContext("/clear", new ClearHandler());


        server.createContext("/person/", new PersonHandler());


        server.createContext("/person", new PersonsHandler());


        server.createContext("/event/", new EventHandler());


        server.createContext("/event", new EventsHandler());


        server.createContext("/load", new LoadHandler());


        server.createContext("/fill/", new FillHandler());

        //Default, must be last
        server.createContext("/", new FileHandler());


        System.out.println("Starting server");


        server.start();


        System.out.println("Server started");
    }


    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
