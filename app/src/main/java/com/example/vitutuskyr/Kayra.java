package com.example.vitutuskyr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Kayra extends AppCompatActivity {

    private LineChart mpLineChart;
    private ArrayList<Merkinta> lista;
    private TextView averageText;
    private TextView maaraText;
    private DecimalFormat df = new DecimalFormat("0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayra);

        readFile();

        averageText = findViewById(R.id.averageText);
        df.setRoundingMode(RoundingMode.UP);
        if(average() <= 3) {
            averageText.setText("Ka. " + df.format(average()) + " ↓");
            averageText.setTextColor(Color.rgb(252,145,58));
        } else if(average() >= 7) {
            averageText.setText("Ka. " + df.format(average()) + " ↑");
            averageText.setTextColor(Color.rgb(54,128,45));
        } else { averageText.setTextColor(Color.BLACK);averageText.setText("Ka. " + df.format(average()) + " -");}

        maaraText = findViewById(R.id.maaraText);
        maaraText.setText(lista.size() + " Merkintää");

        mpLineChart = findViewById(R.id.lineChart);
        mpLineChart.setTouchEnabled(true);
        mpLineChart.setScaleEnabled(true);
        mpLineChart.setPinchZoom(true);

        XAxis xAxis = mpLineChart.getXAxis();
        YAxis leftAxis = mpLineChart.getAxisLeft();
        mpLineChart.getAxisRight().setEnabled(false);

        XAxis.XAxisPosition position = XAxis.XAxisPosition.BOTTOM;
        xAxis.setPosition(position);
        leftAxis.setTextSize(16);
        xAxis.setTextSize(12);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel()));
        leftAxis.setValueFormatter(new IndexAxisValueFormatter(yAxisLabel()));

        leftAxis.setAxisMaximum(11);
        leftAxis.setAxisMinimum(0);
        leftAxis.setSpaceBottom(10);
        leftAxis.setLabelCount(6);

        xAxis.setAxisMinimum(-0.25f);
        xAxis.setAxisMaximum(xAxisLabel().size()-0.5f);
        xAxis.setLabelCount(4);

        mpLineChart.setScaleMinima((0.5f), 1.225f);

        LineDataSet lineDataSet = new LineDataSet(dataValues(), "Käyrä");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + ((int) value);
            }
        });

        mpLineChart.setData(data);
        mpLineChart.invalidate();

        Description description = new Description();
        description.setText("");
        mpLineChart.setDescription(description);
        mpLineChart.getLegend().setEnabled(false);

        lineDataSet.setLineWidth(4);
        lineDataSet.setColor(Color.parseColor("#9c27B0"));
        lineDataSet.setCircleColor(Color.parseColor("#9C27B0"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleRadius(8);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setValueTextSize(16);

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

    private ArrayList<Entry> dataValues(){
        ArrayList<Entry> dataVals = new ArrayList<>();
        for(int i = 0; i < lista.size(); i++){
            dataVals.add(new Entry(i, lista.get(i).getNumero()));
        }
        return dataVals;
    }

    private ArrayList<String> xAxisLabel() {
        ArrayList<String> xAxisList = new ArrayList<>();
        for(int i=0; i < lista.size(); i++){
            xAxisList.add(lista.get(i).getCurrentTime());
        }
        return xAxisList;
    }

    private ArrayList<String> yAxisLabel(){
        ArrayList<String> label = new ArrayList<>();
        label.add("");
        for(int i = 1; i < 11; i++){
            label.add(Integer.toString(i));
        }
        return label;
    }

    private Float average(){
        float total = 0;
        for (int i = 0; i < lista.size(); i++){
            total += lista.get(i).getNumero();
        }
        return total/(lista.size());
    }
}