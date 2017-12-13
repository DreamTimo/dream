package com.lykj.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.dlbase.Model.DLFileModel;
import com.dlbase.Model.DLImageModel;
import com.dlbase.Model.DLLocationModel;
import com.dlbase.Model.DLVideoModel;
import com.dlbase.Model.DLVoiceModel;
import com.dlbase.base.DLBaseModel;
import com.dlbase.util.DLJsonUtil;
import com.dlbase.util.DLStringUtil;


public class LYChatItemModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1262123593707945020L;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public static enum TChatType{
		EChat_Text,
		EChat_Image,
		EChat_Voice,
		EChat_Video,
		EChat_Card,
		EChat_Location,
		EChat_File,
		EChat_Project,
		EChat_System
	}

	private String chatContent;
	private String chatTimer;
	private boolean showTimer;
	private String lastTime;
	//1文字 2图片 3视频 4音频 5文件 6名片 7地理位置 8 项目 9 系统消息
	private TChatType chatType;
	private String contentType;
	private boolean isRead;
	private LYUserModel fromModel;
	private LYUserModel toUserModel;
	private LYGroupItemModel toGroupModel;
	private boolean isfrom;
	private String id;
	private String msgId;
	private String messageType;
	//0 未发送 1 正在发送 2 已发送 3 发送失败
	private String status;
	private String mqttMsgId;
	
	private DLImageModel imageModel;
	private DLVoiceModel voiceModel;
	private DLFileModel fileModel;
	private LYUserModel cardModel;
	private DLVideoModel videoModel;
	private DLLocationModel locationModel;
	private LYProjectModel projectModel;
	
	private String autoId;
	private String uid;
	
	public LYChatItemModel(){
		isRead = false;
		setIsfrom(true);
		status="0";
		setChatType(TChatType.EChat_Text);
		messageType = "0";
		showTimer = true;
	}
	
	public String getSendMessageJson(){
		
		JSONObject tempData = new JSONObject();
		try {
			
			tempData.put("from", DLStringUtil.strToInt(fromModel.getUserId()));
			if(messageType.equals("0")){
				tempData.put("to", DLStringUtil.strToInt(toUserModel.getUserId()));
			}else if(messageType.equals("1")){
				tempData.put("to", DLStringUtil.strToInt(toGroupModel.getGroupId()));
			}
			//1文字 2图片 3视频 4音频 5文件 6名片 7地理位置 8 项目 9 系统消息
			tempData.put("content_type", DLStringUtil.strToInt(contentType));

			tempData.put("type","0");

			switch (getChatType()) {
			case EChat_Text:
				tempData.put("content", chatContent);
				break;
			case EChat_Image:{
				if(getImageModel()!=null){
					JSONObject tempContent = new JSONObject();
					tempContent.put("fileurl", getImageModel().getImageUrl());
					tempData.put("content", tempContent);
				}
			}
				break;
			case EChat_Voice:{
				if(getVoiceModel()!=null){
					JSONObject tempContent = new JSONObject();
					tempContent.put("fileurl", getVoiceModel().getVoiceUrl());
					tempContent.put("voicelength", getVoiceModel().getVoiceLength());
					tempData.put("content", tempContent);
				}
			}
				break;
			case EChat_Video:{
				if(getVideoModel()!=null){
					JSONObject tempContent = new JSONObject();
					tempContent.put("fileurl", getVideoModel().getVideoUrl());
					tempContent.put("length", getVideoModel().getVideoLength());
					tempContent.put("image", getVideoModel().getVideoLogoUrl());
					tempData.put("content", tempContent);
				}
			}
				break;
			case EChat_File:{
				if(getFileModel()!=null){
					JSONObject tempContent = new JSONObject();
					tempContent.put("fileurl", getFileModel().getFileUrl());
					tempContent.put("filename", getFileModel().getFileName());
					tempContent.put("filesize", getFileModel().getFileSize());
					tempData.put("content", tempContent);
				}
			}
				break;
			case EChat_Card:{
				if(getCardModel()!=null){
					tempData.put("content", getCardModel().getUserId());
				}
			}
				break;
			case EChat_Location:{
				if(getLocationModel()!=null){
					JSONObject tempContent = new JSONObject();
					tempContent.put("lat", Double.valueOf(getLocationModel().getLat()));
					tempContent.put("lng", Double.valueOf(getLocationModel().getLng()));
					tempContent.put("fileurl", getLocationModel().getImageUrl());
					tempData.put("content", tempContent);
				}
			}
				break;
			case EChat_Project:{
				if(getProjectModel()!=null){
					tempData.put("content", DLStringUtil.strToInt(getProjectModel().getProjectId()));
				}
			}
				break;
			default:
				break;
			}
			
			// 0个人 1群组
			tempData.put("message_type", DLStringUtil.strToInt(messageType));
			tempData.put("messageId", id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tempData.toString();
	}
	
	public String getToId(){
		if(DLStringUtil.notEmpty(messageType)){
			if(messageType.equals("0")){
				return toUserModel.getUserId();
			}else if(messageType.equals("1")){
				return toGroupModel.getGroupId();
			}
		}
		return null;
	}
	
	public String getToName(){
		if(DLStringUtil.notEmpty(messageType)){
			if(messageType.equals("0")){
				return toUserModel.getUserName();
			}else if(messageType.equals("1")){
				return toGroupModel.getGroupName();
			}
		}
		return null;
	}
	
	public String getToAvatar(){
		if(DLStringUtil.notEmpty(messageType)){
			if(messageType.equals("0")){
				return toUserModel.getAvatar();
			}else if(messageType.equals("1")){
				return toGroupModel.getGroupAvatar();
			}
		}
		return null;
	}
	
	public String getToType(){
		if(DLStringUtil.notEmpty(messageType)){
			if(messageType.equals("0")){
				return toUserModel.getType();
			}else if(messageType.equals("1")){
				return "";
			}
		}
		return null;
	}
	
	public static LYChatItemModel parseJson(String json){
		LYChatItemModel tempChat = new LYChatItemModel();
		
		try {
			JSONObject tempO = new JSONObject(json);

			tempChat.parseJsonObject(tempO);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tempChat;
	}

	public void parseJsonObject(JSONObject data){

		JSONObject tempO = data;

		if(tempO!=null){

			this.setMessageType(DLJsonUtil.hasAndGetString(tempO, "message_type"));
			this.setChatTimer(DLJsonUtil.hasAndGetString(tempO, "timestamp"));
			this.setId(DLJsonUtil.hasAndGetString(tempO, "messageId"));
			this.setContentType(DLJsonUtil.hasAndGetString(tempO, "content_type"));
			this.setMsgId(DLJsonUtil.hasAndGetString(tempO,"id"));

			switch (this.getChatType()) {
				case EChat_Text:{
					this.setChatContent(DLJsonUtil.hasAndGetString(tempO, "content"));
				}
				break;
				case EChat_Image:{
					JSONObject tempContent = DLJsonUtil.hasAndGetJsonObject(tempO, "content");
					if(tempContent!=null){
						DLImageModel tempImage = new DLImageModel();
						tempImage.setImageUrl(DLJsonUtil.hasAndGetString(tempContent, "fileurl"));
						this.setImageModel(tempImage);
					}
				}
				break;
				case EChat_Card:{
					JSONObject tempContent = DLJsonUtil.hasAndGetJsonObject(tempO, "content");
					if(tempContent!=null){
						LYUserModel tempCard = new LYUserModel();
						tempCard.parseJsonDic(tempContent);
						this.setCardModel(tempCard);
					}
				}
				break;
				case EChat_File:{
					JSONObject tempContent = DLJsonUtil.hasAndGetJsonObject(tempO, "content");
					if(tempContent!=null){
						DLFileModel tempFile = new DLFileModel();
						tempFile.setFileUrl(DLJsonUtil.hasAndGetString(tempContent, "fileurl"));
						tempFile.setFileSize(DLJsonUtil.hasAndGetString(tempContent, "filesize"));
						tempFile.setFileName(DLJsonUtil.hasAndGetString(tempContent, "filename"));
						this.setFileModel(tempFile);
					}
				}
				break;
				case EChat_Location:{
					JSONObject tempContent = DLJsonUtil.hasAndGetJsonObject(tempO, "content");
					if(tempContent!=null){
						DLLocationModel tempLocation = new DLLocationModel();
						tempLocation.setLat(DLJsonUtil.hasAndGetString(tempContent, "lat"));
						tempLocation.setLng(DLJsonUtil.hasAndGetString(tempContent, "lng"));
						tempLocation.setImageUrl(DLJsonUtil.hasAndGetString(tempContent, "fileurl"));
						this.setLocationModel(tempLocation);
					}
				}
				break;
				case EChat_Voice:{
					JSONObject tempContent = DLJsonUtil.hasAndGetJsonObject(tempO, "content");
					if(tempContent!=null){
						DLVoiceModel tempVoice = new DLVoiceModel();
						tempVoice.setVoiceUrl(DLJsonUtil.hasAndGetString(tempContent, "fileurl"));
						tempVoice.setVoiceLength(DLJsonUtil.hasAndGetString(tempContent, "voicelength"));
						this.setVoiceModel(tempVoice);
					}
				}
				break;
				case EChat_Video:{
					JSONObject tempContent = DLJsonUtil.hasAndGetJsonObject(tempO, "content");
					if(tempContent!=null){
						DLVideoModel tempVideo = new DLVideoModel();
						tempVideo.setVideoUrl(DLJsonUtil.hasAndGetString(tempContent, "fileurl"));
						tempVideo.setVideoLength(DLJsonUtil.hasAndGetString(tempContent, "length"));
						tempVideo.setVideoLogoUrl(DLJsonUtil.hasAndGetString(tempContent, "image"));
						this.setVideoModel(tempVideo);
					}
				}
				break;
				case EChat_Project:{
					JSONObject tempContent = DLJsonUtil.hasAndGetJsonObject(tempO, "content");
					if(tempContent!=null){
						LYProjectModel tempProjcet = new LYProjectModel();
//						tempProjcet.parseJson(tempContent);
						this.setProjectModel(tempProjcet);
					}
				}
				break;
				default:
					break;
			}

			JSONObject tempFrom = DLJsonUtil.hasAndGetJsonObject(tempO, "from");
			if(tempFrom!=null){
				LYUserModel tempUser = new LYUserModel();
				tempUser.parseJsonDic(tempFrom);
				this.setFromModel(tempUser);
			}

			JSONObject tempTo = DLJsonUtil.hasAndGetJsonObject(tempO, "to");
			if(tempTo!=null){
				if(DLStringUtil.notEmpty(this.getMessageType())){
					if(this.getMessageType().equals("0")){
						LYUserModel tempUser = new LYUserModel();
						tempUser.parseJsonDic(tempTo);
						this.setToUserModel(tempUser);
					}else if(this.getMessageType().equals("1")){

					}
				}
			}
		}
	}
	

	public LYUserModel getFromModel() {
		return fromModel;
	}

	public void setFromModel(LYUserModel fromModel) {
		this.fromModel = fromModel;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public String getChatContent() {
		return chatContent;
	}


	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}


	public String getChatTimer() {
		return chatTimer;
	}


	public void setChatTimer(String chatTimer) {
		this.chatTimer = chatTimer;
	}


	public TChatType getChatType() {
		return chatType;
	}


	public void setChatType(TChatType chatType) {
		this.chatType = chatType;
		
		//1文字 2图片 3视频 4音频 5文件 6名片 7地理位置 8 项目 9 系统消息
		
		switch (chatType) {
		case EChat_Text:
			this.contentType = "1";
			break;
		case EChat_Image:
			this.contentType = "2";
			break;
		case EChat_Video:
			this.contentType = "3";
			break;
		case EChat_Voice:
			this.contentType = "4";
			break;
		case EChat_File:
			this.contentType = "5";
			break;
		case EChat_Card:
			this.contentType = "6";
			break;
		case EChat_Location:
			this.contentType = "7";
			break;
		case EChat_Project:
			this.contentType = "8";
			break;
		case EChat_System:
			this.contentType = "9";
		default:
			break;
		}
	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public Boolean getIsRead() {
		return isRead;
	}


	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public boolean isIsfrom() {
		return isfrom;
	}

	public void setIsfrom(boolean isfrom) {
		this.isfrom = isfrom;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
		
		//1文字 2图片 3视频 4音频 5文件 6名片 7地理位置 8 项目 9 系统消息
		
		if(contentType.equals("1")){
			this.chatType = TChatType.EChat_Text;
		}else if(contentType.equals("2")){
			this.chatType = TChatType.EChat_Image;
		}else if(contentType.equals("3")){
			this.chatType = TChatType.EChat_Video;
		}else if(contentType.equals("4")){
			this.chatType = TChatType.EChat_Voice;
		}else if(contentType.equals("5")){
			this.chatType = TChatType.EChat_File;
		}else if(contentType.equals("6")){
			this.chatType = TChatType.EChat_Card;
		}else if(contentType.equals("7")){
			this.chatType = TChatType.EChat_Location;
		}else if(contentType.equals("8")){
			this.chatType = TChatType.EChat_Project;
		}else if(contentType.equals("9")){
			this.chatType = TChatType.EChat_System;
		}
	}

	public LYUserModel getToUserModel() {
		return toUserModel;
	}

	public void setToUserModel(LYUserModel toUserModel) {
		this.toUserModel = toUserModel;
	}

	public LYGroupItemModel getToGroupModel() {
		return toGroupModel;
	}

	public void setToGroupModel(LYGroupItemModel toGroupModel) {
		this.toGroupModel = toGroupModel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DLVoiceModel getVoiceModel() {
		return voiceModel;
	}

	public void setVoiceModel(DLVoiceModel voiceModel) {
		this.voiceModel = voiceModel;
	}

	public DLFileModel getFileModel() {
		return fileModel;
	}

	public void setFileModel(DLFileModel fileModel) {
		this.fileModel = fileModel;
	}

	public LYUserModel getCardModel() {
		return cardModel;
	}

	public void setCardModel(LYUserModel cardModel) {
		this.cardModel = cardModel;
	}

	public DLVideoModel getVideoModel() {
		return videoModel;
	}

	public void setVideoModel(DLVideoModel videoModel) {
		this.videoModel = videoModel;
	}

	public DLImageModel getImageModel() {
		return imageModel;
	}

	public void setImageModel(DLImageModel imageModel) {
		this.imageModel = imageModel;
	}

	public DLLocationModel getLocationModel() {
		return locationModel;
	}

	public void setLocationModel(DLLocationModel locationModel) {
		this.locationModel = locationModel;
	}

	public String getMqttMsgId() {
		return mqttMsgId;
	}

	public void setMqttMsgId(String mqttMsgId) {
		this.mqttMsgId = mqttMsgId;
	}

	public boolean isShowTimer() {
		return showTimer;
	}

	public void setShowTimer(boolean showTimer) {
		this.showTimer = showTimer;
	}

	public String getLastTime() {
		return lastTime;
	}

	public void setLastTime(String lastTime) {
		this.lastTime = lastTime;
	}

	public String getAutoId() {
		return autoId;
	}

	public void setAutoId(String autoId) {
		this.autoId = autoId;
	}

	public LYProjectModel getProjectModel() {
		return projectModel;
	}

	public void setProjectModel(LYProjectModel projectModel) {
		this.projectModel = projectModel;
	}

	
	
	
}
