package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class notesdetail extends AppCompatActivity {

    private TextView mshowtitle,mContent;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notesdetail);
        mshowtitle=findViewById(R.id.shownotetitle);
        mContent=findViewById(R.id.shownotecontent);
        Toolbar toolbar;
        toolbar=findViewById(R.id.toolshowenote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        floatingActionButton=findViewById(R.id.gotoeditnotes);
         Intent data=getIntent();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      Intent intent=new Intent(notesdetail.this,editnoteactivity.class);
                      intent.putExtra("title",data.getStringExtra("title"));
                      intent.putExtra("content",data.getStringExtra("content"));
                      intent.putExtra("docId",data.getStringExtra("docId"));
                      startActivity(intent);
            }
        });
        mshowtitle.setText(data.getStringExtra("title"));
        mContent.setText(data.getStringExtra("content"));
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