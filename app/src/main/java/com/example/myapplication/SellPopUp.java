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

import org.json.JSONException;
import org.json.JSONObject;

import static java.lang.Float.parseFloat;
import static java.lang.Float.valueOf;
import static java.lang.Integer.parseInt;

public class SellPopUp extends AppCompatActivity {

    String stock_name;
    String open;
    String low;
    String high;
    String close;
    String total;
    String size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_pop_up);

        final TextView view_close = findViewById(R.id.closeValue);
        final TextView view_open = findViewById(R.id.openValue);
        final TextView view_low = findViewById(R.id.lowValue);
        final TextView view_high = findViewById(R.id.highValue);
        final TextView view_buy = findViewById(R.id.buyValue);
        final EditText view_size = findViewById(R.id.inputSize);

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
        EditText view_size = findViewById(R.id.inputSize);
        String s = view_size.getText().toString();
        int sizeTotal =parseInt(s)*100;
        float totalPrice = parseFloat(close) * sizeTotal;
        total = String.valueOf(totalPrice);
        size = String.valueOf(sizeTotal);
        Intent i = new Intent(getApplicationContext(), BuyConfirm.class);
        i.putExtra("stock",stock_name);
        i.putExtra("size",size);
        i.putExtra("total",total);
        startActivity(i);
    }

    public void cancelBtn(View view){
        Intent i = new Intent(getApplicationContext(), Trade.class);
        i.putExtra("Stock",stock_name);
        startActivity(i);
    }
}

