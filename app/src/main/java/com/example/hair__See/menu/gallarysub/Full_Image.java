package com.example.hair__See.menu.gallarysub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hair__See.MainActivity;
import com.example.hair__See.R;
import com.example.hair__See.utils.OnSingleClickListener;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Full_Image extends Fragment implements MainActivity.OnBackPressedListener{


    // TODO: Rename and change types of parameters
    private byte[] bitmapArr;
    private String path;
    private Bitmap bm;
    private Context context;
    private String mBasePath;
    private String Position;
    private View view;
    private ImageView fullimg,afterImg,beforeImg;
    String[] array,array2;
    String fileName,folderPath;

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

        afterImg.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                afterImg.setImageResource(R.drawable.after1);
                beforeImg.setImageResource(R.drawable.before2);

            }
        });
        beforeImg.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                afterImg.setImageResource(R.drawable.after2);
                beforeImg.setImageResource(R.drawable.before1);
            }
        });
        return view;
    }

    @Override
    public void onBackPressed() {

    }
}