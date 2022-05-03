package com.example.diyhub.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diyhub.Notifications.UserNotif;
import com.example.diyhub.R;
import com.example.diyhub.SellerHomePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class UpdateProduct extends AppCompatActivity {

    EditText name,quantity,stocks;
    String prodName,prodQuant,prodStocks,prodID;
    Button updateProd;
    ProgressDialog progressDialog;
    FirebaseFirestore dbFirestore;
    String playImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fillust58-7479-01-removebg-preview.png?alt=media&token=63a829e1-660e-47e6-9b26-dc66d8eaac48";
    String pauseImageStatus = "https://firebasestorage.googleapis.com/v0/b/diy-hub-847fb.appspot.com/o/PRODUCTSTATUS%2Fpause__video__stop-removebg-preview.png?alt=media&token=dc125631-d226-41e1-91ac-6abf0b97c18d";

    String productID;
    String prodImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        name = findViewById(R.id.updateProdName);
        quantity = findViewById(R.id.updateProdQuantity);
        stocks = findViewById(R.id.updateProdStocks);
        updateProd = findViewById(R.id.updateProduct);
        progressDialog = new ProgressDialog(this);
        dbFirestore = FirebaseFirestore.getInstance();


        Bundle extra = getIntent().getExtras();
        if(extra != null)
        {
            prodName = extra.getString("ProductName");
            prodQuant = extra.getString("ProductQuantity");
            prodStocks = extra.getString("ProductStocks");
            prodID = extra.getString("ProductID");
            prodImage = extra.getString("ProductImage");
        }

        productID = prodID;
        name.setText(prodName);
        quantity.setText(prodQuant);
        stocks.setText(prodStocks);

        updateProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().trim().isEmpty())
                {
                    name.setError("Product Name is Required");
                    name.requestFocus();
                    return;
                }
                else if(quantity.getText().toString().trim().isEmpty())
                {
                    quantity.setError("Product Quantity is Required");
                    quantity.requestFocus();
                    return;
                }
                else if(stocks.getText().toString().trim().isEmpty())
                {
                    stocks.setError("Product Stocks is Required");
                    stocks.requestFocus();
                    return;
                }
                else {
                    String id = prodID;
                    String name1 = name.getText().toString().trim();
                    String quan = quantity.getText().toString().trim();
                    String sto = stocks.getText().toString();

                    updateData(id,name1,quan,sto);
                }
            }
        });




    }

    private void updateData(String id1, String name1, String quan1, String stocks1)
    {
        int q = Integer.parseInt(quan1);
        int s = Integer.parseInt(stocks1);
        if(q > s)
        {
            quantity.setError("Product Quantity should not be greater than Product Stocks");
            quantity.requestFocus();
            return;
        }
        progressDialog.setTitle("Updating Product");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if(q == s)
        {


            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

            DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("SellerProducts").child(firebaseUser.getUid()).child(id1);
            HashMap<String, Object> hashMap4 = new HashMap<>();
            hashMap4.put("ProductName", name1);
            hashMap4.put("ProductQuantity", quan1);
            hashMap4.put("ProductStocks", stocks1);
            hashMap4.put("ProductImage", prodImage);
            hashMap4.put("ProductID", id1);
            hashMap4.put("ProductStatus", "Hold");
            hashMap4.put("ProductStatusImage", playImageStatus);
            reference4.updateChildren(hashMap4);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        UserNotif user = snapshot.getValue(UserNotif.class);

                        assert user != null;
                        assert firebaseUser != null;

                        if(!user.getId().equals(firebaseUser.getUid()))
                        {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("ProductName", name1);
                            hashMap.put("ProductQuantity", quan1);
                            hashMap.put("ProductStocks", stocks1);
                            hashMap.put("ProductImage", prodImage);
                            hashMap.put("ProductID", id1);
                            hashMap.put("ProductStatusImage", playImageStatus);
                            hashMap.put("ProductStatus", "Hold");
                            reference.child("RestockNotification").child(user.getId()).child(id1).setValue(hashMap);

                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RestockNotification").child(user.getId()).child(id1);
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("ProductName", name1);
                            hashMap1.put("ProductQuantity", quan1);
                            hashMap1.put("ProductStocks", stocks1);
                            hashMap1.put("ProductStatus", "Hold");
                            hashMap1.put("ProductStatusImage", playImageStatus);
                            reference2.updateChildren(hashMap1);
                            progressDialog.dismiss();

                            finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
       else if(q < s)
        {

            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");


            DatabaseReference reference4 = FirebaseDatabase.getInstance().getReference("SellerProducts").child(firebaseUser.getUid()).child(id1);
            HashMap<String, Object> hashMap4 = new HashMap<>();
            hashMap4.put("ProductName", name1);
            hashMap4.put("ProductQuantity", quan1);
            hashMap4.put("ProductStocks", stocks1);
            hashMap4.put("ProductImage", prodImage);
            hashMap4.put("ProductID", id1);
            hashMap4.put("ProductStatus", "Active");
            hashMap4.put("ProductStatusImage", pauseImageStatus);
            reference4.updateChildren(hashMap4);

            Toast.makeText(UpdateProduct.this, "Product Updated Successfully", Toast.LENGTH_SHORT).show();

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        UserNotif user = snapshot.getValue(UserNotif.class);

                        assert user != null;
                        assert firebaseUser != null;

                        if(!user.getId().equals(firebaseUser.getUid()))
                        {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("ProductName", name1);
                            hashMap.put("ProductQuantity", quan1);
                            hashMap.put("ProductStocks", stocks1);
                            hashMap.put("ProductImage", prodImage);
                            hashMap.put("ProductID", id1);
                            hashMap.put("ProductStatusImage", pauseImageStatus);
                            hashMap.put("ProductStatus", "Active");
                            reference.child("RestockNotification").child(user.getId()).child(id1).setValue(hashMap);


                            DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("RestockNotification").child(user.getId()).child(id1);
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("ProductName", name1);
                            hashMap1.put("ProductQuantity", quan1);
                            hashMap1.put("ProductStocks", stocks1);
                            hashMap1.put("ProductStatus", "Active");
                            hashMap1.put("ProductStatusImage", playImageStatus);
                            reference2.updateChildren(hashMap1);

                            progressDialog.dismiss();

                            finish();


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


}