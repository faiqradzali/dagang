package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.DateFormat;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

public class SellPopUp extends BaseActivity {

    String stock_name;
    String open;
    String low;
    String high;
    String close;
    String total;
    String size;
    String balance;
    String mMoney;
    String currentDate;
    String mName;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    SessionManager sessionManager;
    private static final String KEY_DATE = "date";
    private static final String KEY_PRICE = "price";
    private static final String KEY_SIZE = "size";
    private static final String KEY_STOCK = "stock";
    private static final String KEY_TYPE = "type";
    private static final String KEY_NOTES= "notes";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_pop_up);

        final TextView view_close = findViewById(R.id.closeValue);
        final TextView view_open = findViewById(R.id.openValue);
        final TextView view_low = findViewById(R.id.lowValue);
        final TextView view_high = findViewById(R.id.highValue);
        final TextView view_buy = findViewById(R.id.buyValue);

        currentDate = java.text.DateFormat.getDateTimeInstance().format(new Date());


        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        mMoney = user.get(sessionManager.MONEY);
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
        view_buy.setText(close);
    }
//error on session manager buy sell price logic
    public void goToConfirm(View view){
        sessionManager = new SessionManager(this);
        final HashMap<String, String> user = sessionManager.getUserDetail();
        mMoney = user.get(sessionManager.MONEY);
        mName = user.get(sessionManager.NAME);
        Log.d("Size in doc: ", stock_name);
        db.collection("user_accounts").document(mName).collection("portfolio").document(stock_name).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot != null) {
                                Intent i = new Intent(getApplicationContext(), SellConfirm.class);
                                EditText view_size = findViewById(R.id.inputSize);
                                final String s = view_size.getText().toString();
                                int sizeTotal = parseInt(s) * 100;
                                size = String.valueOf(sizeTotal);
                                String sizeInDoc = documentSnapshot.getString(KEY_SIZE);
                                int x = parseInt(sizeInDoc);

                                if (sizeTotal <= x) {
                                    String newSize = String.valueOf(x - sizeTotal);
                                    Log.d("Size in doc: ", newSize);
                                    float totalPrice = parseFloat(close) * sizeTotal;
                                    Log.d("Price of stockXsize: ", String.valueOf(totalPrice));
                                    Log.d("User Money: ", String.valueOf(mMoney));
                                    float totalBalance = parseFloat(mMoney) + totalPrice;
                                    Log.d("Balance after sell: ", String.valueOf(totalBalance));
                                    balance = df2.format(totalBalance);
                                    total = df2.format(totalPrice);

                                    db.collection("user_accounts").document(mName).collection("portfolio").document(stock_name).update("size",newSize);
                                    db.collection("user_accounts").document(mName).update("capital",balance);
                                    if (newSize.equals("0")){
                                        db.collection("user_accounts").document(mName).collection("portfolio").document(stock_name).delete();
                                    }

                                    i.putExtra("stock", stock_name);
                                    i.putExtra("size", size);
                                    i.putExtra("total", total);
                                    Log.d("money: ", balance);
                                    i.putExtra("capital", balance);

                                    Map<String, Object> log = new HashMap<>();
                                    log.put(KEY_DATE, currentDate);
                                    log.put(KEY_PRICE, close);
                                    log.put(KEY_SIZE, size);
                                    log.put(KEY_STOCK, stock_name);
                                    log.put(KEY_TYPE, "SELL");
                                    log.put(KEY_NOTES, "After editting, click the tick button on the right corner.");
                                    db.collection("user_accounts").document(mName).collection("log").document().set(log);
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(SellPopUp.this, "Size exceed limit.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("error", "else in not null snapshot ");
                            }

                        }
                    }
                });
    }

    public void cancelBtn(View view){
        Intent i = new Intent(getApplicationContext(), Trade.class);
        i.putExtra("Stock",stock_name);
        startActivity(i);
    }
}

