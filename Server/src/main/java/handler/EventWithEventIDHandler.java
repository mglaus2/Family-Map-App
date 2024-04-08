package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.EventWithEventIDRequest;
import response.EventWithEventIDResponse;
import service.EventWithEventIDService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

public class EventWithEventIDHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Gson gson = new Gson();
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String reqData = exchange.getRequestURI().toString();
                    String[] tempString = reqData.split("/");   // splits the string by each segment containing /
                    String authtoken = reqHeaders.getFirst("Authorization");

                    EventWithEventIDRequest request = new EventWithEventIDRequest(tempString[2], authtoken);
                    EventWithEventIDService service = new EventWithEventIDService();
                    EventWithEventIDResponse response = service.event(request);

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
