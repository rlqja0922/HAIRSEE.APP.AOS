package com.example.hair__See.menu.CamSub;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hair__See.Hairlist;
import com.example.hair__See.Hairlist2;
import com.example.hair__See.MainActivity;
import com.example.hair__See.R;
import com.example.hair__See.detection.DetectorActivity;
import com.example.hair__See.listClass.Hair;
import com.example.hair__See.listClass.Hair2;
import com.example.hair__See.listClass.ItemClickSupport;
import com.example.hair__See.utils.MyAlert;
import com.example.hair__See.utils.OnSingleClickListener;
import com.example.hair__See.utils.SharedStore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Camera3Fragment extends Fragment implements MainActivity.OnBackPressedListener{
    public View view;
    public ImageView camerstart_iv;
    private Activity mainActivity;
    public RecyclerView hairChoice,hairChoice2; //컬,스트레이트
    private ArrayList<Hair> items=new ArrayList<>();
    private ArrayList<Hair2> items2=new ArrayList<>();
    private Hairlist mHairlist;
    private Hairlist2 mHairlist2;
    private Context context;
    private int sex;
    public static int type;
    public static final int Toast_Result = 1500;
    public static final int PERMISSIONS_REQUEST = 1;
    private static final int REQ = 0;
    public static int permissionCamera;
    public static int permissionRead;
    public static int permissionWrite;
    public static final String[] CAM_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera3, container, false);
        hairChoice = view.findViewById(R.id.hairChoice1);
        hairChoice2 = view.findViewById(R.id.hairChoice2);

        camerstart_iv = view.findViewById(R.id.camerstart_iv);
        context = getContext();
        mainActivity = getActivity();

        sex = getArguments().getInt("sex");
        curlhair();
        straighthair();
        mHairlist = new Hairlist(items);
        mHairlist2 = new Hairlist2(items2);
        hairChoice.setAdapter(mHairlist);
        hairChoice.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        hairChoice2.setAdapter(mHairlist2);
        hairChoice2.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        ItemClickSupport.addTo(hairChoice).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (items.get(position).getTitle().equals("준비중")){

                }else{
                    type = items.get(position).getType();
                    SharedStore.setHairType(context,String.valueOf(items.get(position).getType()));
                    for (int i =0;i < items.size(); i++){
                        if (i == position){
                            items.get(position).setPick(true);
                        }else{
                            items.get(i).setPick(false);
                        }
                    }
                    for (int i =0;i < items2.size(); i++){
                        items2.get(i).setPick(false);
                    }
                }
                hairChoice2.getAdapter().notifyDataSetChanged();
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        ItemClickSupport.addTo(hairChoice2).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (items2.get(position).getTitle().equals("준비중")){

                }else{
                    type = items2.get(position).getType();
                    SharedStore.setHairType(context,String.valueOf(items2.get(position).getType()));
                    for (int i =0;i < items2.size(); i++){
                        if (i == position){
                            items2.get(position).setPick(true);
                        }else{
                            items2.get(i).setPick(false);
                        }
                    }
                    for (int i =0;i < items.size(); i++){
                        items.get(i).setPick(false);
                    }
                }
                hairChoice.getAdapter().notifyDataSetChanged();
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });

        camerstart_iv.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                permission();
                permissionCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
                permissionRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionWrite = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCamera != PackageManager.PERMISSION_GRANTED
                        || permissionRead != PackageManager.PERMISSION_GRANTED
                        || permissionWrite != PackageManager.PERMISSION_GRANTED) {
                    MyAlert.MyDialog_single(context, "카메라 및 저장공간 권한을 허용해주십시오", v1 -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
                        context.getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        MyAlert.dialogrem.dismiss();
                    });

                }
                else {
                    btnRegistrationClickHandler();
                }
            }
        });
        return view;
    }
    private void permission(){
        // 카메라, 오디오, 저장공간 확인
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            requestVideoPermissions(); // 권한 확인

        }
    }
    public void requestVideoPermissions() {
        ActivityCompat.requestPermissions(mainActivity,CAM_PERMISSIONS, PERMISSIONS_REQUEST);
    }

    //btnRegistration
    private void btnRegistrationClickHandler() {
        Intent intent = new Intent(context, DetectorActivity.class);
        intent.putExtra("btnFlag", "registraion");
        startActivityForResult(intent, Toast_Result);
    }
    public void curlhair() {
        if (sex == 1){
            //남성
            items.add(new Hair("8",R.drawable.common_google_signin_btn_icon_dark,8,false));
            items.add(new Hair("24",R.drawable.common_google_signin_btn_icon_dark,24,false));
            items.add(new Hair("32",R.drawable.common_google_signin_btn_icon_dark,32,false));
            items.add(new Hair("66",R.drawable.common_google_signin_btn_icon_dark,66,false));
            items.add(new Hair("116",R.drawable.common_google_signin_btn_icon_dark,116,false));
            items.add(new Hair("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items.add(new Hair("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items.add(new Hair("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items.add(new Hair("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
        }else if (sex ==2){
            //여성
            items.add(new Hair("4",R.drawable.common_google_signin_btn_icon_dark,4,false));
            items.add(new Hair("20",R.drawable.common_google_signin_btn_icon_dark,20,false));
            items.add(new Hair("56",R.drawable.common_google_signin_btn_icon_dark,56,false));
            items.add(new Hair("57",R.drawable.common_google_signin_btn_icon_dark,57,false));
            items.add(new Hair("72",R.drawable.common_google_signin_btn_icon_dark,72,false));
            items.add(new Hair("89",R.drawable.common_google_signin_btn_icon_dark,89,false));
            items.add(new Hair("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items.add(new Hair("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items.add(new Hair("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));

        }
    }
    public void straighthair(){
        if (sex == 1){
            //남성

            items2.add(new Hair2("33",R.drawable.common_google_signin_btn_icon_dark,33,false));
            items2.add(new Hair2("52",R.drawable.common_google_signin_btn_icon_dark,52,false));
            items2.add(new Hair2("63",R.drawable.common_google_signin_btn_icon_dark,63,false));
            items2.add(new Hair2("75",R.drawable.common_google_signin_btn_icon_dark,75,false));
            items2.add(new Hair2("52",R.drawable.common_google_signin_btn_icon_dark,52,false));
            items2.add(new Hair2("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items2.add(new Hair2("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items2.add(new Hair2("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items2.add(new Hair2("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
        }else if (sex ==2){
            //여성
            items2.add(new Hair2("1",R.drawable.common_google_signin_btn_icon_dark,1,false));
            items2.add(new Hair2("17",R.drawable.common_google_signin_btn_icon_dark,17,false));
            items2.add(new Hair2("64",R.drawable.common_google_signin_btn_icon_dark,64,false));
            items2.add(new Hair2("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items2.add(new Hair2("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items2.add(new Hair2("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items2.add(new Hair2("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items2.add(new Hair2("준비중",R.drawable.common_google_signin_btn_icon_dark,0,false));
            items2.add(new Hair2("준비중", R.drawable.m52_m,0,false));

        }
    }
    @Override
    public void onBackPressed() {
        return;
    }
}