package com.example.hair__See.menu.gallarysub;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hair__See.MainActivity;
import com.example.hair__See.R;
import com.example.hair__See.ResultActivity;
import com.example.hair__See.utils.OnSingleClickListener;
import com.example.hair__See.utils.SharedStore;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Full_Image extends Fragment implements MainActivity.OnBackPressedListener{


    // TODO: Rename and change types of parameters
    private ConstraintLayout const_share,const_trash;
    private byte[] bitmapArr;
    private String path;
    private Bitmap bm;
    private Context context;
    private String mBasePath;
    private String Position;
    private View view;
    private ImageView fullimg,afterImg,beforeImg;
    String[] array,array2;
    String fileName,folderPath,folderPath_state;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bitmapArr = getArguments().getByteArray("bitmap");
            path = getArguments().getString("path");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_full__image, container, false);
        if (getArguments() != null) {
            bitmapArr = getArguments().getByteArray("bitmap");
            path = getArguments().getString("path");
        }
        //  String saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/헤보자/"+status;
        bm = BitmapFactory.decodeFile(path);
        array = path.split("/");
        array2 = path.split("after");
        folderPath = array2[0];
        fileName = array[array.length-1];
        fullimg = view.findViewById(R.id.iv_full);
        fullimg.setImageBitmap(bm);
        afterImg = view.findViewById(R.id.afterImg);
        beforeImg = view.findViewById(R.id.beforeImg);
        const_share = view.findViewById(R.id.const_share);
        const_trash = view.findViewById(R.id.const_trash);
        folderPath_state = folderPath +"after/";

        const_share.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                File file = new File(folderPath_state, fileName);
                sharingIntent.setType("image/*");
                Uri imageUri = FileProvider.getUriForFile(getContext(),"com.example.hair__see.fileprovider",file);
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                sharingIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                sharingIntent.putExtra("return-data", true);


                Intent chooser = Intent.createChooser(sharingIntent, "Share File");

                List<ResolveInfo> resInfoList = getContext().getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    getContext().grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                startActivity(chooser);
            }
        });
        const_trash.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                File file = new File(folderPath_state, fileName);
                boolean deleted = file.delete();
                Log.w(TAG, "기존 이미지 삭제 : " + deleted);
                FileOutputStream output = null;
            }
        });
        afterImg.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                afterImg.setImageResource(R.drawable.after1);
                beforeImg.setImageResource(R.drawable.before2);
                folderPath_state = folderPath +"after/";
                bm = BitmapFactory.decodeFile(folderPath_state+fileName);
                fullimg.setImageBitmap(bm);

            }
        });
        beforeImg.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                afterImg.setImageResource(R.drawable.after2);
                beforeImg.setImageResource(R.drawable.before1);
                folderPath_state = folderPath +"before/";
                bm = BitmapFactory.decodeFile(folderPath_state+fileName);
                fullimg.setImageBitmap(bm);
            }
        });
        return view;
    }

    @Override
    public void onBackPressed() {

    }
}