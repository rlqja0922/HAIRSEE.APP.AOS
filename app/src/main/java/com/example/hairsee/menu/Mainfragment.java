package com.example.hairsee.menu;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.hairsee.MainActivity;
import com.example.hairsee.R;
import com.example.hairsee.utils.MyAlert;
import com.example.hairsee.utils.OnSingleClickListener;
import com.example.hairsee.utils.SharedStore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Mainfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mainfragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private EditText fcmtext;
    private Context context;
    private ConstraintLayout hot, top10, gall, guide, pro;
    public static int permissionCamera;
    public static int permissionRead;
    public static int permissionWrite;
    public static final int Toast_Result = 1500;
    private static final int REQ = 0;
    public Mainfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mainfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Mainfragment newInstance(String param1, String param2) {
        Mainfragment fragment = new Mainfragment();
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
        view = inflater.inflate(R.layout.fragment_mainfragment, container, false);
        context = getContext();
        fcmtext = view.findViewById(R.id.fcmtext);
        hot = view.findViewById(R.id.hotLayout);
        gall = view.findViewById(R.id.gallLayout);
        guide = view.findViewById(R.id.infoLayout);
        pro = view.findViewById(R.id.proLayout);
        top10 = view.findViewById(R.id.top10);


        fcmtext.setText(SharedStore.getFcmToken(context));
        hot.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.Fragment,new HairThreeFragment()).commit();
            }
        });
        pro.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.Fragment,new PROfragment()).commit();
            }
        });
        top10.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.Fragment,new Top10()).commit();
            }
        });
        gall.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.Fragment,new Galleryfragment()).commit();
            }
        });
//
//        gall = view.findViewById(R.id.gallery_mn);
//        cam = view.findViewById(R.id.camera_mn);
//        home = view.findViewById(R.id.home_mn);
//
//        cam.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View v) {
//                permissionCamera = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
//                permissionRead = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
//                permissionWrite = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//                if (permissionCamera != PackageManager.PERMISSION_GRANTED
//                        || permissionRead != PackageManager.PERMISSION_GRANTED
//                        || permissionWrite != PackageManager.PERMISSION_GRANTED) {
//                    MyAlert.MyDialog_single(MainActivity.this, "안내", "카메라 및 저장공간 권한을 허용해주십시오", v1 -> {
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + MainActivity.this.getPackageName()));
//                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
//                        MainActivity.this.finish();
////                        startActivity(intent1);
//                        MainActivity.this.getApplicationContext().startActivity(intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                        MainActivity.this.getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                    });
//                }
//                else {
//                    btnRegistrationClickHandler();
//                }
//            }
//        });
//
//        gall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(intent,REQ);
//            }
//        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Task<String> token = FirebaseMessaging.getInstance().getToken();
        token.addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if(task.isSuccessful()){
                    Log.d("FCM Token", task.getResult());// FCM 토큰 확인용
                    SharedStore.setFcmToken(context, task.getResult());
                    fcmtext.setText(SharedStore.getFcmToken(context));
                }
            }
        });
    }
}