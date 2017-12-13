package com.lykj.UI.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LYViewPagerFixed extends android.support.v4.view.ViewPager {

    public LYViewPagerFixed(Context context) {
        super(context);
    }

    public LYViewPagerFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
