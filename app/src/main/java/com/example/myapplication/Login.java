package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToRegister (View view){
        Intent intent = new Intent (this, Register.class);
        startActivity(intent);
    }

    public void goToDashboard(View view){
        Intent intent = new Intent (this, Dashboard.class);
        startActivity(intent);
    }
}
