package com.dlbase.dateutil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.dlbase.dateutil.DLBirthDateSource.TBirthType;
import com.dlbase.util.DLStringUtil;
import com.luyz.dlbaselib.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

/**
 * @author 
 * 日期选择
 */
public class DLBirthDateDialog extends Dialog implements android.view.View.OnClickListener {
	/**
	 * 自定义Dialog监听
	 */
	public interface PriorityListener {
		/**
		 * 回调函数，用于在Dialog的监听事件触发后刷新Activity的UI显示
		 */
		public void refreshPriorityUI(String seleteValue);
	}

	private DLBirthDateSource dataSource;
	
	private PriorityListener lis;
	@SuppressWarnings("unused")
	private Context context = null;
	private DLNumericWheelAdapter year_adapter = null;
	private DLNumericWheelAdapter month_adapter = null;
	private DLNumericWheelAdapter day_adapter = null;
	private DLNumericWheelAdapter hours_adapter = null;
	private DLNumericWheelAdapter mins_adapter = null;
	private Button btn_sure = null;
	private Button btn_cancel = null;
	
	private DLWheelView monthview = null;
	private DLWheelView yearview = null;
	private DLWheelView dayview = null;
	private DLWheelView hoursview = null;
	private DLWheelView minsview = null;
	private static int theme = R.style.myDialog;// 主题
	private LinearLayout date_layout;
	private int width, height;// 对话框宽高
	
	private RelativeLayout rl_hour,rl_mins;

	public DLBirthDateDialog(final Context context,final PriorityListener listener, DLBirthDateSource source,int width,int height) {
		super(context, theme);
		this.context = context;
		lis = listener;
		this.dataSource = source;
		this.width = width;
		this.height = height;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.date_select_wheel);
		
		btn_sure = (Button) findViewById(R.id.confirm_btn);
		btn_sure.setOnClickListener(this);
		btn_cancel = (Button) findViewById(R.id.cancel_btn);
		btn_cancel.setOnClickListener(this);
		
		date_layout = (LinearLayout) findViewById(R.id.date_selelct_layout);
		LayoutParams lparams_hours = new LayoutParams(width, height / 3 + 5);
		date_layout.setLayoutParams(lparams_hours);
		
		rl_hour = (RelativeLayout)findViewById(R.id.rl_hour);
		rl_mins = (RelativeLayout)findViewById(R.id.rl_mins);
		
		yearview = (DLWheelView) findViewById(R.id.year);
		monthview = (DLWheelView) findViewById(R.id.month);
		dayview = (DLWheelView) findViewById(R.id.day);
		hoursview = (DLWheelView) findViewById(R.id.hours);
		minsview = (DLWheelView) findViewById(R.id.mins);
		
		yearview.addScrollingListener(slistener);
		monthview.addScrollingListener(slistener);
		dayview.addScrollingListener(slistener);
		hoursview.addScrollingListener(slistener);
		minsview.addScrollingListener(slistener);
		
		rl_mins.setVisibility(View.GONE);
		rl_hour.setVisibility(View.GONE);
		
		if(dataSource.getType() == TBirthType.EBirth_Time){
			rl_mins.setVisibility(View.VISIBLE);
			rl_hour.setVisibility(View.VISIBLE);
		}
		
		Calendar calendar = Calendar.getInstance();
		if (this.dataSource.getCurYear() == 0 || this.dataSource.getCurMonth() == 0) {
			dataSource.setCurYear(calendar.get(Calendar.YEAR));
			dataSource.setCurMonth(calendar.get(Calendar.MONTH) + 1);
			dataSource.setCurDay(calendar.get(Calendar.DAY_OF_MONTH));
		}
		
		initData();
	}
	
	public OnDLWheelScrollListener slistener = new OnDLWheelScrollListener() {
		
		@Override
		public void onScrollingStarted(DLWheelView wheel) {

		}
		
		@Override
		public void onScrollingFinished(DLWheelView wheel) {
			
			changeValues();
		}
	};
	
	private void initData(){
		
		updateYears(dataSource.getMinDate()[0], dataSource.getMaxData()[0],true);
		updateMonths(dataSource.getMinDate()[1], dataSource.getMaxData()[1],true);
		updateDays(dataSource.getMinDate()[2], dataSource.getMaxData()[2],true);
		if(dataSource.getType() == TBirthType.EBirth_Time){
			updateHours(dataSource.getMinDate()[3], dataSource.getMaxData()[3],true);
			updateMins(dataSource.getMinDate()[4], dataSource.getMaxData()[4],true);
		}
		
	}

	private void changeValues(){
		
		int year_num = yearview.getCurrentItem() + dataSource.getMinDate()[0];
		int month_num = monthview.getCurrentItem() + dataSource.getMinDate()[1];
		int day_num = dayview.getCurrentItem() + dataSource.getMinDate()[2];
		int hour_num = hoursview.getCurrentItem() + dataSource.getMinDate()[3];
		
		if(dataSource.getMinDate()[0] == year_num){
			if(dataSource.getMinDate()[1] == month_num){
				if(dataSource.getMinDate()[2] == day_num){
					if(dataSource.getType() == TBirthType.EBirth_Time){
						if(dataSource.getMinDate()[3] == hour_num){
							updateMins(dataSource.getMinDate()[4], dataSource.getMaxData()[4],false);
						}else{
							updateHours(dataSource.getMinDate()[3], dataSource.getMaxData()[3],false);
							hour_num = hoursview.getCurrentItem() + dataSource.getMinDate()[3];
							if(hour_num == dataSource.getMinDate()[3]){
								updateMins(dataSource.getMinDate()[4], dataSource.getMaxData()[4],false);
							}else{
								updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
							}
						}
					}
				}else{
					updateDays(dataSource.getMinDate()[2], dataSource.getMaxData()[2],false);
					if(dataSource.getType() == TBirthType.EBirth_Time){
						day_num = dayview.getCurrentItem() + dataSource.getMinDate()[2];
						if(day_num == dataSource.getMinDate()[2]){
							updateHours(dataSource.getMinDate()[3], dataSource.getMaxData()[3],false);
							hour_num = hoursview.getCurrentItem() + dataSource.getMinDate()[3];
							if(hour_num == dataSource.getMinDate()[3]){
								updateMins(dataSource.getMinDate()[4], dataSource.getMaxData()[4],false);
							}else{
								updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
							}
						}else{
							updateHours(DLBirthDateSource.MIN_DATE[3], DLBirthDateSource.MAX_DATE[3],false);
							updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
						}
					}
				}
			}else{
				updateMonths(dataSource.getMinDate()[1], dataSource.getMaxData()[1],false);
				month_num = monthview.getCurrentItem() + dataSource.getMinDate()[1];
				if(month_num == dataSource.getMinDate()[1]){
					updateDays(dataSource.getMinDate()[2], dataSource.getMaxData()[2],false);
					if(dataSource.getType() == TBirthType.EBirth_Time){
						day_num = dayview.getCurrentItem() + dataSource.getMinDate()[2];
						if(day_num == dataSource.getMinDate()[2]){
							updateHours(dataSource.getMinDate()[3], dataSource.getMaxData()[3],false);
							hour_num = hoursview.getCurrentItem() + dataSource.getMinDate()[3];
							if(hour_num == dataSource.getMinDate()[3]){
								updateMins(dataSource.getMinDate()[4], dataSource.getMaxData()[4],false);
							}else{
								updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
							}
						}else{
							updateHours(DLBirthDateSource.MIN_DATE[3], DLBirthDateSource.MAX_DATE[3],false);
							updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
						}
					}
				}else{
					updateDays(DLBirthDateSource.MIN_DATE[2], DLBirthDateSource.MAX_DATE[2],false);
					if(dataSource.getType() == TBirthType.EBirth_Time){
						updateHours(DLBirthDateSource.MIN_DATE[3], DLBirthDateSource.MAX_DATE[3],false);
						updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
					}
				}
			}
		}else if(dataSource.getMaxData()[0] == year_num){
			if(dataSource.getMaxData()[1] == month_num){
				if(dataSource.getMaxData()[2] == day_num){
					if(dataSource.getType() == TBirthType.EBirth_Time){
						if(dataSource.getMaxData()[3] == hour_num){
							updateMonths(dataSource.getMinDate()[1], dataSource.getMaxData()[1],false);
							updateDays(dataSource.getMinDate()[2], dataSource.getMaxData()[2],false);
							updateHours(dataSource.getMinDate()[3], dataSource.getMaxData()[3],false);
							updateMins(dataSource.getMinDate()[4], dataSource.getMaxData()[4],false);
						}else{
							updateHours(dataSource.getMinDate()[3], dataSource.getMaxData()[3],false);
							hour_num = hoursview.getCurrentItem() + dataSource.getMinDate()[3];
							if(hour_num == dataSource.getMaxData()[3]){
								updateMins(dataSource.getMinDate()[4], dataSource.getMaxData()[4],false);
							}else{
								updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
							}
						}
					}else{
						updateMonths(dataSource.getMinDate()[1], dataSource.getMaxData()[1],false);
						updateDays(dataSource.getMinDate()[2], dataSource.getMaxData()[2],false);
					}
				}else{
					updateDays(dataSource.getMinDate()[2], dataSource.getMaxData()[2],false);
					if(dataSource.getType() == TBirthType.EBirth_Time){
						day_num = dayview.getCurrentItem() + dataSource.getMinDate()[2];
						if(day_num == dataSource.getMaxData()[2]){
							updateHours(dataSource.getMinDate()[3], dataSource.getMaxData()[3],false);
							hour_num = hoursview.getCurrentItem() + dataSource.getMinDate()[3];
							if(hour_num == dataSource.getMaxData()[3]){
								updateMins(dataSource.getMinDate()[4], dataSource.getMaxData()[4],false);
							}else{
								updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
							}
						}else{
							updateHours(DLBirthDateSource.MIN_DATE[3], DLBirthDateSource.MAX_DATE[3],false);
							updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
						}
					}
				}
			}else{
				updateMonths(dataSource.getMinDate()[1], dataSource.getMaxData()[1],false);
				month_num = monthview.getCurrentItem() + dataSource.getMinDate()[1];
				if(month_num == dataSource.getMaxData()[1]){
					updateDays(dataSource.getMinDate()[2], dataSource.getMaxData()[2],false);
					if(dataSource.getType() == TBirthType.EBirth_Time){
						day_num = dayview.getCurrentItem() + dataSource.getMinDate()[2];
						if(day_num == dataSource.getMaxData()[2]){
							updateHours(dataSource.getMinDate()[3], dataSource.getMaxData()[3],false);
							hour_num = hoursview.getCurrentItem() + dataSource.getMinDate()[3];
							if(hour_num == dataSource.getMaxData()[3]){
								updateMins(dataSource.getMinDate()[4], dataSource.getMaxData()[4],false);
							}else{
								updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
							}
						}else{
							updateHours(DLBirthDateSource.MIN_DATE[3], DLBirthDateSource.MAX_DATE[3],false);
							updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
						}
					}
				}else{
					updateDays(DLBirthDateSource.MIN_DATE[2], DLBirthDateSource.MAX_DATE[2],false);
					if(dataSource.getType() == TBirthType.EBirth_Time){
						updateHours(DLBirthDateSource.MIN_DATE[3], DLBirthDateSource.MAX_DATE[3],false);
						updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
					}
				}
			}
		}else{
			updateMonths(DLBirthDateSource.MIN_DATE[1], DLBirthDateSource.MAX_DATE[1],false);
			updateDays(DLBirthDateSource.MIN_DATE[2], DLBirthDateSource.MAX_DATE[2],false);
			if(dataSource.getType() == TBirthType.EBirth_Time){
				updateHours(DLBirthDateSource.MIN_DATE[3], DLBirthDateSource.MAX_DATE[3],false);
				updateMins(DLBirthDateSource.MIN_DATE[4], DLBirthDateSource.MAX_DATE[4],false);
			}
		}
	}
	
	private void updateYears(int min,int max,boolean showIndex){
		year_adapter = new DLNumericWheelAdapter(min, max);
		yearview.setAdapter(year_adapter);
		if(showIndex){
			int yindex = this.dataSource.getCurYear() - min;// 按下标来
			yearview.setCurrentItem(yindex);
		}
		yearview.setCyclic(false);
		yearview.setVisibleItems(3);
	}
	
	private void updateMonths(int min,int max,boolean showIndex){
		
		int mindex = 0;
		
		if(showIndex){
			mindex = this.dataSource.getCurMonth() - min;
		}else{
			mindex = monthview.getCurrentItem() + month_adapter.getMinValue()-min;
		}
		if(mindex <0){
			mindex = 0;
		}
		if(mindex > max-min){
			mindex = max-min;
		}
		
		month_adapter = new DLNumericWheelAdapter(min, max, "%02d");
		monthview.setAdapter(month_adapter);
		monthview.setCurrentItem(mindex);
		monthview.setCyclic(false);
		monthview.setVisibleItems(3);
	}
	
	private void updateDays(int min,int max,boolean showIndex){
		// 添加大小月月份并将其转换为list,方便之后的
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		
		int year_num = yearview.getCurrentItem() + dataSource.getMinDate()[0];
		int month_num = monthview.getCurrentItem() + dataSource.getMinDate()[1];
		
		int dindex = 0;
		
		if(showIndex){
			dindex = dataSource.getCurDay()- min;
		}else{
			dindex = dayview.getCurrentItem() + day_adapter.getMinValue()-min;
		}
		if(dindex <0){
			dindex = 0;
		}
		if(dindex > max-min){
			dindex = max-min;
		}
		
		// 判断大小月及是否闰年,用来确定
		if (list_big.contains(String.valueOf(month_num))) {
			day_adapter = new DLNumericWheelAdapter(min, max>31?31:max, "%02d");
		} else if (list_little.contains(String.valueOf(month_num))) {
			day_adapter = new DLNumericWheelAdapter(min, max>30?30:max, "%02d");
		} else {
			if ((year_num % 4 == 0 && year_num % 100 != 0)|| year_num % 400 == 0)
				day_adapter = new DLNumericWheelAdapter(min, max>29?29:max, "%02d");
			else
				day_adapter = new DLNumericWheelAdapter(min, max>28?28:max, "%02d");
		}
		dayview.setAdapter(day_adapter);
		dayview.setCurrentItem(dindex);
		dayview.setCyclic(false);
		dayview.setVisibleItems(3);
	}
	
	private void updateHours(int min,int max,boolean showIndex){
		
		int hindex = 0;
		
		if(showIndex){
			hindex = this.dataSource.getHours() - min;
		}else{
			hindex = hoursview.getCurrentItem() + hours_adapter.getMinValue()-min;
		}
		if(hindex <0){
			hindex = 0;
		}
		if(hindex > max-min){
			hindex = max-min;
		}
		
		hours_adapter = new DLNumericWheelAdapter(min, max, "%02d");
		hoursview.setAdapter(hours_adapter);
		hoursview.setCurrentItem(hindex);
		hoursview.setCyclic(false);
		hoursview.setVisibleItems(3);
	}
	
	private void updateMins(int min,int max,boolean showIndex){
		
		int sindex = 0;
		
		if(showIndex){
			sindex = this.dataSource.getMins() - min;
		}else{
			sindex = minsview.getCurrentItem() + mins_adapter.getMinValue()-min;
		}
		if(sindex < 0){
			sindex = 0;
		}
		if(sindex > max-min){
			sindex = max-min;
		}
		
		mins_adapter = new DLNumericWheelAdapter(min, max, "%02d");
		minsview.setAdapter(mins_adapter);
		minsview.setCurrentItem(sindex);
		minsview.setCyclic(false);
		minsview.setVisibleItems(3);
	}

	public DLBirthDateDialog(Context context, PriorityListener listener) {
		super(context, theme);
		this.context = context;
	}

	public DLBirthDateDialog(Context context, String birthDate) {
		super(context, theme);
		this.context = context;
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId() == R.id.confirm_btn){
			
			if(dataSource.getType() == TBirthType.EBirth_Date){
				dataSource.setSeleteValue(year_adapter.getValues()+"-"+month_adapter.getValues()+"-"+day_adapter.getValues());
			}else if(dataSource.getType() == TBirthType.EBirth_Time){
				dataSource.setSeleteValue(year_adapter.getValues()+"-"+month_adapter.getValues()+"-"+day_adapter.getValues()+" "+hours_adapter.getValues()+":"+mins_adapter.getValues());
			}
			
			if(dataSource.getMinDate()!=null && dataSource.getMaxData()!=null){
			
				int tempYear = DLStringUtil.strToInt(year_adapter.getValues());
				int tempMonth = DLStringUtil.strToInt(month_adapter.getValues());
				int tempDay = DLStringUtil.strToInt(day_adapter.getValues());
				
				if(tempYear < dataSource.getMinDate()[0] || tempYear > dataSource.getMaxData()[0]){
					return;
				}
				if(tempMonth<dataSource.getMinDate()[1] || tempMonth>dataSource.getMaxData()[1]){
					return;
				}
				if(tempDay<dataSource.getMinDate()[2] || tempDay>dataSource.getMaxData()[2]){
					return;
				}
				if(dataSource.getType() == TBirthType.EBirth_Time){
					int tempHour = DLStringUtil.strToInt(hours_adapter.getValues());
					int tempMin = DLStringUtil.strToInt(mins_adapter.getValues());
					
					if(tempHour<dataSource.getMinDate()[3] || tempHour>dataSource.getMaxData()[3]){
						return;
					}
					if(tempMin<dataSource.getMinDate()[4] || tempMin>dataSource.getMaxData()[4]){
						return;
					}
				}
			}
			
			if(lis!=null){
				lis.refreshPriorityUI(dataSource.getSeleteValue());
				this.dismiss();
			}
		}else if(v.getId() == R.id.cancel_btn){
			this.dismiss();
		}
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}
