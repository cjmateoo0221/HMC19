package com.example.dashboard1.Admin;

public class GetDate {
    int date, count;
    public GetDate(){

    }

    public GetDate(int date, int count) {
        this.date = date;
        this.count = count;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
