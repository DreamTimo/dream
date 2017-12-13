package com.lykj.UI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import com.dlbase.Model.DLFileModel;
import com.dlbase.base.DLBaseActivity;
import com.dlbase.util.DLCallOtherOpeanFile;
import com.dlbase.util.DLFileSizeUtil;
import com.dlbase.util.DLFileUtil;
import com.dlbase.util.DLFolderManager;
import com.dlbase.util.DLStringUtil;
import com.dlbase.video.VideoPlayerActivity;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;
import com.lykj.model.LYChatItemModel;

public class LYDocudetailActivity extends DLBaseActivity{
	
	public static final String PAGE_CHATMODEL = "chatModel";
	public static final String PAGE_FILEMODEL = "fileModel";
	
	private ImageView iv_docudetail_image;
	private TextView tv_docudetail_cardImage,tv_docudetail_cardState,tv_docudetail_cardSize;
	private Button bt_docudetail_download;
	private ProgressBar pb_docudetail_progress;
	
	
	private DLFileModel fileModel;
	private int pos=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lydocudetail);
		
		pos = getIntent().getIntExtra(PAGE_CHATMODEL, -1);
		
		fileModel = (DLFileModel)getIntent().getSerializableExtra(PAGE_FILEMODEL);
		
		initView();
	}
	
	public void initView(){
		
		initNavView("文件", TTopBackType.ETopBack_Black, false, R.id.top_view);
		
		iv_docudetail_image = (ImageView) findViewById(R.id.iv_docudetail_image);
		tv_docudetail_cardImage = (TextView) findViewById(R.id.tv_docudetail_cardImage);
		tv_docudetail_cardSize = (TextView) findViewById(R.id.tv_docudetail_cardSize);
		tv_docudetail_cardState = (TextView) findViewById(R.id.tv_docudetail_cardState);

		bt_docudetail_download = (Button) findViewById(R.id.bt_docudetail_download);
		pb_docudetail_progress  = (ProgressBar) findViewById(R.id.pb_docudetail_progress);
		//点击事件监听
		
		bt_docudetail_download.setOnClickListener(this);

		pb_docudetail_progress.setVisibility(View.GONE);
		bt_docudetail_download.setVisibility(View.GONE);
		tv_docudetail_cardState.setVisibility(View.GONE);
		
		if(fileModel!=null){

			if(DLStringUtil.notEmpty(fileModel.getFileTypeIcon())){
				iv_docudetail_image.setImageResource(DLStringUtil.strToInt(fileModel.getFileTypeIcon()));
			}
			
			if(DLStringUtil.notEmpty(fileModel.getFileName())){
				tv_docudetail_cardImage.setText(fileModel.getFileName());
			}
			
			if(DLStringUtil.notEmpty(fileModel.getFileSize())){
				tv_docudetail_cardSize.setText(DLFileSizeUtil.FormetFileSize(DLStringUtil.strToLong(fileModel.getFileSize())));
			}

			if (fileModel.getDownState() == 2 || fileModel.getDownState() == 3){

				File tempFile = new File(DLFolderManager.getFileFolderForUser(LyImEngine.getInstance().getUserId()),fileModel.getFileName());

				fileModel.setFilePath(tempFile.getAbsolutePath());

				if(DLFileUtil.isFileExist(tempFile.getAbsolutePath())){

					pb_docudetail_progress.setVisibility(View.GONE);
					bt_docudetail_download.setText("查看文件");
					bt_docudetail_download.setVisibility(View.VISIBLE);

				}else{
					bt_docudetail_download.setText("下载文件");
					bt_docudetail_download.setVisibility(View.VISIBLE);
				}
			}
			mHandle.sendEmptyMessage(890);
		}
	} 

	Handler mHandle = new Handler(){

		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			if (msg.what == 890){
				startDown();
			}else if (msg.what == 891){
				openFile(fileModel.getFilePath());
			}
		}
	};

	private void downFile(){
		
		pb_docudetail_progress.setVisibility(View.VISIBLE);
		pb_docudetail_progress.setProgress(0);
		pb_docudetail_progress.incrementProgressBy(1);

		String tempName = fileModel.getFileName();

		if (fileModel.getDownState() == 0){
			File tempFile = new File(DLFolderManager.getFileFolderForUser(LyImEngine.getInstance().getUserId()),tempName);
			if(tempFile!=null){
				if(DLFileUtil.isFileExist(tempFile.getAbsolutePath())){
					String exp = DLFileUtil.getExtension(tempName);
					String name = tempName.substring(0,tempName.length()-exp.length()-1);
					int index = 0;
					do {
						index++;
						tempName = name+"_"+index+"."+exp;
						tempFile = new File(DLFolderManager.getFileFolderForUser(LyImEngine.getInstance().getUserId()),tempName);
					}while (DLFileUtil.isFileExist(tempFile.getAbsolutePath()));
				}
			}
		}

		final String tempNewName = tempName;

		fileModel.setFileName(tempNewName);

		if (LyImEngine.getInstance().getLyimListener()!=null) {
			LyImEngine.getInstance().getLyimListener().onDownFile(fileModel, tempNewName, new LyImEngine.onDownListener() {
				@Override
				public void onDown(Object model, LYChatItemModel.TChatType type) {

					fileModel = (DLFileModel) model;

					pb_docudetail_progress.setVisibility(View.GONE);
					bt_docudetail_download.setText("查看文件");
					fileModel.setFileName(tempNewName);
					fileModel.setDownState(2);
					fileModel.setUserId(LyImEngine.getInstance().getUserId());

					if(DLStringUtil.notEmpty(fileModel.getFileName())){
						tv_docudetail_cardImage.setText(fileModel.getFileName());
					}

					if (LyImEngine.getInstance().getDbHelper() != null) {
						LyImEngine.getInstance().getDbHelper().getDownFileTable().checkAndInsertDownFle(fileModel);
					}

					mHandle.sendEmptyMessage(891);
				}

				@Override
				public void onProgressChange(long fileSize, long downloadedSize) {
					int progress = (int) (downloadedSize * 100 / fileSize);
					pb_docudetail_progress.setProgress(progress);
				}
			});
		}
	}
	
	private void openFile(String path){
		
		if(fileModel!=null){
			
			if(fileModel.getFileType() == DLFileModel.TFileType.EFile_Image){
				
				ArrayList<String> tempStr = new ArrayList<String>();
				tempStr.add(fileModel.getFileUrl());
				Intent intent = new Intent(mContext,LYShowPhotosActivity.class);
				intent.putExtra(LYShowPhotosActivity.PAGEKEY_PHOTOS, tempStr);
				intent.putExtra(LYShowPhotosActivity.PAGEKEY_POSITION, 0);
				startActivity(intent);
				
			}else if(fileModel.getFileType() == DLFileModel.TFileType.EFile_Video){
				
				Intent intent = new Intent(mContext,VideoPlayerActivity.class);
				intent.putExtra(VideoPlayerActivity.PAGEKEY_URL, fileModel.getFileUrl());
				intent.putExtra(VideoPlayerActivity.PAGEKEY_NAME, fileModel.getFileName());
				startActivity(intent);
				
			}else{
				
				File tempFile = new File(DLFolderManager.getFileFolderForUser(LyImEngine.getInstance().getUserId()),fileModel.getFileName());
				DLCallOtherOpeanFile.openFile(tempFile, mContext);
			}
		}
	}
	
	private void close(){

		Intent intent = new Intent();
		if(pos>-1){
			intent.putExtra(PAGE_CHATMODEL, pos);
		}
		intent.putExtra("filemodel", fileModel);
		setResult(RESULT_OK,intent);

		finish();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		close();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.top_left) {
			close();
		}else if(v.getId() == R.id.bt_docudetail_download){
			startDown();
		}
	}

	private void startDown(){
		if(fileModel!=null && DLStringUtil.notEmpty(fileModel.getFileUrl())){

			File tempFile = new File(DLFolderManager.getFileFolderForUser(LyImEngine.getInstance().getUserId()),fileModel.getFileName());
			if(tempFile!=null){

				if(DLFileUtil.isFileExist(tempFile.getAbsolutePath()) && fileModel.getDownState() == 2){
					openFile(tempFile.getAbsolutePath());
				}else{
					downFile();
				}
			}
		}
	}
   
}
