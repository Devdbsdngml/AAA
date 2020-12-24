package com.example.aaa;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickLinstener implements View.OnClickListener {

    private static final long MIN_CLICK_INTERVAL = 3000;
    private long mLastClickTime = 0;

    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        mLastClickTime = currentClickTime;

        // 중복클릭 아닌 경우
        if (elapsedTime > MIN_CLICK_INTERVAL) {
            onSingleClick(v);
        }
    }
}
