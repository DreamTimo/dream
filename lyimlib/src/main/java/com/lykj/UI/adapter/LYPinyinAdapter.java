package com.lykj.UI.adapter;

import java.util.ArrayList;
import java.util.Collections;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LYPinyinAdapter extends BaseExpandableListAdapter{
	    int type = 0;// 3.认证  1.投资人
	    int authState = 2;//0 未认证 1 认证中 2 认证成功 －1 认证失败
		private ArrayList<LYUserModel> strList;
		private DLAssortPinyinList assort = new DLAssortPinyinList();
		@SuppressWarnings("unused")
		private Context context;
		private LayoutInflater inflater;
		private DLLanguageComparator_CN cnSort = new DLLanguageComparator_CN();

		public LYPinyinAdapter(Context context, ArrayList<LYUserModel> data) {
			super();
			this.context = context;
			this.inflater = LayoutInflater.from(context);
			this.strList = data;
			
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
			CircleImageView head = (CircleImageView)contentView.findViewById(R.id.head);
			TextView textView = (TextView) contentView.findViewById(R.id.name);
			ImageView iv_fl_flag = (ImageView)contentView.findViewById(R.id.iv_fl_flag);
			TextView tempdate = (TextView)contentView.findViewById(R.id.date);
			
			LYUserModel tempUser = (LYUserModel)assort.getHashList().getValueIndex(group, child);
			if(tempUser!=null){
				
				head.setImageResource(R.drawable.image_default);
				textView.setText("");
				tempdate.setText("");
				tempdate.setVisibility(View.GONE);
				
				if(DLStringUtil.notEmpty(tempUser.getAvatar())){
					LyImEngine.getInstance().DownImage(tempUser.getAvatar()+ LYIMConfig.THUMBNAIL100,head,R.drawable.image_default);
				}
				
				if(DLStringUtil.notEmpty(tempUser.getUserName())){
					textView.setText(tempUser.getUserName());
				}
			}
			
			
			TextView tempBtn = (TextView)contentView.findViewById(R.id.itembtn);
			
			tempBtn.setVisibility(View.GONE);
			
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
				contentView = inflater.inflate(R.layout.adapter_group_item, null);
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

	}

