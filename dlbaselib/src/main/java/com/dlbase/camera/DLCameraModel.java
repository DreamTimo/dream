package com.dlbase.camera;

import com.dlbase.base.DLBaseModel;

public class DLCameraModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1728343625242398654L;

	private String filePath;
	private String thamnialPath;
	private boolean video;
	
	public DLCameraModel(){
		
	}
	
	public DLCameraModel(String path,String image,boolean mVideo){
		
		setFilePath(path);
		setThamnialPath(image);
		setVideo(mVideo);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getThamnialPath() {
		return thamnialPath;
	}

	public void setThamnialPath(String thamnialPath) {
		this.thamnialPath = thamnialPath;
	}

	public boolean isVideo() {
		return video;
	}

	public void setVideo(boolean video) {
		this.video = video;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
}
