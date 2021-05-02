package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class editnoteactivity extends AppCompatActivity {
     private EditText editTitle,editcontent;
     FloatingActionButton floatingActionButton;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editnoteactivity);
        Toolbar toolbar;
        toolbar=findViewById(R.id.tooleditnote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTitle=findViewById(R.id.editnotetitle);
        editcontent=findViewById(R.id.editnotecontent);
        floatingActionButton=findViewById(R.id.saveeditnote);

        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();

        Intent data=getIntent();
        editTitle.setText(data.getStringExtra("title"));
        editcontent.setText(data.getStringExtra("content"));
        String id=data.getStringExtra("docId");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newtitle=editTitle.getText().toString();
                String newcontent=editcontent.getText().toString();
                if(newcontent.isEmpty() || newtitle.isEmpty())
                {
                    Toast.makeText(editnoteactivity.this, "Both Fields are Required!!", Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Mynotes").document(id);
                    Map<String,Object> note=new HashMap<>();
                    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd ");
                    String keepDate=simpleDateFormat.format(new Date());
                    note.put("title",newtitle);
                    note.put("content",newcontent);
                    note.put("date",keepDate);

                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(editnoteactivity.this, "Note Updated Successfully!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editnoteactivity.this,NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(editnoteactivity.this, "Failed Update Note!!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(editnoteactivity.this,NotesActivity.class));

                        }
                    });
                }

            }

        });
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}