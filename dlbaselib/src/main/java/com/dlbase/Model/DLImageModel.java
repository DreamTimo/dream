package com.dlbase.Model;


import java.io.IOException;
import com.dlbase.base.DLBaseModel;

import android.graphics.Bitmap;

public class DLImageModel extends DLBaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3851161743386219000L;

	
	public String imageId;
	public String imagePath;
	public Bitmap imageBitmap;
	public String imageUrl;
	
	private boolean isadd ;
	
	public DLImageModel(){
		isadd = true; 
	}

	public String getImageId() {
		return imageId;
	}


	public void setImageId(String imageId) {
		this.imageId = imageId;
	}


	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Bitmap getBitmap() {
		//相册中滑动时，进行判断使用
//		if(imageBitmap == null){
//			try {
//				imageBitmap = Bimp.revitionImageSize(imagePath);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		return imageBitmap;
	}
	
	public Bitmap getImageBitmap() {
		return imageBitmap;
	}

	public void setImageBitmap(Bitmap imageBitmap) {
		this.imageBitmap = imageBitmap;
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

	public boolean isIsadd() {
		return isadd;
	}

	public void setIsadd(boolean isadd) {
		this.isadd = isadd;
	}
	
	
}
