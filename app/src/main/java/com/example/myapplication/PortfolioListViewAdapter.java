package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.auth.User;

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
            TextView buyPrice = convertView.findViewById(R.id.p_buy_price);
            TextView currentPrice = convertView.findViewById(R.id.p_current_price);

            stockName.setText(portfolioObject.getStock());
            lotSize.setText(portfolioObject.getLotSize());
            buyPrice.setText(portfolioObject.getBuyPrice());
            currentPrice.setText(portfolioObject.getClose());

//            if (firstName != null) {
//                firstName.setText(user.getFirstName());
//            }
//            if (lastName != null) {
//                lastName.setText((user.getLastName()));
//            }
//            if (favFood != null) {
//                favFood.setText((user.getFavFood()));
//            }
        }

        return convertView;
    }

}
