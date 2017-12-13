package com.lykj.UI.adapter;

import java.util.ArrayList;

import com.dlbase.Model.DLFileModel;
import com.dlbase.base.DLBaseAdapter;
import com.dlbase.util.DLDateUtils;
import com.dlbase.util.DLFileSizeUtil;
import com.dlbase.util.DLStringUtil;
import com.dlbase.util.ViewHolder;
import com.luyz.lyimlib.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class LYDocumentAdapter extends DLBaseAdapter{
	
	
	private Context mContext;
	private ArrayList<DLFileModel> list;
	private View.OnClickListener openListener;
	
	public LYDocumentAdapter(Context context, ArrayList<DLFileModel> data){
		mContext = context;
		list = data;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size()==0?0:list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		     
		 ViewHolder holder = ViewHolder.get(mContext, convertView, parent, R.layout.adapter_lydocument, position);

		 ImageView  iv_document_itemPhoto = holder.getView(R.id.iv_document_itemPhoto);
		 TextView   tv_document_itemName = holder.getView(R.id.tv_document_itemName);
		 TextView   tv_document_itemSize = holder.getView(R.id.tv_document_itemSize);
		 TextView  tv_document_btn_open = holder.getView(R.id.btn_open);
	
		 DLFileModel tempItem = list.get(position);
		 if(tempItem!=null){
			
			if (DLStringUtil.notEmpty(tempItem.getFileName())) {
				tv_document_itemName.setText(tempItem.getFileName());
			}
			
			if (DLStringUtil.notEmpty(tempItem.getFileSize())) {
				tv_document_itemSize.setText(DLFileSizeUtil.FormetFileSize(DLStringUtil.strToLong(tempItem.getFileSize())));
			}
			
			if(DLStringUtil.notEmpty(tempItem.getFileTypeIcon())){
				iv_document_itemPhoto.setImageResource(DLStringUtil.strToInt(tempItem.getFileTypeIcon()));
			}
		 }

		tv_document_btn_open.setOnClickListener(openListener);
		tv_document_btn_open.setTag(position);
	       
		return holder.getConvertView();
    }

	public View.OnClickListener getOpenListener() {
		return openListener;
	}

	public void setOpenListener(View.OnClickListener openListener) {
		this.openListener = openListener;
	}
}