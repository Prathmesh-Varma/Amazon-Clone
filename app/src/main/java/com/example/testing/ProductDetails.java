package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.testing.MenuFiles.SearchActivity;
import com.example.testing.model.AddProductModel;
import com.example.testing.viewholder.RelatedProductsHolder;
import com.example.testing.viewholder.ViewProductHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {

    Intent intent;
    ImageView productImg;
    TextView productName,productCategory,productPrice,productDesc;
    Button order;
    Toolbar detailsToolbar;

    FirebaseAuth auth;
    String uniqueid,relCategory,name,checkname;
    RecyclerView related_prod_list;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        auth=FirebaseAuth.getInstance();

        productImg=findViewById(R.id.product_img);
        productName=findViewById(R.id.product_Name);
        productPrice=findViewById(R.id.product_Price);
        productCategory=findViewById(R.id.product_category);
        productDesc=findViewById(R.id.product_desc);

        detailsToolbar = findViewById(R.id.detailsToolbar);

        back=findViewById(R.id.product_back);
        order=findViewById(R.id.order);

        related_prod_list=findViewById(R.id.related_prod_list);
        related_prod_list.setLayoutManager(new LinearLayoutManager(ProductDetails.this,
                LinearLayoutManager.HORIZONTAL,true));

        intent=getIntent();

        productCategory.setText(intent.getStringExtra("category"));

        int id=intent.getIntExtra("id",1);
        uniqueid=intent.getStringExtra("uniqueid");

        relCategory=productCategory.getText().toString();

        if (id==1){
            productName.setText(intent.getStringExtra("name").replaceAll("\n"," "));
            uniqueid=uniqueid.replaceAll("\n"," ");
            productPrice.setText("Rs 3000");
            productDesc.setText("bohot acha hai");
            productImg.setImageResource(R.drawable.shoes1);
        }else if (id==2){
            productName.setText(intent.getStringExtra("name").replaceAll("\n"," "));
            uniqueid=uniqueid.replaceAll("\n"," ");
            productPrice.setText("Rs 9500");
            productDesc.setText("bohot acha hai");
            productImg.setImageResource(R.drawable.shoes2);
        }else if (id==3){
            productName.setText(intent.getStringExtra("name").replaceAll("\n"," "));
            productPrice.setText(intent.getStringExtra("pprice"));
            productDesc.setText(intent.getStringExtra("description"));
            String img=intent.getStringExtra("imagename");
            productImg.setImageResource(this.getResources().getIdentifier(img,"drawable",this.getPackageName()));

        }else{
            productName.setText(intent.getStringExtra("addProdname"));
            productPrice.setText(intent.getStringExtra("addProdPrice"));
            productCategory.setText(intent.getStringExtra("addprodcategory"));
            productDesc.setText(intent.getStringExtra("addproddesc"));
            String imgUri=intent.getStringExtra("img");
            Picasso.get().load(imgUri).into(productImg);
        }

        back.bringToFront();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProductDetails.this,HomeActivity.class));
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addingToCartList();
            }
        });

        onStart();
    }
    private void addingToCartList(){
        String saveCurrentDate,saveCurrentTime;

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("Cart list");

        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("pid",uniqueid);
        cartMap.put("name",productName.getText().toString());
        cartMap.put("price",productPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);

        cartListRef.child("User view").child(auth.getCurrentUser().getUid()).child("products")
                .child(uniqueid).updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ProductDetails.this, "added to cart", Toast.LENGTH_SHORT).show();
                            Intent intentcart=new Intent(ProductDetails.this,HomeActivity.class);
                            startActivity(intentcart);
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference prodListRef=FirebaseDatabase.getInstance().getReference().child("View All")
                .child("user View").child("Products");

        FirebaseRecyclerOptions<AddProductModel> options=new FirebaseRecyclerOptions.Builder<AddProductModel>()
                .setQuery(prodListRef.orderByChild("category").startAfter(relCategory),AddProductModel.class).build();

        FirebaseRecyclerAdapter<AddProductModel, RelatedProductsHolder> adapter=
              new FirebaseRecyclerAdapter<AddProductModel, RelatedProductsHolder>(options) {
                  @Override
                  protected void onBindViewHolder(@NonNull RelatedProductsHolder holder, int position, @NonNull AddProductModel model) {
                      name=model.getName();
                      String price=model.getPrice();
                      String imgUri=model.getImg();

                      holder.relatedProdName.setText(name);
                      holder.relatedProdPrice.setText(price);
                      Picasso.get().load(imgUri).into(holder.relatedProdImage);

                      holder.itemView.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              Intent intent = new Intent(ProductDetails.this, ProductDetails.class);
                              intent.putExtra("id", 4);
                              intent.putExtra("uniqueid", name);
                              intent.putExtra("addProdName", name);
                              intent.putExtra("addProdPrice", price);
                              intent.putExtra("addproddesc", model.getDescription());
                              intent.putExtra("addprodcategory", model.getCategory());
                              intent.putExtra("img", imgUri);
                              startActivity(intent);
                          }
                      });

                  }

                  @NonNull
                  @Override
                  public RelatedProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.related_prod_adapter, parent, false);
                      RelatedProductsHolder holder=new RelatedProductsHolder(view);
                      return holder;
                  }
              };
        related_prod_list.setAdapter(adapter);
        adapter.startListening();
    }
}