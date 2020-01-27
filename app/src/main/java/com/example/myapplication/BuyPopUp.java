package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
    String mMoney;
    String currentDate;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    SessionManager sessionManager;
    private static final String KEY_DATE = "date";
    private static final String KEY_PRICE = "price";
    private static final String KEY_SIZE = "size";
    private static final String KEY_STOCK = "stock";
    private static final String KEY_TYPE = "type";
    private static final String KEY_NOTES = "notes";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
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

    public void goToConfirm(View view){
        nDialog = new ProgressDialog(BuyPopUp.this);
        nDialog.setMessage("Locating stock in database..");
        nDialog.setTitle("Approving transaction..");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
        EditText view_size = findViewById(R.id.inputSize);
        final String s = view_size.getText().toString();
        final int sizeTotal =parseInt(s)*100;
        float totalPrice = parseFloat(close) * sizeTotal;
        double totalBalance = parseFloat(mMoney) - totalPrice;
        balance = df2.format(totalBalance);
        Log.d("check balance", balance);
        total = df2.format(totalPrice);
        size = String.valueOf(sizeTotal);

        if (parseFloat(mMoney) < totalPrice){
            Intent i = new Intent(getApplicationContext(), BuyPopUp.class);
            Toast.makeText(this, "Insufficient Balance", Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
        else{
            Intent i = new Intent(getApplicationContext(), BuyConfirm.class);
//            Toast.makeText(this, "Approving transaction...", Toast.LENGTH_SHORT).show();
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
    }

    public void cancelBtn(View view){
        Intent i = new Intent(getApplicationContext(), Trade.class);
        i.putExtra("Stock",stock_name);
        startActivity(i);
    }
}

