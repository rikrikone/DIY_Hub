package com.example.diyhub.Fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.diyhub.MESSAGES.ChatPage;
import com.example.diyhub.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailToBookCustomizationPage extends AppCompatActivity {


    ImageView standardPageImage;

    String position;
    ArrayList<OrdersList> list;
    Spinner bookingAddressSpinner;
    Spinner customerRequestSpinner;
    Spinner orderTrackerSpinner;
    ArrayAdapter bookingAdapter;
    ArrayAdapter customerAdapter;
    ArrayAdapter orderAdapter;
    ArrayList<String> bookingAddressList;
    ArrayList<String> customerRequestList;
    ArrayList<String> orderTrackerList;
    EditText itemCode,itemName,quantity,orderType,buyerName,orderDate;
    Button paymentStatus;
    ImageView buyerImage;
    ImageView contactBuyer;

    ImageView backButton;
    ImageButton copyButton;

    ImageView moveToToReceive;
    CardView notif;


    EditText riderName, plateNumber;

    ImageView moveToCustom;

    List<OrderCustomizationsSpecs> specsList;

    CardView customerReqNotif;

    Button viewPriceLiquidationButton;
    int pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_to_book_customization_page);

        standardPageImage = findViewById(R.id.CustomPageImageToBook);
        bookingAddressSpinner = findViewById(R.id.bookingAddressSpinnerCustomToBook);
        customerRequestSpinner = findViewById(R.id.customerRequestSpinnerCustomToBook);
        itemCode = findViewById(R.id.itemCodeTxtCustomToBook);
        itemName = findViewById(R.id.itemNameTxtCustomToBook);
        quantity = findViewById(R.id.quantityTxtCustomToBook);
        orderType = findViewById(R.id.orderTypeTxtCustomToBook);
        buyerName = findViewById(R.id.buyerNameTxtCustomToBook);
        paymentStatus = findViewById(R.id.paymentStatusTxtCustomToBook);
        orderDate = findViewById(R.id.orderDateTxtCustomToBook);
        buyerImage = findViewById(R.id.buyerImageCustomToBook);
        contactBuyer = findViewById(R.id.contactBuyerButtonCustomToBook);
        backButton = findViewById(R.id.backButtonToBookCustom);
        copyButton = findViewById(R.id.copyButtonCustomPageToBook);
        moveToToReceive = findViewById(R.id.moveToToReceiveCustom);
        notif = findViewById(R.id.notificationNumberContainerToBookCustom);
        riderName = findViewById(R.id.riderNameTxtCustom);
        plateNumber = findViewById(R.id.plateNumberTxtCustom);
        moveToCustom = findViewById(R.id.moveToCustomizationButtonToBook);
        customerReqNotif = findViewById(R.id.notificationNumberContainerToBookCustomCustomerRequest);
        viewPriceLiquidationButton = findViewById(R.id.viewPriceLiquidationToBookCustom);





        Bundle extras = getIntent().getExtras();
        if(extras != null){
            position = extras.getString("position");
            list = extras.getParcelableArrayList("list");
        }

        pos = Integer.parseInt(position);

        viewPriceLiquidationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        OrderDetailToBookCustomizationPage.this, R.style.BottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet, (LinearLayout)findViewById(R.id.bottomSheetContainer));
                bottomSheetView.findViewById(R.id.confirmButtonPriceLiquidation).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                TextView merchTotal = (TextView) bottomSheetView.findViewById(R.id.merchSubtotalTxt);
                TextView shippingTotal = (TextView) bottomSheetView.findViewById(R.id.shippingSubTotalTxt);
                TextView addfees = (TextView) bottomSheetView.findViewById(R.id.additionalFeesTxt);
                TextView quantity = (TextView) bottomSheetView.findViewById(R.id.totalNumOfItemsTxt);
                TextView totalpayment = (TextView) bottomSheetView.findViewById(R.id.totalPaymentTxt);
                merchTotal.setText("???"+String.valueOf(list.get(pos).getOrderProductPrice()));
                shippingTotal.setText("???"+String.valueOf(list.get(pos).getOrderShippingFee()));
                addfees.setText("???"+String.valueOf(list.get(pos).getOrderAdditionalFee()));
                quantity.setText("x"+String.valueOf(list.get(pos).getOrderQuantity()));
                totalpayment.setText("???"+String.valueOf(list.get(pos).getOrderTotalPayment()));
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
            }
        });

        moveToCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailToBookCustomizationPage.this, OrderDetailCustomPagePreview.class);
                intent.putExtra("ProductID", list.get(pos).getProductID());
                intent.putExtra("ProductImage", list.get(pos).getOrderProductImage());
                startActivity(intent);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderDetailToBookCustomizationPage.this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });



        //Display All Data (OrdersList)
        Glide.with(this).load(list.get(pos).getOrderProductImage()).into(standardPageImage);
        Glide.with(this).load(list.get(pos).getBuyerImage()).into(buyerImage);
        itemCode.setText(list.get(pos).getItemCode());
        itemName.setText(list.get(pos).getOrderProductName());
        quantity.setText(""+list.get(pos).getOrderQuantity());
        orderType.setText(list.get(pos).getOrderType());
        buyerName.setText(list.get(pos).getBuyerName());



        contactBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatPage.class);
                startActivity(intent);            }
        });

        if(list.get(pos).getPaymentOption().equalsIgnoreCase("COD"))
        {
            paymentStatus.setBackgroundResource(R.drawable.custom_red);
            paymentStatus.setText(list.get(pos).getPaymentStatus());
        }
        else
        {
            paymentStatus.setBackgroundResource(R.drawable.custom_green);
            paymentStatus.setText(list.get(pos).getPaymentStatus());

        }
        orderDate.setText(list.get(pos).getOrderDate());





        bookingAddressList = new ArrayList<>();
        customerRequestList = new ArrayList<>();
        specsList = new ArrayList<>();

        bookingAddressList.add(0, "Booking Address");
        bookingAddressList.add(1, list.get(pos).getBookingAddress());
        customerRequestList.add(0, "Customer Request");

        if(bookingAddressList.size() > 1)
        {
            notif.setVisibility(View.VISIBLE);
        }

        //Booking Address Spinner
        bookingAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, bookingAddressList)
        {
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        bookingAddressSpinner.setAdapter(bookingAdapter);
        bookingAddressSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(), "Selected: "+item, Toast.LENGTH_SHORT).show();
                    notif.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        moveToToReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String OrderProductName, OrderQuantity;
                String OrderProductImage,OrderID,OrderType,PaymentOption;
                String ItemCode, BuyerName, PaymentStatus, OrderDate;
                String BuyerImage;

                if(riderName.getText().toString().trim().isEmpty())
                {
                    riderName.setError("Required");
                    riderName.requestFocus();
                    return;
                }
                else if(plateNumber.getText().toString().trim().isEmpty())
                {
                    plateNumber.setError("Required");
                    riderName.requestFocus();
                    return;
                }
                else {

                    //Seller
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("OrderStatus", "To Receive");
                    hashMap.put("RiderName", riderName.getText().toString().trim());
                    hashMap.put("PlateNumber", plateNumber.getText().toString().trim());
                    reference.child("Orders").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(hashMap);

                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> map = new HashMap<>();
                    map.put("IsSeen","true");
                    map.put("NotifHeader","To Receive");
                    reference1.child("Notifications").child(user.getUid()).child(list.get(pos).getOrderID()).updateChildren(map);

                    //Buyer
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
                    HashMap<String, Object> hashMap2 = new HashMap<>();
                    hashMap2.put("OrderStatus", "To Receive");
                    hashMap2.put("RiderName", riderName.getText().toString().trim());
                    hashMap2.put("PlateNumber", plateNumber.getText().toString().trim());
                    reference2.child("BuyerPurchase").child(list.get(pos).getBuyerID()).child(list.get(pos).getOrderID()).updateChildren(hashMap2);

                    DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference();
                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("IsSeen","true");
                    map3.put("NotifHeader","To Receive");
                    reference3.child("Notifications").child(list.get(pos).getBuyerID()).child(list.get(pos).getOrderID()).updateChildren(map3);

                    Toast.makeText(OrderDetailToBookCustomizationPage.this, "Order is moved to TO RECEIVE", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        showDataCat1();

        customerRequestSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0)
                {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(), "Selected: "+item, Toast.LENGTH_SHORT).show();
                    customerReqNotif.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void showDataCat1()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders").child(user.getUid()).child(list.get(pos).getProductID()).child("OrderCustomizationsSpecs").child("Category-Textbox");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    OrderCustomListSeller addCustomSpecsSellerList = snapshot.getValue(OrderCustomListSeller.class);
                    customerRequestList.add(addCustomSpecsSellerList.getSpecsName());
                }
                customerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, customerRequestList)
                {
                    @Override
                    public boolean isEnabled(int position){
                        if(position == 0)
                        {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        }
                        else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                customerRequestSpinner.setAdapter(customerAdapter);
                if(customerRequestList.size() > 1)
                {
                    customerReqNotif.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}