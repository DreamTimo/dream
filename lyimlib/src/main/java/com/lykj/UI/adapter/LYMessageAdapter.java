package com.lykj.UI.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.dlbase.Model.DLFileModel;
import com.dlbase.Model.DLImageModel;
import com.dlbase.Model.DLLocationModel;
import com.dlbase.Model.DLVoiceModel;
import com.dlbase.base.DLBaseAdapter;
import com.dlbase.util.DLFileSizeUtil;
import com.dlbase.util.DLLogUtil;
import com.dlbase.util.DLScreenUtil;
import com.dlbase.util.DLStringUtil;
import com.dlbase.util.DLTimeUtil;
import com.dlbase.view.CircleImageView;
import com.dlbase.view.RoundImageView;

import com.luyz.lyimlib.LYIMConfig;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;
import com.lykj.emojicon.EmojiconTextView;
import com.lykj.model.LYChatItemModel;

import com.dlbase.util.ViewHolder;
import com.lykj.model.LYProjectModel;
import com.lykj.model.LYUserModel;
import com.lykj.util.LYImageLoaderUtil;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class LYMessageAdapter extends DLBaseAdapter{
	
	private Context mContext;
	private ArrayList<LYChatItemModel> list;
	
	private OnClickListener avatarListener ;//头像
	private OnClickListener itemListener ;//内容
    private OnLongClickListener longListener;//长按键
    private OnClickListener resendListener;//重发
	
	public LYMessageAdapter(Context context, ArrayList<LYChatItemModel> data){
		mContext = context;
		list = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.list.size()==0?0:this.list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.list.get(position);
	}

	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	 /**
     * 刷新页面
     */
    public void refresh() {
        notifyDataSetChanged();
    }
    
	@SuppressWarnings("unused")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		     
		ViewHolder tempView= ViewHolder.get(mContext, convertView, parent, R.layout.adapter_lychat, position);
		//公共部分
		CircleImageView headerView_left = tempView.getView(R.id.iv_left_image);
		CircleImageView headerView_right = tempView.getView(R.id.iv_right_image);
		TextView timestamp = tempView.getView(R.id.timestamp);
		TextView systemtext = tempView.getView(R.id.systemtext);
		systemtext.setVisibility(View.GONE);
		
		LinearLayout message_layout = (LinearLayout)tempView.getView(R.id.message_layout);
		
		//消息发送状态
		RelativeLayout rl_chat_state = tempView.getView(R.id.rl_chat_state);
		ProgressBar progressbar = tempView.getView(R.id.progressbar);
		ImageView send_failure = tempView.getView(R.id.send_failure);
		
		RelativeLayout rl_chat_layout = tempView.getView(R.id.rl_chat_layout);
		
		 //文本
		LinearLayout ll_text_background	= tempView.getView(R.id.ll_text_background);
		EmojiconTextView tv_text_content  = tempView.getView(R.id.tv_text_content);
		
		 //图片、地理位置
		LinearLayout ll_picture_background = tempView.getView(R.id.ll_picture_background);
		RoundImageView iv_picture = tempView.getView(R.id.iv_picture);
		 
		 //文件
		LinearLayout ll_files_background = tempView.getView(R.id.ll_files_background);
		ImageView iv_files_picture = tempView.getView(R.id.iv_files_picture);
		TextView tv_file_name = tempView.getView(R.id.tv_file_name);
		TextView tv_file_size = tempView.getView(R.id.tv_file_size);
		TextView tv_file_down = tempView.getView(R.id.tv_file_down);
		
		 //个人名片 项目
		LinearLayout ll_card_background = tempView.getView(R.id.ll_card_background);
		TextView tv_card_title = tempView.getView(R.id.tv_card_title);
		CircleImageView iv_card_picture = tempView.getView(R.id.iv_card_picture);
		TextView tv_card_name = tempView.getView(R.id.tv_card_name);
		TextView tv_card_privateNumber = tempView.getView(R.id.tv_card_privateNumber);
		
		 //语音 点击事件
		RelativeLayout rl_voie_layout = tempView.getView(R.id.rl_voie_layout);
		//from
		LinearLayout ll_voie_fromlayout  = tempView.getView(R.id.ll_voie_fromlayout);
		LinearLayout ll_vioce_frombackground  = tempView.getView(R.id.ll_vioce_frombackground);
		ImageView iv_from_voice = tempView.getView(R.id.iv_from_voice);
		TextView tv_from_length = tempView.getView(R.id.tv_from_length); 
		ImageView iv_voice_fromunread = tempView.getView(R.id.iv_voice_fromunread);
		//to
		LinearLayout ll_voie_tolayout = tempView.getView(R.id.ll_voie_tolayout);
		LinearLayout ll_vioce_tobackground  = tempView.getView(R.id.ll_vioce_tobackground);
		TextView tv_to_length  = tempView.getView(R.id.tv_to_length);
		ImageView iv_to_voice  = tempView.getView(R.id.iv_to_voice);
		
		final LYChatItemModel tempItem = list.get(position);
		
		if(tempItem!=null){
			
			rl_voie_layout.setVisibility(View.GONE);
			headerView_left.setVisibility(View.GONE);
			headerView_right.setVisibility(View.GONE);
			ll_text_background.setVisibility(View.GONE);
			ll_picture_background.setVisibility(View.GONE);
			ll_files_background.setVisibility(View.GONE);
			ll_card_background.setVisibility(View.GONE);
			timestamp.setVisibility(View.GONE);
			ll_voie_fromlayout.setVisibility(View.GONE);
			ll_voie_tolayout.setVisibility(View.GONE);
			//状态
			 rl_chat_state.setVisibility(View.GONE);
			 progressbar.setVisibility(View.GONE);
		     send_failure.setVisibility(View.GONE);//发送失败
		     
		     tv_file_down.setVisibility(View.GONE);

		     if(DLStringUtil.notEmpty(tempItem.getChatTimer())){
				
				long time1 = DLStringUtil.strToLong(tempItem.getChatTimer());
				timestamp.setText(DLTimeUtil.ToTime(time1));
			 }
			 timestamp.setVisibility(tempItem.isShowTimer()?View.VISIBLE:View.GONE);
		     
		     if(tempItem.getChatType() == LYChatItemModel.TChatType.EChat_System){
		    	 message_layout.setVisibility(View.GONE);
		    	 systemtext.setVisibility(View.VISIBLE);
		    	 
		    	 if(DLStringUtil.notEmpty(tempItem.getChatContent())){
					systemtext.setText(tempItem.getChatContent());
				 }
		     }else{
		    	 
		    	 message_layout.setVisibility(View.VISIBLE);
					if(tempItem.isIsfrom()){
						headerView_left.setVisibility(View.VISIBLE);
						headerView_left.setBackgroundResource(R.drawable.image_default);
						if(tempItem.getFromModel()!=null&&DLStringUtil.notEmpty(tempItem.getFromModel().getAvatar())){
//							LyImEngine.getInstance().DownImage(tempItem.getFromModel().getAvatar(),headerView_left,R.drawable.image_default);
							if (!TextUtils.isEmpty(tempItem.getFromModel().getAvatar())){
								LYImageLoaderUtil.picassoLoad(mContext,tempItem.getFromModel().getAvatar()+LYIMConfig.THUMBNAIL100,headerView_left,R.drawable.image_default);
							}else {
								headerView_left.setImageResource(R.drawable.image_default);
							}
						}
					}else{
						headerView_right.setVisibility(View.VISIBLE);
						headerView_right.setBackgroundResource(R.drawable.image_default);
//						LyImEngine.getInstance().DownImage(LyImEngine.getInstance().getUserModel().getAvatar(),headerView_right,R.drawable.image_default);
						if (LyImEngine.getInstance().getUserModel()!=null&&!TextUtils.isEmpty(LyImEngine.getInstance().getUserModel().getAvatar())){
							LYImageLoaderUtil.picassoLoad(mContext,LyImEngine.getInstance().getUserModel().getAvatar()+LYIMConfig.THUMBNAIL100,headerView_right,R.drawable.image_default);
						}
					}
					LinearLayout tempLayout = null;
					
					String tempImageTagType = "image";
					switch (tempItem.getChatType()) {
					case EChat_Text:{
						
						if(DLStringUtil.notEmpty(tempItem.getChatContent())){
							tv_text_content.setText(tempItem.getChatContent());
						}
						tempLayout = ll_text_background;

						if (tempItem.isIsfrom()){
							tv_text_content.setTextColor(Color.parseColor("#ff000000"));
						}else{
							tv_text_content.setTextColor(Color.parseColor("#ffffffff"));
						}

						updateRelativeParam(rl_chat_state,tempItem.isIsfrom(),R.id.ll_text_background);
					}
						break;
					case EChat_Image:{
						
						DLImageModel tempImage = tempItem.getImageModel();
						
						if(tempImage!=null){
							try{
								iv_picture.setType(RoundImageView.TYPE_ROUND);
								iv_picture.setRoundBorderRadius(5);
							}catch (Exception e){
								DLLogUtil.printErrorMessage(e);
							}
							try{
//								iv_picture.setBackgroundResource(R.drawable.img_default);
								Picasso.with(mContext).load(R.drawable.img_default).into(iv_picture);
							}catch (Exception e){
								DLLogUtil.printErrorMessage(e);
							}
		                    if(DLStringUtil.notEmpty(tempImage.getImagePath())){
								LyImEngine.getInstance().loadSDCARDImage(tempImage.getImagePath(), iv_picture);
							}else if (DLStringUtil.notEmpty(tempImage.getImageUrl())) {
								LyImEngine.getInstance().DownImage(tempImage.getImageUrl()+LYIMConfig.THUMBNAIL300,iv_picture,R.drawable.img_default);
							}
						}
						 tempLayout = ll_picture_background;
						 updateRelativeParam(rl_chat_state,tempItem.isIsfrom(),R.id.ll_picture_background);
					}
						break;
					case EChat_Card:{
						LYUserModel tempCard = tempItem.getCardModel();
						if(tempCard!=null){
							tv_card_title.setText("个人名片");
							if(DLStringUtil.notEmpty(tempCard.getUserName())){
								tv_card_name.setText(tempCard.getUserName());
							}
							if(DLStringUtil.notEmpty(tempCard.getAvatar())){
								//HttpEngine.displayImage(tempCard.getAvatar()+NetUtil.THUMBNAIL100, iv_card_picture, R.drawable.image_default);
//								LyImEngine.getInstance().DownImage(tempCard.getAvatar()+LYIMConfig.THUMBNAIL100,iv_card_picture,R.drawable.image_default);
								LYImageLoaderUtil.picassoLoad(mContext,tempCard.getAvatar()+LYIMConfig.THUMBNAIL100,iv_card_picture,R.drawable.image_default);
							}
						}
						
						tempLayout = ll_card_background;
						updateRelativeParam(rl_chat_state,tempItem.isIsfrom(),R.id.ll_card_background);
					}
						break;
					case EChat_Voice:{ 
						
						DLVoiceModel tempVoice = tempItem.getVoiceModel();
						if(tempVoice!=null){
							
							//HttpEngine.downVoiceFile(tempVoice.getVoiceUrl(), tempVoice);

							if (LyImEngine.getInstance().getLyimListener()!=null){
								LyImEngine.getInstance().getLyimListener().onDownVoice(tempVoice,null);
							}

							rl_voie_layout.setVisibility(View.VISIBLE);
							int min = 150;
							int max = (DLScreenUtil.getScreenWidth(mContext)/3)*2-min;
							int length = 0;
							if(tempItem.isIsfrom()){
								ll_voie_fromlayout.setVisibility(View.VISIBLE);
								if(DLStringUtil.notEmpty(tempVoice.getVoiceLength())){
									tv_from_length.setText(tempVoice.getVoiceLength()+"\"");
									length = DLStringUtil.strToInt(tempVoice.getVoiceLength());
								}
								ll_vioce_frombackground.setLayoutParams(new LinearLayout.LayoutParams(length>0?max*length/60+min:min,LayoutParams.WRAP_CONTENT));
								iv_voice_fromunread.setVisibility(tempVoice.isRead()?View.VISIBLE:View.GONE);
								AnimationDrawable animationDrawable = (AnimationDrawable) iv_from_voice.getDrawable();
								if(tempVoice.isPlay()){
									animationDrawable.start();	
								}else{
									animationDrawable.stop();
								}
							}else{
								
								ll_voie_tolayout.setVisibility(View.VISIBLE);
								if(DLStringUtil.notEmpty(tempVoice.getVoiceLength())){
									tv_to_length.setText(tempVoice.getVoiceLength()+"\"");
									length = DLStringUtil.strToInt(tempVoice.getVoiceLength());
								}
								ll_vioce_tobackground.setLayoutParams(new LinearLayout.LayoutParams(length>0?max*length/60+min:min,LayoutParams.WRAP_CONTENT));
								AnimationDrawable animationDrawable = (AnimationDrawable) iv_to_voice.getDrawable();
								if(tempVoice.isPlay()){
									animationDrawable.start();	
								}else{
									animationDrawable.stop();
								}
							}
							
						}
						updateRelativeParam(rl_chat_state,tempItem.isIsfrom(),R.id.rl_voie_layout);
					}
						break;
					case EChat_Video:
					               {
						
					}
						break;
						
					case EChat_File:{
						DLFileModel tempFile = tempItem.getFileModel();
						if(tempFile!=null){
							if(DLStringUtil.notEmpty(tempFile.getFileName())){
								tv_file_name.setText(tempFile.getFileName());	
							}
							if(DLStringUtil.notEmpty(tempFile.getFileSize())){
								tv_file_size.setText(DLFileSizeUtil.FormetFileSize(DLStringUtil.strToLong(tempFile.getFileSize())));	
							}
							if(DLStringUtil.notEmpty(tempFile.getFileTypeIcon())){
								iv_files_picture.setImageResource(DLStringUtil.strToInt(tempFile.getFileTypeIcon()));
							}
							if(tempItem.isIsfrom()){
								//未下载
								if(tempFile.getDownState() == 0){
									tv_file_down.setVisibility(View.VISIBLE);
									tv_file_down.setText("未下载");
								}
								//已下载
								else if(tempFile.getDownState() == 2){
									tv_file_down.setVisibility(View.VISIBLE);
									tv_file_down.setText("已下载");
								}
							}
						}
						
						tempLayout = ll_files_background;
						updateRelativeParam(rl_chat_state,tempItem.isIsfrom(),R.id.ll_files_background);
					}
						
						break;
					case EChat_Location:{ //通图片处理
						DLLocationModel tempLocation = tempItem.getLocationModel();
						if(tempLocation!=null){
							try{
								iv_picture.setType(RoundImageView.TYPE_ROUND);
								iv_picture.setRoundBorderRadius(5);
//								iv_picture.setBackgroundResource(R.drawable.img_default);
								Picasso.with(mContext).load(R.drawable.img_default).into(iv_picture);
							}catch (Exception e){
								DLLogUtil.printErrorMessage(e);
							}
							if(DLStringUtil.notEmpty(tempLocation.getImagePath())){
								LyImEngine.getInstance().loadSDCARDImage(tempLocation.getImagePath(), iv_picture);
							}else if(DLStringUtil.notEmpty(tempLocation.getImageUrl())){
								LyImEngine.getInstance().DownImage(tempLocation.getImageUrl()+LYIMConfig.THUMBNAIL300,iv_picture,R.drawable.img_default);
							}
						}
						 tempLayout = ll_picture_background;
						 updateRelativeParam(rl_chat_state,tempItem.isIsfrom(),R.id.ll_picture_background);
					}
						
						break;
					case EChat_Project:{
						LYProjectModel tempProject = tempItem.getProjectModel();
						if(tempProject!=null){
							
							tv_card_title.setText("项目");
							
							if(DLStringUtil.notEmpty(tempProject.getProjectName())){
								tv_card_name.setText(tempProject.getProjectName());
							}
							if(DLStringUtil.notEmpty(tempProject.getProjectLogo())){

								LyImEngine.getInstance().DownImage(tempProject.getProjectLogo()+ LYIMConfig.THUMBNAIL100,iv_card_picture,R.drawable.img_default);

							}
						}
						
						tempLayout = ll_card_background;
						updateRelativeParam(rl_chat_state,tempItem.isIsfrom(),R.id.ll_card_background);
					}
						
						break;
					default:
						break;
					}
					
					 send_failure.setOnClickListener(resendListener);
					 send_failure.setTag(position);
					
					 headerView_left.setOnClickListener(avatarListener);
				     headerView_left.setTag(position);
				     
					 rl_chat_layout.setOnClickListener(itemListener);
					 rl_chat_layout.setOnLongClickListener(longListener);
					 HashMap<String, String> tempLongTag = new HashMap<String,String>();
					 tempLongTag.put("position", ""+position);
					 tempLongTag.put("type", tempItem.getContentType());
					 tempLongTag.put("voice", "from");
					 rl_chat_layout.setTag(tempLongTag);
					 
					if(DLStringUtil.notEmpty(tempItem.getStatus())){
						if(tempItem.getStatus().equals("0")||tempItem.getStatus().equals("1")){
							rl_chat_state.setVisibility(View.VISIBLE);
							progressbar.setVisibility(View.VISIBLE);
						}else if(tempItem.getStatus().equals("2")){
							rl_chat_state.setVisibility(View.GONE);
							progressbar.setVisibility(View.GONE);
						}else if(tempItem.getStatus().equals("3")){
							rl_chat_state.setVisibility(View.VISIBLE);
							progressbar.setVisibility(View.GONE);
							send_failure.setVisibility(View.VISIBLE);
						}
					}
					
					if(tempLayout!=null){ //背景是否显示以及居左或右
						tempLayout.setVisibility(View.VISIBLE);
						tempLayout.setBackgroundResource(tempItem.isIsfrom()?R.drawable.chatfrom_bg:R.drawable.chatto_bg);
						updateLayoutParam(tempLayout,tempItem.isIsfrom());
					}
		     }
		  }
	       
		return tempView.getConvertView();
	}
	
	//聊天内容背景居左居右的封装
	private void updateLayoutParam(LinearLayout layout,boolean isLeft){
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
	            LayoutParams.WRAP_CONTENT);
		
		if(isLeft){
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			layout.setPadding(14, 0, 0, 0);
		}else{
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			layout.setPadding(0, 0, 14, 0);
		}
		
		layout.setLayoutParams(params);	
	}
	
	private void updateRelativeParam(RelativeLayout layout,boolean isLeft,int resourceId){
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
	            LayoutParams.WRAP_CONTENT);
	                            	
		if(!isLeft){
			params.addRule(RelativeLayout.LEFT_OF,resourceId );
		}
		
		layout.setLayoutParams(params);
	}

	public OnClickListener getAvatarListener() {
		return avatarListener;
	}

	public void setAvatarListener(OnClickListener avatarListener) {
		this.avatarListener = avatarListener;
	}

	public OnLongClickListener getLongListener() {
		return longListener;
	}

	public void setLongListener(OnLongClickListener longListener) {
		this.longListener = longListener;
	}

	public OnClickListener getItemListener() {
		return itemListener;
	}

	public void setItemListener(OnClickListener itemListener) {
		this.itemListener = itemListener;
	}

	public OnClickListener getResendListener() {
		return resendListener;
	}

	public void setResendListener(OnClickListener resendListener) {
		this.resendListener = resendListener;
	}
}