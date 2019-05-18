package com.example.exchangeadvisingapp.Classes;

public class Career {

    private String school;
    private String nombre;

    public Career() {
    }

    public Career(String nombre) {
        this.nombre = nombre;
    }

    public Career(String school, String nombre) {
        this.school = school;
        this.nombre = nombre;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
