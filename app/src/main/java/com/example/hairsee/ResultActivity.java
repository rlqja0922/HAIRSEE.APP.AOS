package com.example.hairsee;

import static android.content.ContentValues.TAG;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.hairsee.API.HairRequest;
import com.example.hairsee.API.RetrofitInterface;
import com.example.hairsee.utils.OnSingleClickListener;
import com.example.hairsee.utils.SharedStore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
    /**
     * 영상 전송 API
     */
    private void ImgAPI(){
        String file_path = SharedStore.getImgPath(mContext);
        Log.d(TAG, "file_path" + file_path);
        File file=new File(file_path);
        String ipStr = "http://hairboza.asuscomm.com:25005/";

        ArrayList<MultipartBody.Part> imageList = new ArrayList<>();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("img", file.getName(), requestFile);
        imageList.add(uploadFile);
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody record = RequestBody.create(MediaType.parse("text/plain"), "testType");
        map.put("hairType", record);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"),"testColor");
        map.put("hairColor", type);
        RequestBody fcm = RequestBody.create(MediaType.parse("text/plain"),  SharedStore.getFcmToken(ResultActivity.this));
        map.put("fcm", fcm);
//            UploadTask uploadTask=new UploadTask();
//            uploadTask.execute(new String[]{file_path,ipStr});
//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("recordSeq", file.getName(), requestFile);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ipStr)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
        Call<HairRequest> call = retrofitInterface.getRequest(map,imageList);
        call.enqueue(new Callback<HairRequest>() {
            @Override
            public void onResponse(Call<HairRequest> call, Response<HairRequest> response) {
                if (response.isSuccessful()) {
                    HairRequest RequestData = response.body();
                    Boolean status = RequestData.isStatus();
                    if (status == true) {
                        Log.d(TAG, "적용요청결과" + status);
                    }
                    else if (status == false) {

                    }
                }
            }

            @Override
            public void onFailure(Call<HairRequest> call, Throwable t) {
                Log.d(TAG,"에러메세지"+t.getMessage());
                t.getMessage();
            }
        });
    }
}