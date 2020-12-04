package com.example.vitutuskyr;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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

    public Merkinta() {
        numero = 1;
        note = "";
    }

    public String getNote() {
        return note;
    }

    public String getCurrentTime(){
        return ct.get(Calendar.DATE) + "." + ct.get(Calendar.MONTH);
    }

    public Calendar getCalendar(){
        return ct;
    }

    public int getNumero() {
        return numero;
    }

    public String getStringNumero(){
        return Integer.toString(numero);
    }
}
