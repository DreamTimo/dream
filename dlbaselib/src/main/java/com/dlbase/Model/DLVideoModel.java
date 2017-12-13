package com.dlbase.Model;


import com.dlbase.base.DLBaseModel;

import android.graphics.Bitmap;

public class DLVideoModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6148981141432174859L;

	private String videoPath;
	private String videoUrl;
	private String videoLogoPath;
	private String videoLogoUrl;
	private String videoLength;
	private Bitmap videoLogo;
	private boolean isAdd;
	
	public DLVideoModel(){
		isAdd = true;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoLogoPath() {
		return videoLogoPath;
	}

	public void setVideoLogoPath(String videoLogoPath) {
		this.videoLogoPath = videoLogoPath;
	}

	public String getVideoLogoUrl() {
		return videoLogoUrl;
	}

	public void setVideoLogoUrl(String videoLogoUrl) {
		this.videoLogoUrl = videoLogoUrl;
	}

	public String getVideoLength() {
		return videoLength;
	}

	public void setVideoLength(String videoLength) {
		this.videoLength = videoLength;
	}

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Bitmap getVideoLogo() {
		return videoLogo;
	}

	public void setVideoLogo(Bitmap videoLogo) {
		this.videoLogo = videoLogo;
	}
	
	
	
}
