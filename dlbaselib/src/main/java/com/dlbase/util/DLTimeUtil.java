package com.dlbase.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("all")
public class DLTimeUtil {
  public static String ToTime(Long time){
		Long timestamp=time*1000;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String datetime=sdf.format(new Date(timestamp));
		String result = "刚刚";
		try {
			Date  date = sdf.parse(datetime);
			Date nowdate=new Date();
			 long l = nowdate.getTime() - date.getTime();
			 long day=l/(24*60*60*1000);
			 long hour=(l/(60*60*1000)-day*24);
			 long min=((l/(60*1000))-day*24*60-hour*60);
			if(new Long(day).intValue()>0){
				result=day+"天前";
				if(day>7){
					result = DLDateUtils.getDate(timestamp);
				}
			}else if(new Long(hour).intValue()>0){
				result=hour+"小时前";
			}else if(new Long(min).intValue()>0){
				result=min+"分钟前";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
  }
  public static String DateToTime(Date date){
	    SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
	    String time=format.format(date);
		return time;
}
  public static Date TimeToData(String time){
	  SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
	    Date date = null;
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
}

  public static String TimeToData(long time){
	  time=time*1000;
	  SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	  String result = "";
		try {
			result=sdf.format(new Date(time));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
  }
  public static String getTime(Long time){
	    time=time*1000;
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String datetime=sdf.format(new Date(time));
		String result = "刚刚";
		try {
			Date  date = sdf.parse(datetime);
			Date nowdate=new Date();
			 long l = nowdate.getTime() - date.getTime();
			 long day=l/(24*60*60*1000);
			 long hour=(l/(60*60*1000)-day*24);
			 long min=((l/(60*1000))-day*24*60-hour*60);
			if(new Long(day).intValue()>0){
				result=sdf1.format(new Date(time));
			}else if(new Long(hour).intValue()>0){
				result=hour+"小时前";
			}else if(new Long(min).intValue()>0){
				result=min+"分钟前";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
}
  public static String getLongTime(Long time){
	     StringBuffer result=new StringBuffer();
		 long day=time/(24*60*60);
		 long hour=(time/(60*60)-day*24);
		 long min=((time/(60))-day*24*60-hour*60);
		 long ss=(time-day*24*60*60-hour*60*60-min*60);
		if(new Long(day).intValue()>0){
			result.append(day+"天");
			return result.toString();
		} if(new Long(hour).intValue()>0){
			result.append(hour+"小时");
		} if(new Long(min).intValue()>0){
			result.append(min+"分钟");
		} if(new Long(ss).intValue()>0){
			result.append(ss+"秒");
		}
		return result.toString();
}
  public static String getNowTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		Date date = new Date();
		sdf.format(date);
		return sdf.format(date);
	}
  public static String getDateTime(long str) {
 	 Long timestamp=str*1000;
			SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
			String datetime=format.format(new Date(timestamp));
	 return datetime;
   }
  public static Long getTime1(long str) {
	 	 Long timestamp=str*1000;
	     SimpleDateFormat format=new SimpleDateFormat("yyyy");
		 String datetime=format.format(new Date(timestamp));
		 String time=format.format(new Date());
		 return Long.parseLong(time)-Long.parseLong(datetime);
	   }
 
}
