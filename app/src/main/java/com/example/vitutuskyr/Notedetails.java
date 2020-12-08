package com.example.vitutuskyr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

public class Notedetails extends AppCompatActivity {

    TextView textnote, textka;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notedetails);

        Bundle b = getIntent().getExtras();
        int i = b.getInt(Muistiinpanot.EXTRA, 0);

        textnote = findViewById(R.id.textNote);
        textka = findViewById(R.id.textKa);

        textnote.setText(GlobalModel.getInstance().getLista().get(i).getNote());
    }
}