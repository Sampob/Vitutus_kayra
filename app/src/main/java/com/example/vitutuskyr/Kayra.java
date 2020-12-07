package com.example.vitutuskyr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Kayra extends AppCompatActivity {

    private LineChart mpLineChart;      //MP Android Chart moduulista käytetään LineChart luokkaa joka piirtää käyrän
    private ArrayList<Merkinta> lista;  //Merkinnät ladataan ja tallennetaan lista muuttujaan
    private int index;                  //Tallentaa listan tietyn kohdan paikan
    private boolean delete = false;     //Varmistetaan ettei käyttäjä pysty poistamaan ilman valittua kohdetta

    private ImageButton trashB;         //Kohdan poistamisen nappula haetaan ja tallennetaan tähän

    private TextView noteText;          //Merkinnän arvot näytetään näissä kahdessa elementissä
    private TextView dateText;

    private final DecimalFormat df = new DecimalFormat("0.0");  //mpLineChart näyttää numerot oletuksena liukuluvuissa (1.0 tai 5.0). Float pitää muuttaa muotoon Int.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayra);

        lineDefault();                                                                              //Luodaan mpLineChart käyrä
        mpLineChart.moveViewToX(lista.size());                                                      //Siirretään alussa näkymä listan viimeiseen kohteeseen

        noteText = findViewById(R.id.noteText);                                                     //Haetaan teksti elementit
        dateText = findViewById(R.id.dateText);

        noteText.setTextColor(Color.BLACK);                                                         //Elementtien oletusarvot asetetaan ja poistetaan näkyvistä
        dateText.setTextColor(Color.BLACK);
        dateText.setText("");
        noteText.setText("");

        trashB = findViewById(R.id.trashButton);                                                    //Haetaan poisto nappi ja piilotetaan se
        trashB.setVisibility(View.INVISIBLE);

        /* Asetetaan kuuntelija käyrä elementille */
        mpLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            /* Kun valitaan, hakee listasta merkinnän tiedot */
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                index =(int) e.getX();                                                              //Missä kohtaa listaa merkintä on

                delete = true;                                                                      //Aktivoidaan poisto napin käyttö
                trashB.setVisibility(View.VISIBLE);                                                 //Poisto nappi näkyviin

                noteText.setText(lista.get(index).getNote());                                       //Asetetaan teksti elementeille merkinnän arvot
                dateText.setText("Aika: " + getTime());
            }

            /* Kun valinta poistetaan piilotetaan kohdat */
            @Override
            public void onNothingSelected() {
                noteText.setText("");
                dateText.setText("");
                delete = false;
                trashB.setVisibility(View.INVISIBLE);
            }
        });

    }

    /**
     * Hakee viivakaavion elementin ja asettaa sille oletusparametrit jotka eivät muutu. Asettaa miten käyrää
     * voidaan liikuttaa, miten Y akseli näkyy yAxisLabel() kautta ja mitä arvoja siinä on sekä piilottaa turhia tekstejä näkyvistä.
     * Kutsuu lopuksi updateLine() joka rakentaa käyrän.
     * @see #updateLine()
     * @see #yAxisLabel()
     * */

    public void lineDefault(){
        mpLineChart = findViewById(R.id.lineChart);                                                 //Etsitään MP Android Chart elementti

        mpLineChart.setTouchEnabled(true);                                                          //Käyrää voi liikuttaa vaaka sekä pystysuorassa
        mpLineChart.setScaleEnabled(false);                                                         //Käyrää ei voi skaalata Y akselin suunnassa. Tuo ongelmia Y akselin arvojen näyttämisessä koska vain kokonaislukuja.
        mpLineChart.setPinchZoom(false);                                                            //Käyrää ei voi zoomata, samat ongelmat kuin skaalauksessa.

        YAxis leftAxis = mpLineChart.getAxisLeft();                                                 //Tallennetaan vasen Y akseli
        mpLineChart.getAxisRight().setEnabled(false);                                               //Poistetaan oikea Y akseli näkyvistä

        leftAxis.setValueFormatter(new IndexAxisValueFormatter(yAxisLabel()));                      //Y akselin arvot

        leftAxis.setTextSize(16);                                                                   //Y akselin tekstin koko 16
        mpLineChart.getXAxis().setTextSize(12);                                                     //X akselin tekstin koko 12

        leftAxis.setAxisMinimum(0);                                                                 //Mistä Y akseli alkaa
        leftAxis.setAxisMaximum(11);                                                                //Mihin Y akseli loppuu
        leftAxis.setLabelCount(10);                                                                 //Asettaa kuinka monta arvoa näytetään Y akselilla

        Description description = new Description();
        description.setText("");
        mpLineChart.setDescription(description);
        mpLineChart.getLegend().setEnabled(false);

        updateLine();
    }

    /**
     * Rakentaa käyrän sekä asettaa X akselille arvot.
     * <p>
     * readFile() lukee tallennetut merkinnät ja tallentaa sen lista muuttujaan. Käyrä rakennetaan dataValues() funktion avulla ja
     * lisätään ArrayListaan, missä voi olla useita data settejä. Data muotoillaan haluttavalla tavalla ja
     * asetetaan elementtiin.
     * <p>
     * lineDefault() kutsuu.
     * @see #lineDefault()
     * @see #readFile()
     * @see #dataValues()
     * */

    private void updateLine(){
        readFile();                                                                                 //Lukee tallennetut merkinnät ja laittaa ne lista ArrayListiin
        XAxis xAxis = mpLineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);                                              //Siirretään X akseli alas, oletus arvo on TOP

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabel()));
        xAxis.setAxisMinimum(-0.25f);                                                               //Mistä X akseli alkaa
        xAxis.setAxisMaximum(xAxisLabel().size()-0.5f);                                             //Mihin X akseli loppuu. Aina 0.5 enemmän kuin merkintöjen määrä

        /* X akselin arvojen määrä on muuttuva, joten jotta saadaan yhdenmukainen näkymä
         * täytyy X akselilla näkyvien arvojen määrän muuttua kunnes pystytään näyttää 8 arvoa. */
        if(lista.size() < 8){
            xAxis.setLabelCount(lista.size());                                                      //X akselilla näkyvien kohtien määrä muuttuu listan koon mukaan
            mpLineChart.setVisibleXRange(0, lista.size());                                          //X akseli zoomaa niin että juuri nämä näkyvät
        } else {
            xAxis.setLabelCount(8);                                                                 //Kun ollaan yli 8 kohdan, arvot pysyvät
            mpLineChart.setVisibleXRange(0, 8);
        }
        mpLineChart.setDragOffsetX(150);                                                            //Kuinka paljon X akselia pystyy vetämään yli minimi ja maksimi arvojen

        /* Käyrät rakennetaan data seteistä, jotka lisätään ArrayList<ILineDataSet>.
         * Jos halutaan näyttää enemmän käyriä,
         * lisättäisiin niiden data dataSets ArrayListaan.
         * Käyrän data rakennetaan dataValues funktion avulla ja se lisätään listaan dataSets. */
        LineDataSet lineDataSet = new LineDataSet(dataValues(), "Käyrä");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);                                                     //Pakkaa kaikki data setit

        /* MP Android Chart käyttää liukulukuja oletuksena, Value Formatterin avulla
         * muutetaan käyrän data pisteistä kokonaislukuja */
        data.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "" + ((int) value);                                                          //Ottaa arvon ja palauttaa kokonaisluvun. Voidaan muuttaa kohdat mihin muotoon return avulla (esim. $3 tai 15€)
            }
        });

        mpLineChart.setData(data);                                                                  //Asettaa datan käyräksi

        /* Asetetaan käyrän ominaisuudet. Itsestäänselviä */
        lineDataSet.setLineWidth(6);
        lineDataSet.setColor(Color.parseColor("#9c27B0"));
        lineDataSet.setCircleColor(Color.parseColor("#9C27B0"));
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleRadius(8);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setValueTextSize(18);

        setAverageText();

        mpLineChart.invalidate();                                                                   //Piirtämisen jälkeen poistetaan käytöstä, jotta saadaan uusi data nopeasti näkyviin jos tarve
    }

    /**
     * Aktiviteetin vasemmassa alareunassa näkyy käyrän merkintöjen keskiarvo ja 10 viimeisen keskiarvo.
     * Keskiarvo numeron väri vaihtuu riippuen ollaanko huonoon vai hyvään suuntaan.
     * Alle kolmen väri vaihtuu oranssiksi ja yli seitsemän vihreäksi, muuten väri on musta.
     * 10 viimeisen keskiarvo väri muuttuu jos sen arvo on 0.5 enemmän tai vähemmän koko keskiarvon.
     * <p>
     * updateLine() kutsuu.
     * @see #updateLine()
     * */

    private void setAverageText(){
        TextView averageText = findViewById(R.id.averageText);                                      //Etsii teksti elementin joka näyttää keskiarvon
        df.setRoundingMode(RoundingMode.CEILING);                                                   //Liukuluvun muuntaja kokonaislukuun asettaminen pyöristämään ylös

        /* Logiikka milloin keskiarvon väri muuttuu. Alle kolmen muuttuu oranssiksi,
         * yli seitsemän vihreäksi, muuten musta */
        if(average() <= 3) {
            averageText.setText(df.format(average()));
            averageText.setTextColor(Color.rgb(252,145,58));
        } else if(average() >= 7) {
            averageText.setText(df.format(average()));
            averageText.setTextColor(Color.rgb(54,128,45));
        } else if(lista.isEmpty()) {
            averageText.setText("-");                                                               //Jos lista on tyhjä, numero on NaN joten muutetaan se "-"
        } else {
            averageText.setText(df.format(average()));
            averageText.setTextColor(Color.BLACK);
        }

        /* Keskiarvon yläpuolella lukee kuinka monta merkintää on. Logiikka muutamaan sitä */
        TextView maaraText = findViewById(R.id.maaraText);
        if(lista.size() == 1){
            maaraText.setText(lista.size() + " Merkintä");                                          //Yksittäinen merkintä
        } else {
            maaraText.setText(lista.size() + " Merkintää");                                         //Muuten merkintää, myös jos ei yhtään merkintää
        }

        /* Keskiarvon alapuolella on viimeisen 10 merkinnän keskiarvo. Jaettu kolmeen teksti elementtiin.
         * Itse keskiarvo tenAverageText, nuoli joka näyttää trendin koko keskiarvoon arrowText,
         * ja tenText joka kertoo mitä numero näyttää. */
        TextView tenAverageText = findViewById(R.id.tenAverageText);
        TextView arrowText = findViewById(R.id.arrowText);
        TextView tenText = findViewById(R.id.tenText);
        tenText.setText(R.string.tenText);
        if(lista.size() >= 11) {                                                                    //Kun listassa 11 merkintää, alkaa näyttämään viimeisen 10 ka.

            /* Jos viimeisen 10 ka. on 0.5 enemmän tai vähemmän kuin koko keskiarvo,
             * näytetään trendi suuntaan ylös tai alas */
            if (tenAverage() < average() - 0.5f) {
                tenAverageText.setText(df.format(tenAverage()));
                arrowText.setText( "↓");                                                            //Trendi alas ja värit muutetaan oranssiin
                tenAverageText.setTextColor(Color.rgb(252,145,58));
                arrowText.setTextColor(Color.rgb(252,145,58));
            } else if(tenAverage() > average() + 0.5f) {
                tenAverageText.setText(df.format(tenAverage()));
                arrowText.setText("↑");                                                             //Trendi ylös ja värit muutetaan vihreään
                tenAverageText.setTextColor(Color.rgb(54,128,45));
                arrowText.setTextColor(Color.rgb(54,128,45));
            }

            //Jos tenAverage ja average ovat 0.5 toisistaan trendi on neutraali ja väri musta.
            else {
                tenAverageText.setTextColor(Color.BLACK);
                tenAverageText.setText(df.format(tenAverage()));
                arrowText.setText("");
            }
        }

        //Alle 11 merkinnässä "piilotetaan" kohdat näyttämällä niissä ei mitään
        else {
            tenAverageText.setText("");
            arrowText.setText("");
            tenText.setText("");
        }
    }

    /**
     * Sama kuin MainActivityssä. Hakee SharedPreferenssin, joka on tallennettu muotoon Gson.
     * Muuttaa datan Gsonista ArrayList<Merkinta> muotoon ja tallentaa sen muuttujaan lista. Jos dataa ei ole,
     * rakentaa uuden ArrayListan.
     * <p>
     * updateLine() kutsuu.
     * @see #updateLine()
     */
    private void readFile() {
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("lista", null);
        Type type = new TypeToken<ArrayList<Merkinta>>() {}.getType();
        lista = gson.fromJson(json, type);

        if(lista == null) {
            lista = new ArrayList<>();
        }
    }

    /**Käyrä rakennetaan Entryjen avulla. Entry tallentaa X ja Y paikan, joka voidaan näyttää koordinaatistossa.
     * Entryt tallennetaan listaan.
     * <p>
     * updateLine() kutsuu.
     * @return  Merkintöjen numeroarvot (Y arvot) järjestyksessä joista rakennetaan käyrä.
     * @see #updateLine()
     * */
    private ArrayList<Entry> dataValues(){
        ArrayList<Entry> dataVals = new ArrayList<>();

        /* For loopilla saadaan arvot järjestykseen X akselilla
         * ja haetaan listasta merkintöjen arvo Y akselille */
        for(int i = 0; i < lista.size(); i++){
            dataVals.add(new Entry(i, lista.get(i).getNumero()));
        }
        return dataVals;
    }

    /**X akselin arvot haetaan listan merkinnöistä, joihin on tallennettu päivä
     * jolloin merkintä on luotu. Nämä kootaan ArrayListaksi joka palautetaan.
     * <p>
     * updateLine() kutsuu.
     * @return String ArrayLista merkintöjen luomispäivistä muodossa "dd.mm".
     * @see #updateLine()
     * */
    private ArrayList<String> xAxisLabel() {
        ArrayList<String> xAxisList = new ArrayList<>();
        for(int i=0; i < lista.size(); i++){
            xAxisList.add(lista.get(i).getCurrentTime());
        }
        return xAxisList;
    }

    /**Y akselin arvot ovat 1 - 10 joka rakennetaan for loopilla.
     * Nämä kootaan ArrayListaksi joka palautetaan.
     * <p>
     * lineDefault() kutsuu.
     * @return String ArrayLista arvona 1 - 10
     * @see #lineDefault()
     */
    private ArrayList<String> yAxisLabel(){
        ArrayList<String> label = new ArrayList<>();
        label.add("");
        for(int i = 1; i < 11; i++){
            label.add(Integer.toString(i));
        }
        return label;
    }

    /**Laskee listan merkintöjen keskiarvo. Lisätään kaikki arvot
     * ja jaetaan se merkintöjen määrällä.
     * <p>
     * setAverageText() kutsuu.
     * @return Palauttaa listan merkintöjen keskiarvon.
     * @see #setAverageText()
     * */
    private Float average(){
        float total = 0;
        for (int i = 0; i < lista.size(); i++){
            total += lista.get(i).getNumero();
        }
        return total / lista.size();
    }

    /**Laskee listan 10 viimeisen merkinnän keskiarvon. Lisätään viimeiset 10 arvoa yhteen
     * ja palautetaan tämä numero jaettuna 10
     * <p>
     * setAverageText() kutsuu.
     * @return Palauttaa listan 10 viimeisen merkinnän keskiarvon.
     * @see #setAverageText()
     */
    private Float tenAverage(){
        float total = 0;
        for (int i = lista.size() - 10; i < lista.size(); i++){
            total += lista.get(i).getNumero();
        }
        return total / 10;
    }

    /** Liitetään poisto nappulaan.
     * Poisto nappula poistaa kohdan vain jos poistettavaa on.
     * Index arvo on tallentanut merkinnän kohdan listassa
     * ja se poistetaan. Tämän jälkeen tallennetaan SharedPreference,
     * poistetaan merkinnän arvot näkyvistä ja rakennetaan käyrä uudestaan.
     * Jos poistettavaa ei ole, näytetään Toast "Ei poistettavaa".
     * */
    public void delB(View v){
        if(lista.size() > 0 && delete) {
            lista.remove(index);

            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(lista);
            editor.putString("lista", json);
            editor.apply();

            noteText.setText("");
            dateText.setText("");

            delete = false;
            trashB.setVisibility(View.INVISIBLE);

            updateLine();

        } else {
            Toast.makeText(Kayra.this, "Ei poistettavaa", Toast.LENGTH_SHORT).show();
        }
    }

    /**Merkintöihin on tallennettu Calendar joka sisältää ajankohdan jolloin merkintä on luotu.
     * Tämä voidaan muuttaa haluttuun muotoon SimpleDateFormat avulla.
     * Tästä saatu String palautetaan ja näytetään. */
    public String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM HH:mm", Locale.getDefault());
        return sdf.format(lista.get(index).getCalendar().getTime());
    }

}