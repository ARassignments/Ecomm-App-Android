package com.example.ecomm.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.ecomm.R;

public class GetStartedActivity extends AppCompatActivity {

    Button btnStarted;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences = getSharedPreferences("myData",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        btnStarted = findViewById(R.id.btnStarted);
        if(!sharedPreferences.getString("loginStatus",null).isEmpty()){
            startActivity(new Intent(GetStartedActivity.this, DashboardActivity.class));
            finish();
        }
        btnStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStartedActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}