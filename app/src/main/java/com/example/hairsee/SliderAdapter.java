package com.example.hairsee;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.hairsee.splashpage.First_Fragment;
import com.example.hairsee.splashpage.Second_Fragment;

//홈화면 카드뷰 어뎁터
public class SliderAdapter extends FragmentStateAdapter {
    private final int mSetItemCount = 3;

    public SliderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        int iViewIdx = getRealPosition(position);
        switch (iViewIdx){
            case 0      : { return new First_Fragment(); }
            case 1      : { return new Second_Fragment(); }

            default :     return null;
        }
    }

    public int getRealPosition(int _iPosition){
        return _iPosition % mSetItemCount;
    }

    @Override

    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}