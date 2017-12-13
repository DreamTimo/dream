package com.lykj.UI.activity;

import java.util.ArrayList;
import java.util.List;
import com.dlbase.base.DLBaseActivity;
import com.dlbase.photoutil.DLPhotoView;
import com.luyz.lyimlib.R;
import com.lykj.Photos.LYBimp;
import com.lykj.UI.view.LYViewPagerFixed;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author xinsuinizouyuan
 *  相册 -->滑动预览
 */
public class LYGalleryActivity extends DLBaseActivity{

	//获取前一个activity传过来的position
	private int position;
	//当前的位置
	private int location = 0;
	
	private ArrayList<View> listViews = null;
	private LYViewPagerFixed iv_newphoto_image;
	private MyPageAdapter adapter;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();

	RelativeLayout photo_relativeLayout;
	
	private TextView tv_newphoto_count;
    private	Button bt_newphoto_send;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lygallery);
	
		initNavView("相册", TTopBackType.ETopBack_Black, false,R.id.top_view);
    	tv_newphoto_count = (TextView) findViewById(R.id.tv_newphoto_count);
    	
    	bt_newphoto_send = (Button) findViewById(R.id.bt_newphoto_send);
    	bt_newphoto_send.setOnClickListener(new GallerySendListener());
		
		position = Integer.parseInt(getIntent().getStringExtra("position"));
		isShowOkBt();
		// 为发送按钮设置文字
		iv_newphoto_image = (LYViewPagerFixed) findViewById(R.id.iv_newphoto_image);
		
		iv_newphoto_image.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < LYBimp.tempSelectBitmap.size(); i++) {
			initListViews( LYBimp.tempSelectBitmap.get(i).getBitmap());
		}
		
		adapter = new MyPageAdapter(listViews);
		iv_newphoto_image.setAdapter(adapter);
		
		iv_newphoto_image.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.px0));
		int id = getIntent().getIntExtra("ID", 0);
		iv_newphoto_image.setCurrentItem(id);

	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.top_left) {
			finish();
		}
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		DLPhotoView img = new DLPhotoView(mContext);
		img.setBackgroundColor(0xff000000);
		img.setImageBitmap(bm);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		listViews.add(img);
	}

	// 完成按钮的监听
	private class GallerySendListener implements OnClickListener {
		public void onClick(View v) {
			setResult(RESULT_OK);
			finish();
		}
	}
	
	public void isShowOkBt() {
		
		if (LYBimp.tempSelectBitmap.size() > 0) {
			bt_newphoto_send.setText("发送");
			tv_newphoto_count.setText(""+LYBimp.tempSelectBitmap.size());
			bt_newphoto_send.setPressed(true);
			bt_newphoto_send.setClickable(true);
			bt_newphoto_send.setTextColor(Color.parseColor("#03A9F4"));
		} else {
			bt_newphoto_send.setPressed(false);
			bt_newphoto_send.setClickable(false);
			bt_newphoto_send.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}
	
	
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((LYViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((LYViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
				
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
	
}

