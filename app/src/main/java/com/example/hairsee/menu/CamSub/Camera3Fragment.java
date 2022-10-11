package com.example.hairsee.menu.CamSub;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.hairsee.Hairlist;
import com.example.hairsee.R;
import com.example.hairsee.listClass.Hair;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Camera3Fragment extends Fragment {
    public View view;
    public RecyclerView hairChoice,hairChoice2; //컬,스트레이트
    private ArrayList<Hair> items=new ArrayList<>();
    private ArrayList<Hair> items2=new ArrayList<>();
    private Hairlist mHairlist;

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
        curlhair();
        straighthair();
        mHairlist = new Hairlist(items);
        hairChoice.setAdapter(mHairlist);
        hairChoice.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        hairChoice2.setAdapter(mHairlist);
        hairChoice2.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        return view;
    }
    public void curlhair() {

        items.add(new Hair("안유진",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진2",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진3",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진4",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진5",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진6",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진7",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진8",R.drawable.common_google_signin_btn_icon_dark));
        items.add(new Hair("안유진9",R.drawable.common_google_signin_btn_icon_dark));
    }
    public void straighthair(){

        items2.add(new Hair("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair("장원영",R.drawable.common_google_signin_btn_icon_dark));
        items2.add(new Hair("장원영",R.drawable.common_google_signin_btn_icon_dark));
    }
}