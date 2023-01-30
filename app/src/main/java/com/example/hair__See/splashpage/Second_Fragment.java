package com.example.hair__See.splashpage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.hair__See.R;

//홈 화면 카드뷰
public class Second_Fragment extends Fragment {


    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_second_, container, false);

        return rootView;
    }
}