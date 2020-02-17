package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.DateFormat;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BuyPopUp extends BaseActivity {

    String stock_name;
    String open;
    String low;
    String high;
    String close;
    String total;
    String size;
    String balance;
    String mName;
    String currentDate;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    SessionManager sessionManager;
    private static final String KEY_DATE = "date";
    private static final String KEY_PRICE = "price";
    private static final String KEY_SIZE = "size";
    private static final String KEY_STOCK = "stock";
    private static final String KEY_TYPE = "type";
    private static final String KEY_NOTES = "notes";
    private static final String KEY_MONEY = "capital";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    HashMap<String, String> user;
    ProgressDialog nDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_pop_up);

        final TextView view_close = findViewById(R.id.closeValue);
        final TextView view_open = findViewById(R.id.openValue);
        final TextView view_low = findViewById(R.id.lowValue);
        final TextView view_high = findViewById(R.id.highValue);
        final TextView view_buy = findViewById(R.id.buyValue);
        final TextView view_bal = findViewById(R.id.buyValue2);

        currentDate = java.text.DateFormat.getDateTimeInstance().format(new Date());


        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();
        mName = user.get(sessionManager.NAME);
//        Log.d("Money", mMoney);
        final Intent intent = getIntent();
        stock_name = intent.getStringExtra("stock");
        open = intent.getStringExtra("open");
        close = intent.getStringExtra("close");
        high = intent.getStringExtra("high");
        low = intent.getStringExtra("low");
        TextView view_stock_name = findViewById(R.id.stockName);
        view_stock_name.setText(stock_name);
        view_close.setText(close);
        view_open.setText(open);
        view_high.setText(high);
        view_low.setText(low);
        view_buy.setText("RM " +close);

        db.collection("user_accounts").document(mName).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot != null) {
                                String mTradingLimit = documentSnapshot.getString("capital");


                                double doubleTradingLimit = Double.parseDouble(mTradingLimit);
                                view_bal.setText("RM "+String.format("%.2f", doubleTradingLimit));
                            }
                        }
                    }
                });
    }

    public void goToConfirm(View view){
        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetail();
        mName = user.get(sessionManager.NAME);
        Log.d("name of current user: ", mName);
        db.collection("user_accounts").document(mName).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot != null) {
                                String mMoney = documentSnapshot.getString(KEY_MONEY);
                                EditText view_size = findViewById(R.id.inputSize);
                                final String s = view_size.getText().toString();
                                if (s.equals("")){
                                    Toast.makeText(BuyPopUp.this, "Enter size", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    final int sizeTotal =parseInt(s)*100;
                                    float totalPrice = parseFloat(close) * sizeTotal;
                                    Log.d("Price of stockXsize: ", String.valueOf(totalPrice));
                                    Log.d("user money db: ", mMoney);
                                    float totalBalance = parseFloat(mMoney) - totalPrice;
                                    Log.d("Balance after buy: ", String.valueOf(totalBalance));
                                    balance = df2.format(totalBalance);
                                    Log.d("check balance", balance);
                                    total = df2.format(totalPrice);
                                    size = String.valueOf(sizeTotal);

                                    if (parseFloat(mMoney) < totalPrice){
                                        Intent i = new Intent(getApplicationContext(), Trade.class);
                                        i.putExtra("Stock",stock_name);
                                        Toast.makeText(BuyPopUp.this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
                                        startActivity(i);
                                    }
                                    else if (parseFloat(mMoney) >= totalPrice){
                                        Intent i = new Intent(getApplicationContext(), BuyConfirm.class);
                                        i.putExtra("stock",stock_name);
                                        i.putExtra("size",size);
                                        i.putExtra("total",total);
                                        i.putExtra("balance",balance);

                                        final HashMap<String, String> user = sessionManager.getUserDetail();
                                        final String mName = user.get(sessionManager.NAME);
                                        Map<String, Object> log = new HashMap<>();
                                        log.put(KEY_DATE, currentDate);
                                        log.put(KEY_PRICE, close);
                                        log.put(KEY_SIZE, size);
                                        log.put(KEY_STOCK, stock_name);
                                        log.put(KEY_TYPE, "BUY");
                                        log.put(KEY_NOTES, "After editting, click the tick button on the right corner.");

                                        final Map<String, Object> portfolio = new HashMap<>();
                                        portfolio.put(KEY_DATE,currentDate);
                                        portfolio.put(KEY_PRICE, close);
                                        portfolio.put(KEY_SIZE, size);

                                        db.collection("user_accounts").document(mName).collection("log").document().set(log);

                                        db.collection("user_accounts").document(mName).collection("portfolio").document(stock_name).get()
                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        if (documentSnapshot.exists()) {
                                                            if (documentSnapshot != null) {
                                                                size = documentSnapshot.getString(KEY_SIZE);
                                                                String s = String.valueOf(sizeTotal+parseInt(size));
                                                                db.collection("user_accounts").document(mName).collection("portfolio").document(stock_name).update("size",s);

                                                            }
                                                            else {
                                                                Log.d("error", "else in not null snapshot ");
                                                            }

                                                        } else {
                                                            db.collection("user_accounts").document(mName).collection("portfolio").document(stock_name).set(portfolio);
                                                        }
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(BuyPopUp.this, "Error", Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                        startActivity(i);
                                    }
                                    else{
                                        Intent i = new Intent(getApplicationContext(), Trade.class);
                                        Toast.makeText(BuyPopUp.this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
                                        i.putExtra("Stock",stock_name);
                                        startActivity(i);
                                    }

                                }
                            }}}
                    });
    }

    public void cancelBtn(View view){
        Intent i = new Intent(getApplicationContext(), Trade.class);
        i.putExtra("Stock",stock_name);
        startActivity(i);
    }
}

