package com.dlbase.util;

import android.util.Base64;

public class DLBase64Util {
	// 加密
	public static String encode(String str) {
		if(str == null || "".equals(str)) {
			return null;
		}
		return new String(Base64.encode(str.getBytes(), Base64.DEFAULT));
	}

	// 解密
	public static String decode(String str) {
		if(str == null || "".equals(str)) {
			return null;
		}
		return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
	}
}
