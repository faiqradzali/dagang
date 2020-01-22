package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static java.lang.Float.parseFloat;
import static java.lang.Float.valueOf;
import static java.lang.Integer.parseInt;

public class SellConfirm extends AppCompatActivity {
    String stock_name;
    String totalPrice;
    String stock_size;
    String balance;
    String mName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_confirm);

        final TextView view_total= findViewById(R.id.totalVal);
        final TextView view_total_size= findViewById(R.id.stockVal);
        final TextView view_bal = findViewById(R.id.cashBal);
        TextView view_stock_name = findViewById(R.id.stockName);

        Intent intent = getIntent();
        stock_name = intent.getStringExtra("stock");
        stock_size = "X"+(intent.getStringExtra("size"));
        totalPrice = intent.getStringExtra("total");

        view_stock_name.setText(stock_name);
        view_total.setText(totalPrice);
        view_total_size.setText(stock_size);

        HashMap<String, String> user = sessionManager.getUserDetail();
        mName = user.get(sessionManager.NAME);

        db.collection("user_accounts").document(mName).update("capital",balance);
    }

    public void okButton(View view){
        Intent i = new Intent(getApplicationContext(),Stocklists.class);
        startActivity(i);
    }


}

