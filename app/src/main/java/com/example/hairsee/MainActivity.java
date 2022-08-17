package com.example.hairsee;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.hairsee.detection.DetectorActivity;
import com.example.hairsee.utils.MyAlert;
import com.example.hairsee.utils.OnSingleClickListener;
import com.example.hairsee.utils.SharedStore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public Button camBt,gallBt;
    public static int permissionCamera;
    public static int permissionRead;
    public static int permissionWrite;
    public static final int Toast_Result = 1500;
    private static final int REQ = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gallBt = findViewById(R.id.gallery_bt);
        camBt = findViewById(R.id.camera_bt);
        camBt.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                permissionCamera = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
                permissionRead = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                permissionWrite = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permissionCamera != PackageManager.PERMISSION_GRANTED
                        || permissionRead != PackageManager.PERMISSION_GRANTED
                        || permissionWrite != PackageManager.PERMISSION_GRANTED) {
                    MyAlert.MyDialog_single(MainActivity.this, "안내", "카메라 및 저장공간 권한을 허용해주십시오", v1 -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + MainActivity.this.getPackageName()));
                        Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                        MainActivity.this.finish();
//                        startActivity(intent1);
                        MainActivity.this.getApplicationContext().startActivity(intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        MainActivity.this.getApplicationContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    });
                }
                else {
                    btnRegistrationClickHandler();
                }
            }
        });
        gallBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQ);
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