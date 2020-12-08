package com.example.vitutuskyr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.github.mikephil.charting.data.Entry;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.net.Proxy;
import java.util.ArrayList;

public class Muistiinpanot extends AppCompatActivity {

    public static final String EXTRA = "EXTRA";
    private ArrayList<Merkinta> lista;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muistiinpanot);

        readFile();
        lv = findViewById(R.id.list);
        lv.setAdapter(new ArrayAdapter<Merkinta>(this, android.R.layout.simple_expandable_list_item_1, lista));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent noteDetails = new Intent(Muistiinpanot.this, Notedetails.class);
                noteDetails.putExtra(EXTRA, i);
                startActivity(noteDetails);
            }
        });
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