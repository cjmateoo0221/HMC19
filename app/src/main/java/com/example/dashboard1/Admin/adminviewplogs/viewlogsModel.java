package com.example.dashboard1.Admin.adminviewplogs;

public class viewlogsModel {
    String action, date;
    viewlogsModel(){

    }

    public viewlogsModel(String action, String date) {
        this.action = action;
        this.date = date;
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
}
