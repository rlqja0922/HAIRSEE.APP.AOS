package com.example.hairsee.menu.CamSub;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.hairsee.Hairlist;
import com.example.hairsee.MainActivity;
import com.example.hairsee.R;
import com.example.hairsee.detection.DetectorActivity;
import com.example.hairsee.listClass.Hair;
import com.example.hairsee.listClass.Hair2;
import com.example.hairsee.utils.MyAlert;
import com.example.hairsee.utils.OnSingleClickListener;

import java.util.ArrayList;
import java.util.List;

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
    private Context context;
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
        curlhair();
        straighthair();
        mHairlist = new Hairlist(items);
        hairChoice.setAdapter(mHairlist);
        hairChoice.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        hairChoice2.setAdapter(mHairlist);
        hairChoice2.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
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
            btnRegistrationClickHandler();

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

        items.add(new Hair("안유진",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진2",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진3",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진4",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진5",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진6",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진7",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진8",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진9",R.drawable.common_google_signin_btn_icon_dark));
    }
    public void straighthair(){

        items2.add(new Hair2("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair2("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair2("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair2("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair2("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair2("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair2("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair2("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair2("장원영",R.drawable.common_google_signin_btn_icon_dark));
    }
    @Override
    public void onBackPressed() {
        return;
    }
}