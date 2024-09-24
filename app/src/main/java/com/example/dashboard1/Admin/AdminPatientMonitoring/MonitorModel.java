package com.example.dashboard1.Admin.AdminPatientMonitoring;

public class MonitorModel {
    String barangay, patientnum, name, status, email, refnum, currentStatus;
    MonitorModel(){

    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRefnum() {
        return refnum;
    }

    public void setRefnum(String refnum) {
        this.refnum = refnum;
    }

    public MonitorModel(String barangay, String currentStatus,String email, String refnum, String patientnum, String name, String status) {
        this.patientnum = patientnum;
        this.email = email;
        this.name = name;
        this.refnum = refnum;
        this.status = status;
        this.currentStatus = currentStatus;
        this.barangay = barangay;
    }

    public String getPatientnum() {
        return patientnum;
    }

    public void setPatientnum(String patientnum) {
        this.patientnum = patientnum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
