package com.dlbase.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DLStreamUtil {

	private DLStreamUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	/**
	 * InputStream输入流转Byte数组
	 * @param InputStream输入流
	 * @return Byte数组
	 * @throws IOException
	 */
	public static byte[] InputStreamToByte(InputStream is) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = is.read()) != -1) {
			bytestream.write(ch);
		}
		byte outdata[] = bytestream.toByteArray();
		bytestream.close();
		return outdata;
	}
}
