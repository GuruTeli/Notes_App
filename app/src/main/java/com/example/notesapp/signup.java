package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {

    private EditText Email,password,mconfirmpass;
    private Button signup1;
    private TextView gobacktologin;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        Email=findViewById(R.id.signmail);
        password=findViewById(R.id.signpass);
        mconfirmpass=findViewById(R.id.confirmpass);
        signup1=findViewById(R.id.sign_button);
        gobacktologin=findViewById(R.id.Go_login);
        firebaseAuth=FirebaseAuth.getInstance();
        gobacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(signup.this,MainActivity.class);
                startActivity(intent);
            }
        });
        signup1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=Email.getText().toString();
                String pass=password.getText().toString();
                String cpass=mconfirmpass.getText().toString();

                        if(!mail.isEmpty() && !pass.isEmpty() && !cpass.isEmpty())
                        {
                                 if(pass.equals(cpass) )
                                 {
                                     if(pass.length()<7 || cpass.length()<7)
                                     {
                                         Toast.makeText(signup.this, "Not A Strong Password!!", Toast.LENGTH_SHORT).show();
                                     }else {
                                                   firebaseAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<AuthResult> task) {
                                                                  if(task.isSuccessful())
                                                                  {
                                                                      Toast.makeText(signup.this, "Registration Successful !!", Toast.LENGTH_SHORT).show();
                                                                      sendEmailVerification1();
                                                                  }else
                                                                      Toast.makeText(signup.this, "Failed To Register !!", Toast.LENGTH_SHORT).show();
                                                       }
                                                   });
                                         }
                                 }else {
                                     Toast.makeText(signup.this, "Passwords Not Matched !!", Toast.LENGTH_SHORT).show();
                                 }
                        }else {
                            Toast.makeText(signup.this, "Fill All The Field!!", Toast.LENGTH_SHORT).show();
                        }

            }
        });
    }
    //Email Verification Function
    private void sendEmailVerification1()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(signup.this, "Verification Email is Sent,Verify and Log In Again !!", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(signup.this,MainActivity.class));
                }
            });
        }else Toast.makeText(signup.this, "Failed To Send Verification Email !!", Toast.LENGTH_SHORT).show();

    }
}

