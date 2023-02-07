package com.example.hair__See.API;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @POST("/hair/request")
    @Multipart //이미지 폼데이터 전송
    Call<HairRequest> getRequest(@PartMap Map<String, RequestBody> map, @Part ArrayList<MultipartBody.Part> files);

    //test
    @POST("/fcm")
    Call<Test> getToken(@Body Test test);
}
