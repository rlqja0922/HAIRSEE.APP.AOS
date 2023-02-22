package com.example.hair__See;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hair__See.detection.DetectorActivity;
import com.example.hair__See.menu.CameraFragment;
import com.example.hair__See.menu.Galleryfragment;
import com.example.hair__See.menu.Mainfragment;
import com.example.hair__See.utils.OnSingleClickListener;
import com.example.hair__See.utils.SharedStore;

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
    public LinearLayout mainBack;
    public ConstraintLayout appbar;
    public TextView tv_main_title;
    Fragment homeFragment, galleyFragment, cameraFragment;

    private long backPressedTime = 0;
    private Toast toast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gall = findViewById(R.id.gallery_mn);
        cam = findViewById(R.id.camera_mn);
        home = findViewById(R.id.home_mn);
        fragment = findViewById(R.id.Fragment);
        mainBack = findViewById(R.id.mainBack);
        tv_main_title = findViewById(R.id.tv_main_title);
        homeFragment = new Mainfragment();
        galleyFragment = new Galleryfragment();
        cameraFragment = new CameraFragment();
        appbar = findViewById(R.id.appbar);
        mainBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onBackPressed();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, homeFragment).commit();
        gall.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, galleyFragment).commit();
                tv_main_title.setText("갤러리");
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
                tv_main_title.setText("헤어선택");
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

    //백버튼 실행 시 홈 화면으로
    public interface OnBackPressedListener {

        void onBackPressed();
    }
    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().getFragments().get(0);
        String fragmentText = String.valueOf(fragment.getClass());
        Log.d("Fragment", fragmentText);
        if (fragmentText.equals("class com.example.hair__See.menu.Mainfragment")){
            if (System.currentTimeMillis() > backPressedTime + 2000) {
                backPressedTime = System.currentTimeMillis();
                toast = Toast.makeText(MainActivity.this, "뒤로 버튼을\n한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            if (System.currentTimeMillis() <= backPressedTime + 2000) {
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch (Exception e){
                    Log.d(TAG, e.getMessage());
                }
                toast.cancel();
            }
        }
        else if (String.valueOf(fragment.getClass()).equals("class com.example.hair__See.menu.gallarysub.Full_Image")){
            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, galleyFragment).commit();
            appbar.setVisibility(View.VISIBLE);
        }
        else{
            appbar.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.Fragment, homeFragment).commit();
        }
//         if (System.currentTimeMillis() > backPressedTime + 2000) {
//            backPressedTime = System.currentTimeMillis();
//            toast = Toast.makeText(getActivity(), "뒤로 버튼을\n한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
//            toast.show();
//            return;
//        }
//        if (System.currentTimeMillis() <= backPressedTime + 2000) {
//            try {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(intent.CATEGORY_HOME);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }catch (Exception e){
//                Log.d(TAG, e.getMessage());
//            }
//            toast.cancel();
//        }

        return;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (String.valueOf(fragment.getClass()).equals("class com.example.hair__See.menu.gallarysub.Full_Image")){
            appbar = findViewById(R.id.appbar);
            appbar.setVisibility(View.GONE);
        }else {
            appbar = findViewById(R.id.appbar);
            appbar.setVisibility(View.VISIBLE);
        }
        //class com.example.hairsee.menu.gallarysub.Full_Image
    }
}