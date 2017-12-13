package com.lykj.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dlbase.pinyin.DLPinyinModel;
import com.dlbase.util.DLJsonUtil;
import com.dlbase.util.DLStringUtil;


/**
 * @author luyz
 * 用户基础信息
 */
public class LYUserModel extends DLPinyinModel{

	private static final long serialVersionUID = -8720895562340302029L;

	private String userId;
	private String nickName;
	private String trueName;
	private String phone;
	/**
	 * 头像
	 */
	private String avatar;
	/**
	 * //用户类型(0普通用户，1投资人，2投资机构，3企业管理人)
	 */
	private String type;
	//显示用的userName
	private String userName;
	////0 添加（通讯录） 1 已添加 2 接受（别人加你）3邀请
	private String applyType;
	/**
	 * //-2 没关系 -1 被驳回 0 已申请对方好友 1 好友 －3 未注册
	 */
	private String status;

	public LYUserModel(){
		applyType = "2";
	}

	public void parseJsonDic(JSONObject data){
		if (data!=null){
			this.setUserId(DLJsonUtil.hasAndGetString(data, "id"));
			this.setTrueName(DLJsonUtil.hasAndGetString(data, "name"));
			this.setNickName(DLJsonUtil.hasAndGetString(data, "nick"));
			this.setAvatar(DLJsonUtil.hasAndGetString(data, "avatar"));
			this.setType(DLJsonUtil.hasAndGetString(data, "type"));
			this.setPhone(DLJsonUtil.hasAndGetString(data,"phone"));

			setFirstName();
		}
	}

	/**
	 * 获取用户名称
	 * @return
	 */
	public String getUserName(){
		
		if(DLStringUtil.notEmpty(getTrueName())){
			setUserName(getTrueName());
		}else if(DLStringUtil.notEmpty(getNickName())){
			setUserName(getNickName());
		}else if(DLStringUtil.notEmpty(getPhone())){
			setUserName(getPhone().substring(0,3)+"****"+getPhone().substring(getPhone().length()-4));
		}
		
		return userName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTrueName() {
		return trueName;
	}


	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getAvatar() {
		return avatar;
	}


	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setFirstName(){
		if(DLStringUtil.notEmpty(getTrueName())){
			setName(getTrueName());
		}else if(DLStringUtil.notEmpty(getNickName())){
			setName(getNickName());
		}else if(DLStringUtil.notEmpty(getPhone())){
			setName(getPhone().substring(0,3)+"****"+getPhone().substring(getPhone().length()-4));
		}
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;

		if(DLStringUtil.notEmpty(status)){
			if(status.equals("-2")){
				setApplyType("0");
			}else if(status.equals("-1")){
				setApplyType("0");
			}else if(status.equals("0")){
				setApplyType("1");
			}else if(status.equals("1")){
				setApplyType("1");
			}else if(status.equals("-3")){
				setApplyType("3");
			}
		}
	}
}
