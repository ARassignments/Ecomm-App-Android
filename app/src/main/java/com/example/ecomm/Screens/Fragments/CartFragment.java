package com.example.ecomm.Screens.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecomm.CheckoutActivity;
import com.example.ecomm.R;
import com.example.ecomm.Screens.DashboardActivity;
import com.example.ecomm.Screens.SearchActivity;
import com.example.ecomm.databinding.FragmentCartBinding;

import java.util.zip.CheckedInputStream;

public class CartFragment extends Fragment {

    FragmentCartBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentCartBinding.inflate(inflater, container, false);
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
        return binding.getRoot();
    }
}