package com.example.vitutuskyr;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Merkinta implements Serializable {

    private String note;
    private int numero;
    private Date currentTime;

    public Merkinta(String note, int numero){
        this.note = note;
        this.numero = numero;
        currentTime = Calendar.getInstance().getTime();
    }

    public Merkinta(int numero){
        this.numero = numero;
        this.note = "";
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public String getStringNumero(){
        return Integer.toString(numero);
    }

    public String toString(){
        return note;
    }
}

