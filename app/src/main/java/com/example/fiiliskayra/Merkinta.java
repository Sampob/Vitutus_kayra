package com.example.fiiliskayra;

import java.util.Calendar;

/**
 * Luokka tallentaa käyttäjän muistiinpanon, valitun numeron sekä luomisajan.
 * @author Sampo Bredenberg
 */

public class Merkinta {

    private String note;
    private int numero;
    private Calendar ct;

    /**
     * Merkintä olioon tallennetaan muistiinpano, valittu kokonaisluku sekä luomisaika.
     * @param note Käyttäjän määrittämä muistiinpano. Tyhjä muistiinpano tallentaa tyhjän merkkijonon.
     * @param numero Käyttäjän määrittämä kokonaisluku.
     */

    public Merkinta(String note, int numero){
        this.note = note;
        this.numero = numero;
        ct = Calendar.getInstance();
    }

    /**@return Muistiinpano merkkijono*/
    public String getNote() {
        return note;
    }

    /**@return Kokonaisnumero 1 - 10*/
    public int getNumero() {
        return numero;
    }

    /**
     * Palauttaa luomishetken tiedot.
     * SimpleDateFormatin avulla pystyy muotoilla haluttuun muotoon.
     * @return Luomishetken kaikki data.
     */

    public Calendar getCalendar(){
        return ct;
    }

    public String toString(){
        return note;
    }
}

