package com.example.ecomm.Screens.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecomm.MainActivity;
import com.example.ecomm.R;
import com.example.ecomm.Screens.Admin.AdminDashboardActivity;
import com.example.ecomm.Screens.DashboardActivity;
import com.example.ecomm.Screens.LoginActivity;
import com.example.ecomm.Screens.ProductsActivity;
import com.example.ecomm.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userId;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        preferences = inflater.getContext().getSharedPreferences("myData", MODE_PRIVATE);
        editor = preferences.edit();
        userId = preferences.getString("userId",null);
        MainActivity.myRef.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String roleCheck = snapshot.child("role").getValue().toString().trim();
                    binding.roleTextView.setText(roleCheck);
                    binding.nameTextView.setText(snapshot.child("name").getValue().toString().trim());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.productsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(inflater.getContext(), ProductsActivity.class));
            }
        });


        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAuth.signOut();
                editor.clear();
                editor.commit();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        binding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return binding.getRoot();
    }
}