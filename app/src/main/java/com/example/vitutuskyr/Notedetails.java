package com.example.vitutuskyr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class Notedetails extends AppCompatActivity {

    TextView textnote, textka, textday;
    private ArrayList<Merkinta> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        Bundle b = getIntent().getExtras();
        int i = b.getInt(Muistiinpanot.EXTRA, 0);

        textnote = findViewById(R.id.textNote);
        textka = findViewById(R.id.textKa);
        textday = findViewById(R.id.textDay);

        readFile();
        textnote.setText(lista.get(i).getNote());
        textka.setText("" + lista.get(i).getNumero());
        textday.setText("Päivä: " + lista.get(i).getCalendar().getTime());
    }
    private void readFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("lista", null);
        Type type = new TypeToken<ArrayList<Merkinta>>() {}.getType();
        lista = gson.fromJson(json, type);

        if (lista == null){
            lista = new ArrayList<>();
        }
    }
}