package com.dlbase.pickutil;

import com.dlbase.pickutil.DLScrollerNumberPicker.OnSelectListener;
import com.luyz.dlbaselib.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Picker
 * 
 * @author zihao
 * 
 */
public class DLPickerView extends LinearLayout {
	/** 滑动控件 */
	private DLScrollerNumberPicker firstPicker;
	private DLScrollerNumberPicker secondPicker;
	private DLScrollerNumberPicker threePicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/**数据源 */
	private DLPickerDataSource pickData = null;

	public DLPickerDataSource getPickData() {
		return pickData;
	}

	public void setPickData(DLPickerDataSource pickData) {
		this.pickData = pickData;
		
		switch (pickData.getPickComponentsCount()) {
		case 1:{
			secondPicker.setVisibility(View.GONE);
			threePicker.setVisibility(View.GONE);
			firstPicker.setData(pickData.getFirstData());
			firstPicker.setDefault(pickData.getDefaultFirstIndex());
		}
			break;
		case 2:{
			threePicker.setVisibility(View.GONE);
			firstPicker.setData(pickData.getFirstData());
			firstPicker.setDefault(pickData.getDefaultFirstIndex());
			DLPickerItemModel tempFirst = pickData.getFirstItem(pickData.getDefaultFirstIndex());
			secondPicker.setData(pickData.getSecondData(tempFirst.getPickName()));
			secondPicker.setDefault(pickData.getDefaultSecondIndex());
		}
			break;
		case 3:{
			firstPicker.setData(pickData.getFirstData());
			firstPicker.setDefault(pickData.getDefaultFirstIndex());
			DLPickerItemModel tempFirst = pickData.getFirstItem(pickData.getDefaultFirstIndex());
			secondPicker.setData(pickData.getSecondData(tempFirst.getPickName()));
			secondPicker.setDefault(pickData.getDefaultSecondIndex());
			threePicker.setData(pickData.getThreeData(pickData.getSecondItem(tempFirst.getPickName(), pickData.getDefaultSecondIndex()).getPickName()));
			threePicker.setDefault(pickData.getDefaultThreeIndex());	
		}
			break;
		default:
			break;
		}
	}

	@SuppressWarnings("unused")
	private Context context;
	
	public DLPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getaddressinfo();
		// TODO Auto-generated constructor stub
	}

	public DLPickerView(Context context) {
		super(context);
		this.context = context;
		getaddressinfo();
		// TODO Auto-generated constructor stub
	}

	// 信息初始化
	private void getaddressinfo() {
		// TODO Auto-generated method stub
		pickData = new DLPickerDataSource();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		LayoutInflater.from(getContext()).inflate(R.layout.view_picker, this);
		
		// 获取控件引用
		firstPicker = (DLScrollerNumberPicker) findViewById(R.id.province);
		secondPicker = (DLScrollerNumberPicker) findViewById(R.id.city);
		threePicker = (DLScrollerNumberPicker) findViewById(R.id.couny);
		
		firstPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				System.out.println("id-->" + id + "text----->" + text);
				if (text.equals("") || text == null)
					return;
				if (pickData.getFirstIndex() != id) {
					System.out.println("endselect");
					
					if(pickData.getPickComponentsCount() == 1){
						
					}else if(pickData.getPickComponentsCount() == 2){
						String selectDay = secondPicker.getSelectedText();
						if (selectDay == null || selectDay.equals(""))
							return;
						secondPicker.setData(pickData.getSecondData(pickData.getFirstItem(id).getPickName()));
						secondPicker.setDefault(0);
						pickData.setSecondIndex(0);
					}else if(pickData.getPickComponentsCount() == 3){
						String selectDay = secondPicker.getSelectedText();
						if (selectDay == null || selectDay.equals(""))
							return;
						String selectMonth = threePicker.getSelectedText();
						if (selectMonth == null || selectMonth.equals(""))
							return;
						secondPicker.setData(pickData.getSecondData(pickData.getFirstItem(id).getPickName()));
						secondPicker.setDefault(0);
						pickData.setSecondIndex(0);
						threePicker.setData(pickData.getThreeData(pickData.getSecondItem(pickData.getFirstItem(id).getPickName(), 0).getPickName()));
						threePicker.setDefault(0);
						pickData.setThreeIndex(0);
					} 
					
					int lastDay = Integer.valueOf(firstPicker.getListSize());
					if (id > lastDay) {
						firstPicker.setDefault(lastDay - 1);
					}
				}
				pickData.setFirstIndex(id);
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub
			}
		});
		secondPicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub
				if (text.equals("") || text == null)
					return;
				if (pickData.getSecondIndex() != id) {
					
					if(pickData.getPickComponentsCount() == 2){
						String selectDay = firstPicker.getSelectedText();
						if (selectDay == null || selectDay.equals(""))
							return;
						
						
					}else if(pickData.getPickComponentsCount() == 3){
						String selectDay = firstPicker.getSelectedText();
						if (selectDay == null || selectDay.equals(""))
							return;
						
						String selectMonth = threePicker.getSelectedText();
						if (selectMonth == null || selectMonth.equals(""))
							return;
						
						threePicker.setData(pickData.getThreeData(pickData.getSecondItem(pickData.getFirstItem(pickData.getFirstIndex()).getPickName(), id).getPickName()));
						threePicker.setDefault(0);
						pickData.setThreeIndex(0);
					}
					int lastDay = Integer.valueOf(secondPicker.getListSize());
					if (id > lastDay) {
						secondPicker.setDefault(lastDay - 1);
					}
				}
				pickData.setSecondIndex(id);
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});
		threePicker.setOnSelectListener(new OnSelectListener() {

			@Override
			public void endSelect(int id, String text) {
				// TODO Auto-generated method stub

				if (text.equals("") || text == null)
					return;
				if (pickData.getThreeIndex() != id) {
					if(pickData.getPickComponentsCount() == 3){
						String selectDay = firstPicker.getSelectedText();
						if (selectDay == null || selectDay.equals(""))
							return;
						String selectMonth = secondPicker.getSelectedText();
						if (selectMonth == null || selectMonth.equals(""))
							return;
						
						
					}
					
					int lastDay = Integer.valueOf(threePicker.getListSize());
					if (id > lastDay) {
						threePicker.setDefault(lastDay - 1);
					}
				}
				pickData.setThreeIndex(id);
				Message message = new Message();
				message.what = REFRESH_VIEW;
				handler.sendMessage(message);
			}

			@Override
			public void selecting(int id, String text) {
				// TODO Auto-generated method stub

			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case REFRESH_VIEW:
				if (onSelectingListener != null)
					onSelectingListener.selected(true);
				break;
			default:
				break;
			}
		}

	};

	public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
		this.onSelectingListener = onSelectingListener;
	}

	public interface OnSelectingListener {

		public void selected(boolean selected);
	}
}
