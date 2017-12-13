package com.lykj.model;

import android.graphics.Bitmap;

import com.dlbase.Model.DLImageModel;
import com.lykj.Photos.LYBimp;

import java.io.IOException;

/**
 * Created by luyz on 2017/5/26.
 */

public class LYImageModel extends DLImageModel {

    public LYImageModel() {
        super();
    }

    @Override
    public Bitmap getBitmap() {

        //相册中滑动时，进行判断使用
		if(imageBitmap == null){
			try {
				imageBitmap = LYBimp.revitionImageSize(imagePath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return imageBitmap;
    }
}
