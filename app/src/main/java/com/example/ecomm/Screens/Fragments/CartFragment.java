package com.example.ecomm.Screens.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ecomm.CheckoutActivity;
import com.example.ecomm.MainActivity;
import com.example.ecomm.R;
import com.example.ecomm.Screens.DashboardActivity;
import com.example.ecomm.Screens.Models.AddToCartModel;
import com.example.ecomm.Screens.SearchActivity;
import com.example.ecomm.databinding.FragmentCartBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.zip.CheckedInputStream;

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    ArrayList<AddToCartModel> datalist = new ArrayList<>();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentCartBinding.inflate(inflater, container, false);
        preferences = container.getContext().getSharedPreferences("myData", MODE_PRIVATE);
        editor = preferences.edit();
        userId = preferences.getString("userId",null);
//        DashboardActivity.updateCartCount(20);
        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(), CheckoutActivity.class));
            }
        });
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(), SearchActivity.class));
            }
        });
        MainActivity.myRef.child("AddToCart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(userId.equals(ds.child("UID").getValue().toString().trim())){
                        AddToCartModel model = new AddToCartModel(
                                ds.getKey(),
                                ds.child("PID").getValue().toString().trim(),
                                ds.child("UID").getValue().toString().trim(),
                                ds.child("qty").getValue().toString().trim()
                        );
                        datalist.add(model);
                    }
                }
                Collections.reverse(datalist);
                if(datalist.size() < 1){
                    binding.notfoundContainer.setVisibility(View.VISIBLE);
                    binding.cartContainer.setVisibility(View.GONE);
                } else {
                    MyAdapter adapter = new MyAdapter(container.getContext(),datalist);
                    binding.listView.setAdapter(adapter);
                    binding.notfoundContainer.setVisibility(View.GONE);
                    binding.cartContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }

    public class MyAdapter extends BaseAdapter{

        Context context;
        ArrayList<AddToCartModel> data;

        public MyAdapter(Context context, ArrayList<AddToCartModel> data) {
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
            View itemView = LayoutInflater.from(context).inflate(R.layout.product_cart_listview,null);
            ImageView pImage, qtyMinus, qtyAdd, delete;
            TextView pName, pPrice, pPriceOff, pQty;
            pImage = itemView.findViewById(R.id.pImage);
            pName = itemView.findViewById(R.id.pName);
            pPrice = itemView.findViewById(R.id.pPrice);
            pPriceOff = itemView.findViewById(R.id.pPriceOff);
            pQty = itemView.findViewById(R.id.pQty);
            qtyMinus = itemView.findViewById(R.id.qtyMinus);
            qtyAdd = itemView.findViewById(R.id.qtyAdd);
            delete = itemView.findViewById(R.id.delete);

            pQty.setText(data.get(i).getQty());
            MainActivity.myRef.child("Products").child(data.get(i).getPID()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Glide.with(context).load(snapshot.child("pImage").getValue().toString().trim()).into(pImage);
                        pName.setText(snapshot.child("pName").getValue().toString().trim());
                        pPrice.setText("$"+snapshot.child("pPrice").getValue().toString().trim());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            return itemView;
        }
    }
}