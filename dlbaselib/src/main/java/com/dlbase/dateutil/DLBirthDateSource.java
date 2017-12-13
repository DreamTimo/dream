package com.dlbase.dateutil;

import java.util.Calendar;

import com.dlbase.util.DLDateUtils;


public class DLBirthDateSource {

	public enum TBirthType{
		EBirth_Date,
		EBirth_Time
	}
	
	private TBirthType type;
	//yyyy-MM-dd HH:mm:ss
	private String seleteValue;
	//yyyy-MM-dd HH:mm:ss
	private String defaultValue;
	
	private int curYear = 0;
	private int curMonth = 0;
	private int curDay = 0;
	private int hours = 0;
	private int mins = 0;
	
	public final static  int[] MIN_DATE = new int[]{1900,1,1,0,0}; 
	public final static  int[] MAX_DATE = new int[]{2100,12,31,23,59}; 
	
	private int[] minDate = null;
	private int[] maxData = null;
	
	public DLBirthDateSource(){
		
		type = TBirthType.EBirth_Date;
		defaultValue = DLDateUtils.getCurrentTimeInString();
		
		minDate = new int[]{1900,1,1,0,0};
		maxData = new int[]{2100,12,31,23,59};
	}
	
	public int getCurYear() {
		return curYear;
	}

	public void setCurYear(int curYear) {
		this.curYear = curYear;
	}

	public int getCurMonth() {
		return curMonth;
	}

	public void setCurMonth(int curMonth) {
		this.curMonth = curMonth;
	}

	public int getCurDay() {
		return curDay;
	}

	public void setCurDay(int curDay) {
		this.curDay = curDay;
	}

	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMins() {
		return mins;
	}

	public void setMins(int mins) {
		this.mins = mins;
	}

	public TBirthType getType() {
		return type;
	}

	public void setType(TBirthType type) {
		this.type = type;
	}

	public String getSeleteValue() {
		return seleteValue;
	}

	public void setSeleteValue(String seleteValue) {
		this.seleteValue = seleteValue;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * 设置默认值 格式：yyyy-MM-dd HH:mm:ss
	 * @param defaultValue
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
		
		if(this.type==TBirthType.EBirth_Date){
			Calendar calendar = DLDateUtils.getCalendarFromDate(defaultValue);
			if(calendar!=null){
				setCurYear(calendar.get(Calendar.YEAR));
				setCurMonth(calendar.get(Calendar.MONTH)+1);
				setCurDay(calendar.get(Calendar.DAY_OF_MONTH));
			}
		}else if(this.type == TBirthType.EBirth_Time){
			Calendar calendar = DLDateUtils.getCalendarFromTimeMM(defaultValue);
			if(calendar!=null){
				setCurYear(calendar.get(Calendar.YEAR));
				setCurMonth(calendar.get(Calendar.MONTH)+1);
				setCurDay(calendar.get(Calendar.DAY_OF_MONTH));
				setHours(calendar.get(Calendar.HOUR_OF_DAY));
				setMins(calendar.get(Calendar.MINUTE));
			}
		}
	}

	public int[] getMinDate() {
		return minDate;
	}

	/**
	 * 设置最小时间限制 格式：yyyy-MM-dd HH:mm:ss
	 * @param min
	 */
	public void setMinDate(String min) {
		
		if(this.type==TBirthType.EBirth_Date){
			Calendar calendar = DLDateUtils.getCalendarFromDate(min);
			if(calendar!=null){
				minDate[0] = calendar.get(Calendar.YEAR);
				minDate[1] = calendar.get(Calendar.MONTH)+1;
				minDate[2] = calendar.get(Calendar.DAY_OF_MONTH);
			}
		}else if(this.type == TBirthType.EBirth_Time){
			Calendar calendar = DLDateUtils.getCalendarFromTimeMM(min);
			if(calendar!=null){
				minDate[0] = calendar.get(Calendar.YEAR);
				minDate[1] = calendar.get(Calendar.MONTH)+1;
				minDate[2] = calendar.get(Calendar.DAY_OF_MONTH);
				minDate[3] = calendar.get(Calendar.HOUR_OF_DAY);
				minDate[4] = calendar.get(Calendar.MINUTE);
			}
		}
	}

	public int[] getMaxData() {
		return maxData;
	}

	/**
	 * 设置最大时间限制 格式：yyyy-MM-dd HH:mm:ss
	 * @param max
	 */
	public void setMaxData(String max) {
		if(this.type==TBirthType.EBirth_Date){
			Calendar calendar = DLDateUtils.getCalendarFromDate(max);
			if(calendar!=null){
				maxData[0] = calendar.get(Calendar.YEAR);
				maxData[1] = calendar.get(Calendar.MONTH)+1;
				maxData[2] = calendar.get(Calendar.DAY_OF_MONTH);
			}
		}else if(this.type == TBirthType.EBirth_Time){
			Calendar calendar = DLDateUtils.getCalendarFromTimeMM(max);
			if(calendar!=null){
				maxData[0] = calendar.get(Calendar.YEAR);
				maxData[1] = calendar.get(Calendar.MONTH)+1;
				maxData[2] = calendar.get(Calendar.DAY_OF_MONTH);
				maxData[3] = calendar.get(Calendar.HOUR_OF_DAY);
				maxData[4] = calendar.get(Calendar.MINUTE);
			}
		}
	}
	
	
}
