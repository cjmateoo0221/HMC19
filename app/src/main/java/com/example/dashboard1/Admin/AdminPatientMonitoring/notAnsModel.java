package com.example.dashboard1.Admin.AdminPatientMonitoring;

public class notAnsModel {
    String contactnum, barangay, patientnum, firstname, lastname, status, email, refnum, currentStatus;
    notAnsModel(){

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

    public String getContactnum() {
        return contactnum;
    }

    public void setContactnum(String contactnum) {
        this.contactnum = contactnum;
    }

    public notAnsModel(String contactnum, String lastname, String barangay, String currentStatus, String email, String refnum, String patientnum, String firstname, String status) {
        this.patientnum = patientnum;
        this.email = email;
        this.firstname = firstname;
        this.contactnum = contactnum;
        this.lastname = lastname;
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
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
