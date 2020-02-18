package com.example.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class PortfolioActivity extends BaseActivity {

    private static final String TAG = "PortfolioActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SessionManager sessionManager;
    ArrayList<PortfolioObject> portfolioList = new ArrayList<>();
    ListView listView;
    TextView tradingLimit;
    ProgressDialog nDialog;
    String TradingLimit;



    public void generatePortfolio(String stockname, String size, String buyprice, String TradingLimit){
        final String stockName = stockname;
        final String lotSize = size;
        final String buyPrice = buyprice;
        final String tradingLimit = TradingLimit;

        Log.d(TAG, "Name passed " + stockName);

        final String[] split_close;
        String last_close = null;
        final String URL = "https://www.isaham.my/api/chart/data?stock=" + stockName + "&key=19f04a05i12q";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        Log.d(TAG, URL);

        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        String last_close = null;
                        Log.d(TAG, "onResponse: ");
                        try {
                            
                            String close = response.getString("c");
                            close = close.substring(1, close.length() - 1);
                            String[] split_close = close.split(",");
                            last_close = split_close[split_close.length - 1];

                            Log.d(TAG, "onResponse: close price set" + last_close);

                            PortfolioObject portfolioObject = new PortfolioObject(stockName,lotSize,buyPrice,last_close);

                            Log.d(TAG, "onResponse: " + lotSize);
                            portfolioList.add(portfolioObject);


                            if (response != null) {
                                Log.d(TAG, "generateList: called");
                                PortfolioListViewAdapter adapter = new PortfolioListViewAdapter(getApplicationContext(), R.layout.layout_listportfolio, portfolioList);
                                Log.d(TAG, adapter.toString());
                                listView = (ListView) findViewById(R.id.list_view);
                                listView.setAdapter(adapter);




                                double totalValue = 0;
                                double totalProfit = 0;
                                for(int i=0;i<portfolioList.size();i++){
                                    double buyPrice=Double.parseDouble(portfolioList.get(i).getBuyPrice());
                                    double lotSize=Double.parseDouble(portfolioList.get(i).getLotSize());
                                    double closePrice=Double.parseDouble(portfolioList.get(i).getClose());

                                    totalValue += (closePrice * lotSize);
                                    totalProfit += ((closePrice * lotSize)-(buyPrice * lotSize));
                                }

                                double accValue = totalValue + Double.parseDouble(tradingLimit);
                                final TextView viewProfit = findViewById(R.id.p_total_profit);
                                final TextView viewAccValue = findViewById(R.id.p_account_value);
                                final TextView viewInvestmentValue = findViewById(R.id.p_total_investment_value);



                                viewInvestmentValue.setText(String.format("%.2f", totalValue));
                                viewAccValue.setText(String.format("%.2f", accValue));
                                viewProfit.setText(String.format("%.2f", totalProfit));
                                nDialog.hide();


                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        portfolioList.get(position);
                                        Toast.makeText(PortfolioActivity.this, portfolioList.get(position).getStock(), Toast.LENGTH_SHORT).show();
                                        Intent intent =  new Intent(PortfolioActivity.this, PortfolioDetailActivity.class);
                                        intent.putExtra("StockName", portfolioList.get(position).getStock());
                                        intent.putExtra("ClosePrice", portfolioList.get(position).getClose());
                                        intent.putExtra("LotSize", portfolioList.get(position).getLotSize());
                                        intent.putExtra("BuyPrice", portfolioList.get(position).getBuyPrice());
                                        PortfolioActivity.this.startActivity(intent);

                                    }

                                });

                            }

                            
                        } catch (JSONException e) {
                            Log.d("Error", "hehe");

                        }

//                        if (portfolioList != null)
//                            generateList();

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest Response", error.toString());
                        Log.d(TAG, "onErrorResponse: ");
                    }
                }

        );

        requestQueue.add(objectRequest);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);




        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);

        nDialog = new ProgressDialog(PortfolioActivity.this);
        nDialog.setMessage("Populating portfolio..");
        nDialog.setTitle("Loading..");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
        

        db.collection("user_accounts").document(mName).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "onSuccess: capital");
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot != null) {
                                String mTradingLimit = documentSnapshot.getString("capital");

                                tradingLimit = findViewById(R.id.p_trading_limit);
                                double doubleTradingLimit = Double.parseDouble(mTradingLimit);
                                tradingLimit.setText(String.format("%.2f", doubleTradingLimit));
                                TradingLimit = mTradingLimit;
                            }
                        }
                    }
                    });

        db.collection("user_accounts").document(mName).collection("portfolio").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {


                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                            for (DocumentSnapshot snapshot : queryDocumentSnapshots)
                                generatePortfolio(snapshot.getId(), snapshot.getString("size"), snapshot.getString("price"), TradingLimit);



                    }
                }
                )

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }


}
