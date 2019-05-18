package com.example.exchangeadvisingapp.Classes;

public class School {

    private String city;
    private String nombre;

    public School() {

    }

    public School(String city, String nombre) {
        this.city = city;
        this.nombre = nombre;
    }

    public School(String nombre) {
        this.nombre = nombre;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
