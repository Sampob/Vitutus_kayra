package com.example.vitutuskyr;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class GlobalModel {
    private ArrayList<Merkinta> lista;
    private static final GlobalModel ourInstance = new GlobalModel();

    public static GlobalModel getInstance() {
        return ourInstance;
    }

    private GlobalModel() {
        lista = new ArrayList<Merkinta>();
    }

    public ArrayList<Merkinta> getLista() {
        return lista;
    }
}

