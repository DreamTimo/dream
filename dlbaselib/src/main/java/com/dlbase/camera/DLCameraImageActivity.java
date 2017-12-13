package com.dlbase.camera;

import com.dlbase.base.DLBaseActivity;
import com.dlbase.util.DLFileUtil;
import com.luyz.dlbaselib.R;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class DLCameraImageActivity extends DLBaseActivity {

	public static final String PAGEKEY_CAMERAMODEL = "cameramodel";
	
	private ImageView lv_view,videoicon,iv_cancel,iv_sure;

	private DLCameraModel cameraModel;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cameraimage);

		cameraModel = (DLCameraModel)getIntent().getSerializableExtra(PAGEKEY_CAMERAMODEL);
		
		lv_view = (ImageView)findViewById(R.id.lv_view);
		videoicon = (ImageView)findViewById(R.id.videoicon);
		
		iv_cancel = (ImageView)findViewById(R.id.iv_cancel);
		iv_sure = (ImageView)findViewById(R.id.iv_sure);
		
		iv_sure.setOnClickListener(this);
		iv_cancel.setOnClickListener(this);
		
		if(cameraModel!=null){
		
			if(cameraModel.isVideo()){
				videoicon.setVisibility(View.VISIBLE);
				if(cameraModel.getThamnialPath()!=null){
					Bitmap tempBm = DLFileUtil.getBitmapFromSDCard(cameraModel.getThamnialPath());
					if (tempBm!=null){
						lv_view.setImageBitmap(tempBm);
					}
					//IRequest.loadSDCARDImage(cameraModel.getThamnialPath(), lv_view);
				}
			}else{
				videoicon.setVisibility(View.GONE);
		
				if(cameraModel.getFilePath()!=null){
					Bitmap tempBm = DLFileUtil.getBitmapFromSDCard(cameraModel.getFilePath());
					if (tempBm!=null){
						lv_view.setImageBitmap(tempBm);
					}
					//IRequest.loadSDCARDImage(cameraModel.getFilePath(), lv_view);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		
		if(v.getId() == R.id.iv_cancel){
			
			DLFileOperateUtil.deleteSourceFile(cameraModel.getFilePath(), mContext);
			
			finish();
		}else if(v.getId() == R.id.iv_sure){
			
			Intent intent = new Intent();
			intent.putExtra(PAGEKEY_CAMERAMODEL, cameraModel);
			setResult(RESULT_OK,intent);
			finish();
		}
	}
	
	
		
}
