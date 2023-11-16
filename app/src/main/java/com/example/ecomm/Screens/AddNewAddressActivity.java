package com.example.ecomm.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.ecomm.R;
import com.example.ecomm.databinding.ActivityAddNewAddressBinding;

public class AddNewAddressActivity extends AppCompatActivity {

    ActivityAddNewAddressBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_address);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityAddNewAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences("myData", MODE_PRIVATE);
        editor = preferences.edit();
        userId = preferences.getString("userId",null);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewAddressActivity.super.onBackPressed();
            }
        });
    }
}