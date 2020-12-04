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
    private TextView tenAverageText;
    private TextView arrowText;
    private DecimalFormat df = new DecimalFormat("0.0");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayra);

        readFile();

        averageText = findViewById(R.id.averageText);
        df.setRoundingMode(RoundingMode.CEILING);
        if(average() <= 3) {
            averageText.setText(df.format(average()));
            averageText.setTextColor(Color.rgb(252,145,58));
        } else if(average() >= 7) {
            averageText.setText(df.format(average()));
            averageText.setTextColor(Color.rgb(54,128,45));
        } else {
            averageText.setTextColor(Color.BLACK);
            averageText.setText(df.format(average()));
        }

        tenAverageText = findViewById(R.id.tenAverageText);
        arrowText = findViewById(R.id.arrowText);
        if(lista.size() >= 10) {
            if (tenAverage() <= average()) {
                tenAverageText.setText(df.format(tenAverage()));
                arrowText.setText( "↓");
                tenAverageText.setTextColor(Color.rgb(252,145,58));
                arrowText.setTextColor(Color.rgb(252,145,58));
            } else if(tenAverage() >= average()) {
                tenAverageText.setText(df.format(tenAverage()));
                arrowText.setText("↑");
                tenAverageText.setTextColor(Color.rgb(54,128,45));
                arrowText.setTextColor(Color.rgb(54,128,45));
            } else {
                tenAverageText.setTextColor(Color.BLACK);
                tenAverageText.setText(df.format(tenAverage()));
                arrowText.setText("");
            }
        }
        maaraText = findViewById(R.id.maaraText);
        maaraText.setText(lista.size() + " Merkintää");

        mpLineChart = findViewById(R.id.lineChart);
        mpLineChart.setTouchEnabled(true);
        mpLineChart.setScaleEnabled(false);
        mpLineChart.setPinchZoom(false);

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
        leftAxis.setLabelCount(10);

        xAxis.setAxisMinimum(-0.25f);
        xAxis.setAxisMaximum(xAxisLabel().size()-0.5f);
        if(lista.size() < 8){
            xAxis.setLabelCount(lista.size());
            mpLineChart.setVisibleXRange(0, lista.size());
        } else {
            xAxis.setLabelCount(8);
            mpLineChart.setVisibleXRange(0, 8);
        }
        mpLineChart.setDragOffsetX(150);
        mpLineChart.moveViewToX(lista.size());

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
        lineDataSet.setValueTextSize(18);

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
        return total / lista.size();
    }

    private Float tenAverage(){
        float total = 0;
        for (int i = lista.size() - 10; i < lista.size(); i++){
            total += lista.get(i).getNumero();
        }
        return total / 10;
    }

}