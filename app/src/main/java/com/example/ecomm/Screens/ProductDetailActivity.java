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
    int pPrice = 0, pStock = 0, pDiscount = 0 ,pQty = 1;
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
                    binding.pNameTitle.setText(snapshot.child("pName").getValue().toString().trim());
                    binding.pDesc.setText(snapshot.child("pDesc").getValue().toString().trim());
                    binding.totalPrice.setText("$"+snapshot.child("pPrice").getValue().toString().trim());
                    if(Integer.parseInt(snapshot.child("pStock").getValue().toString().trim()) > 0){
                        binding.pStock.setText(snapshot.child("pStock").getValue().toString().trim()+" Stock");
                    } else {
                        binding.pStock.setText("Soled");
                        binding.btnAddToCart.setVisibility(View.GONE);
                        binding.qtyContainer.setVisibility(View.GONE);
                        binding.totalContainer.setVisibility(View.GONE);
                        binding.btnOutOfStock.setVisibility(View.VISIBLE);
                    }
                    if(!snapshot.child("pDiscount").getValue().toString().trim().equals("0")){
                        binding.pDiscount.setVisibility(View.VISIBLE);
                        binding.pDiscount.setText(snapshot.child("pDiscount").getValue().toString().trim()+"% OFF");
                        binding.pPriceOff.setVisibility(View.VISIBLE);
                        binding.pPriceOff.setText("$"+snapshot.child("pPrice").getValue().toString().trim());
                        double discount = Double.parseDouble(snapshot.child("pDiscount").getValue().toString().trim())/100;
                        double calcDiscount = Double.parseDouble(snapshot.child("pPrice").getValue().toString().trim()) * discount;
                        double totalPrice = Double.parseDouble(snapshot.child("pPrice").getValue().toString().trim()) - calcDiscount;
                        binding.totalPrice.setText("$"+Math.round(totalPrice));
                    }
                    setData(
                            snapshot.child("pPrice").getValue().toString().trim(),
                            snapshot.child("pStock").getValue().toString().trim(),
                            snapshot.child("pDiscount").getValue().toString().trim()
                    );
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
        binding.pQty.setText(""+pQty);
        binding.qtyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addQty();
            }
        });
        binding.qtyMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtractQty();
            }
        });
    }
    public void setData(String pPriceVal, String pStockVal, String pDiscountVal){
        pPrice = Integer.parseInt(pPriceVal);
        pStock = Integer.parseInt(pStockVal);
        pDiscount = Integer.parseInt(pDiscountVal);
    }
    public void addQty(){
        if(pQty < pStock){
            pQty++;
            binding.pQty.setText(""+pQty);
            if(pDiscount > 0){
                double discount = Double.parseDouble(""+pDiscount)/100;
                double calcDiscount = Double.parseDouble(""+pPrice) * discount;
                double totalPrice = Double.parseDouble(""+pPrice) - calcDiscount;
                binding.pPriceOff.setText("$"+(pPrice*pQty));
                binding.totalPrice.setText("$"+(Math.round(totalPrice)*pQty));
            } else {
                binding.totalPrice.setText("$"+(pPrice*pQty));
            }
        }
    }
    public void subtractQty(){
        if(pQty > 1){
            pQty--;
            binding.pQty.setText(""+pQty);
            if(pDiscount > 0){
                double discount = Double.parseDouble(""+pDiscount)/100;
                double calcDiscount = Double.parseDouble(""+pPrice) * discount;
                double totalPrice = Double.parseDouble(""+pPrice) - calcDiscount;
                binding.pPriceOff.setText("$"+(pPrice*pQty));
                binding.totalPrice.setText("$"+(Math.round(totalPrice)*pQty));
            } else {
                binding.totalPrice.setText("$"+(pPrice*pQty));
            }
        }
    }
}