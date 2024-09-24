package com.example.dashboard1.Patient.notif;

public class pnmodel {
    String date, message, receiver, sender;
    pnmodel(){

    }
    public pnmodel(String date, String message, String receiver, String sender) {
        this.date = date;
        this.message = message;
        this.receiver = receiver;
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
