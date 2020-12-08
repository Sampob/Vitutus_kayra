package com.example.vitutuskyr;

import java.util.Calendar;
import java.util.Date;

public class Merkinta {

    private String note;
    private int numero;
    private Calendar ct;

    public Merkinta(String note, int numero){
        this.note = note;
        this.numero = numero;
        ct = Calendar.getInstance();
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

    public String getCurrentTime(){
        return ct.get(Calendar.DATE) + "." + ct.get(Calendar.MONTH);
    }

    public Calendar getCalendar(){
        return ct;
    }

    public String getStringNumero(){
        return Integer.toString(numero);
    }

    public String toString(){
        return note;
    }
}

