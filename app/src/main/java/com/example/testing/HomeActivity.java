package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.testing.MenuFiles.BaseActivity;
import com.example.testing.MenuFiles.CartAvtivity;
import com.example.testing.MenuFiles.SearchActivity;
import com.example.testing.constant.Constant;
import com.example.testing.model.Product;
import com.example.testing.viewholder.ProductAdapter;
import com.example.testing.viewholder.SliderAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class HomeActivity extends BaseActivity {
    Toolbar toolbar;
    SliderView sliderView;
    int[] images= {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four};
    CardView shoes1,shoes2,shoes3,shoes4,shoes5;
    TextView oddshoename,oddshoeprice,evenshoename,evenshoeprice,viewAll;
    LinearLayout dynamicContent;
    LinearLayout bottomNavBar;

    public static ImageView home_cart;

    Intent intentcart;
    String getcartupdate;

    FirebaseStorage storage;
    StorageReference storageReference;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home);
        dynamicContent=findViewById(R.id.dynamicContent);
        bottomNavBar=findViewById(R.id.bottomNavBar);
        View wizard= getLayoutInflater().inflate(R.layout.activity_home,null);
        dynamicContent.addView(wizard);

        RadioGroup rg=findViewById(R.id.radioGroup1);
        RadioButton rb=findViewById(R.id.bottom_home);

        rb.setBackgroundColor(R.color.item_selected);
       rb.setTextColor(Color.parseColor("#3F5185"));

        sliderView=findViewById(R.id.image_slider);
        SliderAdapter sliderAdapter=new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        toolbar=findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(R.drawable.bg_color);

        shoes1=findViewById(R.id.shoes1);
        shoes2=findViewById(R.id.shoes2);
        shoes3=findViewById(R.id.shoes3);
        shoes4=findViewById(R.id.shoes4);
        shoes5=findViewById(R.id.shoes5);

        oddshoename=findViewById(R.id.oddshoename);
        oddshoeprice=findViewById(R.id.oddshoeprice);
        evenshoename=findViewById(R.id.evenshoename);
        evenshoeprice=findViewById(R.id.evenshoeprice);

        viewAll=findViewById(R.id.viewAllProducts);
        home_cart=findViewById(R.id.home_cart);

        intentcart=getIntent();
        if (intentcart.getStringExtra("cartadd")!=null&& intentcart.getStringExtra("cartadd").equals("yes")){
            home_cart.setImageResource(R.drawable.cart_notify);
        }else if(intentcart.getStringExtra("cartadd")!=null && intentcart.getStringExtra("caradd").equals("no")){
            home_cart.setImageResource(R.drawable.cart);
        }else{
            home_cart.setImageResource(R.drawable.cart);
        }

        storage=FirebaseStorage.getInstance();

        shoes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActivity.this,ProductDetails.class);
                i.putExtra("name",oddshoename.getText().toString());
                i.putExtra("category","men's running shoes");
                i.putExtra("price",oddshoeprice.getText().toString());
                i.putExtra("uniqueid",oddshoename.getText().toString());
                i.putExtra("id",1);
                startActivity(i);

            }
        });

        shoes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActivity.this,ProductDetails.class);
                i.putExtra("name",evenshoename.getText().toString());
                i.putExtra("category","men's  shoes");
                i.putExtra("price",evenshoeprice.getText().toString());
                i.putExtra("uniqueid",evenshoename.getText().toString());
                i.putExtra("id",2);
                startActivity(i);
            }
        });

        shoes3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActivity.this,ProductDetails.class);
                i.putExtra("name",oddshoename.getText().toString());
                i.putExtra("category","men's running shoes");
                i.putExtra("price",oddshoeprice.getText().toString());
                i.putExtra("uniqueid",oddshoename.getText().toString());
                i.putExtra("id",1);
                startActivity(i);
            }
        });

        shoes4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActivity.this,ProductDetails.class);
                i.putExtra("name",evenshoename.getText().toString());
                i.putExtra("category","men's  shoes");
                i.putExtra("price",evenshoeprice.getText().toString());
                i.putExtra("uniqueid",evenshoename.getText().toString());
                i.putExtra("id",2);
                startActivity(i);
            }
        });

        shoes5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HomeActivity.this,ProductDetails.class);
                i.putExtra("name",oddshoename.getText().toString());
                i.putExtra("category","men's running shoes");
                i.putExtra("price",oddshoeprice.getText().toString());
                i.putExtra("uniqueid",oddshoename.getText().toString());
                i.putExtra("id",1);
                startActivity(i);
            }
        });

        ListView lvProducts =findViewById(R.id.productlist);

        ProductAdapter productAdapter=new ProductAdapter(this);
        productAdapter.updateProduct(Constant.PRODUCT_LIST);
        lvProducts.setAdapter(productAdapter);
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Product product=Constant.PRODUCT_LIST.get(i);
                Intent intent=new Intent(HomeActivity.this,ProductDetails.class);
                intent.putExtra("id",3);
                intent.putExtra("uniqueid",product.getpName());
                intent.putExtra("name",product.getpName());
                intent.putExtra("Description",product.getpDescription());
                intent.putExtra("category","smartphone");
                intent.putExtra("pprice",Constant.CURRENCY+String.valueOf(product.getpPrice().setScale(0, BigDecimal.ROUND_HALF_UP)));
                intent.putExtra("imagename",product.getpImagename());
                startActivity(intent) ;
            }






        });

        home_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, CartAvtivity.class);
                startActivity(intent);
            }
        });

        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    private void addingToProductList(){
        String saveCurrentDate,saveCurrentTime;
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        DatabaseReference prodListRef= FirebaseDatabase.getInstance().getReference().child("View All");
        String name="Redme note 11";

        final HashMap<String,Object> prodMap=new HashMap<>();
        prodMap.put("pid",name);
        prodMap.put("name",name);
        prodMap.put("price","16999");
        prodMap.put("category","Smartphone");
        prodMap.put("description","bohot acha hai");
        prodMap.put("time",saveCurrentTime);
        prodMap.put("date",saveCurrentDate);


        prodListRef.child("User View").child("Products")
                .child(name).updateChildren(prodMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Log.i("task","sauccesful");
                       }
                    }
                });

    }
}