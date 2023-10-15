package com.example.ecomm.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomm.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    ImageView backBtn;
    TextInputLayout nameLayout, emailLayout, passwordLayout, cpasswordLayout;
    TextInputEditText nameEditText, emailEditText, passwordEditText, cpasswordEditText;
    Button submitBtn;
    TextView loginBtn;

    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference db = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        backBtn = findViewById(R.id.backBtn);
        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        cpasswordLayout = findViewById(R.id.cpasswordLayout);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        cpasswordEditText = findViewById(R.id.cpasswordEditText);
        submitBtn = findViewById(R.id.submitBtn);
        loginBtn = findViewById(R.id.loginBtn);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupActivity.super.onBackPressed();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignupActivity.super.onBackPressed();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nameValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cpasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cPasswordValidation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public boolean nameValidation(){
        String input = nameEditText.getText().toString().trim();
        if(input.equals("")){
            nameLayout.setError("Name is Required!!!");
            return false;
        } else if(!Pattern.compile("^[a-zA-Z\\s]*$").matcher(input).matches()){
            nameLayout.setError("Name In Only Text!!!");
            return false;
        } else if(input.length() < 3){
            nameLayout.setError("Name at least 3 characters!!!");
            return false;
        } else {
            nameLayout.setError(null);
            return true;
        }
    }

    public boolean emailValidation(){
        String input = emailEditText.getText().toString().trim();
        String pattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if(input.equals("")){
            emailLayout.setError("Email Address is Required!!!");
            return false;
        } else if(!input.matches(pattern)){
            emailLayout.setError("Enter Valid Email Address!!!");
            return false;
        } else {
            emailLayout.setError(null);
            return true;
        }
    }

    public boolean passwordValidation(){
        String input = passwordEditText.getText().toString().trim();
        if(input.equals("")){
            passwordLayout.setError("Password is Required!!!");
            return false;
        } else if(input.length() < 8){
            passwordLayout.setError("Password at least 8 characters!!!");
            return false;
        } else {
            passwordLayout.setError(null);
            return true;
        }
    }

    public boolean cPasswordValidation(){
        String input = cpasswordEditText.getText().toString().trim();
        String input2 = passwordEditText.getText().toString().trim();
        if(input.equals("")){
            cpasswordLayout.setError("Confirm Password is Required!!!");
            return false;
        } else if(input.length() < 8){
            cpasswordLayout.setError("Confirm Password at least 8 characters!!!");
            return false;
        } else if(!input.equals(input2)) {
            cpasswordLayout.setError("Confirm Password is not matched!!!");
            return false;
        } else {
            cpasswordLayout.setError(null);
            return true;
        }
    }

    public void validation(){
        boolean nameErr = false, emailErr = false, passwordErr = false, cpasswordErr = false;
        nameErr = nameValidation();
        emailErr = emailValidation();
        passwordErr = passwordValidation();
        cpasswordErr = cPasswordValidation();
        if((nameErr && emailErr && passwordErr && cpasswordErr) == true){
            Dialog loaddialog = new Dialog(SignupActivity.this);
            loaddialog.setContentView(R.layout.dialo_loading);
            loaddialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            loaddialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loaddialog.getWindow().setGravity(Gravity.CENTER);
            loaddialog.setCancelable(false);
            loaddialog.setCanceledOnTouchOutside(false);
            TextView message = loaddialog.findViewById(R.id.message);
            message.setText("Creating...");
            loaddialog.show();
            // Signup Here
            myAuth.createUserWithEmailAndPassword(emailEditText.getText().toString().trim(),passwordEditText.getText().toString().trim())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            loaddialog.dismiss();
                            Dialog alertdialog = new Dialog(SignupActivity.this);
                            alertdialog.setContentView(R.layout.dialog_success);
                            alertdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            alertdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            alertdialog.getWindow().setGravity(Gravity.CENTER);
                            alertdialog.setCancelable(false);
                            alertdialog.setCanceledOnTouchOutside(false);
                            alertdialog.show();

                            FirebaseUser user = myAuth.getCurrentUser();

                            HashMap<String,String> obj = new HashMap<String,String>();
                            obj.put("name",nameEditText.getText().toString().trim());
                            obj.put("email",emailEditText.getText().toString().trim());
                            obj.put("role","user");
                            obj.put("status","1");

                            db.child("Users").child(user.getUid()).setValue(obj);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertdialog.dismiss();
                                    SignupActivity.super.onBackPressed();
                                }
                            },2000);

//                            SignupActivity.super.onBackPressed();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loaddialog.dismiss();
                            Dialog alertdialog = new Dialog(SignupActivity.this);
                            alertdialog.setContentView(R.layout.dialog_error);
                            alertdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                            alertdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                            alertdialog.getWindow().setGravity(Gravity.CENTER);
                            alertdialog.setCancelable(false);
                            alertdialog.setCanceledOnTouchOutside(false);
                            TextView message = alertdialog.findViewById(R.id.message);
                            message.setText("Your is Already Exist!!!");
                            alertdialog.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    alertdialog.dismiss();
                                }
                            },3000);
                        }
                    });
        }
    }
}