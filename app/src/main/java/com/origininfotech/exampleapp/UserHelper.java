package com.origininfotech.exampleapp;

public class UserHelper {
    String name,email,number,college,city,role,securityquestionsort;

    public UserHelper() {
    }

    public UserHelper(String name, String email, String number, String college, String city, String role, String securityquestionsort) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.college = college;
        this.city = city;
        this.role = role;
        this.securityquestionsort = securityquestionsort;
    }

    public UserHelper(String username, String unumber, String ucollege, String ucity) {
        this.name = name;
        this.number = number;
        this.college = college;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
