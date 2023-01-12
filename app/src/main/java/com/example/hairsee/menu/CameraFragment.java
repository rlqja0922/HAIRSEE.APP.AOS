package com.example.hairsee.menu;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.hairsee.MainActivity;
import com.example.hairsee.R;
import com.example.hairsee.detection.DetectorActivity;
import com.example.hairsee.menu.CamSub.Camera2Fragment;
import com.example.hairsee.menu.CamSub.Camera3Fragment;
import com.example.hairsee.menu.gallarysub.Full_Image;
import com.example.hairsee.utils.MyAlert;
import com.example.hairsee.utils.MyImageUtils;
import com.example.hairsee.utils.OnSingleClickListener;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment implements MainActivity.OnBackPressedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private Activity mainActivity;
    private String mParam1;
    private String mParam2;
    private View view;
    public ImageView camnextBt;
    private Context context;
    private FrameLayout sexChoice1,sexChoice2;
    private int sexChoice =0;
    public static int permissionCamera;
    public static int permissionRead;
    public static int permissionWrite;
    public static final int Toast_Result = 1500;
    public static final int PERMISSIONS_REQUEST = 1;
    private static final int REQ = 0;
    public static final String[] CAM_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera, container, false);

        context = getContext();
        mainActivity = getActivity();
        AppCompatActivity activity = (AppCompatActivity) context;
        camnextBt = view.findViewById(R.id.camnextBt);
        sexChoice1 = view.findViewById(R.id.sexChoice1);
        sexChoice2 = view.findViewById(R.id.sexChoice2);

        sexChoice1.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                sexChoice = 1;
            }
        });
        sexChoice2.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                sexChoice = 2;
            }
        });

        camnextBt.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (sexChoice != 0){

                    Bundle bundle = new Bundle();
                    bundle.putInt("sex",sexChoice);
                    Camera3Fragment camera3 = new Camera3Fragment();
                    camera3.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.Fragment,camera3).commit();
                }
            }
        });
//        cambt.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View v) {
//                permission();
//                permissionCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
//                permissionRead = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
//                permissionWrite = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                if (permissionCamera != PackageManager.PERMISSION_GRANTED
//                        || permissionRead != PackageManager.PERMISSION_GRANTED
//                        || permissionWrite != PackageManager.PERMISSION_GRANTED) {
//                    MyAlert.MyDialog_single(context, "안내", "카메라 및 저장공간 권한을 허용해주십시오", v1 -> {
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.getPackageName()));
//                        context.getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                    });
//                }
//                else {
//                    btnRegistrationClickHandler();
//                }
//            }
//        });
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


    @Override
    public void onBackPressed() {

    }
}