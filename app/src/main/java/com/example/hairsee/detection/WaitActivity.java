package com.example.hairsee.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.example.hairsee.API.HairRequest;
import com.example.hairsee.API.RetrofitInterface;
import com.example.hairsee.R;
import com.example.hairsee.ResultActivity;
import com.example.hairsee.utils.SharedStore;

import java.io.File;
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

public class WaitActivity extends AppCompatActivity {

    private static final String TAG = "WaitActivity";
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        mContext = getApplicationContext();
        ImgAPI();
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