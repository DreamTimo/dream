package com.timo.timolib.tools.utils;

import android.util.Log;

import com.timo.timolib.Timo_BaseConstancts;

public class LogUtil {

	private String TAG = Timo_BaseConstancts.TAG;
	private boolean isLoggable = Timo_BaseConstancts.ISLOG;
	public static LogUtil instance;
	public static LogUtil getInstance(){
		if(instance==null){
			instance=new LogUtil();
		}
		return instance;
	}

	public void e(String msg) {
		if (isLoggable) {
			Log.e(TAG, msg);
		}
	}
	public void printErrorMessage(Exception e) {
		if (isLoggable) {
			e.printStackTrace();
		}
	}
}