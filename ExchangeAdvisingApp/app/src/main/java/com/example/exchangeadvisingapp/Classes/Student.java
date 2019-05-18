package com.example.exchangeadvisingapp.Classes;

public class Student {

    private String username,name,lastname,dateOfBirth,gender,phonenumber,city,school,career,about;

    public Student() {

    }

    public Student(String name, String lastname, String dateOfBirth, String phonenumber, String city, String school, String career, String about) {
        this.name = name;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.phonenumber = phonenumber;
        this.city = city;
        this.school = school;
        this.career = career;
        this.about = about;
    }

    public Student(String username, String name, String lastname, String dateOfBirth, String gender, String phonenumber, String city, String school, String career, String about) {
        this.username = username;
        this.name = name;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phonenumber = phonenumber;
        this.city = city;
        this.school = school;
        this.career = career;
        this.about = about;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getCity() {
        return city;
    }

    public String getSchool() {
        return school;
    }

    public String getCareer() {
        return career;
    }

    public String getAbout() {
        return about;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
