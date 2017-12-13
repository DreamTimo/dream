package com.dlbase.view;

import java.util.List;

import com.dlbase.util.ViewHolder;
import com.luyz.dlbaselib.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class Dialog_ShowChatAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> mList;

	
	public Dialog_ShowChatAdapter(Context context,List<String> data){
		mContext = context;
		mList = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mList.size() == 0?0:this.mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder = ViewHolder.get(mContext, convertView, parent, R.layout.item_showchat, position);
		
		TextView tempName = holder.getView(R.id.tv_content);
		
		String tempItem = mList.get(position);
		
		if(tempItem!=null){
			
			tempName.setText(tempItem);
		}
		
		return holder.getConvertView();
	}

}
