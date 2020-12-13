package com.example.fiiliskayra;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

/**
 * Sovelluksen päänäkymä.
 * Rakentaa Merkinta muuttujia käyttäjän syötteiden mukaan ja tallentaa ne.
 * Palattaessa muista aktiviteeteista, lataa merkinnät uudestaa mahdollisten muutosten vuoksi.
 * @author Heta Huostila
 */

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private NumberPicker picker;

    private ArrayList<Merkinta> lista;

    private TextView menoText;
    private ArrayList<String> menoLista = new ArrayList<String>();

    /**
     * Käynnistyessä lataa merkinnät ja asettaa oletukset päänäkymän toimintaan.
     */

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

        menoLista.add("Miten menee?"); menoLista.add("Mikä meno?"); menoLista.add("Kuis kulkee?"); menoLista.add("Onko hyvä päivä?");
        menoText = findViewById(R.id.menoText);
        menoText.setText(menoLista.get(new Random().nextInt(menoLista.size())));

    }

    /**
     * Palattaessa lataa merkinnät uudestaan.
     */

    @Override
    protected void onResume(){
        super.onResume();
        readFile();
    }

    /**
     * Hakee tekstikentän merkkijonon ja asettaa sen tyhjäksi.
     * @return Syötetyn merkkijonon.
     */

    public String getMessage() {
        EditText editText = findViewById(R.id.noteBox);
        String s = editText.getText().toString();
        editText.setText("");
        return s;
    }

    /**
     * Hakee numeron numerovalitsimesta.
     * @return Valitun kokonaisnumeron.
     */

    public int getNumber(){
        return picker.getValue();
    }

    /**
     * Hakee merkkijonon sekä numeron ja lisää niiden avulla uuden Merkinta olion listaan.
     * Lista muutetaan merkkijonoksi Gson moduulin avulla.
     * <p>
     * Liitetään tallennus nappulaan.
     */

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

    /**
     * Lukee tallennetut merkinnät ja tallentaa ne "lista" muuttujaan.
     * SharedPreference muutetaan Gson moduulin avulla listaksi. Jos ladattavaa ei ole, luo uuden listan.
     */

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

    /**
     * Näyttää ruudulla tekstiä sovelluksen toiminnasta.
     * */

    public void infoButton(View v){
        Toast toast = Toast.makeText(MainActivity.this, "Tallenna fiiliksesi asteikolla 1-10 ja lisää muistiinpanoon mikä fiilis.", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();
    }

    public void menuPopup(View v) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(this);
        menu.inflate(R.menu.popup_menu);
        menu.show();
    }

    /**
     * Pudotusvalikon luoja.
     * Luo intentit kahdelle muulle aktiviteetille.
     */

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Intent intentKayra = new Intent(this, Kayra.class);
        Intent intentPanot = new Intent(this, Muistiinpanot.class);
        switch (item.getItemId()) {
            case R.id.kayra:
                startActivity(intentKayra);
                return true;
            case R.id.muistiinpanot:
                startActivity(intentPanot);
                return true;
            default:
                return false;
        }
    }
}