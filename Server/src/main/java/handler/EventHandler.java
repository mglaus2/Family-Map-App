package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.EventRequest;
import response.EventResponse;
import service.EventService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

public class EventHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Gson gson = new Gson();
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {   // makes sure the headers contain an authorization key
                    String authtoken = reqHeaders.getFirst("Authorization");

                    EventRequest request = new EventRequest(authtoken);
                    EventService service = new EventService();
                    EventResponse response = service.event(request);    // calls the service with the request

                    if (!response.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                    }
                    else {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }

                    Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                    gson.toJson(response, resBody);
                    resBody.close();
                }
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
