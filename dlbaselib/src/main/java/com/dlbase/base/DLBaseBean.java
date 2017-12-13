package com.dlbase.base;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.dlbase.util.DLJsonUtil;

public abstract class DLBaseBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2309802232576811119L;
	private String msg;
	private int code;
	private String timestamp;
	
	public DLBaseBean() {
		super();
	}
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

	public static <T> T newClass(Class<T> cls,String json){
		T b = null;
		try {
			b = cls.newInstance();
			((DLBaseBean)b).parseJson(json);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return b;
	}

	public JSONObject parseJsonObject(String  json){
		
		JSONObject tempObject = null;
		try {
			tempObject = new JSONObject(json);
			
			if(tempObject!=null){

				setCode(DLJsonUtil.hasAndGetInt(tempObject, "code"));
				setMsg(DLJsonUtil.hasAndGetString(tempObject, "msg"));
				setTimestamp(DLJsonUtil.hasAndGetString(tempObject, "timestamp"));

			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		return tempObject;
	}

	public void parseJson(String json){
		parseObject(parseJsonObject(json));
	}

	public abstract void parseObject(JSONObject object);

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
