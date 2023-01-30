package com.example.hair__See;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hair__See.utils.OnSingleClickListener;

public class firstsplash_veiwpager extends AppCompatActivity {

    public ViewPager2 viewPageSetup;
    private String page;
    public ImageView pageindex1,pageindex2,pageindex3,mainBt;
    public TextView mainBt2;
    public LinearLayout splashback,splashcancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstsplash_veiwpager);
        viewPageSetup = findViewById(R.id.viewPagerImageSlider);
        pageindex1 = findViewById(R.id.pageindex1);
        pageindex2 = findViewById(R.id.pageindex2);
        pageindex3 = findViewById(R.id.pageindex3);
        mainBt = findViewById(R.id.mainBt);
        mainBt2 = findViewById(R.id.mainBt2);
        splashback = findViewById(R.id.splashBack);
        splashcancel = findViewById(R.id.splashCancel);

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
                String page1 = String.valueOf(viewPageSetup.getCurrentItem());
                Log.d("ViewPager",page1);
                if (page1.equals("0")){
                    pageindex1.setImageResource(R.drawable.pageertrue);
                    pageindex2.setImageResource(R.drawable.pagerfalse);
                    pageindex3.setImageResource(R.drawable.pagerfalse);
                    mainBt.setVisibility(View.GONE);
                    mainBt2.setVisibility(View.GONE);
                    splashback.setVisibility(View.GONE);
                }else  if (page1.equals("1")){
                    pageindex1.setImageResource(R.drawable.pagerfalse);
                    pageindex2.setImageResource(R.drawable.pageertrue);
                    pageindex3.setImageResource(R.drawable.pagerfalse);
                    mainBt.setVisibility(View.GONE);
                    mainBt2.setVisibility(View.GONE);
                    splashback.setVisibility(View.VISIBLE);
                }else if (page1.equals("2")){
                    pageindex1.setImageResource(R.drawable.pagerfalse);
                    pageindex2.setImageResource(R.drawable.pagerfalse);
                    pageindex3.setImageResource(R.drawable.pageertrue);
                    mainBt.setVisibility(View.VISIBLE);
                    mainBt2.setVisibility(View.VISIBLE);
                    splashback.setVisibility(View.VISIBLE);
                }
            }
        });

        viewPageSetup.setPageTransformer(compositePageTransformer);
        mainBt.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(firstsplash_veiwpager.this,MainActivity.class);
                startActivity(intent);
            }
        });
        splashcancel.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(firstsplash_veiwpager.this,MainActivity.class);
                startActivity(intent);
            }
        });
        splashback.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String page1 = String.valueOf(viewPageSetup.getCurrentItem());
                Log.d("ViewPager",page1);
                    if (page1.equals("1")){
                        viewPageSetup.setCurrentItem(0);
                    }else if (page1.equals("2")){
                        viewPageSetup.setCurrentItem(1);
                    }
            }
        });

    }
}