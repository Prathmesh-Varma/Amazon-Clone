package com.example.testing.MenuFiles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.testing.HomeActivity;
import com.example.testing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddProduct extends BaseActivity {
    ImageView addProdImg,addProductBack;
    EditText addProdName,addProdPrice,addProdCategory,addProdDesc;
    TextView confirmAdd;
    FirebaseStorage storage;
    StorageReference storageReference;
    Uri setImageUri;
    String finalImageUri;
    Toolbar addtoolbar;

    LinearLayout dynamicContent;
    LinearLayout bottomNavBar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_product);

        dynamicContent=findViewById(R.id.dynamicContent);
        bottomNavBar=findViewById(R.id.bottomNavBar);
        View wizard= getLayoutInflater().inflate(R.layout.activity_add_product,null);
        dynamicContent.addView(wizard);

        RadioGroup rg=findViewById(R.id.radioGroup1);
        RadioButton rb=findViewById(R.id.bottom_home);

        rb.setBackgroundColor(R.color.item_selected);
        rb.setTextColor(Color.parseColor("#3F5185"));

        addProdImg=findViewById(R.id.addPraductImg);
        addProdName=findViewById(R.id.addProductName);
        addProdPrice=findViewById(R.id.addProductPrice);
        addProdDesc=findViewById(R.id.addProductDesc);
        addProdCategory=findViewById(R.id.addProductCategory);
        confirmAdd=findViewById(R.id.confirmAddProd);
        addProductBack=findViewById(R.id.addProductBack);
        addtoolbar=findViewById(R.id.addtoolbar);

        addtoolbar.setBackgroundResource(R.drawable.bg_color);

        storage=FirebaseStorage.getInstance();

        addProdImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });

        confirmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=addProdName.getText().toString();
                String price=addProdPrice.getText().toString();
                String Desc=addProdDesc.getText().toString();
                String Category=addProdCategory.getText().toString();

                if (TextUtils.isEmpty(name)){
                    addProdName.setError("please enter name");
                    Toast.makeText(AddProduct.this, "please fill all the details", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(price)){
                    addProdPrice.setError("please enter price");
                    Toast.makeText(AddProduct.this, "please enter price", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(Desc)){
                    addProdDesc.setError("please give description");
                    Toast.makeText(AddProduct.this, "Please give description", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(Category)){
                    addProdCategory.setError("please enter category");
                    Toast.makeText(AddProduct.this, "Please enter category", Toast.LENGTH_SHORT).show();
                }else if (addProdImg==null||addProdImg.getDrawable().equals(R.drawable.add)){
                    Toast.makeText(AddProduct.this, "Please choose correct image", Toast.LENGTH_SHORT).show();
                }else{
                    storageReference= storage.getReference().child("products").child(name);
                    addingToProdList();
                }
            }
        });

        addProductBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(AddProduct.this, HomeActivity.class);
                startActivity(intent);
            }
        });

    }
    private void addingToProdList(){
        String saveCurrentDate,saveCurrentTime;

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        DatabaseReference productListRef= FirebaseDatabase.getInstance().getReference().child("ViewAll");

        final HashMap<String,Object> prodMap= new HashMap<>();
        prodMap.put("id",addProdName.getText().toString());
        prodMap.put("name",addProdName.getText().toString());
        prodMap.put("price",addProdPrice.getText().toString());
        prodMap.put("Category",addProdCategory.getText().toString());
        prodMap.put("Description",addProdDesc.getText().toString());
        prodMap.put("date",currentDate);
        prodMap.put("time",currentTime);

        if(setImageUri!=null){
            storageReference.putFile(setImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                  storageReference.getDownloadUrl() .addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                          finalImageUri=uri.toString();
                          Log.i("image","added to succesfully");
                          prodMap.put("img",finalImageUri);

                          productListRef.child("UserView").child("Products")
                                  .child(addProdName.getText().toString()).updateChildren(prodMap)
                                  .addOnCompleteListener(new OnCompleteListener<Void>() {
                                      @Override
                                      public void onComplete(@NonNull Task<Void> task) {
                                          if (task.isSuccessful()){
                                              Toast.makeText(AddProduct.this, "Product added succesfully after verification", Toast.LENGTH_SHORT).show();
                                              Intent intent=new Intent(AddProduct.this,HomeActivity.class);
                                              startActivity(intent);
                                              finish();
                                          }
                                      }
                                  });
                      }
                  }) ;
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        setImageUri=data.getData();

        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),setImageUri);
                addProdImg.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}