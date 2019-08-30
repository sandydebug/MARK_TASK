package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class start extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private DatabaseReference databaseReference;
    String title;
    String timeStamp;
    String task;
    private int x=0;


    private ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        progressDialog=new ProgressDialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);

        progressDialog.setMessage("Hang on while we load your notes ");
        progressDialog.show();




        if(x==0) {
            x=1;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                }
            }, 2500);
        }

        gridLayoutManager=new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);

        firebaseAuth=FirebaseAuth.getInstance();


        recyclerView=findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        if(firebaseAuth.getCurrentUser()!=null){
            databaseReference= FirebaseDatabase.getInstance().getReference().child("Notes").child(firebaseAuth.getCurrentUser().getUid());
        }

    }

    @Override
    public void onStart() {

        super.onStart();

        FirebaseRecyclerAdapter<noteModel,NoteViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<noteModel, NoteViewHolder>(
                noteModel.class,
                R.layout.single_note,
                NoteViewHolder.class,
                databaseReference

        ) {
            @Override
            protected void populateViewHolder(final NoteViewHolder noteViewHolder, noteModel noteModel, int i) {

                final String noteId=getRef(i).getKey();
                databaseReference.child(noteId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("timestamp")) {
                            title = dataSnapshot.child("title").getValue().toString();
                            task=dataSnapshot.child("task").getValue().toString();
                            timeStamp = dataSnapshot.child("timestamp").getValue().toString();
                            String content =dataSnapshot.child("content").getValue().toString();
                            noteViewHolder.setTextView2(task);

                            noteViewHolder.setTitle(title);
                            noteViewHolder.setContent(content);
                            GetTimeAgo getTimeAgo=new GetTimeAgo();
                            noteViewHolder.setTime(getTimeAgo.getTimeAgo(Long.parseLong(timeStamp),getApplicationContext()));
                            noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(start.this, task.class);
                                    intent.putExtra("noteId", noteId);
                                    startActivity(intent);
                                }
                            });
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.new_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.add:
                Intent intent=new Intent(start.this,task.class);
                startActivity(intent);
                break;
            case R.id.logout:

                new AlertDialog.Builder(this)
                        .setTitle("LOGOUT")
                        .setMessage("Are you sure you want to logout ?  I suggest spend some more time :) ")

                        .setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                finish();
                                startActivity(new Intent(start.this,login.class));
                            }
                        })
                        .setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), null)
                        .setIcon(android.R.drawable.ic_lock_power_off)
                        .show();
                break;
            case R.id.profile:
                break;

        }
        return true;
    }

}
