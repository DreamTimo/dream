package com.dlbase.util;

import java.util.Locale;

import com.dlbase.app.DLBaseApp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Display;

public class DLSystemUtil {

	/**
	 * 获取应用ID 包名
	 * @return
	 */
	public static String getAppid(){
		return DLBaseApp.getApplication().getPackageName();
	}
	
	/**
	 * 获得版本号
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		
		try {
			PackageManager pm = context.getPackageManager();
	        PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(),
	        		PackageManager.GET_CONFIGURATIONS);
	        return pinfo.versionName;
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 获得版本号
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		
		try {
			PackageManager pm = context.getPackageManager();
	        PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), 
	        		  PackageManager.GET_CONFIGURATIONS);
	        return pinfo.versionCode;
		} catch(Exception ex) {
			ex.printStackTrace();
			return 0;
		}
		
	}
	
	/**
	 * 获得语言信息
	 * @return
	 */
	public static String getLanguage() {
		
		return Locale.getDefault().getLanguage() + "-" + Locale.getDefault().getCountry();
		
	}
	
	/**
	 * 获得imei
	 * @param context
	 * @return
	 */
	public static String getImei(Context context) {
		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		return imei;
		
	}

	/**
	 * 获得imsi
	 * @param context
	 * @return
	 */
	public static String getImsi(Context context) {
		
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		return imsi == null ? "" : imsi;
		
	}
	
	/**
	 * 获得手机类型
	 * @return
	 */
	public static String getPhoneType() {
		
		return Build.MODEL;
		
	}
	
	/**
	 * 获得OS类型
	 * @return
	 */
	public static String getOsType() {
		
		return "android_" + getSdkInfo();
		
	}
	
	/**
	 * 获得sdk level
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getSdkLevel() {
		
		return Build.VERSION.SDK;
	}
	
	/**
	 * 获得sdk信息
	 * @return
	 */
	public static String getSdkInfo() {
		
		return Build.VERSION.RELEASE;
	}
	
	/**
	 * 获得屏幕尺寸
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getScreen(Context context) {
		
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		return display.getWidth() + "*" + display.getHeight();
	}
	//
	/**
	 * Pseudo-Unique ID
	 * @return
	 */
	public static String getPseudoUniqueID(){
		String m_szDevIDShort = "35" + //we make this look like a valid IMEI
				Build.BOARD.length()%10 +
				Build.BRAND.length()%10 +
				Build.CPU_ABI.length()%10 +
				Build.DEVICE.length()%10 +
				Build.DISPLAY.length()%10 +
				Build.HOST.length()%10 +
				Build.ID.length()%10 +
				Build.MANUFACTURER.length()%10 +
				Build.MODEL.length()%10 +
				Build.PRODUCT.length()%10 +
				Build.TAGS.length()%10 +
				Build.TYPE.length()%10 +
				Build.USER.length()%10 ; //13 digits
		return m_szDevIDShort;
	}

	/**
	 * The Android ID
	 * @return
	 */
	public static String getTheAndroidID(ContentResolver resolver){
		String m_szAndroidID = Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
		return m_szAndroidID;
	}

	/**
	 * The WLAN MAC Address string
	 * @return
	 */
	public static String getTheWLANMACAddressString(Context context){
		WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();
		return m_szWLANMAC;
	}

	/**
	 * 获取手机唯一标示
	 * @param context
	 * @return
	 */
	public static String getUdidStr(Context context){
		String m_szLongID = getImei(context) + getPseudoUniqueID()
				+ getTheAndroidID(context.getContentResolver())+ getTheWLANMACAddressString(context);

		return DLMD5Util.md5To32(m_szLongID);
	}
}
