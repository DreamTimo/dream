package com.lykj.model;


import org.json.JSONException;
import org.json.JSONObject;

import com.dlbase.base.DLBaseModel;
import com.dlbase.util.DLJsonUtil;
import com.dlbase.util.DLStringUtil;
import com.luyz.lyimlib.LyImEngine;

public class LYTopisItemModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7882799275557292909L;

	private String userId;
	private String qos;
	private String name;
	//0 系统消息  1 一对一 2 群聊
	private String type;
	
	private String id;
	private String target_id;
	private String createtime;
	
	public LYTopisItemModel(){
		qos = "1";
	}

	public void parseJson(JSONObject data){
		if(data!=null){

			this.setUserId(LyImEngine.getInstance().getUserId());

			this.setTarget_id(DLJsonUtil.hasAndGetString(data, "target_uid"));
			this.setName(DLJsonUtil.hasAndGetString(data, "name"));
			this.setQos(DLJsonUtil.hasAndGetString(data, "qos"));
			this.setType(DLJsonUtil.hasAndGetString(data, "type"));
			
			this.setId(DLJsonUtil.hasAndGetString(data, "id"));
			this.setCreatetime(DLJsonUtil.hasAndGetString(data, "created_at"));
		}
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTarget_id() {
		return target_id;
	}


	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}


	public String getCreatetime() {
		return createtime;
	}


	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getQos() {
		return qos;
	}

	public void setQos(String qos) {
		this.qos = qos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "uid:"+userId+"--qos:"+qos+"--name:"+name+"--type:"+type;
	}
	
	
}
