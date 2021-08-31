package com.example.opticalcharacterrecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static int timeout=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animated_main);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
           @Override
            public void run(){
               Intent i=new Intent(getApplicationContext(),AnimatedMainActivity.class);
               startActivity(i);
               finish();
           }
        },timeout);
    }
}
