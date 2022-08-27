package com.example.testing.MenuFiles;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.testing.HomeActivity;
import com.example.testing.IntroActivity;
import com.example.testing.R;
import com.example.testing.ShowHistory;
import com.example.testing.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends BaseActivity {

    ImageView back, done, profileImg;
    EditText profileUsername;
    TextView profileEmail, profileLogout, profileHistory, displayUsername;

    String emailId;

    Toolbar profileToolbar;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri setImgUri;

    Dialog dialog;

    ProgressDialog progressDialog;

    LinearLayout dynamicContent;
    LinearLayout bottomNavBar;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
        dynamicContent=findViewById(R.id.dynamicContent);
        bottomNavBar=findViewById(R.id.bottomNavBar);
        View wizard= getLayoutInflater().inflate(R.layout.activity_profile,null);
        dynamicContent.addView(wizard);

        RadioGroup rg=findViewById(R.id.radioGroup1);
        RadioButton rb=findViewById(R.id.bottom_home);

        rb.setBackgroundColor(R.color.item_selected);
        rb.setTextColor(Color.parseColor("#3F5185"));

        profileToolbar=findViewById(R.id.profileToolbar);

        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setCancelable(false);

        back=findViewById(R.id.profileback);
        done=findViewById(R.id.done);
        profileImg=findViewById(R.id.profileImage);
        profileUsername=findViewById(R.id.profileusername);
        profileEmail=findViewById(R.id.profileEmail);
        profileLogout=findViewById(R.id.profileLogout);
        profileHistory=findViewById(R.id.profileHistory);
        displayUsername=findViewById(R.id.displayusername);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();

        DatabaseReference reference= database.getReference().child("users").child(auth.getCurrentUser().getUid());
        StorageReference storageReference=storage.getReference().child("upload").child(auth.getCurrentUser().getUid());

        progressDialog.show();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emailId=snapshot.child("email").getValue().toString();
                String name=snapshot.child("name").getValue().toString();
                String img=snapshot.child("imageUrl").getValue().toString();

                profileUsername.setText(name);
                displayUsername.setText(name);
                profileEmail.setText(emailId);
                Picasso.get().load(img).into(profileImg);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profileHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProfileActivity.this, ShowHistory.class);
                startActivity(intent);

            }
        });

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
            }
        });
        
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name=profileUsername.getText().toString();
                String email=profileEmail.getText().toString();
                displayUsername.setText(name);
                
                if (setImgUri!=null){
                    storageReference.putFile(setImgUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String finalImageUri=uri.toString();
                                    Users users=new Users(auth.getCurrentUser().getUid(),name,email,finalImageUri);
                                    Log.i("Uri",storageReference.getDownloadUrl().toString());
                                    
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(ProfileActivity.this, "Chane Saved", Toast.LENGTH_SHORT).show();
                                            }else {
                                                progressDialog.dismiss();
                                                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }else {
                    String imageUri="https://firebasestorage.googleapis.com/v0/b/fir-f165a.appspot.com/o/picon.jpg?alt=media&token=a6cd0c38-e0fa-4eb4-a9c9-4ded0c4e36a8";
                    Users users=new Users(auth.getCurrentUser().getUid(),name,email,imageUri);

                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Chane Saved", Toast.LENGTH_SHORT).show();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            //check whtsa
        });
        profileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new Dialog(ProfileActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_layout);

                TextView yesButton,noButton;
                yesButton=dialog.findViewById(R.id.yesButton);
                noButton=dialog.findViewById(R.id.noButton);

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(ProfileActivity.this, IntroActivity.class));
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        setImgUri=data.getData();

        if (requestCode==1 && resultCode==RESULT_OK && data!=null){
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(this.getContentResolver(),setImgUri);
                profileImg.setImageBitmap(bitmap);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}