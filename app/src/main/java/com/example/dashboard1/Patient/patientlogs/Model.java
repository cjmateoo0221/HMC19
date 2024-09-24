package com.example.dashboard1.Patient.patientlogs;

public class Model {
    String date, action;
    Model(){

    }
    public Model(String date, String action) {
        this.date = date;
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
