package com.example.vitutuskyr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Notedetails extends AppCompatActivity {

    TextView textnote, textarvosana, textday;                 //Hakee teksti elementit
    private ArrayList<Merkinta> lista;                  //Hakee listan, johon tallennetaan ja ladataan merkinnät

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        Bundle b = getIntent().getExtras();                                     //Reagoi listan klikkaamiseen
        int i = b.getInt(Muistiinpanot.EXTRA, 0);

        textnote = findViewById(R.id.textNote);                                 //Hakee teksti elementit
        textarvosana = findViewById(R.id.textArvosana);
        textday = findViewById(R.id.textDay);

        readFile();                                                             //Lukee tallennetut merkinnät, jotta teksti elementit saavat oikeat arvot
        textnote.setText(lista.get(i).getNote());                               //Asettaa teksti elementeille arvot
        textarvosana.setText("" + lista.get(i).getNumero());
        textday.setText("Päivä: " + lista.get(i).getCalendar().getTime());
    }
    private void readFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);   //Hakee SharedPreferenssin, joka on tallennettu muotoon Gson
        Gson gson = new Gson();
        String json = sharedPreferences.getString("lista", null);   //Muuttaa datan Gsonista ArrayList<Merkinta> muotoon ja tallentaa sen muuttujan listaan
        Type type = new TypeToken<ArrayList<Merkinta>>() {}.getType();
        lista = gson.fromJson(json, type);

    if (lista == null){                                                       //Rakentaa uuden listan, jos dataa ei ole
            lista = new ArrayList<>();
        }
    }
}