package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static java.lang.Float.valueOf;
import static java.lang.Integer.parseInt;

public class SellConfirm extends AppCompatActivity {
    String stock_name;
    String totalPrice;
    String stock_size;
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
    }

    public void okButton(View view){
        Intent i = new Intent(getApplicationContext(), StocklistActivity.class);
        startActivity(i);
    }


}

