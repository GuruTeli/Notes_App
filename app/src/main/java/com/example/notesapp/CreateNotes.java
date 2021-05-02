package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateNotes extends AppCompatActivity {

     private EditText notetitle,notecontent;
     private FloatingActionButton savenotecontent;
     private FirebaseAuth firebaseAuth;
     private FirebaseUser firebaseUser;
     private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);
        notetitle=findViewById(R.id.createnotetitle);
        notecontent=findViewById(R.id.writecontent);
        savenotecontent=findViewById(R.id.createadd_notes);

        Toolbar toolbar=findViewById(R.id.toolcreatenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        savenotecontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=notetitle.getText().toString();
                String content=notecontent.getText().toString();
                if(title.isEmpty() || content.isEmpty())
                {
                    Toast.makeText(CreateNotes.this, "Both Fields are Required!!", Toast.LENGTH_SHORT).show();
                }else {
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Mynotes").document();
                    Map<String,Object> note=new HashMap<>();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd ");
                    String keepDate=simpleDateFormat.format(new Date());
                    note.put("title",title);
                    note.put("content",content);
                    note.put("date",keepDate);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CreateNotes.this, "Note Created Successfully!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateNotes.this,NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateNotes.this, "Failed To Create Note!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}