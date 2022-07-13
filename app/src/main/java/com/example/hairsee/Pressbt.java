package com.example.hairsee;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class Pressbt extends AppCompatActivity {

    public FrameLayout background, pressLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pressbt);
        background = findViewById(R.id.PressBackGround);
        pressLayout = findViewById(R.id.PressLayout);
        pressLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(Pressbt.this,MainActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }
}