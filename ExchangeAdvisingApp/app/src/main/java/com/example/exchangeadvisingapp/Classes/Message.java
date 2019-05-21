package com.example.exchangeadvisingapp.Classes;

import java.io.Serializable;

public class Message implements Serializable {

    private String emisor;
    private String receptor;
    private String mensaje;

    public Message() {}

    public Message(String emisor, String receptor, String mensaje) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.mensaje = mensaje;
    }

    public Message(String emisor, String mensaje) {
        this.emisor = emisor;
        this.mensaje = mensaje;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
