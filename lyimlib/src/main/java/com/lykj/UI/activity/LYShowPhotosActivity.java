package com.lykj.UI.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.dlbase.Model.DLImageModel;
import com.dlbase.base.DLBaseActivity;
import com.dlbase.photoutil.DLPhotoView;
import com.dlbase.photoutil.DLPhotoViewAttacher.OnViewTapListener;
import com.dlbase.util.DLArrayUtil;
import com.dlbase.util.DLFolderManager;
import com.dlbase.util.DLImageUtil;
import com.dlbase.util.DLMD5Util;
import com.dlbase.util.DLShowDialog;
import com.dlbase.util.DLShowDialog.DialogListener;
import com.dlbase.util.DLStringUtil;
import com.dlbase.util.DLToastUtil;
import com.luyz.lyimlib.LYIMConfig;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;
import com.lykj.UI.view.LYViewPagerFixed;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;


public class LYShowPhotosActivity extends DLBaseActivity {

	public static final String PAGEKEY_POSITION = "position";
	public static final String PAGEKEY_PHOTOS = "photos";
	
	//获取前一个activity传过来的position
	private int position;
	//当前的位置
	private int location = 0;
	
	private ArrayList<View> listViews = null;
	private LYViewPagerFixed iv_newphoto_image;
	private ShowPhotosPageAdapter adapter;

	private ArrayList<String> photosArray;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lyshowphotos);
	
		//initNavView("图片", TTopBackType.ETopBack_Black, false,R.id.top_view);
    	
		position = getIntent().getIntExtra(PAGEKEY_POSITION, 0);
		photosArray = getIntent().getStringArrayListExtra(PAGEKEY_PHOTOS);
		
		// 为发送按钮设置文字
		iv_newphoto_image = (LYViewPagerFixed) findViewById(R.id.iv_newphoto_image);
		
		iv_newphoto_image.setOnPageChangeListener(pageChangeListener);
		
		if(DLArrayUtil.notEmpty(photosArray)){
			for (int i = 0; i < photosArray.size(); i++) {
				initListViews(photosArray.get(i));
			}
		}
		
		adapter = new ShowPhotosPageAdapter(listViews);
		
		iv_newphoto_image.setAdapter(adapter);
		
		iv_newphoto_image.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.px0));
		
		iv_newphoto_image.setCurrentItem(position);
		
		

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		photosArray.clear();
		
		super.onDestroy();
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
	
	private void initListViews(final String bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		
		DLPhotoView img = new DLPhotoView(mContext);
		img.setScaleType(ScaleType.FIT_CENTER);
		img.setBackgroundColor(0xff000000);
		
		if(DLStringUtil.notEmpty(bm)){
			if(DLImageUtil.isImageUrl(bm)){
				LyImEngine.getInstance().DownImage(bm+LYIMConfig.THUMBNAILScreen,img,R.drawable.img_default);
			}else{
				LyImEngine.getInstance().loadSDCARDImage(bm, img);
			}
		}
		
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		
		img.setOnViewTapListener(new OnViewTapListener() {
			
			@Override
			public void onViewTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		img.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				
				DLShowDialog.getInstance().showClewDialog(mContext, "发送给好友", "保存到相册", new DialogListener() {
					
					@Override
					public void onSubmitClick(Object obj) {
						// TODO Auto-generated method stub
						if(obj.equals("first")){
							
							if(DLImageUtil.isImageUrl(bm)){
								
								DLImageModel tempImage = new DLImageModel();
								tempImage.setImageUrl(bm);
								
//								Intent intent = new Intent(mContext,ForwardingActivity.class);
//								intent.putExtra(ForwardingActivity.PAGEKEY_IMAGEMODEL, tempImage);
//								startActivity(intent);
								
							}
						}else if(obj.equals("second")){
							
							File file = new File(DLFolderManager.getPhotoFolderForUser(LyImEngine.getInstance().getUserId()), DLMD5Util.md5To32(bm));
							
							// 其次把文件插入到系统图库
						    try {
						    	String fileName = System.currentTimeMillis() + ".jpg";
						        MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
										file.getAbsolutePath(), fileName, null);
						    } catch (FileNotFoundException e) {
						        e.printStackTrace();
						    }
						    // 最后通知图库更新
						    mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
						    
						    DLToastUtil.showToastShort(mContext, "已保存到相册");
						}
					}
					
					@Override
					public void onCancleClick(Object obj) {
						// TODO Auto-generated method stub
						
					}
				});
				
				return false;
			}
		});
		
		listViews.add(img);
	}

	class ShowPhotosPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public ShowPhotosPageAdapter(ArrayList<View> listViews) {
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
