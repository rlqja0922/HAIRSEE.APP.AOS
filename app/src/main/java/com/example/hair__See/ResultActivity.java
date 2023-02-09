package com.example.hair__See;

import static com.example.hair__See.MainActivity.Toast_Result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.example.hair__See.detection.DetectorActivity;
import com.example.hair__See.utils.OnSingleClickListener;
import com.example.hair__See.utils.SharedStore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ResultActivity extends AppCompatActivity {

    private final String TAG = "ResultActivity";
    private final int ImgageShare = 101;
    public static Context mContext;
    public static TextView tv_resultTop, tv_result_info1, tv_result_info2;
    public ConstraintLayout const_save, const_restart,const_share,fail1,fail2,succ2,background,background1;
    public LinearLayout resultBack;
    int standardSize_X, standardSize_Y;
    float density;
    private Bitmap bitmap = null;
    private ImageView iv_result;
    private boolean save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
//        String CarImg = SharedStore.getCarImg(this);
        mContext= getApplicationContext();
        getStandardSize();// 텍스트 크기조절
        findId();
        ImageLoad();
    }

    public void findId(){

//        String CarImg = SharedStore.getCarImg(this);
//        Log.d(TAG, "Base64 이미지 값 : "+ CarImg);
//        new HttpCarImage(this).execute(CarImg);
        const_restart = findViewById(R.id.const_restart);
        const_save = findViewById(R.id.const_save);
        const_share = findViewById(R.id.const_share);
        resultBack = findViewById(R.id.resultBack);
//      텍스트사이즈 조절 기능 화면 크기비율
//        tv_resultTop.setTextSize((float)(standardSize_X / 33));
//        tv_resultTop.setTextSize((float)(standardSize_Y / 33));
//        tv_result_info1.setTextSize((float)(standardSize_X / 60));
//        tv_result_info1.setTextSize((float)(standardSize_Y / 60));
//        tv_result_info2.setTextSize((float)(standardSize_X / 60));
//        tv_result_info2.setTextSize((float)(standardSize_Y / 60));
        resultBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                new OnBackPressedListener() {
                    @Override
                    public void onBackPressed() {
                        Intent intentHome = new Intent(ResultActivity.this, MainActivity.class);
                        startActivity(intentHome);

                    }
                };
            }
        });
        //백버튼 실행 시 홈 화면으로

        const_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save =  MyImageUtils.saveBitMapImg(bitmap,SharedStore.getImgName(mContext),"after",ResultActivity.this);
                if (save) {
                    Log.d(TAG,"저장완료");
                    SharedStore.deleteUserData(ResultActivity.this);
                    Intent intentHome = new Intent(ResultActivity.this, MainActivity.class);
//                    intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentHome);
                }
            }
        });
        const_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedStore.deleteUserData(ResultActivity.this);
                Intent intentRestart = new Intent(ResultActivity.this, DetectorActivity.class);
                intentRestart.putExtra("btnFlag", "registraion");
                startActivityForResult(intentRestart, Toast_Result);
            }
        });
        //이미지 공유 기능
        const_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (save){  
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    String path = SharedStore.getImgPath(ResultActivity.this);
                    File file = new File(path);
                    sharingIntent.setType("image/*");
                    Uri imageUri = FileProvider.getUriForFile(ResultActivity.this,"com.example.hair__See.fileprovider",file);
                    sharingIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                    startActivity(Intent.createChooser(sharingIntent, null));
                }
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
    public void ImageLoad(){
        iv_result = findViewById(R.id.iv_result);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String ImgURL = "http://1.225.241.111:25001"+getIntent().getStringExtra("url");
                try {
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(ImgURL).getContent());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_result.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        iv_result.setImageBitmap(bitmap);
    }
    public interface OnBackPressedListener {

        void onBackPressed();
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
//        intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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