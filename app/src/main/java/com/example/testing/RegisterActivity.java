package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.testing.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    EditText regName, regEmail, regPass, regConfirmPass;
    Button signUpButton;
    LinearLayout signIntext;
    String imgUri;
    String emailPattern = "\"[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+\"";






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();


        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        regName = findViewById(R.id.regName);
        regEmail = findViewById(R.id.regEmail);
        regPass = findViewById(R.id.regPass);
        regConfirmPass = findViewById(R.id.regConfirmPass);

        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setBackgroundColor(R.drawable.intro_signin);

        signIntext=findViewById(R.id.signInText);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = regName.getText().toString();
                String email = regEmail.getText().toString().trim();
                String password = regPass.getText().toString();
                String cpassword = regConfirmPass.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(cpassword)){
                    Toast.makeText(RegisterActivity.this, "Enter valid Detail", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(name)){
                    regEmail.setError("Invalid Email");
                    Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
                else if(password.length()<=6){
                    regPass.setError("Invalid password");
                    Toast.makeText(RegisterActivity.this, "Please enter more than 6 charachter", Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(cpassword)){
                    Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                DatabaseReference reference=database.getReference().child("users")

                                        .child(auth.getCurrentUser().getUid());
                                StorageReference storageReference; storage.getReference().child("upload").child(auth.getCurrentUser().getUid());

                                imgUri="https://firebasestorage.googleapis.com/v0/b/fir-f165a.appspot.com/o/picon.jpg?alt=media&token=a6cd0c38-e0fa-4eb4-a9c9-4ded0c4e36a8";

                                Users users= new Users(auth.getCurrentUser().getUid(),name,email,imgUri);

                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                                        }else{
                                            Toast.makeText(RegisterActivity.this, "Error in creating a new user", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }else{
                                Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        signIntext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}