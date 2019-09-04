package com.example.notes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/*import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;*/
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/*hello*/

public class login extends AppCompatActivity {

    TextView textView,textView1,textView2;
    EditText editText,editText1;
    Button button;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        editText=(EditText)findViewById(R.id.name);
        editText1=(EditText)findViewById(R.id.password);
        button=(Button)findViewById(R.id.login);
        textView=(TextView)findViewById(R.id.textView);
        textView1=(TextView)findViewById(R.id.textView7);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this,R.style.Theme_AppCompat_Light_Dialog_Alert);
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            startActivity(new Intent(login.this, start.class));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().isEmpty() || editText1.getText().toString().isEmpty()){
                    Toast.makeText(login.this, "Enter all the details", Toast.LENGTH_SHORT).show();
                }
                else{
                    validate(editText.getText().toString(),editText1.getText().toString());
                }}
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(login.this,Registration.class));
            }
        });

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals("")){
                    Toast.makeText(login.this,"Please enter your registered mail id and then press forgot password ",Toast.LENGTH_LONG).show();
                }
                else{
                    firebaseAuth.sendPasswordResetEmail(editText.getText().toString());
                    Toast.makeText(login.this,"Mail with rest link to your registered mail ",Toast.LENGTH_LONG).show();
                }
            }
        });
         }

    private void validate(String username,String password){

        progressDialog.setMessage("Hang on while we connect you ");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(username,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    emailVerification();
                }
                else{
                    Toast.makeText(login.this,"Login Failed!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void emailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getInstance().getCurrentUser();
        Boolean email=firebaseUser.isEmailVerified();
        if(email){
            finish();
            startActivity(new Intent(login.this,start.class));
        }
        else{
            Toast.makeText(login.this,"Verify your mail",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();

        }
    }
}
