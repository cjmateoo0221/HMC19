package com.example.dashboard1.Patient.patientdetails;

public class pdmodel {
    String address, age, contactnum, email, locofwork, firstname, lastname, middlename, occupation, sex, vaccinestat, birthday, healthcond, household, pass, patientnum, status, swab, user, closecontact;

    public pdmodel(String closecontact) {
        this.closecontact = closecontact;
    }

    public String getClosecontact() {
        return closecontact;
    }

    public void setClosecontact(String closecontact) {
        this.closecontact = closecontact;
    }

    pdmodel(){

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

    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public pdmodel(String middlename, String lastname, String address, String age, String contactnum, String email, String locofwork, String firstname, String occupation, String sex, String vaccinestat, String birthday, String healthcond, String household, String pass, String patientnum, String status, String swab, String user) {
        this.address = address;
        this.age = age;
        this.contactnum = contactnum;
        this.email = email;
        this.lastname = lastname;
        this.middlename = middlename;
        this.locofwork = locofwork;
        this.firstname = firstname;
        this.occupation = occupation;
        this.sex = sex;
        this.vaccinestat = vaccinestat;
        this.birthday = birthday;
        this.healthcond = healthcond;
        this.household = household;
        this.pass = pass;
        this.patientnum = patientnum;
        this.status = status;
        this.swab = swab;
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getContactnum() {
        return contactnum;
    }

    public void setContactnum(String contactnum) {
        this.contactnum = contactnum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocofwork() {
        return locofwork;
    }

    public void setLocofwork(String locofwork) {
        this.locofwork = locofwork;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getVaccinestat() {
        return vaccinestat;
    }

    public void setVaccinestat(String vaccinestat) {
        this.vaccinestat = vaccinestat;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHealthcond() {
        return healthcond;
    }

    public void setHealthcond(String healthcond) {
        this.healthcond = healthcond;
    }

    public String getHousehold() {
        return household;
    }

    public void setHousehold(String household) {
        this.household = household;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public String getSwab() {
        return swab;
    }

    public void setSwab(String swab) {
        this.swab = swab;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
