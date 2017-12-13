package com.lykj.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class LYDeviceInfoUtils {
	/**
	 * 从dip转化为px
	 * 
	 * @param context
	 * @param dipNum
	 *            要转换的dip值
	 * @return 转化后的像素值
	 */
	public static int fromDipToPx(Context context, int dipNum) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dipNum * dm.densityDpi / 160;
	}

	/**
	 * 从dip转化为px
	 * 
	 * @param context
	 * @param dipNum
	 *            要转换的dip值
	 * @return 转化后的像素值
	 */
	public static float fromDipToPx(Context context, float dipNum) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dipNum * dm.densityDpi / 160;
	}

	public static int fromPxTodip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 取得density比例
	 * 
	 * @param context
	 * @return 当前设备Density的比例
	 */
	public static float getDensityScale(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.densityDpi * 1.0f / 160;
	}



	public static float getDensity(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.densityDpi;
	}
}
