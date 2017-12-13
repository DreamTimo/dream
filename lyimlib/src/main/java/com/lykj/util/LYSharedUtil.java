package com.lykj.util;

import com.dlbase.util.DLPreferencesUtil;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 公共数据存放类
 * @author 
 *
 */

public class LYSharedUtil {
	
	private static final String install_flag = "install_flag";

	private static final String notification_type = "notification_type";
	private static final String voice_type = "voice_type";
	private static final String voice_offon = "voice_offon";
	private static final String shake_offon = "shake_offon";
	private static final String offline_max = "offlinemax";
	
	private LYSharedUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	public static boolean getInstallFlag(Context context) {
		return getSharedPreferences(context).getBoolean(install_flag, false);
	}
	
	public static void setInstallFlag(Context context) {
		getSharedPreferences(context).edit().putBoolean(install_flag, true).commit();
	}
	
	public static String getOffline_max(Context context,String userId) {
		return getSharedPreferences(context).getString(offline_max+userId, null);
	}
	
	public static void setOffline_max(Context context,String userId, String max) {
		getSharedPreferences(context).edit().putString(offline_max+userId, max).commit();
	}

	
	/**
	 * Get SharedPreferences
	 */
	private static SharedPreferences getSharedPreferences(Context context) {
		return DLPreferencesUtil.getSharedPreferences(context);
	}

	/**
	 * 选择消息通知模式
	 *
	 */
	
	public static boolean getNotificationType(Context context) {
		return getSharedPreferences(context).getBoolean(notification_type, true);
	}
	
	public static void setNotificationType(Context context,boolean show) {
		getSharedPreferences(context).edit().putBoolean(notification_type, show).commit();
	}
	
	/**
	 * 选择声音模式
	 *
	 */
	
	public static boolean getVoiceOffOn(Context context) {
		return getSharedPreferences(context).getBoolean(voice_offon, true);
	}
	
	public static void setVoiceOffOn(Context context,boolean offon) {
		getSharedPreferences(context).edit().putBoolean(voice_offon, offon).commit();
	}
	
	/**
	 * 选择震动模式
	 *
	 */
	
	public static boolean getSharkOffOn(Context context) {
		return getSharedPreferences(context).getBoolean(shake_offon, true);
	}
	
	public static void setSharkOffOn(Context context,boolean offon) {
		getSharedPreferences(context).edit().putBoolean(shake_offon, offon).commit();
	}
	
	/**
	 * 声音选择
	 *
	 */
	
	public static String getVoiceType(Context context) {
		return getSharedPreferences(context).getString(voice_type, "0");
	}
	
	public static void setVoiceType(Context context,String voiceType) {
		getSharedPreferences(context).edit().putString(voice_type, voiceType).commit();
	}

	
}
