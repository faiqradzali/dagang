package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.auth.User;

import java.text.DecimalFormat;

import java.util.ArrayList;

public class PortfolioListViewAdapter extends ArrayAdapter<PortfolioObject> {

    private LayoutInflater mInflater;
    private ArrayList<PortfolioObject> portfolios;
    private int mViewResourceId;

    public PortfolioListViewAdapter(Context context, int textViewResourceId, ArrayList<PortfolioObject> portfolios) {
        super(context, textViewResourceId, portfolios);
        this.portfolios = portfolios;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        PortfolioObject portfolioObject = portfolios.get(position);

        if (portfolioObject != null) {
            TextView stockName = convertView.findViewById(R.id.p_stock_name);
            TextView lotSize = convertView.findViewById(R.id.p_lot_size);
            TextView profitValue = convertView.findViewById(R.id.p_profit_value);
            TextView profitPercent = convertView.findViewById(R.id.p_profit_percent);

            stockName.setText(portfolioObject.getStock());
            lotSize.setText(portfolioObject.getLotSize());


            DecimalFormat decim = new DecimalFormat("0.000");

            double buy_price=Double.parseDouble(decim.format(Double.parseDouble(portfolioObject.getBuyPrice())));
            double close_price=Double.parseDouble(decim.format(Double.parseDouble(portfolioObject.getClose())));
            double lot_size=Double.parseDouble(portfolioObject.getLotSize());


            double profitCalc = (close_price*lot_size) - (buy_price*lot_size);
            double percentCalc = profitCalc/(buy_price*lot_size)*100;

            if (profitCalc > 0) {
                profitValue.setTextColor(Color.parseColor("#12e34a") );
                profitPercent.setTextColor(Color.parseColor("#12e34a") );
            }
            else if (profitCalc < 0){
                profitValue.setTextColor(Color.parseColor("#e31212") );
                profitPercent.setTextColor(Color.parseColor("#e31212") );
            }

            profitValue.setText(String.format("%.2f", profitCalc));
            profitPercent.setText(String.format("%.2f", percentCalc)+"%");

        }



        return convertView;
    }



}
