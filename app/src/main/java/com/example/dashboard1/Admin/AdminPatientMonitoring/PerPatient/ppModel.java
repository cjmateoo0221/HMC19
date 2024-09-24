package com.example.dashboard1.Admin.AdminPatientMonitoring.PerPatient;

public class ppModel {
    String patientnum, status, message, cough, dateofmonitor, diarrhea, fatigue, headache, jointpain, shortness, sorethroat, temp, vomit, answered;
    ppModel(){

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ppModel(String answered) {
        this.answered = answered;
    }

    public String getAnswered() {
        return answered;
    }

    public void setAnswered(String answered) {
        this.answered = answered;
    }

    public String getPatientnum() {
        return patientnum;
    }

    public void setPatientnum(String patientnum) {
        this.patientnum = patientnum;
    }

    public ppModel(String status, String cough, String message, String patientnum,  String dateofmonitor, String diarrhea, String fatigue, String headache, String jointpain, String shortness, String sorethroat, String temp, String vomit) {
        this.cough = cough;
        this.status = status;
        this.dateofmonitor = dateofmonitor;
        this.diarrhea = diarrhea;
        this.fatigue = fatigue;
        this.message = message;
        this.headache = headache;
        this.jointpain = jointpain;
        this.shortness = shortness;
        this.sorethroat = sorethroat;
        this.temp = temp;
        this.vomit = vomit;
        this.patientnum = patientnum;
    }

    public String getCough() {
        return cough;
    }

    public void setCough(String cough) {
        this.cough = cough;
    }

    public String getDateofmonitor() {
        return dateofmonitor;
    }

    public void setDateofmonitor(String dateofmonitor) {
        this.dateofmonitor = dateofmonitor;
    }

    public String getDiarrhea() {
        return diarrhea;
    }

    public void setDiarrhea(String diarrhea) {
        this.diarrhea = diarrhea;
    }

    public String getFatigue() {
        return fatigue;
    }

    public void setFatigue(String fatigue) {
        this.fatigue = fatigue;
    }

    public String getHeadache() {
        return headache;
    }

    public void setHeadache(String headache) {
        this.headache = headache;
    }

    public String getJointpain() {
        return jointpain;
    }

    public void setJointpain(String jointpain) {
        this.jointpain = jointpain;
    }

    public String getShortness() {
        return shortness;
    }

    public void setShortness(String shortness) {
        this.shortness = shortness;
    }

    public String getSorethroat() {
        return sorethroat;
    }

    public void setSorethroat(String sorethroat) {
        this.sorethroat = sorethroat;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getVomit() {
        return vomit;
    }

    public void setVomit(String vomit) {
        this.vomit = vomit;
    }
}
