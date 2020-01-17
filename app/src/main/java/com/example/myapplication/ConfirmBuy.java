package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Float.valueOf;
import static java.lang.Integer.parseInt;

public class ConfirmBuy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_buy);


        final TextView view_total= findViewById(R.id.totalVal);
        final TextView view_total_size= findViewById(R.id.stockVal);
        final TextView view_fee= findViewById(R.id.brokerFee);
        final TextView view_bal = findViewById(R.id.cashBal);

        Intent intent = getIntent();
        String stock_name = intent.getStringExtra("stock");
        final String stock_size = intent.getStringExtra("size");
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

                            String last_open = split_open[split_open.length - 1];

                            String high = response.getString("h");
                            high = high.substring(1, high.length() - 1);
                            String[] split_high = high.split(",");

                            String low = response.getString("l");
                            low = low.substring(1, low.length() - 1);
                            String[] split_low = low.split(",");

                            String last_high = split_high[split_high.length - 1];
                            String last_low = split_low[split_low.length - 1];

                            // set the text of buy value
                            String prev_close = split_close[split_close.length - 19];

                            final float closeSub = (parseFloat(last_close)+((parseFloat(last_close)-parseFloat(prev_close))));
                            String buyView = String.valueOf(closeSub);

                            // set total
                            int a = parseInt(stock_size);
                            int sizeTotal =a*100;
                            float TotalVal = closeSub*sizeTotal;
                            String totalView = String.valueOf(TotalVal);
                            String totalSize = ("x")+(String.valueOf(sizeTotal));
                            view_total.setText(totalView);
                            view_total_size.setText(totalSize);


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

    public void okButton(View view){
        Intent i = new Intent(getApplicationContext(),Stocklists.class);
        startActivity(i);
    }


}

