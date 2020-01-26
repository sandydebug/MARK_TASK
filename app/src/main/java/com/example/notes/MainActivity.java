package com.example.notes;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT =1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,login.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);


        ImageView image = (ImageView) findViewById(R.id.imageView);
        TextView textview = (TextView)findViewById(R.id.textView5);
        image.animate().alpha(0f).scaleX(1.5f).scaleY(1.5f).setDuration(1500);
        textview.animate().alpha(5f).scaleX(1.5f).scaleY(1.5f).setDuration(1500);
        

    }
}
