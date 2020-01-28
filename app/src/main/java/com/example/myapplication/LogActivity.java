package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class LogActivity extends BaseActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LayoutInflater mInflater;
    private int mViewResourceId;
    private String mName;
    ListView listView;
    SessionManager sessionManager;
    ArrayList<LogObject> LogList = new ArrayList<>();
    private DocumentReference docRef;

    public void generateLog(String docID, String date, String stock,String price,String size,String type){
        final String log_ID =  docID;
        final String log_date = date;
        final String log_stock = stock;
        final String log_price= price;
        final String log_size = size;
        final String log_type = type;

        Log.d("Name passed " , log_stock);
        LogObject logs = new LogObject(log_date,log_stock,log_price,log_size,log_type);
        LogList.add(logs);


        LogListAdapter adapter = new LogListAdapter(getApplicationContext(), R.layout.layout_log_list, LogList);

        listView = (ListView) findViewById(R.id.list_view_log);
        listView.setAdapter(adapter);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        final String mName = user.get(sessionManager.NAME);

        db.collection("user_accounts").document(mName).collection("log").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            if (documentSnapshot.getId().equals("init")){
                                continue;
                            }
                            else{
                                String gDate = documentSnapshot.getString("date");
                                String gStock = documentSnapshot.getString("stock");
                                String gPrice = documentSnapshot.getString("price");
                                String gSize = documentSnapshot.getString("size");
                                String gType = documentSnapshot.getString("type");
                                String gID = documentSnapshot.getId();
                                Log.d("ewe", "ID"+gID+"\nDate: " + gDate + "\nDescription: " + gStock + "\nPrice: "+gPrice);

                                LogObject logObject = new LogObject(gID,gDate,gStock,gPrice,gSize,gType);
                                LogList.add(logObject);
                            }

                        }
                        LogListAdapter adapter = new LogListAdapter(getApplicationContext(), R.layout.layout_log_list, LogList);
                        listView = (ListView) findViewById(R.id.list_view_log);
                        listView.setAdapter(adapter);

                    }
                });

    }
}






