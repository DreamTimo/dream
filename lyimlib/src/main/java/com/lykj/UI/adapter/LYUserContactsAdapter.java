package com.lykj.UI.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.dlbase.pinyin.DLAssortPinyinList;
import com.dlbase.pinyin.DLLanguageComparator_CN;
import com.dlbase.pinyin.DLPinyinModel;
import com.dlbase.util.DLStringUtil;
import com.dlbase.view.CircleImageView;
import com.luyz.lyimlib.LYIMConfig;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;
import com.lykj.model.LYUserModel;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LYUserContactsAdapter extends BaseExpandableListAdapter{
	    int type = 0;// 3.认证  1.投资人
	    int authState = 2;//0 未认证 1 认证中 2 认证成功 －1 认证失败
		private ArrayList<LYUserModel> strList;
		private DLAssortPinyinList assort = new DLAssortPinyinList();
		private Context context;
		private LayoutInflater inflater;
		
		private Boolean showBtn;
		private OnClickListener btnListener;
		private boolean showPhone = false;
		
		
		private DLLanguageComparator_CN cnSort = new DLLanguageComparator_CN();

		public LYUserContactsAdapter(Context context, ArrayList<LYUserModel> data, boolean show, OnClickListener listener) {
			super();
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			this.strList = data;
			this.showBtn = show;
			this.btnListener = listener;
			sort();
		}

		private void sort() {
			
			for (LYUserModel str : strList) {
				assort.getHashList().add(str);
			}
			
			assort.getHashList().sortKeyComparator(cnSort);
			
			for(int i=0,length=assort.getHashList().size();i<length;i++)
			{
				List<DLPinyinModel> tempUser =  assort.getHashList().getValueListIndex(i);
				
				List<String> strArray = new ArrayList<String>();
				
				for (int j = 0; j < tempUser.size(); j++) {
					LYUserModel tempItem = (LYUserModel)tempUser.get(j);
					if(tempItem!=null){
						String tempStr = null;
						if(DLStringUtil.notEmpty(tempItem.getUserName())){
							tempStr = tempItem.getUserName();
						} 
						if(DLStringUtil.notEmpty(tempStr)){
							strArray.add(tempStr);
						}
					}
				}
				
				Collections.sort(strArray,cnSort);
			}
		}

		public Object getChild(int group, int child) {
			// TODO Auto-generated method stub
			return assort.getHashList().getValueIndex(group, child);
		}

		public long getChildId(int group, int child) {
			// TODO Auto-generated method stub
			return child;
		} 

		public View getChildView(int group, int child, boolean arg2,
				View contentView, ViewGroup arg4) {
			// TODO Auto-generated method stub
			if (contentView == null) {
				contentView = inflater.inflate(R.layout.adapter_frienditem, null);
			}
			CircleImageView tempHeader = (CircleImageView) contentView.findViewById(R.id.head);
			ImageView iv_fl_flag = (ImageView) contentView.findViewById(R.id.iv_fl_flag);
			TextView tempName = (TextView) contentView.findViewById(R.id.name);
			TextView tempClew = (TextView) contentView.findViewById(R.id.date);
			TextView tempbtn = (TextView) contentView.findViewById(R.id.itembtn);


			LYUserModel tempItem = (LYUserModel)assort.getHashList().getValueIndex(group, child);
			
			if(tempItem!=null){
				
				tempClew.setVisibility(View.GONE);
				tempHeader.setImageResource(R.drawable.image_default);
				tempName.setText("");
				tempClew.setText("");
				
				if(DLStringUtil.notEmpty(tempItem.getAvatar())){
					LyImEngine.getInstance().DownImage(tempItem.getAvatar()+ LYIMConfig.THUMBNAIL100,tempHeader,R.drawable.image_default);
				}
				
				if(DLStringUtil.notEmpty(tempItem.getUserName())){
					tempName.setText(tempItem.getUserName());	
				}

				if(showPhone){
					if(DLStringUtil.notEmpty(tempItem.getPhone())){
						tempClew.setText("手机号："+tempItem.getPhone());
						tempClew.setVisibility(View.VISIBLE);
					}
				}
				//0 添加（通讯录） 1 已添加 2 接受（别人加你）
				if(tempItem.getApplyType().equals("0")){
					tempbtn.setText("添加");
					tempbtn.setTextColor(Color.parseColor("#1f9af6"));
					tempbtn.setBackgroundResource(R.drawable.addition);
				}else if(tempItem.getApplyType().equals("1")){
					tempbtn.setText("已添加");
					tempbtn.setEnabled(false);
					tempbtn.setClickable(false); 
					tempbtn.setBackgroundResource(R.drawable.btn_startbg);
					tempbtn.setTextColor(Color.parseColor("#1f9af6"));
				}else if(tempItem.getApplyType().equals("2")){
					tempbtn.setText("接受");
					tempbtn.setTextColor(Color.parseColor("#ffffff"));
					tempbtn.setBackgroundResource(R.drawable.accept);
				}else if(tempItem.getApplyType().equals("3")){
					tempbtn.setText("邀请");
					tempbtn.setTextColor(Color.parseColor("#ffffff"));
					tempbtn.setBackgroundResource(R.drawable.addinvite);
				}
				
				if(DLStringUtil.notEmpty(tempItem.getUserId())){
					if(tempItem.getUserId().equals(LyImEngine.getInstance().getUserId())){
						showBtn = true;
					}
				}else{
					if(DLStringUtil.notEmpty(tempItem.getPhone())){
						if(tempItem.getPhone().equals(LyImEngine.getInstance().getUserName())){
							showBtn = true;
						}
					}
				}
			}
			
			tempbtn.setOnClickListener(btnListener);
			HashMap<String, Object> tempMap = new HashMap<String,Object>();
			tempMap.put("group", group);
			tempMap.put("child", child);
			tempbtn.setTag(tempMap);
			
			tempbtn.setVisibility(showBtn?View.GONE:View.VISIBLE);
			return contentView;
		}

		

		public int getChildrenCount(int group) {
			// TODO Auto-generated method stub
			return assort.getHashList().getValueListIndex(group).size();
		}

		public Object getGroup(int group) {
			// TODO Auto-generated method stub
			return assort.getHashList().getValueListIndex(group);
		}

		public int getGroupCount() {
			// TODO Auto-generated method stub
			return assort.getHashList().size();
		}

		public long getGroupId(int group) {
			// TODO Auto-generated method stub
			return group;
		}

		public View getGroupView(int group, boolean arg1, View contentView,
				ViewGroup arg3) {
			if (contentView == null) {
				contentView = inflater.inflate(R.layout.adapter_usercontact_group_item, null);
				contentView.setClickable(true);
			}
			
			TextView textView = (TextView) contentView.findViewById(R.id.name);

			LYUserModel tempUser = (LYUserModel)assort.getHashList().getValueIndex(group, 0);
			if(tempUser!=null){
				
				String tempStr = null;
				
				if(DLStringUtil.notEmpty(tempUser.getUserName())){
					tempStr = tempUser.getUserName();
				}
				if(DLStringUtil.notEmpty(tempStr)){
					textView.setText(assort.getFirstChar(tempStr));
				}
			}
			return contentView;
		}

		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return true;
		}

		public DLAssortPinyinList getAssort() {
			return assort;
		}
		
		
		
		public boolean isShowPhone() {
			return showPhone;
		}

		public void setShowPhone(boolean showPhone) {
			this.showPhone = showPhone;
		}

	}

