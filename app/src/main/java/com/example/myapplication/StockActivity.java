package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

public class StockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);
        final TextView view_close = findViewById(R.id.text_close);


        String URL = "https://www.isaham.my/api/chart/data?stock=AME&key=19f04a05i12q";
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

                            String last_close = split_close[split_close.length-1];
                            view_close.setText(last_close);
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
