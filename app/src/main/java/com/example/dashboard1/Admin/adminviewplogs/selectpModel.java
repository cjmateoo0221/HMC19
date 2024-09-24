package com.example.dashboard1.Admin.adminviewplogs;

public class selectpModel {
    String patientnum, firstname, lastname;
    selectpModel(){

    }

    public selectpModel(String patientnum, String firstname, String lastname) {
        this.patientnum = patientnum;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getPatientnum() {
        return patientnum;
    }

    public void setPatientnum(String patientname) {
        this.patientnum = patientname;
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
