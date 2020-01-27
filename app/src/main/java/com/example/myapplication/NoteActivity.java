package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class NoteActivity extends BaseActivity {
    SessionManager sessionManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String mName;
    private String docId;
    private static final String KEY_NOTES = "notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        sessionManager = new SessionManager(this);

        final Intent intent = getIntent();
        HashMap<String, String> user = sessionManager.getUserDetail();
        mName = user.get(sessionManager.NAME);
        docId = intent.getStringExtra("docId");
        Log.d("ID of document: ", docId);


        final TextView tvN = findViewById(R.id.tvNotes);
        db.collection("user_accounts").document(mName).collection("log").document(docId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    if (documentSnapshot != null) {
                        String notes = documentSnapshot.getString(KEY_NOTES);
                        tvN.setText(notes);
                    }
                    else {
                        Log.d("error", "else in not null snapshot ");
                    }
                }
            }

                });
    }

    public void AddNotes(View view){
        EditText tv = findViewById(R.id.editNotes);
        TextView tvN = findViewById(R.id.tvNotes);
        String s = tv.getText().toString();
        tvN.setText(s);
        final Intent intent = getIntent();
        docId = intent.getStringExtra("docId");
        Log.d("name", mName);

        db.collection("user_accounts").document(mName).collection("log").document(docId).update("notes", s);
    }

    public void gotoLog(View view){
        Intent i = new Intent(getApplicationContext(), LogActivity.class);
        startActivity(i);
    }
}
