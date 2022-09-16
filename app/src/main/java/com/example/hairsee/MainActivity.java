package com.example.hairsee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.hairsee.detection.DetectorActivity;
import com.example.hairsee.menu.CameraFragment;
import com.example.hairsee.menu.Galleryfragment;
import com.example.hairsee.menu.Mainfragment;
import com.example.hairsee.utils.OnSingleClickListener;
import com.example.hairsee.utils.SharedStore;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static int permissionCamera;
    public static int permissionRead;
    public static int permissionWrite;
    public static final int Toast_Result = 1500;
    private static final int REQ = 0;
    private ImageView gall, cam, home;
    public FrameLayout fragment;
    Fragment homeFragment, galleyFragment, cameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gall = findViewById(R.id.gallery_mn);
        cam = findViewById(R.id.camera_mn);
        home = findViewById(R.id.home_mn);
        fragment = findViewById(R.id.Fragment);
        homeFragment = new Mainfragment();
        galleyFragment = new Galleryfragment();
        cameraFragment = new CameraFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, homeFragment).commit();
        gall.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, galleyFragment).commit();
            }
        });
        home.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, homeFragment).commit();
            }
        });

        cam.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, cameraFragment).commit();
            }
        });
    }
    //btnRegistration
    private void btnRegistrationClickHandler() {
        Intent intent = new Intent(MainActivity.this, DetectorActivity.class);
        intent.putExtra("btnFlag", "registraion");
        startActivityForResult(intent, Toast_Result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ){
            if (resultCode ==RESULT_OK){
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    SharedStore.setImgPath(MainActivity.this,data.getDataString());
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}