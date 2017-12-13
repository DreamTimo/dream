package com.lykj.UI.activity;

import com.dlbase.Model.DLFileModel;
import com.dlbase.Model.DLImageModel;
import com.dlbase.Model.DLLocationModel;
import com.dlbase.Model.DLVoiceModel;
import com.dlbase.base.DLBaseActivity;
import com.dlbase.util.DLArrayUtil;
import com.dlbase.util.DLAudioUtil;
import com.dlbase.util.DLCapturePhotoHelper;
import com.dlbase.util.DLCapturePhotoHelper.DLCapturePhotoListener;
import com.dlbase.util.DLDateUtils;
import com.dlbase.util.DLFolderManager;
import com.dlbase.util.DLImageUtil;
import com.dlbase.util.DLKeyBoardUtil;
import com.dlbase.util.DLMD5Util;
import com.dlbase.util.DLSDCardUtil;
import com.dlbase.util.DLShowDialog;
import com.dlbase.util.DLShowDialog.DialogListener;
import com.dlbase.util.DLStringUtil;
import com.dlbase.util.DLToastUtil;
import com.dlbase.view.XListView;
import com.dlbase.view.XListView.IXListViewListener;

import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;
import com.lykj.MQTT.LYMQTTHelper;
import com.lykj.Photos.LYBimp;
import com.lykj.UI.adapter.LYExpressionPagerAdapter;
import com.lykj.UI.adapter.LYMessageAdapter;
import com.lykj.emojicon.EmojiconEditText;
import com.lykj.emojicon.EmojiconGridFragment;
import com.lykj.emojicon.EmojiconHandler;
import com.lykj.emojicon.emoji.Emojicon;
import com.lykj.emojicon.emoji.People;
import com.lykj.model.LYChatItemModel;
import com.lykj.model.LYGroupItemModel;
import com.lykj.model.LYPageModel;
import com.lykj.model.LYProjectModel;
import com.lykj.model.LYUserModel;
import com.lykj.util.ToolsUtil;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class LYChatActivity extends DLBaseActivity implements OnClickListener ,SensorEventListener,EmojiconGridFragment.OnEmojiconClickedListener{

	public static final String PAGE_USERMODEL = "user_model";
	public static final String PAGE_GROUPMODEL = "group_model";
	public static final String PAGE_MESSAGETYPE = "message_type";
	public static final int REQUEST_CODE_PICTURE = 1;
	public static final int REQUEST_CODE_CAMERA = 2;
	private static final int REQUEST_CODE_LOCATIONG = 3;
	private static final int REQUEST_CODE_DOCUMENT = 5;
	private static final int REQUEST_CODE_PERSONAL_CARD = 4;
	public static final int REQUEST_CODE_TEXT = 6;
	private View recordingContainer;
    private ImageView micImage;
    private TextView recordingHint,recordingTimer;
    private XListView  listView;
    private EmojiconEditText mEditTextContent;
    private View buttonSetModeKeyboard;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    private LinearLayout emojiIconContainer;
    private LinearLayout btnContainer;
    private View more;
    private ViewPager expressionViewpager;
    
    private Drawable[] micImages;
    
    public static LYChatActivity activityInstance = null;
    // 给谁发送消息
    private String toChatUsername;
    private LYMessageAdapter adapter;
    private ArrayList<LYChatItemModel> mlist;
    
    public static int resendPos;
    private ImageView iv_emoticons_normal;
    private ImageView iv_emoticons_checked;
    
    private RelativeLayout edittext_layout;
    private Button btnMore;
    public String playMsgId;

    String myUserNick = "";
    String myUserAvatar = "";
    String toUserNick="";
    String toUserAvatar="";
    // 分享的照片
    String iamge_path = null;
    
    public static final int REQUEST_CODE_ADD_TO_BLACKLIST = 25;
    
    private LYUserModel toUserModel;
    private LYGroupItemModel toGroupModel;
    private String messageType="0";//0 个人 1 群组
    
    private DLCapturePhotoHelper captureHelper;
    
    private File voicePath;
    private MediaRecorder voiceRecorder;
    private int voiceTimer;
    
    private DLVoiceModel currPlayVoiceModel = null;
    private SensorManager sensorManager = null; // 传感器管理器
    private Sensor mProximiny = null; // 传感器实例
                                                                                        
    private float f_proximiny; // 当前传感器距离
    
    private LYPageModel pageModel;
    
    private int imagetype = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lychat);
		
		toUserModel = (LYUserModel)getIntent().getSerializableExtra(LYChatActivity.PAGE_USERMODEL);
		messageType = (String)getIntent().getSerializableExtra(LYChatActivity.PAGE_MESSAGETYPE);
		toGroupModel = (LYGroupItemModel)getIntent().getSerializableExtra(LYChatActivity.PAGE_GROUPMODEL);
		
		pageModel = new LYPageModel(0, 10, false);
		
		captureHelper = new DLCapturePhotoHelper((FragmentActivity) this);
		
		captureHelper.setListener(new DLCapturePhotoListener() {
			
			@Override
			public void handleVideo(Bitmap bitmap, String videoPath, String imagePath) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void handleImage(Bitmap bitmap, String path) {
				// TODO Auto-generated method stub
				sendPicture(path, false);
			}
		});

		LYMQTTHelper.getInstance().setChatMessageListener(new LYMQTTHelper.onChatMessageListener() {
			@Override
			public void toSendMessage(LYChatItemModel model) {
				if (model!=null){
					handleSendMsg(model);
				}
			}

			@Override
			public void toReceiveMessage(LYChatItemModel model) {
				if (model!=null){
					handleReceiveMsg(model);
				}
			}
		});

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    mProximiny = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		
		initView();

        setUpView();
	}

	private void handleSendMsg(LYChatItemModel model){

		LYChatItemModel tempResult = null;
		int index = -1;

		model.setIsfrom(false);

		if (mlist!=null){
			for (int i=mlist.size()-1;i>-1;i--){
				LYChatItemModel tempItem = mlist.get(i);
				if (tempItem!=null){
					if (tempItem.getId()!=null && tempItem.getId().equals(model.getId())){
						tempResult = tempItem;
						index = i;
						break;
					}
				}
			}
			if (tempResult!=null && index>-1){
				mlist.remove(index);
				mlist.add(index,model);
			}
		}

		adapter.refresh();

		listView.setSelection(listView.getCount() - 1);
	}

	private void handleReceiveMsg(LYChatItemModel model){
		addMessageToList(model);
	}
	
	 @Override
	  protected void onResume() {
	    // TODO Auto-generated method stub
	    super.onResume();
	    // 注册传感器
	    sensorManager.registerListener(this, mProximiny,SensorManager.SENSOR_DELAY_NORMAL);
	  }
	                                                                                      
	  @Override
	  protected void onPause() {
	    // TODO Auto-generated d method stub
	    super.onPause();
	    // 取消注册传感器
	    sensorManager.unregisterListener(this);
	  }
	  
	  /*
	   * 实现SensorEventListener需要实现的两个方法。
	   */
	  @Override
	  public void onSensorChanged(SensorEvent event) {
	    // TODO Auto-generated method stub
		  f_proximiny = event.values[0];
		  DLAudioUtil.playFromReceiver(this, f_proximiny == mProximiny.getMaximumRange());
	  }
	  
	  @Override
	  public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	  }
	
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		voiceStop();
		LyImEngine.getInstance().setCurrChatActivity(null);

		 LYMQTTHelper.getInstance().setChatMessageListener(null);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		LyImEngine.getInstance().setCurrChatActivity(this);
	}

	protected void initView() {

		String title = "";
		if(toUserModel!=null&&!TextUtils.isEmpty(toUserModel.getUserName())){
			title = toUserModel.getUserName();
		}
		
	    initNavView(title, TTopBackType.ETopBack_Black, true,R.id.top_view);
	    topRight.setBackgroundResource(R.drawable.ic_chatsigle);
	    
        recordingContainer = findViewById(R.id.recording_container);
        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);
        recordingTimer = (TextView) findViewById(R.id.recording_timer);
        listView = (XListView) findViewById(R.id.list);
        mEditTextContent =  (EmojiconEditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        expressionViewpager = (ViewPager) findViewById(R.id.vPager);
        emojiIconContainer = (LinearLayout) findViewById(R.id.ll_face_container);
        btnContainer = (LinearLayout) findViewById(R.id.ll_btn_container);
        iv_emoticons_normal = (ImageView) findViewById(R.id.iv_emoticons_normal);
        iv_emoticons_checked = (ImageView) findViewById(R.id.iv_emoticons_checked);

        btnMore = (Button) findViewById(R.id.btn_more);
        iv_emoticons_normal.setVisibility(View.VISIBLE);
        iv_emoticons_checked.setVisibility(View.INVISIBLE);
        more = findViewById(R.id.more);

        listView.removeLoadMore();
        listView.setPullRefreshEnable(true);
        
        // 动画资源文件,用于录制语音时
        micImages = new Drawable[] {
                getResources().getDrawable(R.drawable.record_animate_01),
                getResources().getDrawable(R.drawable.record_animate_02),
                getResources().getDrawable(R.drawable.record_animate_03),
                getResources().getDrawable(R.drawable.record_animate_04),
                getResources().getDrawable(R.drawable.record_animate_05),
             };

        initEmojiData();
        
        edittext_layout.requestFocus();
        
        buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        mEditTextContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                edittext_layout.setVisibility(View.VISIBLE);
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
            }
        });
        // 监听文字框
        mEditTextContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (!TextUtils.isEmpty(s)) {
                    btnMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    btnMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
	
	private void initEmojiData(){
		// 表情list
        Emojicon[] tempEmj = People.DATA;
        // 初始化表情viewpager
        
        ArrayList<EmojiconGridFragment> tempFragments = new ArrayList<EmojiconGridFragment>();
        
        Emojicon[] tempIetms = new Emojicon[32];
        int tempIndex = 0;
        for (int i = 0; i < tempEmj.length; i++) {
			
        	tempIetms[tempIndex] = tempEmj[i];
        	tempIndex++;
        	if(tempIndex>30){
        		tempIetms[31] = Emojicon.fromDelIcon(R.drawable.delete_expression); 
        		tempFragments.add(EmojiconGridFragment.newInstance(tempIetms));
        		
        		tempIndex = 0;
        		tempIetms = new Emojicon[32];
        	}
		}
        
        expressionViewpager.setAdapter(new LYExpressionPagerAdapter(getSupportFragmentManager(),tempFragments));
	}
	
	@Override
	public void onEmojiconClicked(Emojicon emojicon) {
		// TODO Auto-generated method stub
		EmojiconHandler.input(mEditTextContent, emojicon);
	}
	
	@Override
	public void onEmojiconBackspaceClicked(View v) {
		// TODO Auto-generated method stub
		EmojiconHandler.backspace(mEditTextContent);
	}
		
   @SuppressLint("ClickableViewAccessibility")
   private void setUpView() {
	   
        activityInstance = this;
        iv_emoticons_normal.setOnClickListener(this);
        iv_emoticons_checked.setOnClickListener(this);
       
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
        mlist = new ArrayList<LYChatItemModel>();
        adapter = new LYMessageAdapter(this,mlist);
        adapter.setAvatarListener(avatarListener);
        adapter.setItemListener(itemListener);
        adapter.setLongListener(longListener);
        adapter.setResendListener(resendListenr);
        // 显示消息
        listView.setAdapter(adapter);

        listView.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                
            	DLKeyBoardUtil.closeKeybord(mEditTextContent, LYChatActivity.this);
            	
                more.setVisibility(View.GONE);
                iv_emoticons_normal.setVisibility(View.VISIBLE);
                iv_emoticons_checked.setVisibility(View.INVISIBLE);
                emojiIconContainer.setVisibility(View.GONE);
                btnContainer.setVisibility(View.GONE);
                
                return false;
            }
        });
        
        listView.setXListViewListener(new IXListViewListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				getData(true);
			}
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
			}
		});
        
        getData(false);
    }
	   
    private void getData(boolean isMore){
	
    	if(isMore){
			pageModel.setRefresh(true);
		}else{
			mlist.clear();
			pageModel.setNextMax(0);
		}
    	
    	if(DLStringUtil.notEmpty(messageType) && LyImEngine.getInstance().getDbHelper()!=null){
    		if(messageType.equals("0")){
    			if(toUserModel!=null){

    				int maxCount = LyImEngine.getInstance().getDbHelper().getCharTable().queryAllCharTotalCount(toUserModel.getUserId(),LyImEngine.getInstance().getUserId(),messageType);

    				if(maxCount==0){
    					return;
    				}

    				ArrayList<LYChatItemModel> tempArray = LyImEngine.getInstance().getDbHelper().getCharTable().queryAllChar(toUserModel.getUserId(), pageModel.getLimit(), pageModel.getNextMax(),LyImEngine.getInstance().getUserId(),messageType);

    				if(DLArrayUtil.notEmpty(tempArray)){

    					pageModel.setNextMax(pageModel.getNextMax()+tempArray.size());

    					String lastTimer = null ;
	    				for (int i = 0; i < tempArray.size(); i++) {

	    					LYChatItemModel tempItem = tempArray.get(i);
	    					if(tempItem!=null){

	    						if(DLStringUtil.isEmpty(lastTimer)){
	    							lastTimer = tempItem.getChatTimer();
	    							tempItem.setShowTimer(true);
	    						}else{
	    							if(DLDateUtils.getTimeDiff(DLStringUtil.strToLong(tempItem.getChatTimer()), DLStringUtil.strToLong(lastTimer))==0){
	    								tempItem.setShowTimer(false);
	    							}else{
	    								lastTimer = tempItem.getChatTimer();
	    								tempItem.setShowTimer(true);
	    							}
	    						}
	    						tempItem.setLastTime(lastTimer);
	    					}
						}

    					if(pageModel.isRefresh()){
    						listView.stopRefresh();
    						pageModel.setRefresh(false);
    						mlist.addAll(0,tempArray);
    					}else{
    						mlist.addAll(tempArray);
    					}

    					if(pageModel.getNextMax()>=maxCount){
    						listView.setPullRefreshEnable(false);
    					}

    				}else{
    					listView.stopRefresh();
						pageModel.setRefresh(false);
    				}
    			}
    		}else if(messageType.equals("1")){
    			if(toGroupModel!=null){
    				mlist.addAll(LyImEngine.getInstance().getDbHelper().getCharTable().queryAllChar(toGroupModel.getGroupId(),  pageModel.getLimit(), pageModel.getNextMax(),LyImEngine.getInstance().getUserId(),messageType));
    			}
    		}
    	}
    	
    	adapter.refresh();
   
        int count = listView.getCount();
        if (count > 0) {
            listView.setSelection(count - 1);
        }
   }
	   
	   //图像点击事件
	   public OnClickListener avatarListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {

			LYChatItemModel tempItem = mlist.get((int)v.getTag());
			if(tempItem!=null){
				if(tempItem.getFromModel()!=null){
					toUserHomeActivity(tempItem.getFromModel());
				}
			}
			}
		};
			
		private void voicePlay(String path,DLVoiceModel tempVoice){
			currPlayVoiceModel = tempVoice;
			currPlayVoiceModel.setPlay(true);
			DLAudioUtil.playSpeaker(LYChatActivity.this, true);
			DLAudioUtil.play(path, myHandler);
		}
		
		private void voiceStop(){
			if(currPlayVoiceModel!=null){
				currPlayVoiceModel.setPlay(false);
			}
			currPlayVoiceModel = null;
			DLAudioUtil.stop();
		}
		
		@SuppressLint("HandlerLeak")
		public Handler myHandler = new Handler() {  
	        public void handleMessage(Message msg) {   
	             switch (msg.what) {   
	                  case 600:   {
	                  	if(currPlayVoiceModel!=null){
	                  		currPlayVoiceModel.setPlay(false);
	                  		voiceStop();
	                  	}
	                  	adapter.refresh();
	                  }
	                       break; 
	                  case 500:{
	                	  LYChatItemModel tempItem = (LYChatItemModel)msg.obj;
	                	  if(tempItem!=null){
	                		  	tempItem.setStatus("3");
							  	LyImEngine.getInstance().getDbHelper().getCharTable().checkAndInsertCharData(tempItem.getToId(), tempItem);
	    				  		adapter.refresh();
	                	  }
	                  }
	                	  break;
	                   default:
	                  	 break;
	             }   
	             super.handleMessage(msg);   
	        }   
	   };
	   
	   public OnClickListener resendListenr = new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LYChatItemModel tempItem = mlist.get((int)v.getTag());
				if(tempItem!=null){

					tempItem.setStatus("1");

					LYMQTTHelper.getInstance().sendMessage(LYChatActivity.this,tempItem,new LYMQTTHelper.onChatPublishListener(){

						@Override
						public void publishSuccess(String msgId) {
							adapter.refresh();
						}

						@Override
						public void publishFail(String msgId) {
							adapter.refresh();
						}
					});
				}
			}
	  };
		
	  //内容点击事件	
	  public  OnClickListener itemListener = new OnClickListener() {
		
			@SuppressLint("UseValueOf")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				@SuppressWarnings("unchecked")
				HashMap<String, String> tempMap = (HashMap<String, String>) arg0.getTag();
				
				if(tempMap!=null){
					String tempPos = tempMap.get("position");
					final String tempType = tempMap.get("type");
					
					final LYChatItemModel tempItem = mlist.get(DLStringUtil.strToInt(tempPos));
					if(tempItem!=null){
						//1文字 2图片 3视频 4音频 5文件 6名片 7地理位置 8项目
						
						switch (DLStringUtil.strToInt(tempType)) {
						case 1:
							
							break;
						case 2:{
							ArrayList<String> tempImags = new ArrayList<String>();
							int tempIndex = 0;
							for (int i = 0; i < mlist.size(); i++) {
								LYChatItemModel tempChatItem = mlist.get(i);
								if(tempChatItem!=null){
									if(tempChatItem.getChatType() == LYChatItemModel.TChatType.EChat_Image){
										if(DLImageUtil.isImageUrl(tempChatItem.getImageModel().getImageUrl())){
											tempImags.add(tempChatItem.getImageModel().getImageUrl());
											if(DLStringUtil.notEmpty(tempChatItem.getId()) && tempChatItem.getId().equals(tempItem.getId())){
												tempIndex = tempImags.size()-1;
											}
										}else{
											tempImags.add(tempChatItem.getImageModel().getImagePath());
											if(DLStringUtil.notEmpty(tempChatItem.getId()) && tempChatItem.getId().equals(tempItem.getId())){
												tempIndex = tempImags.size()-1;
											}
										}
									}
								}
							}
							if (tempIndex<0){
								tempIndex = 0;
							}
							Intent intent = new Intent(LYChatActivity.this,LYShowPhotosActivity.class);
							intent.putExtra(LYShowPhotosActivity.PAGEKEY_PHOTOS, tempImags);
							intent.putExtra(LYShowPhotosActivity.PAGEKEY_POSITION, tempIndex);
							startActivity(intent);
						}
							break;
						case 3:{
							
						}
							break;
						case 4:{
							final DLVoiceModel tempVoice = tempItem.getVoiceModel();
							if(tempVoice!=null){
								boolean hasplay = true;
								if(currPlayVoiceModel!=null){
									if(currPlayVoiceModel.getVoiceUrl().equals(tempVoice.getVoiceUrl())){
										voiceStop();
										hasplay = false;
									}else{
										currPlayVoiceModel.setPlay(false);
									}
								}
								if(hasplay){
									if(DLStringUtil.notEmpty(tempVoice.getVoicePath())){
										voicePlay(tempVoice.getVoicePath(),tempVoice);
									}else if(DLStringUtil.notEmpty(tempVoice.getVoiceUrl())){
//
										if (LyImEngine.getInstance().getLyimListener()!=null){
											LyImEngine.getInstance().getLyimListener().onDownVoice(tempVoice, new LyImEngine.onDownListener() {
												@Override
												public void onDown(Object model, LYChatItemModel.TChatType type) {

													DLVoiceModel tempVoice2 = (DLVoiceModel) model;

													tempVoice.setVoicePath(tempVoice2.getVoicePath());

													voicePlay(tempVoice.getVoicePath(),tempVoice);
												}

												@Override
												public void onProgressChange(long fileSize, long downloadedSize) {

												}
											});
										}
									}
								}
								
								adapter.refresh();
							}
						}
							break;
						case 5:{
							DLFileModel tempFile = tempItem.getFileModel();
							if(tempFile!=null){
								Intent intent = new Intent(LYChatActivity.this,LYDocudetailActivity.class);
								intent.putExtra(LYDocudetailActivity.PAGE_FILEMODEL, tempFile);
								intent.putExtra(LYDocudetailActivity.PAGE_CHATMODEL, DLStringUtil.strToInt(tempPos));
								startActivityForResult(intent,567);
							}
						}
							break;
						case 6:{
							if(tempItem.getCardModel()!=null){
								toUserHomeActivity(tempItem.getCardModel());
							}
						}
							break;
						case 7:{
							if(tempItem.getLocationModel()!=null){
								Intent intent = new Intent(LYChatActivity.this, LYMapActivity.class);

								intent.putExtra(LYMapActivity.PAGE_USERMODEL, tempItem.getFromModel());
								intent.putExtra(LYMapActivity.PAGE_LAT, tempItem.getLocationModel().getLat());
								intent.putExtra(LYMapActivity.PAGE_LNG, tempItem.getLocationModel().getLng());

								startActivity(intent);

							}
						}
							break;
						case 8:{
							if(tempItem.getProjectModel()!=null){

								if (LyImEngine.getInstance().getLyimListener()!=null){
									LyImEngine.getInstance().getLyimListener().onToProject(LYChatActivity.this,tempItem.getFromModel(),tempItem.getProjectModel());
								}
							}
						}
							break;
						default:
							break;
						}
					}
				}
			}
	  }; 
	
		//长按事件
		public OnLongClickListener longListener = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View arg0) {
			
			@SuppressWarnings("unchecked")
			HashMap<String, String> tempMap = (HashMap<String, String>) arg0.getTag();
			
			if(tempMap!=null){
				String tempPos = tempMap.get("position");
				final String tempType = tempMap.get("type");
				
				final LYChatItemModel tempItem = mlist.get(DLStringUtil.strToInt(tempPos));
				if(tempItem!=null){
					//1文字 2图片 3视频 4音频 5文件 6名片 7地理位置
					ArrayList<String> tempData = new ArrayList<String>();
					switch (DLStringUtil.strToInt(tempType)) {
					case 1:
						tempData.add("复制");
						tempData.add("删除");
//						tempData.add("转发");
						
						break;
					case 2:
						tempData.add("删除");
//						tempData.add("转发");
						tempData.add("保存到相册");
						
						break;
					case 3:
						tempData.add("删除");
//						tempData.add("转发");
						
						break;
					case 4:
						tempData.add("删除");
					
						break;
					case 5:
						tempData.add("删除");
//						tempData.add("转发");
						tempData.add("下载");
						
						break;
					case 6:
						tempData.add("删除");
//						tempData.add("转发");
						
						break;
					case 7:
						tempData.add("删除");
//						tempData.add("转发");
						
						break;
					case 8:
						tempData.add("删除");
//						tempData.add("转发");
						break;
					default:
						break;
					}
					
					DLShowDialog.getInstance().showChatDialog(LYChatActivity.this,tempData,new DialogListener() {
						
						@Override
						public void onSubmitClick(Object obj) {
							// TODO Auto-generated method stub
							int tempIndex = (int)obj;
							switch (DLStringUtil.strToInt(tempType)) {
							case 1:
								if (tempIndex == 0) { //复制
									copy(tempItem.getChatContent());
								}else if (tempIndex == 1) {//删除
									deleteItem(tempItem);
								}else if (tempIndex == 2) { //转发
									toSendOther(tempItem);
								}
								break;
							case 2:
                                if (tempIndex == 0) { //删除
                                	deleteItem(tempItem);
								}else if (tempIndex == 1) { //转发
									toSendOther(tempItem);
								}else if (tempIndex == 2) { //保存
									saveImageToSystem(tempItem);
								}
								break;
							case 3:
								if (tempIndex == 0) { //删除
									deleteItem(tempItem);
								}else if (tempIndex == 1) { //转发
									toSendOther(tempItem);
								}
								break;
							case 4:
								if (tempIndex == 0) { //删除
									deleteItem(tempItem);
								}
								break;
							case 5:
								if (tempIndex == 0) { //删除
									deleteItem(tempItem);
								}else if (tempIndex == 1) { //转发
									toSendOther(tempItem);
								}
								break;
							case 6:
								if (tempIndex == 0) { //删除
									deleteItem(tempItem);
								}else if (tempIndex == 1) { //转发
									toSendOther(tempItem);
								}else if (tempIndex == 2) { //保存
									
								}
								break;
							case 7:
								if (tempIndex == 0) { //删除
									deleteItem(tempItem);
							    }else if (tempIndex == 1) { //转发
							    	toSendOther(tempItem);
							    }
								break;
							case 8:
								if (tempIndex == 0) { //删除
									deleteItem(tempItem);
							    }else if (tempIndex == 1) { //转发
							    	toSendOther(tempItem);
							    }
								break;
							default:
								break;
							}
						}
						
						@Override
						public void onCancleClick(Object obj) {
							// TODO Auto-generated method stub
							
						}
					});
				}
			}
			
			return false;
			}
		};
	
		private void saveImageToSystem(LYChatItemModel model){
			if(model!=null && model.getImageModel()!=null){
				if(DLImageUtil.isImageUrl(model.getImageModel().getImageUrl())){
					File file = new File(DLFolderManager.getPhotoFolderForUser(LyImEngine.getInstance().getUserId()), DLMD5Util.md5To32(model.getImageModel().getImageUrl()));

					// 其次把文件插入到系统图库
					try {
						String fileName = System.currentTimeMillis() + ".jpg";
						MediaStore.Images.Media.insertImage(LYChatActivity.this.getContentResolver(),
								file.getAbsolutePath(), fileName, null);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					// 最后通知图库更新
					LYChatActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));

					DLToastUtil.showToastShort(LYChatActivity.this, "已保存到相册");
				}
			}
		}

		private void toSendOther(LYChatItemModel model){

	//		Intent intent = new Intent(mContext,ForwardingActivity.class);
	//		if(model.getChatType() == LYChatItemModel.TChatType.EChat_Project){
	//			intent.putExtra(ForwardingActivity.PAGEKEY_PROJECT, model.getProjectModel());
	//		}else{
	//			intent.putExtra(ForwardingActivity.PAGEKEY_CHATMODEL, model);
	//		}
	//		startActivity(intent);
		}

		private void deleteItem(LYChatItemModel model){
			if(model!=null){
				String tempStr = null;
				if(messageType.equals("0")){
					tempStr = toUserModel.getUserId();
				}else if(messageType.equals("1")){
					tempStr = toGroupModel.getGroupId();
				}
				if(DLStringUtil.notEmpty(tempStr) && LyImEngine.getInstance().getDbHelper()!=null){
					LyImEngine.getInstance().getDbHelper().getCharTable().deleteChar(tempStr, model);
					LYChatItemModel tempLastItem = LyImEngine.getInstance().getDbHelper().getCharTable().queryLastItem(tempStr,LyImEngine.getInstance().getUserId(),messageType);
					if(tempLastItem!=null){
						LYMQTTHelper.getInstance().insertAndUpdateCharList(tempLastItem);
					}
					mlist.remove(model);
				}
				adapter.notifyDataSetChanged();
			}
		}

		private void copy(String text){
			if(DLStringUtil.notEmpty(text)){
				ClipboardManager myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
				ClipData myClip = ClipData.newPlainText("text", text);
				myClipboard.setPrimaryClip(myClip);
				DLToastUtil.showToastShort(LYChatActivity.this, "已复制到剪切板");
			}
		}

		private void toUserHomeActivity(LYUserModel model){

			if(model!=null){
				if (LyImEngine.getInstance().getLyimListener()!=null){
					LyImEngine.getInstance().getLyimListener().onToUser(LYChatActivity.this,model);
				}
			}
		}
	
	
	
	   @Override
		public void onBackPressed() {
		// TODO Auto-generated method stub
		
		closePage();
		
//		super.onBackPressed();
		}

	   private void closePage(){

		   finish();
	   }
	   
		/**
	     * 消息图标点击事件
	     *
	     */
	    @Override
	    public void onClick(View v) {
	        if (v.getId() == R.id.top_left) {
	        	closePage();
			}else if (v.getId() == R.id.top_right) {//个人主页
	        	
				if(DLStringUtil.notEmpty(messageType)){
					if(messageType.equals("0")){
						if(toUserModel!=null){
			        		toUserHomeActivity(toUserModel);
			        	}
					}else if(messageType.equals("1")){
						if(toGroupModel!=null){
			        		
			        	}
					}
				}
	        }else if (v.getId() == R.id.btn_send) {// 点击发送按钮(发文字和表情)
	            sendText(mEditTextContent.getText().toString());
	        } else if (v.getId() == R.id.btn_picture) { // 点击图片图标
	        	selectPicFromLocal();
	        } else if (v.getId() == R.id.btn_takephoto) { //拍照片
	        	selectPicFromCamera();
	        } else if (v.getId() == R.id.btn_location) { // 位置
	           startActivityForResult(new Intent(this, LYLocationActivity.class), REQUEST_CODE_LOCATIONG);
	        } else if (v.getId() == R.id.iv_emoticons_normal) { // 点击显示表情框
	        	setModeKeyboard(buttonSetModeKeyboard);
	            more.setVisibility(View.VISIBLE);
	            iv_emoticons_normal.setVisibility(View.INVISIBLE);
	            iv_emoticons_checked.setVisibility(View.VISIBLE);
	            btnContainer.setVisibility(View.GONE);
	            emojiIconContainer.setVisibility(View.VISIBLE);
	            DLKeyBoardUtil.closeKeybord(mEditTextContent, LYChatActivity.this);
	        } else if (v.getId() == R.id.iv_emoticons_checked) { // 点击隐藏表情框
	            iv_emoticons_normal.setVisibility(View.VISIBLE);
	            iv_emoticons_checked.setVisibility(View.INVISIBLE);
	            btnContainer.setVisibility(View.VISIBLE);
	            emojiIconContainer.setVisibility(View.GONE);
	            more.setVisibility(View.GONE);
	        } else if (v.getId() == R.id.btn_cars) { //个人名片
				Intent intent = new Intent(LYChatActivity.this,LYFriendsActivity.class);
				intent.putExtra(LYFriendsActivity.PAGEKEY_CARD,true);
	            startActivityForResult(intent, REQUEST_CODE_PERSONAL_CARD);
	        } else if (v.getId() == R.id.btn_file) { // 点击文件图标
	            selectFileFromLocal();
	       }
	    }

	    
	   //点击文字框输入
	   public void editClick(View v) {
	        listView.setSelection(listView.getCount() - 1);
	        if (more.getVisibility() == View.VISIBLE) {
	            more.setVisibility(View.GONE);
	            iv_emoticons_normal.setVisibility(View.VISIBLE);
	            iv_emoticons_checked.setVisibility(View.INVISIBLE);
	        }
	   }
	    
	    /**
	     * 从图库获取图片
	     */
	    public void selectPicFromLocal() {
		    startActivityForResult(LYPhotosActivity.class, REQUEST_CODE_PICTURE);
	    }
	   /**
	     * 显示语音图标按钮
	     * 
	     * @param view
	     */
	    public void setModeVoice(View view) {
	    	DLKeyBoardUtil.closeKeybord(mEditTextContent, LYChatActivity.this);
	        edittext_layout.setVisibility(View.GONE);
	        more.setVisibility(View.GONE);
	        view.setVisibility(View.GONE);
	        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
	        buttonSend.setVisibility(View.GONE);
	        btnMore.setVisibility(View.VISIBLE);
	        buttonPressToSpeak.setVisibility(View.VISIBLE);
	        iv_emoticons_normal.setVisibility(View.VISIBLE);
	        iv_emoticons_checked.setVisibility(View.INVISIBLE);
	        btnContainer.setVisibility(View.VISIBLE);
	        emojiIconContainer.setVisibility(View.GONE);
	    }
	    
	    /**
	     * 照相获取图片
	     */
	    public void selectPicFromCamera() {

	        if (!DLSDCardUtil.isSDCardEnable()) {
	            Toast.makeText(getApplicationContext(), "SD卡不可用，不能拍照",
	                    Toast.LENGTH_SHORT).show();
	            return;
	        }

	        captureHelper.captureImage();
	    }
	    
	    /**
	     * 选择文件
	     */
	    private void selectFileFromLocal() {
		    startActivityForResult(new Intent(LYChatActivity.this, LYDocumentActivity.class), REQUEST_CODE_DOCUMENT);
	    }
	    /**
	     * 显示键盘图标
	     * 
	     * @param view
	     */
	    public void setModeKeyboard(View view) {
	        
	        edittext_layout.setVisibility(View.VISIBLE);
	        more.setVisibility(View.GONE);
	        view.setVisibility(View.GONE);
	        buttonSetModeVoice.setVisibility(View.VISIBLE);
	        mEditTextContent.requestFocus();
	        buttonPressToSpeak.setVisibility(View.GONE);
	        if (TextUtils.isEmpty(mEditTextContent.getText())) {
	            btnMore.setVisibility(View.VISIBLE);
	            buttonSend.setVisibility(View.GONE);
	        } else {
	            btnMore.setVisibility(View.GONE);
	            buttonSend.setVisibility(View.VISIBLE);
	        }
	    }
	   //显示或隐藏图标按钮页
	   public void more(View view) {
	        if (more.getVisibility() == View.GONE) {
	            System.out.println("more gone");
	            DLKeyBoardUtil.closeKeybord(mEditTextContent, LYChatActivity.this);
	            more.setVisibility(View.VISIBLE);
	            btnContainer.setVisibility(View.VISIBLE);
	            emojiIconContainer.setVisibility(View.GONE);
	        } else {
	            if (emojiIconContainer.getVisibility() == View.VISIBLE) {
	                emojiIconContainer.setVisibility(View.GONE);
	                btnContainer.setVisibility(View.VISIBLE);
	                iv_emoticons_normal.setVisibility(View.VISIBLE);
	                iv_emoticons_checked.setVisibility(View.INVISIBLE);
	            } else {
	                more.setVisibility(View.GONE);
	            }
	        }
	    }
	   
	   private PowerManager.WakeLock wakeLock;
	   
	   private void acquireWakeLock() {  
		   if (wakeLock ==null) {  
			    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);  
		   		wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());  
		   		wakeLock.acquire();  
		   }  
		     
	  } 
	   
	   private void releaseWakeLock() {  
		   if (wakeLock !=null&& wakeLock.isHeld()) {  
		   wakeLock.release();  
		   wakeLock =null;  
		   }  
	  } 
	   /**
	     * 按住说话listener
	     * 
	     */
	  class PressToSpeakListen implements OnTouchListener {
	        @SuppressLint({ "ClickableViewAccessibility", "Wakelock" })
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            switch (event.getAction()) {
	            case MotionEvent.ACTION_DOWN:
	            	 recordingContainer.setVisibility(View.VISIBLE);
	                if (!DLSDCardUtil.isSDCardEnable()) {
	                    Toast.makeText(LYChatActivity.this, "发送语音需要sdcard支持！",
	                            Toast.LENGTH_SHORT).show();
	                    return false;
	                }
	                try {
	                    v.setPressed(true);
	                    acquireWakeLock();
	                    
	                    voicePath = new File(DLFolderManager.getTempFolder(),DLDateUtils.getCurrentTimeInLong()+".amr");
	                    voiceRecorder = DLAudioUtil.recordStart(voicePath.getAbsolutePath());
	                    updateMicStatus();
	                    updateVoiceTimer();
	                } catch (Exception e) {
	                    e.printStackTrace();
	                    v.setPressed(false);
	                    releaseWakeLock();
	                    time.cancel();
                		mHandler.removeCallbacks(mUpdateMicStatusTimer);
	                    recordingContainer.setVisibility(View.INVISIBLE);
	                    Toast.makeText(LYChatActivity.this, R.string.recoding_fail,
	                            Toast.LENGTH_SHORT).show();
	                    return false;
	                }

	                return true;
	            case MotionEvent.ACTION_MOVE: {
	                if (event.getY() < 0) {
	                    recordingHint
	                            .setText(getString(R.string.release_to_cancel));
	                    recordingHint
	                            .setBackgroundResource(R.drawable.recording_text_lyhint_bg);
	                } else {
	                    recordingHint
	                            .setText(getString(R.string.move_up_to_cancel));
	                    recordingHint.setBackgroundColor(Color.TRANSPARENT);
	                }
	                return true;
	            }
	            case MotionEvent.ACTION_UP:
	            	actionUp(v);
	                return true;
	            default:
	                recordingContainer.setVisibility(View.INVISIBLE);
	                time.cancel();
            		mHandler.removeCallbacks(mUpdateMicStatusTimer);
	                return false;
	            }
	        }
	    }
	  
	  public void actionUp(View v){
		  v.setPressed(false);
          recordingContainer.setVisibility(View.INVISIBLE);
          releaseWakeLock();
          
          // stop recording and send voice file
          try {
          	
          	if(voiceRecorder!=null){
          		DLAudioUtil.recordStop(voiceRecorder);
          		
          		time.cancel();
          		mHandler.removeCallbacks(mUpdateMicStatusTimer);
          		
          		recordingTimer.setText(0+"\"");
          	}

          	sendVoice(voicePath.getAbsolutePath(), ""+voiceTimer, false);
          	
          } catch (Exception e) {
              e.printStackTrace();
              Toast.makeText(LYChatActivity.this, "发送失败，请检测服务器是否连接",
                      Toast.LENGTH_SHORT).show();
          }
	  }
	  
	  private Runnable mUpdateMicStatusTimer = new Runnable() {  
          public void run() {  
              updateMicStatus();  
          }  
      };  

      private long start_time;
      private TimeCount time = new TimeCount(60000, 1000);; 
      
      private void updateVoiceTimer(){
    	  start_time = System.currentTimeMillis();
    	  time.start();
      }
      
      //计时类
	  class TimeCount extends CountDownTimer {
		  public TimeCount(long millisInFuture, long countDownInterval) {
			  super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		  }
		  @Override
		  public void onFinish() {//计时完毕时触发
			  
			  voiceTimer = 60;
			  recordingTimer.setText(voiceTimer+"\"");
			  
			  actionUp(buttonPressToSpeak);
		  }
		  @Override
		  public void onTick(long millisUntilFinished){//计时过程显示
			  voiceTimer = (int) ((System.currentTimeMillis() - start_time) / 1000);
			  recordingTimer.setText(voiceTimer+"\"");
		  }
     }
      
	  private int BASE = 600;  
	  private int SPACE = 200;// 间隔取样时间
	  
	  private void updateMicStatus() {  
	         if(voiceRecorder!=null){
	             int ratio = voiceRecorder.getMaxAmplitude() / BASE;  
	             int db = 0;// 分贝   
	             if (ratio > 1)  
	                 db = (int) (20 * Math.log10(ratio));  
	                System.out.println("分贝值："+db+"     "+Math.log10(ratio));  
	                //我对着手机说话声音最大的时候，db达到了35左右，
	                mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);  
	                //所以除了2，为的就是对应14张图片
	                mHandler.sendEmptyMessage(db/9);
	         }
	     }
	  
	  @SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler(){
	         public void handleMessage(Message msg) {
	            int what=msg.what;
	            //根据mHandler发送what的大小决定话筒的图片是哪一张
	            //说话声音越大,发送过来what值越大
	           if( what > 4 ){
	                what = 4;
	            }
	             micImage.setImageDrawable(micImages[what]);
	         };
	     }; 
	  
	  private LYChatItemModel getMessage(Object content,LYChatItemModel.TChatType type){
		  
		  	LYChatItemModel tempItem = new LYChatItemModel();
	    	tempItem.setChatType(type);
	    	tempItem.setMessageType(messageType);
	    	tempItem.setStatus("0");
	    	tempItem.setFromModel(LyImEngine.getInstance().getUserModel());
	    	tempItem.setId(ToolsUtil.getCharId());
	    	tempItem.setIsfrom(false);
	    	if(messageType.equals("0")){
	    		tempItem.setToUserModel(toUserModel);
	    	}else if(messageType.equals("1")){
	    		tempItem.setToGroupModel(toGroupModel);
	    	}
	    	tempItem.setUid(LyImEngine.getInstance().getUserId());

	    	tempItem.setChatTimer(DLDateUtils.getCurrentTimeInLong()/1000+"");
	    	switch (type) {
			case EChat_Text:
				tempItem.setChatContent((String)content);
				break;
			case EChat_Image:
				tempItem.setImageModel((DLImageModel)content);
				break;
			case EChat_Voice:
				tempItem.setVoiceModel((DLVoiceModel)content);
				break;
			case EChat_Video:
				break;
			case EChat_File:
				tempItem.setFileModel((DLFileModel)content);
				break;
			case EChat_Card:
				tempItem.setCardModel((LYUserModel)content);
				break;
			case EChat_Location:
				tempItem.setLocationModel((DLLocationModel)content);
				break;
			case EChat_Project:
				tempItem.setProjectModel((LYProjectModel)content);
				break;
			default:
				break;
			}
	    	
	    	return tempItem;
	  }

	  private void sendMessage(final LYChatItemModel model){
		  
		    LYMQTTHelper.getInstance().sendMessage(LYChatActivity.this,model, new LYMQTTHelper.onChatPublishListener() {
				@Override
				public void publishSuccess(String msgId) {
					adapter.refresh();
				}

				@Override
				public void publishFail(String msgId) {
					adapter.refresh();
				}
			});
	  }
	  
	  /**
	     * 发送文本消息
	     * 
	     * @param content
	     *            message content
	     */
	    private void sendText(String content) {

	    	if(DLStringUtil.isEmpty(content)){
	    		DLToastUtil.showToastShort(LYChatActivity.this, "请输入内容");
	    		return;
	    	}
	    	
	    	DLKeyBoardUtil.closeKeybord(mEditTextContent, LYChatActivity.this);
	    	
	    	sendMessage(handleMessageForView(content, LYChatItemModel.TChatType.EChat_Text));
	    	
	    	mEditTextContent.setText("");
	    }
	   
	  
	  /**
	     * 发送图片
	     * 
	     * @param filePath
	     */
	    private void sendPicture(final String filePath, boolean is_share) {
	        
	    	DLImageModel tempImage = new DLImageModel();
			
			tempImage.setImagePath(filePath);
	    	
			upYun(filePath, LYChatItemModel.TChatType.EChat_Image, tempImage);
	    }
	    /**
	     * 发送语音
	     * 
	     * @param filePath
	     * @param length
	     * @param isResend
	     */
	    private void sendVoice(final String filePath, final String length,boolean isResend) {
	        if (!(new File(filePath).exists())) {
	            return;
	        }
	        
	        DLVoiceModel tempVoice = new DLVoiceModel();
			tempVoice.setVoiceLength(length);
			tempVoice.setVoicePath(filePath);
			
			upYun(filePath, LYChatItemModel.TChatType.EChat_Voice, tempVoice);
	    }
	    /**
	     * 发送位置信息
	     *
	     */
	    private void sendLocationMsg(final DLLocationModel item) {
	      
	    	DLLocationModel tempLocation = new DLLocationModel();
	    	tempLocation.setLat(item.getLat());
	    	tempLocation.setLng(item.getLng());
	    	tempLocation.setImagePath(item.getImagePath());
	    	
	    	upYun(tempLocation.getImagePath(), LYChatItemModel.TChatType.EChat_Location, item);
	    }
	    
	    /**
	     * 发送文件
	     * @param data
	     */
	    private void sendFile(final DLFileModel data){
	    	
	    	data.setDownState(2);
	    	
	    	upYun(data.getFilePath(), LYChatItemModel.TChatType.EChat_File,data);
	    	
	    }
	    
	    private LYChatItemModel handleMessageForView(Object item,LYChatItemModel.TChatType mTChatType){
	    	
	    	LYChatItemModel model = getMessage(item,mTChatType);

			addMessageToList(model);
	        
	        return model;
	    }

	    private void addMessageToList(LYChatItemModel model){

			if(mlist.size()>0){
				LYChatItemModel tempItem = (LYChatItemModel) adapter.getItem(mlist.size()-1);
				if(tempItem!=null){
					if(DLStringUtil.notEmpty(model.getChatTimer()) && DLStringUtil.notEmpty(tempItem.getLastTime())){
						if(DLDateUtils.getTimeDiff(DLStringUtil.strToLong(model.getChatTimer()), DLStringUtil.strToLong(tempItem.getLastTime()))==0){
							model.setShowTimer(false);
							model.setLastTime(tempItem.getLastTime());
						}else{
							model.setShowTimer(true);
							model.setLastTime(model.getChatTimer());
						}
					}
				}
			}

			mlist.add(model);

			adapter.refresh();

			listView.setSelection(listView.getCount() - 1);
		}
	    
	    private void upYun(String path, final LYChatItemModel.TChatType echatFile, final Object item){
	    	
	    	final LYChatItemModel tempModel = handleMessageForView(item,echatFile);

			if (LyImEngine.getInstance().getLyimListener()!=null){
				LyImEngine.getInstance().getLyimListener().onUploadFile(LYChatActivity.this,path, new LyImEngine.onUploadFileListener() {
					@Override
					public void onFile(String url, String path) {
						if(url!=null){

							if(echatFile == LYChatItemModel.TChatType.EChat_File){
								tempModel.getFileModel().setFileUrl(url);
							}else if(echatFile == LYChatItemModel.TChatType.EChat_Image){
								tempModel.getImageModel().setImageUrl(url);
							}else if(echatFile == LYChatItemModel.TChatType.EChat_Location){
								tempModel.getLocationModel().setImageUrl(url);
							}else if(echatFile == LYChatItemModel.TChatType.EChat_Voice){
								tempModel.getVoiceModel().setVoiceUrl(url);
							}

							sendMessage(tempModel);
						}
					}

					@Override
					public void onFail() {
						Message msg = new Message();
						msg.what = 500;
						msg.obj = tempModel;
						myHandler.sendMessage(msg);
					}
				});
			}
	    }
	    
	    
	    /**
	     * 发送名片
	     * @param data
	     */
	    public void sendCard(LYUserModel data){
	    	
	    	sendMessage(handleMessageForView(data, LYChatItemModel.TChatType.EChat_Card));
	    }
	    
	    @Override
	    protected void onNewIntent(Intent intent) {
	        // 点击notification bar进入聊天页面，保证只有一个聊天页面
	        String username = intent.getStringExtra("userId");
	        if (toChatUsername.equals(username))
	            super.onNewIntent(intent);
	        else {
	            finish();
	            startActivity(intent);
	        }
	    }
	    
	    /**
	     * onActivityResult
	     */    
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			
			if(captureHelper!=null){
				captureHelper.onCapturePhotoResult(requestCode, resultCode, data);
			}
			
			if(requestCode == REQUEST_CODE_DOCUMENT){    //发送文件
				if(resultCode == LYDocumentActivity.RESULT_CODE_FILE){
					if(data!=null){

						DLFileModel tempItem = (DLFileModel)data.getSerializableExtra(LYDocumentActivity.RESULT_KEY_FILE);
						if(tempItem!=null){
							sendFile(tempItem);
						}
					}
				}
		    }else  if (requestCode == REQUEST_CODE_LOCATIONG) {        // 发送位置
				if(resultCode == LYLocationActivity.RESULT_CODE_LOCATION){
					sendLocationMsg(LYLocationActivity.resultLocation);
				}
		     }
 			else if (requestCode == REQUEST_CODE_PERSONAL_CARD) {     // 发送名片
		    	 if(resultCode == RESULT_OK ){
		    		 if(data!=null){
	    			 	LYUserModel tempItem = (LYUserModel)data.getSerializableExtra(LYFriendsActivity.PAGEKEY_CARD);
						if(tempItem!=null){
							sendCard(tempItem);
						}
		    		 }
				}
	        }
	        else if (requestCode == REQUEST_CODE_PICTURE) {    // 发送图片
                if(resultCode == LYPhotosActivity.RESULT_CODE_PHOTOS){
                	if(DLArrayUtil.notEmpty(LYBimp.tempSelectBitmap)){
						for (int i = 0; i < LYBimp.tempSelectBitmap.size(); i++) {
							DLImageModel tempItem = LYBimp.tempSelectBitmap.get(i);
							if(tempItem!=null){
								sendPicture(tempItem.getImagePath(), false);
							}
						}
					}
                }
	        }
	        else if(requestCode == 567){
	        	if(resultCode == RESULT_OK){
	        		if(data!=null){
	        			int pos = data.getIntExtra(LYDocudetailActivity.PAGE_CHATMODEL, -1);
	        			if(pos>-1){
	        				
	        				LYChatItemModel tempUtem = mlist.get(pos);
	        				if(tempUtem!=null){
	        					DLFileModel dst = (DLFileModel) data.getSerializableExtra("filemodel");
								if (dst!=null) {
									tempUtem.setFileModel(dst);
								}
	        					if(LyImEngine.getInstance().getDbHelper()!=null){
									LyImEngine.getInstance().getDbHelper().getCharTable().checkAndInsertCharData(toUserModel.getUserId(), tempUtem);
	        					}
	        					adapter.notifyDataSetChanged();
	        				}
	        			}
	        		}
	        	}
	        }
//	        else if(requestCode == UnautherizedActivity.REQUEST_TOPAGE){
//	        	if(resultCode == RESULT_OK){
//	        		finish();
//	        	}
//	        }
		}
		
		public void refreshData(){
			getData(false);
		}
		
		public String getUserId(){
			if(toUserModel!=null){
				return toUserModel.getUserId();
			}
			return null;
			
		}

}