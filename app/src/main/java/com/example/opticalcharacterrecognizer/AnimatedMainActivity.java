package com.example.opticalcharacterrecognizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class AnimatedMainActivity extends AppCompatActivity {

    public void textRecognizer(View view)
    {
        Intent i=new Intent(getApplicationContext(),TextRecognizer.class); //Get Application context-gives the current context
        startActivity(i);
    }
    public void speechRecognizer(View view)
    {
        Intent i=new Intent(getApplicationContext(),Speech.class);
        startActivity(i);
    }
    public void bugreport(View view){
        Intent i=new Intent(getApplicationContext(),BugReport.class);
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.applogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //ActionBar actionBar = getActionBar();

// set the icon
        //actionBar.setIcon(R.drawable.applogo);
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);

      getSupportActionBar().setLogo(R.drawable.applogo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);*/

    }
}
