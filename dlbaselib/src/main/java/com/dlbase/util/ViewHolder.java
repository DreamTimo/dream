package com.dlbase.util;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author xinsuinizouyuan
 *
 */
public class ViewHolder {

	private SparseArray<View> mViews;
	private int mPosition = 0;
	private View mConvertView;

	public ViewHolder(Context context,ViewGroup parent,int layoutId,int position) {
		this.setmPosition(position);
		this.mViews = new SparseArray<View>();
		
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}
	
	//入口方法
	public static ViewHolder get(Context context,View convertView,ViewGroup parent,int layoutId,int position){
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}else {
			ViewHolder holder = (ViewHolder)convertView.getTag();
			holder.setmPosition(position);//position
			return holder;
		}
	}
	
	

	/*
	 * 通过ViewId获取控件
	 * 
	 */

	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId){
		
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return  (T)view;
		
	}
	
	public View getConvertView(){
		return mConvertView;
	}

	public int getmPosition() {
		return mPosition;
	}

	public void setmPosition(int mPosition) {
		this.mPosition = mPosition;
	}
	
	/*//对TextView的封装
	public ViewHolder setText(int viewId,String text){
		TextView tv = getView(viewId);
		         tv.setText(text);
		
		return this;
		
	}
	
	//对setImageView的封装
	public ViewHolder setImageResource(int viewId,int resId){
		ImageView view = getView(viewId);
		view.setImageResource(resId);
		return this;
		
	}
	
	//对setImageBitmap的封装
		public ViewHolder setImageBitmap(int viewId,Bitmap bitmap){
			
			ImageView view = getView(viewId);
			view.setImageBitmap(bitmap);
			return this;
			
		}
*/
}
