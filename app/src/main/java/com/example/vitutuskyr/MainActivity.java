package com.example.vitutuskyr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private NumberPicker picker;
    public static final String EXTRA = "com.example.firstapp.test";

    private ArrayList<Merkinta> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readFile();

        picker = findViewById(R.id.numberPicker);

        String[] pickerNums = new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        picker.setMinValue(1);
        picker.setMaxValue(pickerNums.length);
        picker.setWrapSelectorWheel(true);
        picker.setDisplayedValues(pickerNums);

    }

    public String getMessage(){
        EditText editText = findViewById(R.id.noteBox);
        return editText.getText().toString();
    }

    public int getNumber(){
        return picker.getValue();
    }

    public void saveThings(View v) {
        lista.add(new Merkinta(getMessage(), getNumber()));
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(lista);
        editor.putString("lista", json);
        editor.apply();

        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
    }

    public void readFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("lista", null);
        Type type = new TypeToken<ArrayList<Merkinta>>() {}.getType();
        lista = gson.fromJson(json, type);

        if(lista == null) {
            lista = new ArrayList<>();
        }
    }

    public void graphView(View v){
        Intent intent = new Intent(this, Kayra.class);
        startActivity(intent);
    }
    public void noteView(View v){
        Intent intent = new Intent(this, Muistiinpanot.class);
        startActivity(intent);
    }
}