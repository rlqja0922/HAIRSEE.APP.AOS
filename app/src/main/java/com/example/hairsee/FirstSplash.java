package com.example.hairsee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hairsee.utils.SharedStore;
public class FirstSplash extends AppCompatActivity {
//앱 처음킬때
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstsplash);
        if (SharedStore.getSplash(FirstSplash.this)){
            SharedStore.setSplash(FirstSplash.this,true);
            Intent intent;
            intent = new Intent(getApplicationContext(), firstsplash_veiwpager.class);
            startActivity(intent);
            finish();
        }

    }
}