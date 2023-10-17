package com.example.ecomm.Screens.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecomm.R;
import com.example.ecomm.databinding.FragmentOrdersBinding;

public class OrdersFragment extends Fragment {

    FragmentOrdersBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentOrdersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}