package com.dlbase.Model;

import java.io.File;

import com.dlbase.base.DLBaseModel;
import com.dlbase.util.DLDateUtils;
import com.dlbase.util.DLFolderManager;
import com.dlbase.util.DLImageUtil;

import android.graphics.Bitmap;

public class DLLocationModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8230520961860123837L;

	private String lat;
	private String lng;
	private String imagePath;
	private Bitmap imageBitmap;
	private String imageUrl;
	
	public DLLocationModel(){
		
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	
	
	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
		if(imageBitmap!=null){
			File tempPath = new File(DLFolderManager.getTempFolder(),DLDateUtils.getCurrentTimeInLong()+".jpg");
	    	DLImageUtil.saveBitmapFile(tempPath.getAbsolutePath(), imageBitmap);
	    	setImagePath(tempPath.getAbsolutePath());
		}
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
