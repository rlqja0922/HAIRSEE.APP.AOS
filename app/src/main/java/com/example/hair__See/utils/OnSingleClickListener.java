package com.example.hair__See.utils;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener {
    private static final long MIN_CLICK_INTERVAL = 1200;
    private long mLastClickTime = 0;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        // 중복클릭 아닌 경우
        if (elapsedTime > MIN_CLICK_INTERVAL) {
            onSingleClick(v);
        }
    }
}
