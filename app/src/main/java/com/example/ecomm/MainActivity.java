package com.example.ecomm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ecomm.Screens.SplashScreenActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(myRef == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            myRef = FirebaseDatabase.getInstance().getReference();
        }
        startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
        finish();
    }
}