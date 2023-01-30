package com.example.hair__See.menu;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.hair__See.MainActivity;
import com.example.hair__See.R;
import com.example.hair__See.menu.gallarysub.gallImageFragment;
import com.example.hair__See.utils.SharedStore;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Galleryfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Galleryfragment extends Fragment implements MainActivity.OnBackPressedListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public String basePath = null;
    public GridView mGridView;
    public gallImageFragment mCustomImageAdapter;
    public View view;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;

    public Galleryfragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Gallaryfragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Galleryfragment newInstance(String param1, String param2) {
        Galleryfragment fragment = new Galleryfragment();
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
        view =  inflater.inflate(R.layout.fragment_galleryfragment, container, false);
        context = getContext();
        SharedStore.setGallery0(context,true);
        // Inflate the layout for this fragment
         File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");

         if (! mediaStorageDir.exists()){
             if (! mediaStorageDir.mkdirs()){
                 Log.d("MyCameraApp", "failed to create directory");
             }
         }
         basePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString() + "/헤보자/before";
         mGridView = view.findViewById(R.id.gridview); // .xml의GridView와 연결
         mCustomImageAdapter = new gallImageFragment(context, basePath); //앞에서 정의한 Custom Image Adapter와 연결

        mGridView.setAdapter(mCustomImageAdapter); //GridView가Custom Image Adapter에서받은 값을뿌릴 수 있도록연결
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(context, position + " Click event", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onBackPressed() {

    }
}