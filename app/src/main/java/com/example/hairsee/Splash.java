package com.example.hairsee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hairsee.utils.SharedStore;
public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (SharedStore.getSplash(Splash.this)){
            SharedStore.setSplash(Splash.this,true);
            Intent intent;
            intent = new Intent(getApplicationContext(), SecondSplash.class);
            startActivity(intent);
            finish();
        }

    }
}