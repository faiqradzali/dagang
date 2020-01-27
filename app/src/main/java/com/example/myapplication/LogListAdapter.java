package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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

import java.util.ArrayList;

public class LogListAdapter extends ArrayAdapter<LogObject> {

    private LayoutInflater mInflater;
    private ArrayList<LogObject> logList;
    private int mViewResourceId;

    public LogListAdapter(Context context, int textViewResourceId, ArrayList<LogObject> logList) {
        super(context, textViewResourceId, logList);
        this.logList = logList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        LogObject logObject = logList.get(position);
        Log.d("test", logObject.getCurrentDate());
        Log.d("test", logObject.getStock_name());
        Log.d("test", logObject.getClose());
        Log.d("test", logObject.getSize());
        Log.d("test", logObject.getType());

        if (logObject != null) {
            TextView tvDate = convertView.findViewById(R.id.log_date);
            TextView tvStock = convertView.findViewById(R.id.log_stock);
            TextView tvPrice = convertView.findViewById(R.id.log_price);
            TextView tvSize = convertView.findViewById(R.id.log_size);
            TextView tvType = convertView.findViewById(R.id.log_type);

            tvDate.setText(logObject.getCurrentDate());
            tvStock.setText(logObject.getStock_name());
            tvPrice.setText(logObject.getClose());
            tvSize.setText(logObject.getSize());
            tvType.setText(logObject.getType());
        }



        return convertView;
    }



}
