package com.dlbase.photoutil;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class DLViewPagerExtend extends ViewPager {
	
	

    public DLViewPagerExtend(Context context) {
        super(context);
    }

    public DLViewPagerExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
   /*//简单但重要一步，ViewPager获得touch焦点时候，阻止父层ScorllView及祖父层SlidingMenu的拦截
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	boolean ret = super.dispatchTouchEvent(ev);
    	if(ret) 
    	{
    		requestDisallowInterceptTouchEvent(true);
    	}
    	return ret;
    }*/
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) //采用最大的view的高度。 
               height = h;
       }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
   
    
}

