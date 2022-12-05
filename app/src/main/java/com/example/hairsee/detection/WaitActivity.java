package com.example.hairsee.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hairsee.API.HairRequest;
import com.example.hairsee.API.RetrofitInterface;
import com.example.hairsee.MainActivity;
import com.example.hairsee.R;
import com.example.hairsee.ResultActivity;
import com.example.hairsee.utils.MyFirebaseMessagingService;
import com.example.hairsee.utils.SharedStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WaitActivity extends AppCompatActivity implements MainActivity.OnBackPressedListener {

    private static final String TAG = "WaitActivity";
    private Context mContext;
    private Timer timers;
    private String waitStep = "0";
    private ProgressBar wait_pb;
    private TextView wait_pt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        mContext = getApplicationContext();
        ImgAPI();
        wait_pb = findViewById(R.id.wait_pb);
        wait_pt = findViewById(R.id.wait_pt);
        ImageView img_gif = findViewById(R.id.gif);
        Glide.with(this).load(R.drawable.hairlod).into(img_gif);
        SharedStore.setWait(mContext,"스텝1");
        try {
            timers = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    waitStep = SharedStore.getWait(mContext);
                    if (waitStep.equals("스텝2")){
                        wait_pb.setProgress(2);
                        wait_pt.setText("2/3");
                    }else if (waitStep.equals("스텝3")){
                        wait_pb.setProgress(3);
                        wait_pt.setText("3/3");
                        Intent intent;
                        intent = new Intent(WaitActivity.this,ResultActivity.class);
                        intent.putExtra("url",SharedStore.getimgURL(mContext));
                        Log.d("fcm", "imgurl: "+SharedStore.getimgURL(mContext));
                        startActivity(intent);
                        timers.cancel();
                    }
                }
            };
            timers.scheduleAtFixedRate(timerTask,0,5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 영상 전송 API
     */
    private void ImgAPI(){
        String file_path = SharedStore.getImgPath(mContext);
        Log.d(TAG, "file_path" + file_path);
        File file=new File(file_path);
        String ipStr = "1.225.241.111:25001";

        ArrayList<MultipartBody.Part> imageList = new ArrayList<>();
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("img", file.getName(), requestFile);
        imageList.add(uploadFile);
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody record = RequestBody.create(MediaType.parse("text/plain"), "90");
        map.put("hairType", record);
        RequestBody type = RequestBody.create(MediaType.parse("text/plain"),"117");
        map.put("hairColor", type);
        RequestBody fcm = RequestBody.create(MediaType.parse("text/plain"),  SharedStore.getFcmToken(WaitActivity.this));
        map.put("fcm", fcm);
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
                        Log.d(TAG, "적용요청결과" + status);
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