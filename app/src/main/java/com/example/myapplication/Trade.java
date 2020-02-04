package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class Trade extends BaseActivity {
    String last_high;
    String last_low;
    String last_open;
    String last_close;
    String stock_name;
    String mName;
    String checkTrend="not null";
    double predictedVal;
    double predictedAcc;

    HashMap<String, String> user;
    SessionManager sessionManager;

    private static final String KEY_DATE = "date";
    private static final String KEY_PRICE = "price";
    private static final String KEY_SIZE = "size";
    private static final String KEY_STOCK = "stock";
    private static final String KEY_TYPE = "type";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        final TextView view_close = findViewById(R.id.closeValue);
        final TextView view_open = findViewById(R.id.openValue);
        final TextView view_low = findViewById(R.id.lowValue);
        final TextView view_high = findViewById(R.id.highValue);
        final TextView view_predict = findViewById(R.id.predictText);
        final TextView view_predict_value = findViewById(R.id.closePre);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        String mMoney = user.get(sessionManager.MONEY);



        final String stock_name = getIntent().getStringExtra("Stock");

        TextView view_stock_name = findViewById(R.id.stockName);
        view_stock_name.setText(stock_name);
        String URL = "https://www.isaham.my/api/chart/data?stock=" + stock_name + "&key=19f04a05i12q";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String close = response.getString("c");
                            close = close.substring(1, close.length() - 1);
                            String[] split_close = close.split(",");

                            String open = response.getString("o");
                            open = open.substring(1, open.length() - 1);
                            String[] split_open = open.split(",");
                            Log.d("abc", split_open.toString());

                            last_close = split_close[split_close.length - 1];
                            float floatClose = parseFloat(last_close);
                            last_close = String.format("%.3f",floatClose);
                            view_close.setText(last_close);

                            last_open = split_open[split_open.length - 1];
                            float floatOpen = parseFloat(last_open);
                            last_open = String.format("%.3f",floatOpen);
                            view_open.setText(last_open);

                            String high = response.getString("h");
                            high = high.substring(1, high.length() - 1);
                            String[] split_high = high.split(",");

                            String low = response.getString("l");
                            low = low.substring(1, low.length() - 1);
                            String[] split_low = low.split(",");

                            last_high = split_high[split_high.length - 1];
                            float floatHigh = parseFloat(last_high);
                            last_high = String.format("%.3f",floatHigh);
                            view_high.setText(last_high);

                            last_low = split_low[split_low.length - 1];
                            float floatLow= parseFloat(last_low);
                            last_low = String.format("%.3f",floatLow);
                            view_low.setText(last_low);

                            //chart starting
                            CandleStickChart candleStickChart = findViewById(R.id.candle_stick_chart);
                            candleStickChart.setHighlightPerDragEnabled(true);

                            candleStickChart.setDrawBorders(true);

                            candleStickChart.setBorderColor(Color.rgb(220,220,220));

                            YAxis yAxis = candleStickChart.getAxisLeft();
                            YAxis rightAxis = candleStickChart.getAxisRight();
                            yAxis.setDrawGridLines(true);
                            rightAxis.setDrawGridLines(false);
                            candleStickChart.requestDisallowInterceptTouchEvent(true);

                            XAxis xAxis = candleStickChart.getXAxis();

                            xAxis.setDrawGridLines(true);// disable x axis grid lines
                            xAxis.setDrawLabels(false);
                            rightAxis.setTextColor(Color.BLACK);
                            yAxis.setDrawLabels(false);
                            xAxis.setGranularity(1f);
                            xAxis.setGranularityEnabled(true);
                            xAxis.setAvoidFirstLastClipping(true);

                            Legend l = candleStickChart.getLegend();
                            l.setEnabled(true);

                            ArrayList<CandleEntry> yValsCandleStick = new ArrayList<CandleEntry>();

                            int j=20;
                            for(int i=1;i<=20;i++){

                                float openF= parseFloat(split_open[split_open.length - j]);
                                float highF= parseFloat(split_high[split_high.length - j]);
                                float lowF= parseFloat(split_low[split_low.length - j]);
                                float closeF= parseFloat(split_close[split_close.length - j]);
                                yValsCandleStick.add(new CandleEntry(i, highF, lowF, openF, closeF));
                                j--;
                            }

                            CandleDataSet set1 = new CandleDataSet(yValsCandleStick, "Trends");
                            set1.setColor(Color.rgb(80, 80, 80));
                            set1.setShadowColor(Color.rgb(220,220,220));
                            set1.setShadowWidth(0.8f);
                            set1.setDecreasingColor(Color.rgb(211,33,45));
                            set1.setDecreasingPaintStyle(Paint.Style.FILL);
                            set1.setIncreasingColor(Color.rgb(144,238,144));
                            set1.setIncreasingPaintStyle(Paint.Style.FILL);
                            set1.setNeutralColor(Color.LTGRAY);
                            set1.setDrawValues(false);

                            // create a data object with the datasets
                            CandleData data = new CandleData(set1);

                            // set data
                            candleStickChart.setData(data);
                            candleStickChart.invalidate();

                            db.collection("predicted-stock").document("close-value").get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                if (documentSnapshot != null) {
                                                    predictedVal = documentSnapshot.getDouble(stock_name);
                                                    String p = String.valueOf(predictedVal);
                                                    double beforeClose = Double.parseDouble(last_close);
                                                    if (predictedVal > beforeClose) {
                                                        checkTrend = "increase";
                                                        Log.d("Predicted value: ", p);
                                                        Log.d("Value before ", last_close);
                                                        Log.d("Trend: ", checkTrend);
                                                    }
                                                    else{
                                                        checkTrend = "decrease";
                                                        Log.d("Predicted value: ", p);
                                                        Log.d("Value before ", last_close);
                                                        Log.d("Trend: ", checkTrend);
                                                    }
                                                }
                                            }
                                        }
                                    });

                            db.collection("predicted-stock").document("accuracy-value").get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                if (documentSnapshot != null) {
                                                    predictedAcc = documentSnapshot.getDouble(stock_name);
                                                    predictedAcc = predictedAcc*100;
                                                    String accP = String.format("%.0f",predictedAcc);
                                                    String p = String.format("%.3f",predictedVal);
                                                    view_predict.setText(accP+"% chance stock price will "+checkTrend+" tommorow");
                                                    view_predict_value.setText(p);
                                                    if (checkTrend.equals("increase")) {
                                                        view_predict_value.setTextColor(Color.parseColor("#9cd85b") );
                                                    }
                                                    else if (checkTrend.equals("decrease")){
                                                        view_predict_value.setTextColor(Color.parseColor("#ff4a36") );
                                                    }
                                                }
                                            }
                                        }
                                    });

                        } catch (JSONException e) {
                            Log.d("Error", "hehe");
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }

        );

        requestQueue.add(objectRequest);


    }

    public void goPopBuy (View view){
        Intent i=new Intent(this, BuyPopUp.class);
        TextView view_stock_name = findViewById(R.id.stockName);
        stock_name = getIntent().getStringExtra("Stock");
        i.putExtra("stock",stock_name);
        i.putExtra("open",last_open);
        i.putExtra("close",last_close);
        i.putExtra("high",last_high);
        i.putExtra("low",last_low);
        startActivity(i);
    }

    public void goPopSell (View view){
        user = sessionManager.getUserDetail();
        mName = user.get(sessionManager.NAME);
        stock_name = getIntent().getStringExtra("Stock");
        Log.d("name", stock_name);
        db.collection("user_accounts").document(mName).collection("portfolio").document(stock_name).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot != null) {
                                TextView view_stock_name = findViewById(R.id.stockName);
                                String stock_name = getIntent().getStringExtra("Stock");
                                Intent i = new Intent(getApplicationContext(),SellPopUp.class);
                                i.putExtra("stock",stock_name);
                                i.putExtra("open",last_open);
                                i.putExtra("close",last_close);
                                i.putExtra("high",last_high);
                                i.putExtra("low",last_low);
                                Log.d("Open", last_open);
                                startActivity(i);
                            }
                            else {
                                Log.d("error", "else in not null snapshot ");
                            }

                        } else {
                            Toast.makeText(Trade.this, "Can't retrieve stock", Toast.LENGTH_SHORT).show();                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Trade.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

