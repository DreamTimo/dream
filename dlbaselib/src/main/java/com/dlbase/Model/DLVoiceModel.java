package com.dlbase.Model;

import com.dlbase.base.DLBaseModel;

public class DLVoiceModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7867732851196312419L;

	private String voicePath;
	private String voiceUrl;
	private String voiceLength;
	private boolean isDown;
	private boolean isPlay;
	private boolean isRead;
	
	public DLVoiceModel(){
		isDown = false;
	}

	public String getVoicePath() {
		return voicePath;
	}

	public void setVoicePath(String voicePath) {
		this.voicePath = voicePath;
	}

	public String getVoiceUrl() {
		return voiceUrl;
	}

	public void setVoiceUrl(String voiceUrl) {
		this.voiceUrl = voiceUrl;
	}

	public String getVoiceLength() {
		return voiceLength;
	}

	public void setVoiceLength(String voiceLength) {
		this.voiceLength = voiceLength;
	}

	public boolean isDown() {
		return isDown;
	}

	public void setDown(boolean isDown) {
		this.isDown = isDown;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	
	
}
