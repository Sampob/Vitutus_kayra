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

    public Merkinta() {
        numero = 1;
        note = "";
    }

    public String getNote() {
        return note;
    }

    public Date getCurrentTime(){
        return currentTime;
    }

    public int getNumero() {
        return numero;
    }

    public String getStringNumero(){
        return Integer.toString(numero);
    }
}
