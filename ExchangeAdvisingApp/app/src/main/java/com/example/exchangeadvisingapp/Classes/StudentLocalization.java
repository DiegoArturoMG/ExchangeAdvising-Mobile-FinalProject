package com.example.exchangeadvisingapp.Classes;

public class StudentLocalization {

    private String id;
    private String email;

    public StudentLocalization() {
    }

    public StudentLocalization(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
