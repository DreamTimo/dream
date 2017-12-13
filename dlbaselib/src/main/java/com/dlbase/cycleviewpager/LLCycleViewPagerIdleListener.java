package com.dlbase.cycleviewpager;

import android.view.View;

/**
 * viewpager处于空闲状态监听器
 * 
 * @author YD
 *
 */
public interface LLCycleViewPagerIdleListener {
	void onPagerSelected(View view, int position);
	void onItemClick(View view,int position);
}
