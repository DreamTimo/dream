package com.dlbase.upyun;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

import com.dlbase.util.DLBase64Util;

public class UpYunUtils {
	
	public static String makePolicy(String saveKey, long expiration, String bucket) throws UpYunException {

		return makePolicy(saveKey, expiration, bucket, null);
	}
	public static String makePolicy(String saveKey, long expiration, String bucket, HashMap<String, Object> params)
			throws UpYunException {

		if (saveKey == null || saveKey.equals("")) {
			throw new UpYunException(20, "miss param saveKey");
		}
		if (expiration == 0) {
			throw new UpYunException(20, "miss param expiration");
		}
		if (bucket == null || bucket.equals("")) {
			throw new UpYunException(20, "miss param bucket");
		}

		JSONObject obj = new JSONObject();
		try {
			obj.put("save-key", saveKey);
			obj.put("expiration", expiration);
			obj.put("bucket", bucket);

			if (params != null) {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					obj.put(key, params.get(key));
				}
			}

		} catch (JSONException e) {
			throw new UpYunException(21, e.getMessage());
		}
		
		return DLBase64Util.encode(obj.toString());
	}

	public static String signature(String source) {

		try {
			MessageDigest md = MessageDigest.getInstance("MD5");

			md.reset();
			md.update(source.getBytes());

			byte[] mdbytes = md.digest();

			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < mdbytes.length; i++) {
				String hex = Integer.toHexString(0xff & mdbytes[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
