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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText Email,password;
    private Button login,newuser;
    private TextView forgotpass;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Email=findViewById(R.id.loginmail);
        password=findViewById(R.id.loginpass);
        login=findViewById(R.id.login_button);
        newuser=findViewById(R.id.register_button);
        forgotpass=findViewById(R.id.forgot_pass);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));

        }

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,ForgetPass.class);
                startActivity(intent);
            }
        });
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity.this,signup.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=Email.getText().toString();
                String pass=password.getText().toString();
                if(mail.isEmpty() || pass.isEmpty())
                {
                    Toast.makeText(MainActivity.this, "Enter All Fields!!", Toast.LENGTH_SHORT).show();
                }else {
                            firebaseAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                         if(task.isSuccessful())
                                         {
                                             checkMailVerification();
                                         }else {
                                             Toast.makeText(MainActivity.this, "Account Doesn't Exist!", Toast.LENGTH_SHORT).show();
                                         }
                                }
                            });
                }
            }
        });
    }
    private void checkMailVerification()
    {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()==true)
        {
            Toast.makeText(MainActivity.this, "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));
        }else {
            Toast.makeText(MainActivity.this, "Verify Your Email First!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}