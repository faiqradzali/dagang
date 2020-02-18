package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ScreenerDetailActivity extends BaseActivity{
    public DrawerLayout drawer;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mNamesFull = new ArrayList<>();
    StocklistRecyclerViewAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "BaseActivity";

    private static final String KEY_SCREENER = "detection";
    private static final String KEY_DESC = "description";
    private static final String KEY_TIME = "timestamp";

    TextView screenerName, description;
    //    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String screener_name = getIntent().getStringExtra("Screener");



        db.collection("screeners").document(screener_name).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot != null) {
                                String desc = documentSnapshot.getString(KEY_DESC) + " Last updated: " + documentSnapshot.getString(KEY_TIME);


                                screenerName = findViewById(R.id.sd_screener_name);
                                screenerName.setText(screener_name);
                                description = findViewById(R.id.sd_screener_desc);
                                description.setText(desc);

                            }
                        }
                    }
                });


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenerdetail);

        initStockName();
    }


    private void initStockName() {

        String screener_name = getIntent().getStringExtra("Screener");


        Log.d("screener name", screener_name);
        db.collection("screeners").document(screener_name).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot document = task.getResult();
                        List<String> stocklist = (List<String>) document.get(KEY_SCREENER);


                        Log.d("myTag", stocklist.get(0));

                        mNames.addAll((stocklist));
                        mNamesFull.addAll(mNames);

                        initRecyclerView();
                        }
                    }

                )

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        initRecyclerView();
    }






    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new StocklistRecyclerViewAdapter(this, mNames, mNamesFull);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }





}




