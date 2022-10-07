package com.example.hairsee.menu.CamSub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hairsee.Hairlist;
import com.example.hairsee.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Camera3Fragment extends Fragment {
    public View view;
    public ListView hairChoice,hairChoice2; //컬,스트레이트

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_camera3, container, false);
        hairChoice = view.findViewById(R.id.hairChoice1);
        hairChoice2 = view.findViewById(R.id.hairChoice2);
        hairChoice.setAdapter(new Hairlist());
        hairChoice2.setAdapter(new Hairlist());
        return view;
    }
}