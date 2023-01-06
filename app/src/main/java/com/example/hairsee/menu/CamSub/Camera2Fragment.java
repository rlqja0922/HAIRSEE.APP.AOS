package com.example.hairsee.menu.CamSub;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.hairsee.MainActivity;
import com.example.hairsee.R;
import com.example.hairsee.utils.OnSingleClickListener;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Camera2Fragment extends Fragment implements MainActivity.OnBackPressedListener{

    public View view;
    public ImageView nextBt;
    private Context context;
    private FrameLayout lenChoice1, lenChoice2;
    private int lenChoice =0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera2, container, false);
        context = getContext();
        nextBt = view.findViewById(R.id.camnextBt);
        lenChoice1 = view.findViewById(R.id.lenChoice1);
        lenChoice2 = view.findViewById(R.id.lenChoice2);
        lenChoice1.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                lenChoice =1;

            }
        });
        lenChoice2.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                lenChoice =2;
            }
        });
        AppCompatActivity activity = (AppCompatActivity) context;
        nextBt.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (lenChoice != 0){

                    Bundle bundle = new Bundle();
                    bundle.putInt("sex",lenChoice);
                    Camera3Fragment camera3 = new Camera3Fragment();
                    camera3.setArguments(bundle);
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.Fragment,camera3).commit();
                }
            }
        });
        return view;
    }

    @Override
    public void onBackPressed() {

    }
}