package com.example.cloudauthentication.model;

public class profile_data {
    String id;
    String name;
    String email;
    String phone;
    String password;

    public profile_data(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getId() {
        return id;
    }
}
