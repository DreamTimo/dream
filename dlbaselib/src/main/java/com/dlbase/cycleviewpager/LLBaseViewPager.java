package com.dlbase.cycleviewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 能够与轮播共存的viewpager
 * 
 * @author yuedong
 *
 */
public class LLBaseViewPager extends ViewPager {
	private boolean scrollable = true;

	public LLBaseViewPager(Context context) {
		super(context);
	}

	public LLBaseViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置viewpager是否可以滚动
	 * 
	 * @param enable 滚动？
	 */
	public void setScrollable(boolean enable) {
		scrollable = enable;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (scrollable) {
			//默认行为
			return super.onInterceptTouchEvent(event);
		} else {
			//直接将事件分发给子view
			return false;
		}
	}
}