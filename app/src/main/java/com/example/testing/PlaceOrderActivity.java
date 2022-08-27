package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PlaceOrderActivity extends AppCompatActivity implements PaymentResultListener {

    EditText shipName,shipPhone,shipAddress,shipCity;
    Button confirmOrder;
    FirebaseAuth auth;
    Intent intent;
    String totalAmount;
    TextView cartpricetotal;
    Toolbar cartToolbar;
    int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        shipAddress=findViewById(R.id.shipAddress);
        shipCity=findViewById(R.id.shipCity);
        shipName=findViewById(R.id.shipname);
        shipPhone=findViewById(R.id.shipphone);
        confirmOrder=findViewById(R.id.confirmOrder);
        cartpricetotal=findViewById(R.id.cartpricetotal);
        cartToolbar=findViewById(R.id.cart_toolbar);

        auth=FirebaseAuth.getInstance();

        cartToolbar.setBackgroundResource(R.drawable.bg_color);
        confirmOrder.setBackgroundColor(R.drawable.bg_color);

        intent=getIntent();
        totalAmount=intent.getStringExtra("totalAmount");

        cartpricetotal.setText(totalAmount);

        String sAmount="100";

        amount=Math.round(Float.parseFloat(sAmount)*100);

        confirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });
    }

    private void check(){
        if (TextUtils.isEmpty(shipName.getText().toString())){
            shipName.setError("Enter name");
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(shipPhone.getText().toString())){
            shipPhone.setError("Enter phone number");
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(shipAddress.getText().toString())){
            shipPhone.setError("Enter Address");
            Toast.makeText(this, "Please enter your Address", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(shipPhone.getText().toString())){
            shipPhone.setError("Enter City name");
            Toast.makeText(this, "Please enter City name", Toast.LENGTH_SHORT).show();
        }else {
            paymentFunc();
        }
    }

    private void paymentFunc(){

        Checkout checkout=new Checkout();

        checkout.setKeyID("rzp_test_1QYrnLZHsTMkgq");

        checkout.setImage(R.drawable.razorpaylogo);

        JSONObject object=new JSONObject();

        try {
            object.put("name","Andrid user");

            object.put("description","text payment");

            object.put("currency","INR");

            object.put("amount",amount);

            object.put("prefill.contact","9898989598");

            object.put("prefill.email","andriduser@rzp.com");

            checkout.open(PlaceOrderActivity.this,object);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void confirmOrderFunc(){
        String saveCurrentDate,saveCurrentTime;

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        final DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(auth.getCurrentUser().getUid()).child("History")
                .child(saveCurrentDate.replaceAll("/","_")+" "+saveCurrentTime);

        HashMap<String,Object> ordersMap=new HashMap<>();
        ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",shipName.getText().toString());
        ordersMap.put("phone",shipPhone.getText().toString());
        ordersMap.put("address",shipAddress.getText().toString());
        ordersMap.put("city",shipCity.getText().toString());
        ordersMap.put("date",saveCurrentDate);
        ordersMap.put("time",saveCurrentTime);

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("user View").child(auth.getCurrentUser().getUid())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(PlaceOrderActivity.this, "Your order has been placed succesfuly", Toast.LENGTH_SHORT).show();
                                        Intent intent1=new Intent(PlaceOrderActivity.this,HomeActivity.class);
                                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent1);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onPaymentSuccess(String s) {
        confirmOrderFunc();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("PaymentId");
        builder.setMessage(s);
        builder.show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "s", Toast.LENGTH_SHORT).show();

    }
}