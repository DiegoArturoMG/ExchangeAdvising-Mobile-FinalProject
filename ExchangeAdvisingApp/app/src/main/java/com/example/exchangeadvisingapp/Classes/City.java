package com.example.exchangeadvisingapp.Classes;

import java.util.ArrayList;

public class City {

    private String nombre;

    public City() {

    }

    public City(String nombre) {
        this.nombre = nombre;
    }

    public City(String nombre, ArrayList<School> school) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}
