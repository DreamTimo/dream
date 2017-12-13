package com.dlbase.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 * @author luyz
 *
 */
public class DLMD5Util {

	private DLMD5Util() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String md5To16(String text) {
		return md5To16(text, "UTF-8");
	}
	
	public static String md5To32(String text) {
		return md5To32(text, "UTF-8");
	}
	
	public static String md5To16(String text, String charset) {
		return md5To32(text, charset).substring(8, 24);
	}
	
	public static String md5To32(String text, String charset) {
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

	public static char[] encodeHex(byte[] data) {
		int l = data.length;
		char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}
		return out;
	}

	public static void main(String[] args) {
		System.out.println(DLMD5Util.md5To32("aaaa"));
		System.out.println(DLMD5Util.md5To16("aaaa"));
	}

}
