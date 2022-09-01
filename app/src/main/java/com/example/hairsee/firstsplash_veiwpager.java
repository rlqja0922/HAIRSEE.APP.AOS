package com.example.hairsee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

public class firstsplash_veiwpager extends AppCompatActivity {

    public ViewPager2 viewPageSetup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstsplash_veiwpager);
        viewPageSetup = findViewById(R.id.viewPagerImageSlider);

        SliderAdapter SetupPagerAdapter = new SliderAdapter(this);



        viewPageSetup.setClipToPadding(false);
        viewPageSetup.setClipChildren(false);
        viewPageSetup.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        viewPageSetup.setAdapter(SetupPagerAdapter); //FragPagerAdapter를 파라머티로 받고 ViewPager2에 전달 받는다.
        viewPageSetup.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL); //방향은 가로로
        viewPageSetup.setOffscreenPageLimit(2); //페이지 한계 지정 갯수
        // 페이지 시작 점
        viewPageSetup.setCurrentItem(0);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });

        viewPageSetup.setPageTransformer(compositePageTransformer);
    }
}