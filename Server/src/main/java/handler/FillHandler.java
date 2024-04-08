package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.FillRequest;
import response.FillResponse;
import service.FillService;

import java.io.*;
import java.net.HttpURLConnection;

public class FillHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            Gson gson = new Gson();
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                String reqData = exchange.getRequestURI().toString();
                String[] tempString = reqData.split("/");     // splits the string by each segment containing /
                if (tempString.length < 4) {
                    reqData = reqData + "/4";               // if the number of generations is not given, it's set to 4
                }
                tempString = reqData.split("/");

                FillRequest request = new FillRequest(tempString[2], Integer.parseInt(tempString[3]));
                FillService service = new FillService();
                FillResponse response = service.fill(request);

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
        catch (Exception e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}
