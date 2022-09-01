package com.example.hairsee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.FrameLayout;

public class SecondSplash extends AppCompatActivity {

    public FrameLayout background, pressLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressbt);
        background = findViewById(R.id.PressBackGround);
        pressLayout = findViewById(R.id.PressLayout);

        Handler handler= new Handler(Looper.myLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(getApplicationContext(), SecondSplash.class);
                startActivity(intent);
                finish();
            }
        },2000);// 초
    }
}