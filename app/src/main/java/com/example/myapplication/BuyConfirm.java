package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Float.parseFloat;
import static java.lang.Float.valueOf;
import static java.lang.Integer.parseInt;

public class BuyConfirm extends BaseActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String stock_name;
    String totalPrice;
    String stock_size;
    String balance;
    String mName;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_confirm);

        sessionManager = new SessionManager(this);

        final TextView view_total = findViewById(R.id.totalVal);
        final TextView view_total_size = findViewById(R.id.stockVal);
        final TextView view_bal = findViewById(R.id.cashBal);
        TextView view_stock_name = findViewById(R.id.stockName);

        Intent intent = getIntent();
        stock_name = intent.getStringExtra("stock");
        stock_size = "X" + (intent.getStringExtra("size"));
        totalPrice = intent.getStringExtra("total");
        balance = intent.getStringExtra("balance");

        view_stock_name.setText(stock_name);
        view_bal.setText(balance);
        view_total.setText(totalPrice);
        view_total_size.setText(stock_size);

        HashMap<String, String> user = sessionManager.getUserDetail();
        mName = user.get(sessionManager.NAME);

        db.collection("user_accounts").document(mName).update("capital", balance);
    }

    public void okButton(View view) {
        Intent i = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(i);
    }

//    //log read part
//    public void loadLogs(View v) {
//        db.collection("user_accounts").document(mName).collection("log").get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        String data = "";
//                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                            Note note = documentSnapshot.toObject(Note.class);
//
//                            String date = note.getCurrentDate();
//                            String size = note.getSize();
//                            String close = note.getClose();
//                            String type = note.getType();
//                            String stockname = note.getStock_name();
//
//                            data += "date: " + date + "\nstock: " + stockname + "\nsize: " + size + "close: " + close + "\ntype: " + type;
//
//                        }
//                        //recylcer view here
//                        //textViewData.setText(data);
//                    }
//                });
//    }
}

