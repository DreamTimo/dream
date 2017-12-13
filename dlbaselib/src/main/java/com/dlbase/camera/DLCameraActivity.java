package com.dlbase.camera;

import com.dlbase.base.DLBaseActivity;
import com.dlbase.camera.DLCameraContainer.TakePictureListener;
import com.dlbase.camera.DLCameraView.FlashMode;
import com.luyz.dlbaselib.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

public class DLCameraActivity extends DLBaseActivity implements TakePictureListener{
	
	public final static String TAG="CameraAty";
	
	public static final String PAGEKEY_VIDEO = "video";
	public static final String PAGEKEY_MAXVIDEOTIMER = "max_videotime";
	
	private boolean mIsRecordMode=false;
	private String mSaveRoot;
	
	private DLCameraContainer mContainer;
	
	private ImageButton mCameraShutterButton;
	private ImageButton mRecordShutterButton;
	
	private ImageView mFlashView;
	private ImageView mSwitchCameraView;
	
	private View mHeaderBar;
	private boolean isRecording=false;
	private long max_video_time=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_dlcamera);

		mIsRecordMode = getIntent().getBooleanExtra(PAGEKEY_VIDEO, false);
		max_video_time = getIntent().getIntExtra(PAGEKEY_MAXVIDEOTIMER, 0);
		
		mHeaderBar=findViewById(R.id.camera_header_bar);
		mContainer=(DLCameraContainer)findViewById(R.id.container);

		mCameraShutterButton=(ImageButton)findViewById(R.id.btn_shutter_camera);
		mRecordShutterButton=(ImageButton)findViewById(R.id.btn_shutter_record);
		mSwitchCameraView=(ImageView)findViewById(R.id.btn_switch_camera);
		mFlashView=(ImageView)findViewById(R.id.btn_flash_mode);

		mCameraShutterButton.setOnClickListener(this);
		mRecordShutterButton.setOnClickListener(this);
		mFlashView.setOnClickListener(this);
		mSwitchCameraView.setOnClickListener(this);

		mSaveRoot=DLFileOperateUtil.DIR_ROOT;
		mContainer.setRootPath(mSaveRoot);
		
		if(mIsRecordMode){
			mCameraShutterButton.setVisibility(View.GONE);
			mRecordShutterButton.setVisibility(View.VISIBLE);
			//录像模式下隐藏顶部菜单 
			mHeaderBar.setVisibility(View.GONE);
			mIsRecordMode=true;
			mContainer.setMaxvideotime(max_video_time);
			mContainer.switchMode(5);
		}else {
			mCameraShutterButton.setVisibility(View.VISIBLE);
			mRecordShutterButton.setVisibility(View.GONE);
			//拍照模式下显示顶部菜单
			mHeaderBar.setVisibility(View.VISIBLE);
			
			if(mContainer.getSwitchCameraNumber()>1){
				mSwitchCameraView.setVisibility(View.VISIBLE);
			}else{
				mSwitchCameraView.setVisibility(View.GONE);
			}
			
			mIsRecordMode=false;
			mContainer.switchMode(0);
			stopRecord();
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
		if(view.getId() == R.id.btn_shutter_camera){
			mCameraShutterButton.setClickable(false);
			mContainer.takePicture(this);
		}else if(view.getId() == R.id.btn_flash_mode){
			if(mContainer.getFlashMode()==FlashMode.ON){
				mContainer.setFlashMode(FlashMode.OFF);
				mFlashView.setImageResource(R.drawable.btn_flash_off);
			}else if (mContainer.getFlashMode()==FlashMode.OFF) {
				mContainer.setFlashMode(FlashMode.AUTO);
				mFlashView.setImageResource(R.drawable.btn_flash_auto);
			}
			else if (mContainer.getFlashMode()==FlashMode.AUTO) {
				mContainer.setFlashMode(FlashMode.TORCH);
				mFlashView.setImageResource(R.drawable.btn_flash_torch);
			}
			else if (mContainer.getFlashMode()==FlashMode.TORCH) {
				mContainer.setFlashMode(FlashMode.ON);
				mFlashView.setImageResource(R.drawable.btn_flash_on);
			}
		}else if(view.getId() == R.id.btn_shutter_record){
			if(!isRecording){
				isRecording=mContainer.startRecord();
				mContainer.setTPListener(this);
				if (isRecording) {
					mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_recording);
				}
			}else {
				stopRecord();	
			}
		}else if(view.getId() == R.id.btn_switch_camera){
			mContainer.switchCamera();
		}
	}

	private void stopRecord() {
		mContainer.stopRecord();
		isRecording=false;
		mRecordShutterButton.setBackgroundResource(R.drawable.btn_shutter_record);
	}
	
	@Override
	public void onTakePictureEnd(Bitmap thumBitmap) {
		mCameraShutterButton.setClickable(true);	
	}

	@Override
	protected void onResume() {		
		super.onResume();
	}

	@Override
	public void onAnimtionEnd(DLCameraModel model) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,DLCameraImageActivity.class);
		intent.putExtra(DLCameraImageActivity.PAGEKEY_CAMERAMODEL, new DLCameraModel(model.getFilePath(),model.getThamnialPath(),model.isVideo()));
		startActivityForResult(intent,900);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == 900){
			if(resultCode == RESULT_OK){
				setResult(RESULT_OK, data);
				finish();
			}
		}
	}

	@Override
	public void onStopRecordEnd() {
		// TODO Auto-generated method stub
		stopRecord();
	}
	
	
}
