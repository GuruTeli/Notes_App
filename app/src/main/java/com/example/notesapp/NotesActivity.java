package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotesActivity extends AppCompatActivity {

     private FloatingActionButton addnotes;
     private FirebaseAuth firebaseAuth;
     RecyclerView mrecyclerview;
     StaggeredGridLayoutManager staggeredGridLayoutManager;

     FirebaseUser firebaseUser;
     FirebaseFirestore firebaseFirestore;

     FirestoreRecyclerAdapter<firebasemodel,NoteViewHolder> noteAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        getSupportActionBar().setTitle("All Notes");
        addnotes=findViewById(R.id.add_notes);
        firebaseAuth =FirebaseAuth.getInstance();
        addnotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   startActivity(new Intent(NotesActivity.this,CreateNotes.class));
            }
        });
        Query query=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Mynotes").orderBy("title",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<firebasemodel> AlluserNotes=new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();

        noteAdapter=new FirestoreRecyclerAdapter<firebasemodel, NoteViewHolder>(AlluserNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {


                             noteViewHolder.noteTitel.setText(firebasemodel.getTitle());
                             noteViewHolder.notecontent.setText(firebasemodel.getContent());
                             noteViewHolder.setdate.setText(firebasemodel.getDate());
                             String docId=noteAdapter.getSnapshots().getSnapshot(i).getId();

                          ImageView popupmenubutton=noteViewHolder.itemView.findViewById(R.id.popmenu);

                             noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     Intent intent=new Intent(NotesActivity.this,notesdetail.class);
                                       intent.putExtra("title",firebasemodel.getTitle());
                                       intent.putExtra("content",firebasemodel.getContent());
                                       intent.putExtra("docId",docId);
                                       v.getContext().startActivity(intent);

                                 }
                             });

                             popupmenubutton.setOnClickListener(new View.OnClickListener() {
                                 @RequiresApi(api = Build.VERSION_CODES.M)
                                 @Override
                                 public void onClick(View v) {
                                     PopupMenu popupMenu=new PopupMenu(v.getContext(),v);
                                     popupMenu.setGravity(Gravity.END);
                                     popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                         @Override
                                         public boolean onMenuItemClick(MenuItem item) {
                                             Intent intent=new Intent(NotesActivity.this,editnoteactivity.class);
                                             intent.putExtra("title",firebasemodel.getTitle());
                                             intent.putExtra("content",firebasemodel.getContent());
                                             intent.putExtra("docId",docId);
                                             v.getContext().startActivity(intent);
                                             v.getContext().startActivity(intent);

                                             return false;
                                         }
                                     });
                                     popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                                         @Override
                                         public boolean onMenuItemClick(MenuItem item) {
                                             DocumentReference documentReference=firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("Mynotes").document(docId);
                                             documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void aVoid) {
                                                     Toast.makeText(NotesActivity.this, "This Note is Deleted !", Toast.LENGTH_SHORT).show();

                                                 }
                                             }).addOnFailureListener(new OnFailureListener() {
                                                 @Override
                                                 public void onFailure(@NonNull Exception e) {
                                                     Toast.makeText(NotesActivity.this, "Note is not deleted !", Toast.LENGTH_SHORT).show();

                                                 }
                                             });
                                             return false;
                                         }
                                     });
                                    popupMenu.show();

                                 }

                             });


            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };
        mrecyclerview=findViewById(R.id.recyclerview);
        mrecyclerview.setHasFixedSize(true);
        staggeredGridLayoutManager=new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        mrecyclerview.setLayoutManager(staggeredGridLayoutManager);
        mrecyclerview.setAdapter(noteAdapter);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
             private TextView noteTitel;
             private TextView notecontent;
             private TextView setdate;
             LinearLayout mnote;
                   public NoteViewHolder(View itemView)
                   {
                       super(itemView);
                       noteTitel=itemView.findViewById(R.id.notetitle);
                       notecontent=itemView.findViewById(R.id.notecontent);
                       setdate=itemView.findViewById(R.id.setDate);
                       mnote=itemView.findViewById(R.id.note);
                   }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(NotesActivity.this,MainActivity.class));
                return super.onOptionsItemSelected(item);
            case R.id.delete:
                   alertMessage();
                return super.onOptionsItemSelected(item);
                
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (noteAdapter!=null)
        {
            noteAdapter.stopListening();
        }
    }
    private void alertMessage()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(NotesActivity.this);
        builder.setIcon(R.drawable.ic_baseline_delete_24);
        builder.setTitle("Are You Sure?");
        builder.setMessage("Deleting this account will result in completely removing Your Data from our System and You won't be able to access the app.");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                             if(task.isSuccessful())
                             {
                                 Toast.makeText(NotesActivity.this, "Account Deleted!", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(NotesActivity.this,MainActivity.class));
                             }
                             else {
                                 Toast.makeText(NotesActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                             }
                    }
                });
            }
        }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
        
    }
}