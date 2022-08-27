package com.example.testing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth auth;
    EditText emailEditText, passwordEditText;
    Button signInButton;
    LinearLayout signUpText;
    String emailPattern = "\"[a-zA-Z0-9._-]+@[a-z]+\\\\.+[a-z]+\"";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth=FirebaseAuth.getInstance();

        emailEditText=findViewById(R.id.loginEmail);
        passwordEditText=findViewById(R.id.loginPassword);
        signInButton=findViewById(R.id.signInButton);
        signInButton.setBackgroundColor(R.drawable.intro_signin);
        signUpText=findViewById(R.id.signUpText);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=emailEditText.getText().toString();
                String password=passwordEditText.getText().toString();

                if(TextUtils.isEmpty(email)|| TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Plaese enter details", Toast.LENGTH_SHORT).show();
//                }else if(TextUtils.isEmpty(email)){
//                    emailEditText.setError("Invalid email");
//                    Toast.makeText(LoginActivity.this, "invalid email", Toast.LENGTH_SHORT).show();
                }else if(password.length()<=6){
                    passwordEditText.setError("Invalid Password");
                    Toast.makeText(LoginActivity.this, "Please enter more than 6 characterx ", Toast.LENGTH_SHORT).show();
                }else{
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                            }else{
                                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}