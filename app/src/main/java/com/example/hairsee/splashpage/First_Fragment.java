package com.example.hairsee.splashpage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.hairsee.R;


//홈화면 카드뷰
public class First_Fragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = (ViewGroup) inflater.inflate(R.layout.fragment_first_, container, false);
        Button button = rootView.findViewById(R.id.btn_1);






        return rootView;
    }
}