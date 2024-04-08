package com.example.matthewglausfamilymapclient.tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MessageHelper {
    private String content;
    private Handler messageHandler;

    public MessageHelper(String content, Handler messageHandler) {
        this.content = content;
        this.messageHandler = messageHandler;
    }
    public void sendMessage() {
        Message message = Message.obtain();

        Bundle messageBundle = new Bundle();
        messageBundle.putString("message", content);
        message.setData(messageBundle);

        messageHandler.sendMessage(message);
    }
}
