package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.PersonWithPersonIDRequest;
import response.PersonWithPersonIDResponse;
import service.PersonWithPersonIDService;

import java.io.*;
import java.net.HttpURLConnection;

public class PersonWithPersonIDHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Gson gson = new Gson();
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {
                Headers reqHeaders = exchange.getRequestHeaders();
                if (reqHeaders.containsKey("Authorization")) {
                    String reqData = exchange.getRequestURI().toString();
                    String[] tempString = reqData.split("/");
                    String authtoken = reqHeaders.getFirst("Authorization");

                    PersonWithPersonIDRequest request = new PersonWithPersonIDRequest(tempString[2], authtoken);
                    PersonWithPersonIDService service = new PersonWithPersonIDService();
                    PersonWithPersonIDResponse response = service.person(request);

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
