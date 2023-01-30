package com.example.hair__See;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.hair__See.utils.SharedStore;
public class FirstSplash extends AppCompatActivity {
//앱 처음킬때
    protected ConstraintLayout SplashImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstsplash);
        SplashImage = findViewById(R.id.SplashImage);
//        SharedStore.setSplash(FirstSplash.this,true); // 스플레시테스트 끝날시 제거
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
            int randomint = (int) (Math.random()*1000);
            if (randomint>7){
                SplashImage.setBackground(ContextCompat.getDrawable(FirstSplash.this,R.drawable.splashnew2));
            }else if (randomint>4){
                SplashImage.setBackground(ContextCompat.getDrawable(FirstSplash.this,R.drawable.splashnew1));
            }else{
                SplashImage.setBackground(ContextCompat.getDrawable(FirstSplash.this,R.drawable.fitstsplash));
            }
            Intent intent;
            intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}