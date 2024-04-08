package com.example.matthewglausfamilymapclient.model;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import request.EventRequest;
import request.LoginRequest;
import request.PersonRequest;
import request.RegisterRequest;
import response.EventResponse;
import response.LoginResponse;
import response.PersonResponse;
import response.RegisterResponse;

public class ServerProxy {
    private static ServerProxy serverProxy = new ServerProxy();

    private ServerProxy() {
    }

    public static ServerProxy getInstance() {
        if(serverProxy == null) {
            serverProxy = new ServerProxy();
        }
        return serverProxy;
    }

    public LoginResponse login(LoginRequest request, String serverHost, String serverPort)
            throws IOException {
        Gson gson = new Gson();

        URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        http.addRequestProperty("Accept", "application/json");
        http.connect();
        String reqData = gson.toJson(request);
        OutputStream reqBody = http.getOutputStream();
        writeString(reqData, reqBody);
        reqBody.close();

        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            LoginResponse response = gson.fromJson(respData, LoginResponse.class);
            return response;
        }
        else {
            return new LoginResponse("Error: " + http.getResponseMessage());
        }
    }

    public RegisterResponse register(RegisterRequest request, String serverHost, String serverPort)
            throws IOException {
        Gson gson = new Gson();

        URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        http.addRequestProperty("Accept", "application/json");
        http.connect();
        String reqData = gson.toJson(request);
        OutputStream reqBody = http.getOutputStream();
        writeString(reqData, reqBody);
        reqBody.close();

        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            RegisterResponse response = gson.fromJson(respData, RegisterResponse.class);
            return response;
        }
        else {
            return new RegisterResponse("Error: " + http.getResponseMessage());
        }
    }



    public PersonResponse getPeople(PersonRequest request, String serverHost, String serverPort)
            throws IOException {
        Gson gson = new Gson();

        URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("GET");
        http.setDoOutput(false);
        http.addRequestProperty("Authorization", request.getAuthtoken());
        http.addRequestProperty("Accept", "application/json");

        http.connect();

        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            PersonResponse response = gson.fromJson(respData, PersonResponse.class);
            return response;
        }
        else {
            return new PersonResponse("Error: " + http.getResponseMessage());
        }
    }

    public EventResponse getEvents(EventRequest request, String serverHost, String serverPort)
            throws IOException {
        Gson gson = new Gson();

        URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setRequestMethod("GET");
        http.setDoOutput(false);
        http.addRequestProperty("Authorization", request.getAuthtoken());
        http.addRequestProperty("Accept", "application/json");

        http.connect();

        if(http.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            EventResponse response = gson.fromJson(respData, EventResponse.class);
            return response;
        }
        else {
            return new EventResponse("Error: " + http.getResponseMessage());
        }
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
