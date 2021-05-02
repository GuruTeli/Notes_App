package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPass extends AppCompatActivity {

     private EditText recoverPass;
     private TextView gobacklogin;
     private Button recover;
     private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        getSupportActionBar().hide();
        recover=findViewById(R.id.recover_button);
        gobacklogin=findViewById(R.id.Goback_frecover);
        recoverPass=findViewById(R.id.Pass_recover);
        firebaseAuth=FirebaseAuth.getInstance();
        gobacklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ForgetPass.this,MainActivity.class);
                startActivity(intent);
            }
        });
        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String email=recoverPass.getText().toString();
                 if(email.isEmpty())
                 {
                     Toast.makeText(ForgetPass.this, "Enter Email First!!", Toast.LENGTH_SHORT).show();
                 }else {
                              firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                                 if(task.isSuccessful())
                                                 {
                                                     Toast.makeText(ForgetPass.this, "Email Sent! Recover Your Password ! ", Toast.LENGTH_SHORT).show();
                                                     finish();
                                                     startActivity(new Intent(ForgetPass.this,MainActivity.class));
                                                 }else
                                                     Toast.makeText(ForgetPass.this, "Email is Wrong Or Account Not Exist!", Toast.LENGTH_SHORT).show();
                                  }
                              });
                 }
            }
        });

    }
}