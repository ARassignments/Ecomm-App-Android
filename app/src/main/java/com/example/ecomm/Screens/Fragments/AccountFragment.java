package com.example.ecomm.Screens.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ecomm.R;
import com.example.ecomm.Screens.LoginActivity;
import com.example.ecomm.databinding.FragmentAccountBinding;
import com.google.firebase.auth.FirebaseAuth;


public class AccountFragment extends Fragment {

   FragmentAccountBinding binding;
    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentAccountBinding.inflate(inflater, container, false);
        preferences = inflater.getContext().getSharedPreferences("myData", MODE_PRIVATE);
        editor = preferences.edit();
        userId = preferences.getString("userId",null);
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog loaddialog = new Dialog(container.getContext());
                loaddialog.setContentView(R.layout.bottom_logout);
                loaddialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                loaddialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                loaddialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationBottom;
                loaddialog.getWindow().setGravity(Gravity.BOTTOM);
                loaddialog.setCancelable(false);
                loaddialog.setCanceledOnTouchOutside(false);
                Button cancelBtn, yesBtn;
                cancelBtn = loaddialog.findViewById(R.id.cancelBtn);
                yesBtn = loaddialog.findViewById(R.id.yesBtn);
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myAuth.signOut();
                        editor.clear();
                        editor.commit();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loaddialog.dismiss();
                    }
                });
                loaddialog.show();
            }
        });
        return binding.getRoot();
    }
}