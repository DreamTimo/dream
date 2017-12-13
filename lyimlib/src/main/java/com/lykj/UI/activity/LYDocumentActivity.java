package com.lykj.UI.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dlbase.Model.DLFileModel;
import com.dlbase.base.DLBaseActivity;
import com.dlbase.util.DLCallOtherOpeanFile;
import com.dlbase.util.DLDateUtils;
import com.dlbase.util.DLFileSizeUtil;
import com.dlbase.util.DLFileUtil;
import com.dlbase.util.DLFolderManager;
import com.dlbase.util.DLImageUtil;
import com.dlbase.util.DLStringUtil;
import com.dlbase.video.VideoPlayerActivity;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;
import com.lykj.UI.adapter.LYDocumentAdapter;

/**
 * @author 
 *  文件夹页面 -->聊天进入
 */
public class LYDocumentActivity extends DLBaseActivity{

	public static final String RESULT_KEY_FILE = "seletefile";
	public static final String PAGE_KEY_NOTCHECK = "nocheck";
	public static final int RESULT_CODE_FILE = 0x888;
	private static final int FILE_SELECT_CODE = 0X111;

	private ListView  lv_document_listView;
	 
	private ArrayList<DLFileModel> mlist;
	private LYDocumentAdapter adapter;

	private boolean showCheck;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lydocument);
		
		showCheck = getIntent().getBooleanExtra(PAGE_KEY_NOTCHECK, true);
		
		initView();
		initData();
	}
	  public void initView(){

			initNavView("文件夹", TTopBackType.ETopBack_Black,true,R.id.top_view);
			topRight.setText("文件管理");

			lv_document_listView = (ListView) findViewById(R.id.lv_document_listView);

			lv_document_listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
				DLFileModel tempItem = mlist.get(arg2);
				if(tempItem!=null){

					send(tempItem);
				}
				}
			});
	  }

      
      public void initData(){

    	  mlist = new ArrayList<DLFileModel>();
    	  adapter = new LYDocumentAdapter(mContext,mlist);
  		  lv_document_listView.setAdapter(adapter);

		  adapter.setOpenListener(new View.OnClickListener() {
			  @Override
			  public void onClick(View view) {
					int pot = (int)view.getTag();

				  DLFileModel tempModel = mlist.get(pot);
				  if (tempModel!=null){
					  openFile(tempModel);
				  }
			  }
		  });
  		
  		  mHander.sendEmptyMessage(700);
      }
      
      private void getData(){

		  ArrayList<DLFileModel> tempArray = LyImEngine.getInstance().getDbHelper().getDownFileTable().queryAllDownFile(LyImEngine.getInstance().getUserId());

		  if (tempArray!=null){
			  for (int i = 0; i < tempArray.size(); i++) {
				  DLFileModel tempItem = tempArray.get(i);
				  if(tempItem!=null){

					  boolean hasFile = true;
					  if(showCheck){
						  hasFile = true;
					  }else{
							if(tempItem.getFileType() == DLFileModel.TFileType.EFile_Word ||
							tempItem.getFileType() == DLFileModel.TFileType.EFile_Excel ||
							tempItem.getFileType() == DLFileModel.TFileType.EFile_Pdf ||
							tempItem.getFileType() == DLFileModel.TFileType.EFile_Ppt){
								hasFile = true;
							}
					  }
					  if(hasFile){
						  mlist.add(tempItem);
					  }
				  }
			  }
		  }

      }
      
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.top_left) {
			finish();
		}else if (v.getId() == R.id.top_right) {
			OpenSystemFile();
		}
	}
	
	private void send(DLFileModel model){
		
		Intent intent = new Intent();
		intent.putExtra(RESULT_KEY_FILE, model);
		setResult(RESULT_CODE_FILE, intent);
		finish();
	}
	
	public void OpenSystemFile() {
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType(ContactsContract.Contacts.CONTENT_VCARD_TYPE);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		try {
			startActivityForResult(Intent.createChooser(intent, "请选择文件!"),FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
		}
	}

	/** 根据返回选择的文件，来进行操作 **/
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode == FILE_SELECT_CODE){
			if (resultCode == Activity.RESULT_OK) {
				
				Uri uri = data.getData();
				
				String url = uri.getPath();
				
				if(DLStringUtil.notEmpty(uri.getScheme()) && uri.getScheme().equals("content")){
					url = DLImageUtil.getVideoPathFromURI(getContentResolver(),uri);
				}
				
				Message msg = new Message();
				msg.what = 900;
				msg.obj = url;
				mHander.sendMessage(msg);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@SuppressLint("HandlerLeak")
	public Handler mHander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			if(msg.what == 700){
				getData();
			}else if(msg.what == 900){
				
				String fielPath = (String)msg.obj;

				if(DLStringUtil.notEmpty(fielPath)){
					
					String tempName = fielPath.substring(fielPath.lastIndexOf("/") + 1);

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

					try {
						DLFileUtil.copyFile(fielPath, tempFile.getAbsolutePath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					DLFileModel tempFileModel = new DLFileModel();
					tempFileModel.setAdd(false);
					tempFileModel.setFileName(tempName);
					tempFileModel.setFileSize(DLFileSizeUtil.getFileSize(tempFile.getAbsolutePath())+"");
					tempFileModel.setFilePath(tempFile.getAbsolutePath());
					tempFileModel.setFileTime(DLDateUtils.getCurrentTimeInLong()+"");
					tempFileModel.setUserId(LyImEngine.getInstance().getUserId());
					
					if(LyImEngine.getInstance().getDbHelper()!=null){
						LyImEngine.getInstance().getDbHelper().getDownFileTable().checkAndInsertDownFle(tempFileModel);
					}
					send(tempFileModel);
				}
			}
		}
		
	};


	private void openFile(DLFileModel fileModel){

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
  
}
