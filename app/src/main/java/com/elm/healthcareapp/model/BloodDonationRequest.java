package com.elm.healthcareapp.model;

public class BloodDonationRequest {
    String fName,lName,phone,age,bloodType,gender,address;

    public BloodDonationRequest() {
    }

    public BloodDonationRequest(String fName, String lName, String phone, String age, String bloodType, String gender, String address) {
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.age = age;
        this.bloodType = bloodType;
        this.gender = gender;
        this.address = address;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
