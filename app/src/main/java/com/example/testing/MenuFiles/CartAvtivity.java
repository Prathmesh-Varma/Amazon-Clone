package com.example.testing.MenuFiles;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.testing.PlaceOrderActivity;
import com.example.testing.R;
import com.example.testing.model.Cart;
import com.example.testing.viewholder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartAvtivity extends BaseActivity {
    private RecyclerView recyclerView;
    private Button nextBtn;
    RecyclerView.LayoutManager layoutManager;
    TextView totalprice;
    private int overallPrice;
    Toolbar cartToolbar;
    FirebaseAuth auth;
    LinearLayout dynamicContent;
    LinearLayout bottomNavBar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_cart_avtivity);

        dynamicContent=findViewById(R.id.dynamicContent);
        bottomNavBar=findViewById(R.id.bottomNavBar);
        View wizard= getLayoutInflater().inflate(R.layout.activity_cart_avtivity,null);
        dynamicContent.addView(wizard);

        RadioGroup rg=findViewById(R.id.radioGroup1);
        RadioButton rb=findViewById(R.id.bottom_home);

        rb.setBackgroundColor(R.color.item_selected);
        rb.setTextColor(Color.parseColor("#3F5185"));

        cartToolbar=findViewById(R.id.cart_toolbar);

        auth=FirebaseAuth.getInstance();

        recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        nextBtn=findViewById(R.id.next_button);
        totalprice=findViewById(R.id.totalprice);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if (totalprice.getText().toString().equals("₹0")|| totalprice.getText().toString().length()==0){
                  Toast.makeText(CartAvtivity.this, "Cannot place order with 0 item", Toast.LENGTH_SHORT).show();
              }else{
                  Intent intent=new Intent(CartAvtivity.this, PlaceOrderActivity.class);
                  intent.putExtra("totalAmount",totalprice.getText().toString());
                  startActivity(intent);
                  finish();
              }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options=new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("user View").child(auth.getCurrentUser().getUid())
                        .child("Products"),Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter=
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {
                        String name=model.getName().replaceAll("\n"," ");
                        holder.cartProductName.setText(name);
                        holder.cartProductPrice.setText(model.getPrice());

                        String intPrice= model.getPrice().replace("₹"," ");
                        overallPrice+=Integer.valueOf(intPrice);
                        totalprice.setText("₹"+String.valueOf(overallPrice));

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder=new AlertDialog.Builder(CartAvtivity.this);
                                builder.setTitle("Delete item");

                                builder.setMessage("Do you want to remove this product from cart?")
                                        .setCancelable(false)
                                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                cartListRef.child("User View")
                                                        .child(auth.getCurrentUser().getUid())
                                                        .child("Products")
                                                        .child(model.getPid())
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    String intPrice= model.getPrice().replace("₹"," ");
                                                                    overallPrice-=Integer.valueOf(intPrice);
                                                                    totalprice.setText("₹"+String.valueOf(overallPrice));
                                                                    Toast.makeText(CartAvtivity.this, "Item removed succesfully", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.cancel();
                                            }
                                        });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout,parent,false);
                        CartViewHolder holder=new CartViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
}