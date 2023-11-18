package com.example.ecomm.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ecomm.CheckoutActivity;
import com.example.ecomm.MainActivity;
import com.example.ecomm.R;
import com.example.ecomm.databinding.ActivityCheckoutBinding;
import com.example.ecomm.databinding.ActivitySelectAddressBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class SelectAddressActivity extends AppCompatActivity {

    ActivitySelectAddressBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySelectAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences("myData", MODE_PRIVATE);
        editor = preferences.edit();
        userId = preferences.getString("userId",null);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectAddressActivity.super.onBackPressed();
            }
        });
        binding.newAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SelectAddressActivity.this, AddressActivity.class));
            }
        });
        MainActivity.myRef.child("Address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("UID").getValue().toString().trim().equals(userId)){
                        View addressItemLayout = LayoutInflater.from(SelectAddressActivity.this).inflate(R.layout.address_listview,null);
                        TextView addressName, addressDetail, defaultStatus;
                        ImageView editBtn;
                        addressName = addressItemLayout.findViewById(R.id.addressName);
                        addressDetail = addressItemLayout.findViewById(R.id.addressDetail);
                        defaultStatus = addressItemLayout.findViewById(R.id.defaultStatus);
                        editBtn = addressItemLayout.findViewById(R.id.editBtn);
                        editBtn.setVisibility(View.GONE);
                        addressName.setText(ds.child("name").getValue().toString().trim());
                        addressDetail.setText(ds.child("address").getValue().toString().trim());
                        if(ds.child("defaultStatus").getValue().toString().trim().equals("true")){
                            defaultStatus.setVisibility(View.VISIBLE);
                        }
                        binding.addressContainer.addView(addressItemLayout);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}