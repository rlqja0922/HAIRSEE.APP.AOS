package com.example.hairsee;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class GIFActivity extends AppCompatActivity {
    public static Context mContext;
    int standardSize_X, standardSize_Y;
    float density;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gifactivity);
        ImageView img_gif = findViewById(R.id.img_gif);
        TextView tv_gif = findViewById(R.id.tv_gif);
//        Intent intent = getIntent();
//        String data = intent.getStringExtra("CarImg");
        getStandardSize();
        tv_gif.setTextSize((float)(standardSize_X / 30));
        tv_gif.setTextSize((float)(standardSize_Y / 30));
    }
    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size;
    }


    public void getStandardSize() {
        Point ScreenSize = getScreenSize(GIFActivity.this);
        density  = getResources().getDisplayMetrics().density;
        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
    }
    @Override
    public void onBackPressed() {
        return;
    }
}