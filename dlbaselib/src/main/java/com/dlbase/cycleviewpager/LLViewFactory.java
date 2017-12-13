package com.dlbase.cycleviewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.dlbase.util.DLScreenUtil;
import com.luyz.dlbaselib.R;

/**
 * ImageView创建工厂
 */
@SuppressLint("InflateParams")
public class LLViewFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 * @return
	 */
	public static ImageView getImageView(final Context context, String url,Bitmap dbmp) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_llbanner, null);
		if(dbmp!=null){
			imageView.setImageBitmap(dbmp);
		}
		if(url!=null && url.length()>0){
			
			double w = DLScreenUtil.getScreenWidth(context)*0.75;
			double h = DLScreenUtil.getScreenHeight(context)*0.75;
			
			//IRequest.displayImage(url, imageView, dbmp,(int)w,(int)h);
		}
		return imageView;
	}
	/**
	 * 获取ImageView视图的同时加载显示url
	 * @return
	 */
	public static ImageView getImageView(Context context) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(
				R.layout.view_llbanner, null);
		return imageView;
	}
}
