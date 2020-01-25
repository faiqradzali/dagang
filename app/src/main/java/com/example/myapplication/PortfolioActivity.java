package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PortfolioActivity extends BaseActivity {

    private static final String TAG = "PortfolioActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    SessionManager sessionManager;
    ArrayList<PortfolioObject> portfolioList = new ArrayList<>();
    ListView listView;

    public interface VolleyCallback{
        void onSuccess(String result);
    }

    public void generatePortfolio(String stockname, String size, String buyprice){
        final String stockName = stockname;
        final String lotSize = size;
        final String buyPrice = buyprice;
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
                            Log.d(TAG, "onResponse list: " + portfolioList.get(0).getBuyPrice().toString());

                            if (response != null) {
                                Log.d(TAG, "generateList: called");
                                PortfolioListViewAdapter adapter = new PortfolioListViewAdapter(getApplicationContext(), R.layout.layout_listportfolio, portfolioList);
                                Log.d(TAG + "adapter", adapter.toString());
                                listView = (ListView) findViewById(R.id.list_view);


                                if (listView != null) {
                                    Log.d(TAG + "listView", "passed");
                                    listView.setAdapter(adapter);
                                }
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

    public void generateList(){


        Log.d(TAG, "generateList: authorized");
        PortfolioListViewAdapter adapter = new PortfolioListViewAdapter(getApplicationContext(), R.layout.layout_listportfolio, portfolioList);
        listView = (findViewById(R.id.list_view));
        listView.setAdapter(adapter);

    }

    public interface ResponseCallback {

        public void onSuccess (String result);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portfolio);

//        sessionManager = new SessionManager(this);
//        sessionManager.checkLogin();
//
//        HashMap<String, String> user = sessionManager.getUserDetail();
//        String mName = user.get(sessionManager.NAME);

        String mName = "faiq";





        db.collection("user_accounts").document(mName).collection("portfolio").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {


                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot snapshot : queryDocumentSnapshots)

                            generatePortfolio(snapshot.getId(), snapshot.getString("size"), snapshot.getString("price"));


//                        if (portfolioList != null){
//                            PortfolioListViewAdapter adapter =  new PortfolioListViewAdapter(getApplicationContext(), R.layout.layout_listportfolio, portfolioList);
//                            listView = (findViewById(R.id.listView));
//                            listView.setAdapter(adapter);}


                    }
                }








                );






//        int numRows = data.getCount();
//        if(numRows == 0){
//            Toast.makeText(ViewListContents.this,"The Database is empty  :(.",Toast.LENGTH_LONG).show();
//        }else{
//            int i=0;
//            while(data.moveToNext()){
//                user = new PortfolioObject(data.getString(1),data.getString(2),data.getString(3));
//                userList.add(i,user);
//                System.out.println(data.getString(1)+" "+data.getString(2)+" "+data.getString(3));
//                System.out.println(userList.get(i).getFirstName());
//                i++;
//            }
//            ThreeColumn_ListAdapter adapter =  new ThreeColumn_ListAdapter(this,R.layout.list_adapter_view, userList);
//            listView = (ListView) findViewById(R.id.listView);
//            listView.setAdapter(adapter);
//        }
    }
}
