package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.drawerlayout.widget.DrawerLayout;


public class PortfolioDetailActivity extends BaseActivity {

    public DrawerLayout drawer;
    TextView stockName, closePrice, lotSize, avgPrice, costValue, marketValue, profitRm, profitPercent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio_detail);

        final String stock_name = getIntent().getStringExtra("StockName");
        String close_price = getIntent().getStringExtra("ClosePrice");
        String lot_size = getIntent().getStringExtra("LotSize");
        String buy_price = getIntent().getStringExtra("BuyPrice");


        stockName = findViewById(R.id.pd_stock_name);
        stockName.setText(stock_name);

        closePrice = findViewById(R.id.pd_close_price);
        closePrice.setText(close_price);

        lotSize = findViewById(R.id.pd_lot_size);
        lotSize.setText(lot_size);

        avgPrice = findViewById(R.id.pd_avg_price);
        avgPrice.setText(buy_price);

        double d_buyPrice=Double.parseDouble(buy_price);
        double d_lotSize=Double.parseDouble(lot_size);
        double d_closePrice=Double.parseDouble(close_price);

        double cost_value = d_buyPrice * d_lotSize;
        double market_value = d_closePrice * d_lotSize;
        double profit_rm = market_value-cost_value;
        double profit_percent = profit_rm/cost_value;

        costValue = findViewById(R.id.pd_cost);
        costValue.setText(""+cost_value);

        marketValue = findViewById(R.id.pd_market);
        marketValue.setText(""+market_value);

        profitRm = findViewById(R.id.pd_profit_rm);
        profitRm.setText(""+profit_rm);

        profitPercent = findViewById(R.id.pd_profit_percent);
        profitPercent.setText(""+profit_percent+"%");

        Button BuySell = findViewById(R.id.buySellBtn);
// Register the onClick listener with the implementation above
        BuySell.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                Intent intent = new Intent (PortfolioDetailActivity.this, Trade.class);
                intent.putExtra("Stock", stock_name);
                startActivity(intent);
            }
        });

    }


    }













