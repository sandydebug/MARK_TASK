package com.example.notes;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class Registration extends AppCompatActivity {

    EditText editText1, editText2, editText3, editText4;
    String name,email,age,password;
    TextView textView;
    Button button;
    int a=1;
    Bitmap bitmap;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private ImageView imageView;
    private FirebaseStorage firebaseStorage;
    private static int PICK_IMAGE=123;
    Uri uri;
    StorageReference storageReference;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode== RESULT_OK && data.getData()!=null){
            uri=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
                imageView.setTag("Set");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        intialize();

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        storageReference=firebaseStorage.getReference();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Image"),PICK_IMAGE);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String username=editText2.getText().toString().trim();
                    String password =editText3.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendVerification();
                            }
                            else{
                                Toast.makeText(Registration.this,"Registration Failed!!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Registration.this,login.class));
            }
        });
    }

    private void intialize() {
        editText1 = (EditText) findViewById(R.id.etUserName);
        editText2 = (EditText) findViewById(R.id.etUserEmail);
        editText3 = (EditText) findViewById(R.id.etPassword);
        editText4 = (EditText) findViewById(R.id.etAge);
        textView = (TextView) findViewById(R.id.tvUserLogin);
        button = (Button) findViewById(R.id.btnRegister);
        imageView=(ImageView)findViewById(R.id.imageView);
        progressDialog=new ProgressDialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);


    }

    private boolean validate() {
        Boolean res=false;
        name = editText1.getText().toString();
        email = editText2.getText().toString();
        password = editText3.getText().toString();
        age=editText4.getText().toString();

        progressDialog.setMessage("Hang on while we register your profile ");
        progressDialog.show();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || age.isEmpty() ) {
            Toast.makeText(Registration.this, "Enter all the details", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else if(!("Set".equals(imageView.getTag()))){
            Toast.makeText(Registration.this, "Add a image", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else if(password.length()<5 && a!=0){
            Toast.makeText(Registration.this, "BHAI DEKH MUJHE KOI DIKKAT NAHI BUT BADI PASSWORD RAKH LE.....||DOBARA REGISTER||", Toast.LENGTH_LONG).show();
            a=0;
            progressDialog.dismiss();

        }
        else{
            res=true;}

        return res;

    }

    private void sendVerification(){
        final FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){
                       /* sendData();*/
                        Toast.makeText(Registration.this,"Verification Mail Sent !",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(Registration.this,login.class));
                    }
                    else {
                        Toast.makeText(Registration.this,"Mail not sent !",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
   /* private void sendData(){
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference ref=firebaseDatabase.getReference(firebaseAuth.getUid());
        StorageReference imageref=storageReference.child(firebaseAuth.getUid()).child("Images");
        UploadTask uploadTask=imageref.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Registration.this,"Upload Failed",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Registration.this,"Uploaded Successfully",Toast.LENGTH_SHORT).show();
            }
        });
        UserProfile userProfile=new UserProfile(age,email,name);
        ref.setValue(userProfile);
    }*/
}
