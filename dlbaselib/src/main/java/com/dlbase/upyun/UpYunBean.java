package com.dlbase.upyun;

import org.json.JSONException;
import org.json.JSONObject;

import com.dlbase.util.DLJsonUtil;

public class UpYunBean {

	private String url ;
	
	public UpYunBean(){
		
	}
	
	public static UpYunBean parseJson(String json){
		UpYunBean bean = new UpYunBean();
		
		try {
			JSONObject tempData = new JSONObject(json);
			
			int code = DLJsonUtil.hasAndGetInt(tempData, "code");
			if(code == 200){
				bean.setUrl(DLJsonUtil.hasAndGetString(tempData, "url"));
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bean;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
