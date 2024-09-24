package com.example.dashboard1.Admin.AdminPatientView;

public class Admin_newDataModel {
    String firstname, lastname, age, patientnum;

    Admin_newDataModel(){

    }

    public Admin_newDataModel(String firstname, String lastname, String age, String patientnum) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.patientnum = patientnum;
        this.age = age;
    }

    public String getPatientnum() {
        return patientnum;
    }

    public void setPatientnum(String patientnum) {
        this.patientnum = patientnum;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
