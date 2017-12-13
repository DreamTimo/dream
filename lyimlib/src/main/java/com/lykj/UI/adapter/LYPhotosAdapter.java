package com.lykj.UI.adapter;

import java.util.ArrayList;

import com.luyz.lyimlib.R;
import com.lykj.Photos.LYBitmapCache;
import com.lykj.model.LYImageModel;

import com.dlbase.util.ViewHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class LYPhotosAdapter extends BaseAdapter{

	final String TAG = getClass().getSimpleName();
	private Context mContext;
	private ArrayList<LYImageModel> dataList;
	private ArrayList<LYImageModel> selectedDataList;
	private DisplayMetrics dm;
	LYBitmapCache cache;
	public LYPhotosAdapter(Context c, ArrayList<LYImageModel> dataList,
                           ArrayList<LYImageModel> selectedDataList) {
		mContext = c;
		cache = new LYBitmapCache();
		this.dataList = dataList;
		this.selectedDataList = selectedDataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
	}

	public int getCount() {
		return dataList.size();
	}

	public Object getItem(int position) {
		return dataList.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	LYBitmapCache.ImageCallback callback = new LYBitmapCache.ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap,
				Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};
	


	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, R.layout.adapter_lyphotositem, position);
	       ImageView iv_photos_itemImage = holder.getView(R.id.iv_photos_itemImage);
	       ToggleButton toggle_button = holder.getView(R.id.toggle_button);
	       Button ib_photos_itemSelected = holder.getView(R.id.ib_photos_itemSelected);
	       Button ib_photos_itemUnselect = holder.getView(R.id.ib_photos_itemUnselect);
	       
	       String path;
		if (dataList != null && dataList.size() > position)
			path = dataList.get(position).imagePath;
		else
			path = "camera_default";
		if (path.contains("camera_default")) {
			iv_photos_itemImage.setImageResource(R.drawable.ic_check);
		} else {
//			ImageManager2.from(mContext).displayImage(viewHolder.imageView,
//					path, Res.getDrawableID("plugin_camera_camera_default"), 100, 100);
			final LYImageModel item = dataList.get(position);
			iv_photos_itemImage.setTag(item.imagePath);
			cache.displayBmp(iv_photos_itemImage, item.imageUrl, item.imagePath,
					callback);
		}
		toggle_button.setTag(position);
		ib_photos_itemSelected.setTag(position);
		ib_photos_itemUnselect.setTag(position);
		toggle_button.setOnClickListener(new ToggleClickListener(ib_photos_itemSelected));
		if (selectedDataList.contains(dataList.get(position))) {
			toggle_button.setChecked(true);
			ib_photos_itemSelected.setVisibility(View.VISIBLE);
		} else {
			toggle_button.setChecked(false);
			ib_photos_itemSelected.setVisibility(View.GONE);
			ib_photos_itemUnselect.setVisibility(View.VISIBLE);
		}
		return holder.getConvertView();
	}
	
	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}
	private class ToggleClickListener implements OnClickListener{
		Button chooseBt;
		public ToggleClickListener(Button choosebt){
			this.chooseBt = choosebt;
		}
		
		@Override
		public void onClick(View view) {
			if (view instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton) view;
				int position = (Integer) toggleButton.getTag();
				if (dataList != null && mOnItemClickListener != null
						&& position < dataList.size()) {
					mOnItemClickListener.onItemClick(toggleButton, position, toggleButton.isChecked(),chooseBt);
				}
			}
		}
	}
	

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(ToggleButton view, int position,
				boolean isChecked,Button chooseBt);
	}
}
