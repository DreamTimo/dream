package com.timo.timolib.tools.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	private Md5Util() {}
	private static Md5Util instance;
	public static Md5Util getInstance(){
		if (instance==null){
			instance=new Md5Util();
		}
		return instance;
	}

	public String md5To32(String text) {
		return md5To32(text, "UTF-8");
	}
	public String md5To32(String text, String charset) {
		if(text == null || "".equals(text)) {
			return null;
		}
		MessageDigest msgDigest = null;
		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			msgDigest.update(text.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] bytes = msgDigest.digest();
		return new String(encodeHex(bytes));
	}

	public char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}
		return out;
	}
	private char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
