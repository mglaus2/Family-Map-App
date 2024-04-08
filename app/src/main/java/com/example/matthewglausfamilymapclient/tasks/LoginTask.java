package com.example.matthewglausfamilymapclient.tasks;

import android.os.Handler;

import com.example.matthewglausfamilymapclient.model.ServerProxy;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import request.LoginRequest;
import response.LoginResponse;

public class LoginTask implements Runnable {
    private static final String ERROR_MESSAGE = "Login Failed.";

    private final Handler messageHandler;
    private final String serverHost;
    private final String serverPort;
    private final LoginRequest request;

    public LoginTask(Handler messageHandler, String serverHost, String serverPort,
                     LoginRequest request) {
        this.messageHandler = messageHandler;
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.request = request;
    }

    @Override
    public void run() {
        ServerProxy serverProxy = ServerProxy.getInstance();
        LoginResponse response;
        try {
            response = serverProxy.login(request, serverHost, serverPort);

            if(!response.isSuccess()) {
                MessageHelper messageHelper = new MessageHelper(ERROR_MESSAGE, messageHandler);
                messageHelper.sendMessage();
            }

            DataTask dataTask = new DataTask(messageHandler, serverHost, serverPort,
                    response.getAuthtoken(), response.getPersonID());
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(dataTask);
        } catch(IOException e) {
            MessageHelper messageHelper = new MessageHelper(ERROR_MESSAGE, messageHandler);
            messageHelper.sendMessage();
            e.printStackTrace();
        }
    }
}

