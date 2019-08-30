package com.example.notes;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ComponentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Map;

public class task extends AppCompatActivity {

    private EditText editText,editText2;
    private Button button,button2;
    private FirebaseAuth firebaseAuth;
    private Menu mainMenu;
    private DatabaseReference databaseReference;
    String noteId;
    private boolean exists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        button2=(Button)findViewById(R.id.button2);


        try {
            noteId=getIntent().getStringExtra("noteId");
            if(!noteId.trim().equals("")){
                exists=true;
            }
            else{
                exists=false;
            }
        }
        catch (Exception e){
            e.printStackTrace();

        }

        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);
        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Notes").child(firebaseAuth.getCurrentUser().getUid());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText.getText().toString().trim();
                String content = editText2.getText().toString().trim();
                if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(content)) {
                    createNote(title, content);
                } else {
                    Toast.makeText(task.this,"Enter the contents",Toast.LENGTH_SHORT).show();
                }
            }


        });
        putData();
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markDone();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.del_menu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()){
            case R.id.delete:
                if(exists){
                    new AlertDialog.Builder(this,R.style.AlertDialog)
                            .setTitle("DELETE ITEM")
                            .setMessage("Are you sure you want to delete this ? ")
                            .setPositiveButton(Html.fromHtml("<font color='#FF7F27'>Yes</font>"), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteNote();
                                }
                            })
                            .setNegativeButton(Html.fromHtml("<font color='#FF7F27'>Cancel</font>"), null)
                            .setIcon(R.drawable.ic_delete_forever_black_24dp)
                            .show();


                }
                else{
                    Toast.makeText(task.this,"Nothing to Delete.",Toast.LENGTH_SHORT).show();
                }

                break;
        }
        return true;
    }
    private void putData(){
        if(exists) {
            databaseReference.child(noteId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("title") && dataSnapshot.hasChild("content")) {
                        String title = dataSnapshot.child("title").getValue().toString();
                        String content = dataSnapshot.child("content").getValue().toString();
                        editText.setText(title);
                        editText2.setText(content);
                        if(!title.isEmpty() && !content.isEmpty()){
                            button.setText("UPDATE TASK");
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void createNote(String title, String content) {


        if(exists){
            Map updateMap=new HashMap();
            updateMap.put("title", editText.getText().toString());
            updateMap.put("content", editText2.getText().toString());
            updateMap.put("timestamp", ServerValue.TIMESTAMP);
            databaseReference.child(noteId).updateChildren(updateMap);
            FancyToast.makeText(task.this,"Task Updated!!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

        }
        else{
            final DatabaseReference dref = databaseReference.push();
            final Map map = new HashMap();
            map.put("title", title);
            map.put("content", content);
            map.put("timestamp", ServerValue.TIMESTAMP);
            map.put("task","NOT DONE");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    dref.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            if (task.isSuccessful()) {
                                Toast.makeText(task.this, "Note added sucessfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(task.this, "Note adding failed!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            thread.start();
        }

    }
    public void deleteNote(){
        databaseReference.child(noteId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(task.this,"Item Deleted",Toast.LENGTH_SHORT);
                }
                else{
                    Toast.makeText(task.this,"Oops!! Item cannot be Deleted ",Toast.LENGTH_SHORT);
                }
            }
        });
        Intent intent =new Intent(task.this,start.class);
        startActivity(intent);
    }

    public void markDone(){

        Map updateMap=new HashMap();
        updateMap.put("task","DONE");
        databaseReference.child(noteId).updateChildren(updateMap);
        FancyToast.makeText(task.this,"Task Completed!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();

    }


}
