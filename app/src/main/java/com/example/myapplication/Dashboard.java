package com.example.myapplication;

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
import java.util.HashMap;

import static java.lang.Float.parseFloat;

public class Dashboard extends BaseActivity {

    private TextView name;
    private TextView capital;
    private Button btn_logout;
    private String mMoney;
    private String mName;
    SessionManager sessionManager;
    Double last_close, last_2days_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        name = findViewById(R.id.text_name);
        capital = findViewById(R.id.d_trading_limit);
        btn_logout = findViewById(R.id.logoutBtn);

        HashMap<String, String> user = sessionManager.getUserDetail();
        mName = user.get(sessionManager.NAME);
        mMoney = user.get(sessionManager.MONEY);
        Log.d("money user: ", mMoney);

        name.setText(mName);
        capital.setText("Trading Limit: "+mMoney);
        FBMIndexGraph();

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });

    }

    public void FBMIndexGraph(){

        String URL = "https://www.isaham.my/api/chart/data?stock=fbmklci&key=19f04a05i12q";
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

                            last_2days_close = Double.parseDouble(split_close[split_close.length - 2]);
                            last_close = Double.parseDouble(split_close[split_close.length - 1]);
                            TextView view_close= findViewById(R.id.closeVal);

                            if (last_close < last_2days_close)
                                view_close.setTextColor(Color.parseColor("#e31212"));
                            else if (last_close > last_2days_close)
                                view_close.setTextColor(Color.parseColor("#12e34a"));

                            view_close.setText(""+last_close);



                            String high = response.getString("h");
                            high = high.substring(1, high.length() - 1);
                            String[] split_high = high.split(",");

                            String low = response.getString("l");
                            low = low.substring(1, low.length() - 1);
                            String[] split_low = low.split(",");

                            //chart starting
                            CandleStickChart candleStickChart = findViewById(R.id.fbm_chart);
                            candleStickChart.setHighlightPerDragEnabled(true);

                            candleStickChart.setDrawBorders(false);

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
                            Log.d("array length: ", String.valueOf(split_open.length));
                            int j=50;
                            for(int i=1;i<=50;i++){

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
                            set1.setShadowColor(0);
                            set1.setDecreasingColor(Color.rgb(211,33,45));
                            set1.setDecreasingPaintStyle(Paint.Style.FILL);
                            set1.setIncreasingColor(Color.rgb(144,238,144));
                            set1.setIncreasingPaintStyle(Paint.Style.FILL);
                            set1.setNeutralColor(Color.BLACK);
                            set1.setDrawValues(false);

                            // create a data object with the datasets
                            CandleData data = new CandleData(set1);

                            // set data
                            candleStickChart.setData(data);
                            candleStickChart.invalidate();









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

    public void goLog(View view){
        Intent i = new Intent(this,LogActivity.class);
        startActivity(i);
    }

    public void goPortfolio(View view){
        Intent i = new Intent(this,PortfolioActivity.class);
        startActivity(i);
    }
    public void goStock(View view){
        Intent i = new Intent(this,StocklistActivity.class);
        startActivity(i);
    }
    public void goScreener(View view){
        Intent i = new Intent(this,ScreenerlistActivity.class);
        startActivity(i);
    }
}
