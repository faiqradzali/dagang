package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends Navigation {
    private static final String TAG = "Register";

    private static final String KEY_USER = "username";
    private static final String KEY_PASS = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_INIT = "init";
    private static final String KEY_MONEY = "capital";

    private EditText editTextUser;
    private EditText editTextPass;
    private EditText editTextEmail;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUser = findViewById(R.id.usernameInput);
        editTextPass = findViewById(R.id.passwordInput);
        editTextEmail = findViewById(R.id.emailInput);
    }

    public void registerAccount (View v) {
        String user = editTextUser.getText().toString();
        String pass = editTextPass.getText().toString();
        String email = editTextEmail.getText().toString();
        String money = "10000";

        Map<String, Object> account = new HashMap<>();
        account.put(KEY_USER, user);
        account.put(KEY_PASS, pass);
        account.put(KEY_EMAIL, email);
        account.put(KEY_MONEY, money);

        db.collection("user_accounts").document(user).set(account)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Register.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

        Map<String, Object> init_doc = new HashMap<>();
        init_doc.put(KEY_INIT, "init");

        db.collection("user_accounts").document(user).collection("portfolio").document("init").set(init_doc);
        db.collection("user_accounts").document(user).collection("log").document("init").set(init_doc);
    }
}
