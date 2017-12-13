package com.lykj.model;

import com.dlbase.base.DLBaseModel;

public class LYChatListModel extends DLBaseModel{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6142217169311947004L;
	//个人信息
	private LYUserModel fromUserModel;
	//最后一条记录
	private LYChatItemModel lastItemModel;
	//群组信息
	private LYGroupItemModel fromGroupModel;
	//0 个人 1 群组
	private String messageType;
	//新消息数量
	private int newCount;
	//uid
	private String uid;

	public LYChatListModel(){
		newCount = 0;
		messageType = "0";
	}
	
	public LYUserModel getFromUserModel() {
		return fromUserModel;
	}

	public void setFromUserModel(LYUserModel fromUserModel) {
		this.fromUserModel = fromUserModel;
	}

	public LYChatItemModel getLastItemModel() {
		return lastItemModel;
	}

	public void setLastItemModel(LYChatItemModel lastItemModel) {
		this.lastItemModel = lastItemModel;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public LYGroupItemModel getFromGroupModel() {
		return fromGroupModel;
	}

	public void setFromGroupModel(LYGroupItemModel fromGroupModel) {
		this.fromGroupModel = fromGroupModel;
	}

	public int getNewCount() {
		return newCount;
	}

	public void setNewCount(int newCount) {
		this.newCount = newCount;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
}
