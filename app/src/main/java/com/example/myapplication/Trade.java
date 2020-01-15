package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Float.parseFloat;

public class Trade extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        Button b = findViewById(R.id.buyBtn);


        final TextView view_close = findViewById(R.id.closeValue);
        final TextView view_open = findViewById(R.id.openValue);
        final TextView view_low = findViewById(R.id.lowValue);
        final TextView view_high = findViewById(R.id.highValue);


        String stock_name = getIntent().getStringExtra("Stock");

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

                            String last_close = split_close[split_close.length - 20];
                            view_close.setText(last_close);

                            String last_open = split_open[split_open.length - 1];
                            view_open.setText(last_open);

                            String high = response.getString("h");
                            high = high.substring(1, high.length() - 1);
                            String[] split_high = high.split(",");

                            String low = response.getString("l");
                            low = low.substring(1, low.length() - 1);
                            String[] split_low = low.split(",");

                            String last_high = split_high[split_high.length - 1];
                            view_high.setText(last_high);

                            String last_low = split_low[split_low.length - 1];
                            view_low.setText(last_low);

                            //chart starting
                            CandleStickChart candleStickChart = findViewById(R.id.candle_stick_chart);
                            candleStickChart.setHighlightPerDragEnabled(true);

                            candleStickChart.setDrawBorders(true);

                            candleStickChart.setBorderColor(Color.rgb(220,220,220));

                            YAxis yAxis = candleStickChart.getAxisLeft();
                            YAxis rightAxis = candleStickChart.getAxisRight();
                            yAxis.setDrawGridLines(false);
                            rightAxis.setDrawGridLines(false);
                            candleStickChart.requestDisallowInterceptTouchEvent(true);

                            XAxis xAxis = candleStickChart.getXAxis();

                            xAxis.setDrawGridLines(false);// disable x axis grid lines
                            xAxis.setDrawLabels(false);
                            rightAxis.setTextColor(Color.WHITE);
                            yAxis.setDrawLabels(false);
                            xAxis.setGranularity(1f);
                            xAxis.setGranularityEnabled(true);
                            xAxis.setAvoidFirstLastClipping(true);

                            Legend l = candleStickChart.getLegend();
                            l.setEnabled(false);

                            ArrayList<CandleEntry> yValsCandleStick = new ArrayList<CandleEntry>();

                            for(int i=1;i<=20;i++){
                                float openF= parseFloat(split_open[split_open.length - i]);
                                float highF= parseFloat(split_high[split_high.length - i]);
                                float lowF= parseFloat(split_low[split_low.length - i]);
                                float closeF= parseFloat(split_close[split_close.length - i]);
                                yValsCandleStick.add(new CandleEntry(i, highF, lowF, openF, closeF));
                            }

                            CandleDataSet set1 = new CandleDataSet(yValsCandleStick, "DataSet 1");
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






                            Log.d("Rest Responsse", last_close);


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
        Intent i=new Intent(this, PopUpBuy.class);
        String stock_name = getIntent().getStringExtra("Stock");
        i.putExtra("stock",stock_name);
        startActivity(i);


    }


}

