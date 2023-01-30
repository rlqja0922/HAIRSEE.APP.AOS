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
    private ImageView fullimg;

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
        fullimg = view.findViewById(R.id.iv_full);
        fullimg.setImageBitmap(bm);
        return view;
    }

    @Override
    public void onBackPressed() {

    }
}