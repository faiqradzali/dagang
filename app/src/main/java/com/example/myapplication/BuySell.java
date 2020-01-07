package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class BuySell extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buysell);
        final TextView view_close = findViewById(R.id.text_close);
        final TextView view_open = findViewById(R.id.text_open);

        String stock_name = getIntent().getStringExtra("Stock");

        TextView view_stock_name = findViewById(R.id.text_stock_name);
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
                            close = close.substring(1, close.length()-1);
                            String[] split_close = close.split(",");

                            String open = response.getString("o");
                            open = open.substring(1, open.length()-1);
                            String[] split_open = open.split(",");

                            String last_close = split_close[split_close.length-1];
                            view_close.setText(last_close);

                            String last_open = split_open[split_open.length-1];
                            view_open.setText(last_open);





                            Log.d("Rest Responsse", last_close);



                        } catch (JSONException e) {
                            Log.d("Error", "hehe");
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse (VolleyError error) {
                        Log.e("Rest Response", error.toString());
                    }
                }

        );

        requestQueue.add(objectRequest);





    }
}
