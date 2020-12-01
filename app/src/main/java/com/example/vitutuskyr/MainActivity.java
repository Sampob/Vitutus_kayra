package com.example.vitutuskyr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

public class MainActivity extends AppCompatActivity {

    private NumberPicker picker;
    private String[] pickerNums;
    private EditText textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picker = findViewById(R.id.numberPicker);
        String[] pickerNums = new String[] {"10", "9", "8", "7", "6", "5", "4", "3", "2", "1"};
        picker.setMinValue(0);
        picker.setMaxValue(pickerNums.length-1);
        picker.setWrapSelectorWheel(false);

        picker.setDisplayedValues(pickerNums);
    }

}