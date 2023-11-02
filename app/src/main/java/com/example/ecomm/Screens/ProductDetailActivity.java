package com.example.ecomm.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.example.ecomm.MainActivity;
import com.example.ecomm.R;
import com.example.ecomm.databinding.ActivityProductDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class ProductDetailActivity extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    String PID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            PID = extra.getString("pid");
        }

        MainActivity.myRef.child("Products").child(PID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Glide.with(ProductDetailActivity.this).load(snapshot.child("pImage").getValue().toString().trim()).into(binding.pImage);
                    binding.pName.setText(snapshot.child("pName").getValue().toString().trim());
                    binding.pStock.setText(snapshot.child("pStock").getValue().toString().trim()+" Stock");
                    binding.pDesc.setText(snapshot.child("pDesc").getValue().toString().trim());
                    binding.totalPrice.setText("S"+snapshot.child("pPrice").getValue().toString().trim());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetailActivity.super.onBackPressed();
            }
        });
    }
}