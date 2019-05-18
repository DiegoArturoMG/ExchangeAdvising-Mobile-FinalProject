package com.example.exchangeadvisingapp.Classes;

public class MessageReceived {

    private String idStudent;
    private String userName;
    private String message;

    public MessageReceived() {
    }

    public MessageReceived(String idStudent, String userName, String message) {
        this.idStudent = idStudent;
        this.userName = userName;
        this.message = message;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
