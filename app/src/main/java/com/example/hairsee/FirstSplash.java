package com.example.hairsee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.hairsee.utils.SharedStore;
public class FirstSplash extends AppCompatActivity {
//앱 처음킬때
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstsplash);
        if (SharedStore.getSplash(FirstSplash.this)){
            SharedStore.setSplash(FirstSplash.this,false);
            Handler handler= new Handler(Looper.myLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent;
                    intent = new Intent(getApplicationContext(), firstsplash_veiwpager.class);
                    startActivity(intent);
                    finish();
                }
            },2000);// 초
        }else {
            Intent intent;
            intent = new Intent(getApplicationContext(), SecondSplash.class);
            startActivity(intent);
            finish();
        }

    }
}