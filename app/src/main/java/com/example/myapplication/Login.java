package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog nDialog;

    private static final String TAG = "Login";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private static final String KEY_USER = "username";
    private static final String KEY_PASS = "password";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_MONEY = "capital";

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        editTextUsername = findViewById(R.id.username_input);
        editTextPassword = findViewById(R.id.password_input);
    }

    public void goToRegister (View view){
        Intent intent = new Intent (this, Register.class);
        startActivity(intent);
    }

    public void goToDashboard(){
        Log.d(TAG, "goToDashboard: ");

        Intent intent = new Intent (this, Dashboard.class);
        startActivity(intent);
    }

    public void loadUser(View v) {
        try{
            nDialog = new ProgressDialog(Login.this);
            nDialog.setMessage("Verifying username and password..");
            nDialog.setTitle("Logging in..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
            final String username = editTextUsername.getText().toString();
            final String password = editTextPassword.getText().toString();

            db.collection("user_accounts").document(username).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                if (documentSnapshot != null) {
                                    String user = documentSnapshot.getString(KEY_USER);
                                    String pass = documentSnapshot.getString(KEY_PASS);
                                    String email = documentSnapshot.getString(KEY_EMAIL);
                                    String capital = documentSnapshot.getString(KEY_MONEY);

                                    nDialog.hide();

                                    if (password.equals(pass)) {

                                        sessionManager.createSession(user, email, capital);
                                        Intent i = new Intent(getApplicationContext(), Dashboard.class);

                                        startActivity(i);
                                    } else {
                                        Toast.makeText(Login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
                                }


                            } else {
                                Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();

                        }
                    });
        }catch (Exception e){
            nDialog.hide();
            Toast.makeText(Login.this, "User not found", Toast.LENGTH_SHORT).show();
            Log.d(TAG, e.toString());
        }


    }
}
