package com.dlbase.util;

import java.lang.reflect.Field;
import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

/**
 * @author xinsuinizouyuan
 *   监听软键盘弹出和收起
 *   把这个类作为一个工具类，添加到项目中，然后，在需要做优化的Activity的onCreate方法中注册activity即可，如：
 *   AndroidAdjustResizeBugFix.assistActivity(this); 
 */
public class AdjustResizeBugFix {
	 private View mChildOfContent;
	 private int usableHeightPrevious;
	 private int statusBarHeight;
	 private FrameLayout.LayoutParams frameLayoutParams;
	 private Activity mActivity;
	 
	private AdjustResizeBugFix(Activity activity) {
	          mActivity = activity;
	        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
	        mChildOfContent = content.getChildAt(0);
	        statusBarHeight = getStatusBarHeight();
	         mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
	             public void onGlobalLayout() {
	                 possiblyResizeChildOfContent();
	             }
	        });
	         frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
	     }
	     public static void assistActivity(Activity activity) {
	         new AdjustResizeBugFix(activity);
	     }
	     private void possiblyResizeChildOfContent() {
	         int usableHeightNow = computeUsableHeight();
	         if (usableHeightNow != usableHeightPrevious) {
	             int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
	             int heightDifference = usableHeightSansKeyboard - usableHeightNow;
	             if (heightDifference > (usableHeightSansKeyboard / 4)) {
	                 // keyboard probably just became visible
	                 // 如果有高度变化，mChildOfContent.requestLayout()之后界面才会重新测量
	                 // 这里就随便让原来的高度减去了1
	                 frameLayoutParams.height = usableHeightSansKeyboard - 1;
	             } else {
	                 // keyboard probably just became hidden
	                 frameLayoutParams.height = usableHeightSansKeyboard;
	             }
	             mChildOfContent.requestLayout();
	             usableHeightPrevious = usableHeightNow;
	         }
	     }
	     private int computeUsableHeight() {
	         Rect r = new Rect();
	         mChildOfContent.getWindowVisibleDisplayFrame(r);
	         return r.bottom - r.top + statusBarHeight;
	     }
	     private int getStatusBarHeight() {
	         try {
	            Class<?> c = Class.forName("com.android.internal.R$dimen");
	            Object obj = c.newInstance();
	            Field field = c.getField("status_bar_height");
	             int x = Integer.parseInt(field.get(obj).toString());
	             int dimensionPixelSize = mActivity.getResources().getDimensionPixelSize(x);
	             return dimensionPixelSize;
	         } catch (Exception e) {
	            e.printStackTrace();
	        }
	         return 0;
	     }
	 }
