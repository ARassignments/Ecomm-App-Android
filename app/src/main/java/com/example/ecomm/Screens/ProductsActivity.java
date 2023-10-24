package com.example.ecomm.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecomm.MainActivity;
import com.example.ecomm.R;
import com.example.ecomm.databinding.ActivityProductsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsActivity extends AppCompatActivity {

    ActivityProductsBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String userId;
    FirebaseStorage storage;
    StorageReference mStorage;
    StorageTask uploadTask;
    Uri imageUri;

    // Dialog Components
    Dialog loaddialog;
    CircleImageView image;
    ImageButton imageadd;
    TextInputLayout pName, pDescription, pPrice, pStock;
    TextInputEditText pNameEditText, pDescriptionEditText, pPriceEditText, pStockEditText;
    Button cancelBtn, saveChangesBtn;
    TextView imageErrTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        binding = ActivityProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = getSharedPreferences("myData", MODE_PRIVATE);
        editor = preferences.edit();
        userId = preferences.getString("userId",null);
        mStorage = FirebaseStorage.getInstance().getReference();
        MainActivity.myRef.child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String roleCheck = snapshot.child("role").getValue().toString().trim();
                    if(roleCheck.equals("user")){
                        binding.addProductBtn.setVisibility(View.GONE);
                    } else if(roleCheck.equals("admin")){
                        binding.addProductBtn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddialog = new Dialog(ProductsActivity.this);
                loaddialog.setContentView(R.layout.dialog_add_product);
                loaddialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                loaddialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                loaddialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                loaddialog.getWindow().setGravity(Gravity.CENTER);
                image = loaddialog.findViewById(R.id.image);
                imageadd = loaddialog.findViewById(R.id.imageadd);
                pName = loaddialog.findViewById(R.id.pName);
                pDescription = loaddialog.findViewById(R.id.pDescription);
                pPrice = loaddialog.findViewById(R.id.pPrice);
                pStock = loaddialog.findViewById(R.id.pStock);
                pNameEditText = loaddialog.findViewById(R.id.pNameEditText);
                pDescriptionEditText = loaddialog.findViewById(R.id.pDescriptionEditText);
                pPriceEditText = loaddialog.findViewById(R.id.pPriceEditText);
                pStockEditText = loaddialog.findViewById(R.id.pStockEditText);
                cancelBtn = loaddialog.findViewById(R.id.cancelBtn);
                saveChangesBtn = loaddialog.findViewById(R.id.saveChangesBtn);
                imageErrTextView = loaddialog.findViewById(R.id.imageErrTextView);
                imageadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, 420);
                    }
                });
                saveChangesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(uploadTask != null && uploadTask.isInProgress()){
                            Toast.makeText(ProductsActivity.this, "Image Upload In Process!!!", Toast.LENGTH_SHORT).show();
                        } else {
                            validation("false");
                        }
                    }
                });

                loaddialog.show();
            }
        });



    }

    public boolean pnameValidation(){
        String name = pNameEditText.getText().toString().trim();
        if(name.isEmpty()){
            pName.setError("Name is Required!!!");
            return false;
        } else if(!Pattern.compile("^[a-zA-Z\\s]*$").matcher(name).matches()){
            pName.setError("Name In Only Text!!!");
            return false;
        } else {
            pName.setError(null);
            return true;
        }
    }
    public boolean pdescValidation(){
        String name = pDescriptionEditText.getText().toString().trim();
        if(name.isEmpty()){
            pDescription.setError("Description is Required!!!");
            return false;
        } else {
            pDescription.setError(null);
            return true;
        }
    }
    public boolean ppriceValidation(){
        String contact = pPriceEditText.getText().toString().trim();
        if(contact.isEmpty()){
            pPrice.setError("Price is Required!!!");
            return false;
        } else {
            pPrice.setError(null);
            return true;
        }
    }
    public boolean pstockValidation(){
        String contact = pStockEditText.getText().toString().trim();
        if(contact.isEmpty()){
            pStock.setError("Stock is Required!!!");
            return false;
        } else {
            pStock.setError(null);
            return true;
        }
    }
    public boolean imageValidation(){
        if(imageUri == null){
            imageErrTextView.setText("Product Image is Required!!!");
            imageErrTextView.setVisibility(View.VISIBLE);
            return false;
        } else {
            imageErrTextView.setText("");
            imageErrTextView.setVisibility(View.GONE);
            return true;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 420 && resultCode == RESULT_OK){
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void validation(String imageStatus) {
        boolean imageErr = false;
        if(imageStatus.equals("true")){
            imageErr = true;
        } else {
            imageErr = imageValidation();
        }
        if((pnameValidation() && pdescValidation() && ppriceValidation() && pstockValidation() && imageErr) == true){
            product();
        }
    }
    private void product() {
        if(imageUri != null){
            uploadTask = mStorage.child("Images/"+System.currentTimeMillis()+"."+getFileExtension(imageUri)).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String photoLink = uri.toString();
                            String uploadId = MainActivity.myRef.child("Plant").push().getKey();

                            HashMap<String, String> mydata = new HashMap<String, String>();
                            mydata.put("pImage", "" + photoLink);
                            mydata.put("pName", pNameEditText.getText().toString().trim());
                            mydata.put("pDesc", pDescriptionEditText.getText().toString().trim());
                            mydata.put("pStock", pStockEditText.getText().toString().trim());
                            mydata.put("pPrice", pPriceEditText.getText().toString().trim());
                            mydata.put("status", "1");
                            MainActivity.myRef.child("Products").child(uploadId).setValue(mydata);
                            loaddialog.dismiss();
                        }
                    });
                }
            });
        }

    }

}