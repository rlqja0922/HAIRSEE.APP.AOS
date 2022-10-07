package com.example.hairsee.menu.CamSub;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.hairsee.R;
import com.example.hairsee.utils.OnSingleClickListener;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Camera2Fragment extends Fragment {

    public View view;
    public ImageView nextBt;
    private Context context;

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
        AppCompatActivity activity = (AppCompatActivity) context;
        nextBt.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.Fragment,new Camera3Fragment()).commit();
            }
        });
        return view;
    }
}