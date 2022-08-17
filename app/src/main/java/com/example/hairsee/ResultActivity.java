package com.example.hairsee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.hairsee.utils.OnSingleClickListener;
import com.example.hairsee.utils.SharedStore;

import java.io.IOException;
import java.io.InputStream;

public class ResultActivity extends AppCompatActivity {

    private final String TAG = "ResultActivity";
    private final int ImgageShare = 101;
    public static Context mContext;
    public static TextView tv_resultTop, tv_result_info1, tv_result_info2;
    public ConstraintLayout const_gohome, const_restart,const_share,fail1,fail2,succ2,background,background1;
    int standardSize_X, standardSize_Y;
    float density;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
//        String CarImg = SharedStore.getCarImg(this);

        getStandardSize();// 텍스트 크기조절
        findId();
    }

    public void findId(){

//        String CarImg = SharedStore.getCarImg(this);
//        Log.d(TAG, "Base64 이미지 값 : "+ CarImg);
//        new HttpCarImage(this).execute(CarImg);
        fail1 = findViewById(R.id.fail1);
        fail2 = findViewById(R.id.fail2);
        succ2 = findViewById(R.id.suc2);
        background = findViewById(R.id.background);
        background1 = findViewById(R.id.background1);
        const_restart = findViewById(R.id.const_restart);
        const_gohome = findViewById(R.id.const_gohome);
        const_share = findViewById(R.id.const_share);
        tv_resultTop = findViewById(R.id.tv_resultTop);
        tv_result_info1 = findViewById(R.id.tv_resultInfo1);
        tv_result_info2 = findViewById(R.id.tv_resultInfo2);

        tv_resultTop.setTextSize((float)(standardSize_X / 33));
        tv_resultTop.setTextSize((float)(standardSize_Y / 33));
        tv_result_info1.setTextSize((float)(standardSize_X / 60));
        tv_result_info1.setTextSize((float)(standardSize_Y / 60));
        tv_result_info2.setTextSize((float)(standardSize_X / 60));
        tv_result_info2.setTextSize((float)(standardSize_Y / 60));

        const_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedStore.deleteUserData(ResultActivity.this);
                Intent intentHome = new Intent(ResultActivity.this, MainActivity.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentHome);
            }
        });
        const_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedStore.deleteUserData(ResultActivity.this);
                Intent intentRestart = new Intent(ResultActivity.this, CameraActivity.class);
                intentRestart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentRestart);
            }
        });
        const_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("image/*");
                Uri imageUri = Uri.parse(SharedStore.getImgPath(ResultActivity.this));
                sharingIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                startActivity(Intent.createChooser(sharingIntent, null));
            }
        });
    }
    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return  size;
    }


    public void getStandardSize() {
        Point ScreenSize = getScreenSize(ResultActivity.this);
        density  = getResources().getDisplayMetrics().density;
        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedStore.deleteUserData(ResultActivity.this);
//        Log.d(TAG, "삭제후 데이터 1 : "+deleteData1);
//        Log.d(TAG, "삭제후 데이터 2 : "+deleteData2);
    }

    @Override
    public void onBackPressed() {
        SharedStore.deleteUserData(ResultActivity.this);
        Intent intentHome = new Intent(ResultActivity.this, MainActivity.class);
        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentHome);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ImgageShare){
            if (resultCode ==RESULT_OK){
                Toast.makeText(this,"공유성공", Toast.LENGTH_SHORT).show();
            }
        }
    }
}