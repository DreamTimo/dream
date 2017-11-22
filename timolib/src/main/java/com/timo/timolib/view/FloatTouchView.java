package com.timo.timolib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.timo.timolib.BaseTools;
import com.timo.timolib.utils.ScreenUtils;

/**
 * Created by 蔡永汪 on 2017/11/15.
 */

public class FloatTouchView extends CircleImageView {
    public FloatTouchView(Context context) {
        super(context);
        screenWidth = ScreenUtils.getInstance().getScreenWidth(context);
        screenHeight = ScreenUtils.getInstance().getScreenHeight(context);
    }

    public FloatTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        screenWidth = ScreenUtils.getInstance().getScreenWidth(context);
        screenHeight = ScreenUtils.getInstance().getScreenHeight(context);
    }

    public FloatTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenWidth = ScreenUtils.getInstance().getScreenWidth(context);
        screenHeight = ScreenUtils.getInstance().getScreenHeight(context);
    }

    private int lastX = 0;
    private int lastY = 0;

    private int dx;
    private int dy;
    private float movex = 0;
    private float movey = 0;

    private int screenWidth;
    private int screenHeight;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                movex = lastX;
                movey = lastY;
                break;
            case MotionEvent.ACTION_MOVE:
                dx = (int) event.getRawX() - lastX;
                dy = (int) event.getRawY() - lastY;

                int left = getLeft() + dx;
                int top = getTop() + dy;
                int right = getRight() + dx;
                int bottom = getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + getWidth();
                }
                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + getHeight();
                }
                if (bottom > screenHeight - BaseTools.dp2px(120)) {
                    bottom = screenHeight - BaseTools.dp2px(120);
                    top = bottom - getHeight();
                }
                layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                //避免滑出触发点击事件
                if ((int) (event.getRawX() - movex) != 0
                        || (int) (event.getRawY() - movey) != 0) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }
}
