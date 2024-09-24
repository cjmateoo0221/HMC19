package com.example.dashboard1.Admin.newnotif;

public class Model {
    String action, date, patientnum;
    Model(){

    }
    public Model(String action, String date, String patientnum) {
        this.action = action;
        this.date = date;
        this.patientnum = patientnum;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPatientnum() {
        return patientnum;
    }

    public void setPatientnum(String patientnum) {
        this.patientnum = patientnum;
    }
}
