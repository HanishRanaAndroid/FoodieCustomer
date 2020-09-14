package com.valle.foodieapp.models;

public class ChatBeanModel {

    private String date;
    private String meMessage;
    private String yourMessage;
    private String messageType;

    public ChatBeanModel(String date, String meMessage, String yourMessage, String messageType) {
        this.date = date;
        this.meMessage = meMessage;
        this.yourMessage = yourMessage;
        this.messageType = messageType;
    }

    public String getDate() {
        return date;
    }

    public String getMeMessage() {
        return meMessage;
    }

    public String getYourMessage() {
        return yourMessage;
    }

    public String getMessageType() {
        return messageType;
    }
}
