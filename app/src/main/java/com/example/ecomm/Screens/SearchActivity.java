package com.example.ecomm.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ecomm.MainActivity;
import com.example.ecomm.R;
import com.example.ecomm.Screens.Models.ProductModel;
import com.example.ecomm.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    ArrayList<ProductModel> datalist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.super.onBackPressed();
            }
        });
        binding.clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchEditText.setText(null);
                search();
            }
        });
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.searchedWord.setText(binding.searchEditText.getText().toString().trim());
                if(binding.searchEditText.getText().toString().length() > 0){
                    binding.clearText.setVisibility(View.VISIBLE);
                } else {
                    binding.clearText.setVisibility(View.GONE);
                }
                search();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        search();
    }
    public void search(){
        String input = binding.searchEditText.getText().toString().trim();
        if(input.equals("")){
            fetchData("");
            binding.notifyBar.setVisibility(View.GONE);
        } else {
            fetchData(input);
            binding.notifyBar.setVisibility(View.VISIBLE);
        }
    }

    public void fetchData(String data){
        MainActivity.myRef.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    datalist.clear();
                    for(DataSnapshot ds : snapshot.getChildren()){
                        if(data.equals("")){
                            ProductModel model = new ProductModel(
                                    ds.getKey(),
                                    ds.child("pName").getValue().toString().trim(),
                                    ds.child("pPrice").getValue().toString().trim(),
                                    ds.child("pStock").getValue().toString().trim(),
                                    ds.child("pDiscount").getValue().toString().trim(),
                                    ds.child("pImage").getValue().toString().trim(),
                                    ds.child("pDesc").getValue().toString().trim(),
                                    ds.child("status").getValue().toString().trim()
                            );
                            datalist.add(model);
                        } else {
                            if(ds.child("pName").getValue().toString().trim().toLowerCase().contains(data.toLowerCase())){
                                ProductModel model = new ProductModel(
                                        ds.getKey(),
                                        ds.child("pName").getValue().toString().trim(),
                                        ds.child("pPrice").getValue().toString().trim(),
                                        ds.child("pStock").getValue().toString().trim(),
                                        ds.child("pDiscount").getValue().toString().trim(),
                                        ds.child("pImage").getValue().toString().trim(),
                                        ds.child("pDesc").getValue().toString().trim(),
                                        ds.child("status").getValue().toString().trim()
                                );
                                datalist.add(model);
                            }
                        }

                    }
                    if(datalist.size() > 0){
                        binding.gridView.setVisibility(View.VISIBLE);
                        binding.searchContainer.setVisibility(View.GONE);
                        binding.notfoundContainer.setVisibility(View.GONE);
                        Collections.reverse(datalist);
                        MyAdapter adapter = new MyAdapter(SearchActivity.this,datalist);
                        binding.gridView.setAdapter(adapter);
                    } else {
                        binding.gridView.setVisibility(View.GONE);
                        if(data.equals("")){
                            binding.searchContainer.setVisibility(View.VISIBLE);
                        } else {
                            binding.notfoundContainer.setVisibility(View.VISIBLE);
                        }
                    }
                    binding.totalCount.setText(datalist.size()+" found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public class MyAdapter extends BaseAdapter{

        Context context;
        ArrayList<ProductModel> data;

        public MyAdapter(Context context, ArrayList<ProductModel> data) {
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View productItem = LayoutInflater.from(context).inflate(R.layout.product_listview,null);
            ImageView pImage, wishlistBtn;
            TextView pDiscount, pName, pRating, pStock, pPrice, pPriceOff;
            LinearLayout options, item;
            pImage = productItem.findViewById(R.id.pImage);
            wishlistBtn = productItem.findViewById(R.id.wishlistBtn);
            pDiscount = productItem.findViewById(R.id.pDiscount);
            pName = productItem.findViewById(R.id.pName);
            pRating = productItem.findViewById(R.id.pRating);
            pStock = productItem.findViewById(R.id.pStock);
            pPrice = productItem.findViewById(R.id.pPrice);
            pPriceOff = productItem.findViewById(R.id.pPriceOff);
            options = productItem.findViewById(R.id.options);
            item = productItem.findViewById(R.id.item);

            if(!data.get(i).getpDiscount().equals("0")){
                pDiscount.setVisibility(View.VISIBLE);
                pDiscount.setText(data.get(i).getpDiscount()+"% OFF");
            } else {
                pPriceOff.setVisibility(View.GONE);
            }
            pName.setText(data.get(i).getpName());
            pStock.setText(data.get(i).getpStock()+" Stock");
            pPriceOff.setText("$"+data.get(i).getpPrice());
            Glide.with(context).load(data.get(i).getpImage()).into(pImage);

            double discount = Double.parseDouble(data.get(i).getpDiscount())/100;
            double calcDiscount = Double.parseDouble(data.get(i).getpPrice()) * discount;
            double totalPrice = Double.parseDouble(data.get(i).getpPrice()) - calcDiscount;
            pPrice.setText("$"+Math.round(totalPrice));

            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra("pid",data.get(i).getId());
                    startActivity(intent);
                }
            });

            return productItem;
        }
    }
}