package com.dlbase.util;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

/**
 * Toast统一管理类
 * 
 * @author luyz
 */
public class DLToastUtil {
	
	private DLToastUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static boolean isShow = true;

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showToastShort(Context context, CharSequence message) {
		show(context, message, Toast.LENGTH_SHORT);
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showToastShort(Context context, int resId) {
		show(context, resId, Toast.LENGTH_SHORT);
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showToastLong(Context context, CharSequence message) {
		show(context, message, Toast.LENGTH_LONG);
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showToastLong(Context context, int resId) {
		show(context, resId, Toast.LENGTH_LONG);
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void showToast(Context context, CharSequence message, int duration) {
		show(context, message, duration);
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void showToast(Context context, int resid, int duration) {
		show(context, resid, duration);
	}

	public static void showToast(Context context, String text, int gravity, int xOffset, int yOffset, int duration) {
		if (isShow) {
			Toast toast = Toast.makeText(context, text, duration);
			toast.setGravity(gravity, xOffset, yOffset);
			toast.setDuration(duration);
			toast.show();
		}
	}

	public static void showToast(Context context, String text, View view, int gravity, int xOffset, int yOffset,
			int duration) {
		if (isShow) {
			Toast toast = new Toast(context);
			toast.setView(view);
			toast.setText(text);
			toast.setGravity(gravity, xOffset, yOffset);
			toast.setDuration(duration);
			toast.show();
		}
	}
    public static void show(Context context, CharSequence text, int duration) {
    	if (isShow)
    		Toast.makeText(context, text, duration).show();
    }
    public static void show(Context context, int resId, int duration) {
    	if (isShow)
    		Toast.makeText(context, resId, duration).show();
    }

    public static void showToast(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void showToast(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }
    

}
