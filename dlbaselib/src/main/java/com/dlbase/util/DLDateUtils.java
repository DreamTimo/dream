package com.dlbase.util;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * @author luyz
 * 时间类 工具类
 */
@SuppressLint("SimpleDateFormat")
public class DLDateUtils {
	
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DEFAULT_DATEMM_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");
	
	private DLDateUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
     * long time to string
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }
    
    public static String getTimeMM(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATEMM_FORMAT);
    }
    
    /**
     * long date to string, format is {@link #DATE_FORMAT_DATE}
     * 
     * @param timeInMillis
     * @return
     */
    public static String getDate(long timeInMillis) {
        return getTime(timeInMillis, DATE_FORMAT_DATE);
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }
	
	/**
	 * 根据传入的时间，格式化成 2015-12-22 16:00:00 ,
	 * 
	 * @author jmh
	 * @param milliseconds
	 * @return
	 */
	public static String formatTime(long ms) {
		// long now=System.currentTimeMillis();
		Date date = new Date(ms);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdf.format(date);
		return dateStr;
	}

	/**
	 * 根据传入的时间，格式化成 str指定的日期格式,
	 * 
	 * @author jmh
	 * @param milliseconds,日期格式如yyyy-MM-dd
	 *            HH:mm:ss
	 * @return
	 */
	public static String formatTime(long ms, String str) {
		// long now=System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat(str);
		String dateStr = sdf.format(ms + TimeZone.getDefault().getRawOffset());//
		return dateStr;
	}

	/**
	 * 根据传入的时间，格式化成 2013-12-14 16：00 ,
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String formatterDateAndTime(long milliseconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String date = formatterDate(milliseconds, "-");
		StringBuilder str = new StringBuilder();
		str.append(date + " ");
		if (hour < 10) {
			str.append(" ");// 不补0位，补个空格对齐
		}
		str.append(hour);
		str.append(":");
		if (minute < 10) {
			str.append(0);// 添0补位
		}
		str.append(minute);

		return str.toString();
	}

	/**
	 * 通过milliseconds得到一个格式为"yyyy-mm-dd"的日期，比如2009-10-29
	 */
	public static String formatterDate(long milliseconds, String separator) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		StringBuilder str = new StringBuilder();
		str.append(calendar.get(Calendar.YEAR));
		str.append(separator);
		str.append(month);// 月份从0开始
		str.append(separator);
		str.append(day);
		return str.toString();
	}

	/**
	 * 计算两个日期间的相差天数，根据当前的日期，判断两个时间是否在同一小时
	 * 
	 * @param time1
	 * @param time2
	 * @return time2 比 time1多的天数，0表示在同一天
	 */
	@SuppressLint({ "UseValueOf", "SimpleDateFormat" })
	public static int getTimeDiff(long time1, long time2) {
		
		Long timestamp1=time1*1000;
		Long timestamp2=time2*1000;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String datetime1=sdf.format(new Date(timestamp1));
		String datetime2=sdf.format(new Date(timestamp2));
		int result = -1;
		try {
			Date  date = sdf.parse(datetime1);
			Date nowdate=sdf.parse(datetime2);
			 long l =   date.getTime()-nowdate.getTime();
			 long day=l/(24*60*60*1000);
			 long hour=(l/(60*60*1000)-day*24);
//			 long min=((l/(60*1000))-day*24*60-hour*60);
//			 if(new Long(min).intValue()>30){
//				 result = 3;
//			 }else 
		    if(new Long(day).intValue()>0){
				result=2;
			}else if(new Long(hour).intValue()>0){
				result=1;
			}else{
				result = 0;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 计算两个日期间的相差天数，根据当前的日期，判断两个时间是否在同一天
	 * 
	 * @param time1
	 * @param time2
	 * @return time2 比 time1多的天数，0表示在同一天
	 */
	public static int getDateDiff(long time1, long time2) {
		int year1, year2, month1, month2, day1, day2;
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time1*1000);

		year1 = calendar.get(Calendar.YEAR);
		month1 = calendar.get(Calendar.MONTH) + 1;
		day1 = calendar.get(Calendar.DAY_OF_MONTH);

		calendar.setTimeInMillis(time2*1000);
		year2 = calendar.get(Calendar.YEAR);
		month2 = calendar.get(Calendar.MONTH) + 1;
		day2 = calendar.get(Calendar.DAY_OF_MONTH);

		if ((year1 == year2)) {
			if (month1 == month2) {
				if (day1 - day2 == 0) {
					return 0; // 今天
				} else if (day1 - day2 == 1) {
					return 1; // 昨天
				} else {
					return 2;
				}
			} else {
				return 3;
			}
		} else {
			return 4;// 去年
		}
	}
	
	/**
	 * 
	 * 时间戳 比较大小 0 ＝ , 1 > , -1 <
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static int getDateCompareDiff(long time1 ,long time2){
		
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(time1*1000);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(time2*1000);
		
		return calendar1.compareTo(calendar2);
	}
	
	public static int[] getTimerSuffic(long milliseconds){
		
		int[] tempStr = new int[6]; 
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		
		tempStr[0]=calendar.get(Calendar.YEAR);
		tempStr[1]=calendar.get(Calendar.MONTH)+1;
		tempStr[2]=calendar.get(Calendar.DAY_OF_MONTH);
		tempStr[3]=calendar.get(Calendar.HOUR_OF_DAY);
		tempStr[4]=calendar.get(Calendar.MINUTE);
		tempStr[5]=calendar.get(Calendar.SECOND);
		
		return tempStr;
	}
	
	/**
	 * 通过milliseconds得到一个格式为"yyyymmddhhmmssmm"的日期，比如20120521165730520
	 */
	public static String getTimeSuffix(long milliseconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milliseconds);
		StringBuilder str = new StringBuilder();
		int time;
		str.append(calendar.get(Calendar.YEAR));
		if ((time = calendar.get(Calendar.MONTH) + 1) < 10) {
			str.append(0);
		}
		str.append(time);
		if ((time = calendar.get(Calendar.DAY_OF_MONTH)) < 10) {
			str.append(0);
		}
		str.append(time);
		if ((time = calendar.get(Calendar.HOUR_OF_DAY)) < 10) {
			str.append(0);
		}
		str.append(time);
		if ((time = calendar.get(Calendar.MINUTE)) < 10) {
			str.append(0);
		}
		str.append(time);
		if ((time = calendar.get(Calendar.SECOND)) < 10) {
			str.append(0);
		}
		str.append(time);
		if ((time = calendar.get(Calendar.MILLISECOND)) < 10) {
			str.append("00");
		}
		else if (time < 100) {
			str.append("0");
		}
		str.append(time);
		return str.toString();
	}
	
	 //把日期转为字符串  
    public static String ConverToString(Date date)  
    {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
          
        return df.format(date);  
    }  
    //把字符串转为日期  
    public static Date ConverToDate(String strDate)  
    {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
        try {
			return (Date) df.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return null;
    } 
    
  //把字符串转为时间 
    public static Date ConverToTime(String strTime) 
    {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        try {
			return (Date) df.parse(strTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return null;
    }  
    
    /**
     * 获取现在时间
     * 
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
   public static Date getNowDate() {
     Date currentTime = new Date();
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String dateString = formatter.format(currentTime);
     ParsePosition pos = new ParsePosition(8);
     Date currentTime_2 = formatter.parse(dateString, pos);
     return currentTime_2;
   }
    
   /**
     * 获取现在时间
     * 
     * @return返回短时间格式 yyyy-MM-dd
     */
   public static Date getNowDateShort() {
     Date currentTime = new Date();
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     String dateString = formatter.format(currentTime);
     ParsePosition pos = new ParsePosition(8);
     Date currentTime_2 = formatter.parse(dateString, pos);
     return currentTime_2;
   }
    
   /**
     * 获取现在时间
     * 
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
   public static String getStringDate() {
     Date currentTime = new Date();
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String dateString = formatter.format(currentTime);
     return dateString;
   }
    
   /**
     * 获取现在时间
     * 
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
   public static String getStringDateShort() {
     Date currentTime = new Date();
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     String dateString = formatter.format(currentTime);
     return dateString;
   }
    
   /**
     * 获取时间 小时:分;秒 HH:mm:ss
     * 
     * @return
     */
   public static String getTimeShort() {
     SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
     Date currentTime = new Date();
     String dateString = formatter.format(currentTime);
     return dateString;
   }
    
   /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
     * 
     * @param strDate
     * @return
     */
   public static Date strToDateLong(String strDate) {
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     ParsePosition pos = new ParsePosition(0);
     Date strtodate = formatter.parse(strDate, pos);
     return strtodate;
   }
   
   public static Date strToDateLongMM(String strDate) {
	     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	     ParsePosition pos = new ParsePosition(0);
	     Date strtodate = formatter.parse(strDate, pos);
	     return strtodate;
	   }
    
   /**  * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss  *   * @param dateDate  * @return  */
   public static String dateToStrLong(java.util.Date dateDate) {
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String dateString = formatter.format(dateDate);
     return dateString;
   }
    
   /**
     * 将短时间格式时间转换为字符串 yyyy-MM-dd
     * 
     * @param dateDate
     * @param k
     * @return
     */
   public static String dateToStr(java.util.Date dateDate) {
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     String dateString = formatter.format(dateDate);
     return dateString;
   }
    
   /**
     * 将短时间格式字符串转换为时间 yyyy-MM-dd 
     * 
     * @param strDate
     * @return
     */
   public static Date strToDate(String strDate) {
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     ParsePosition pos = new ParsePosition(0);
     Date strtodate = formatter.parse(strDate, pos);
     return strtodate;
   }
    
   /**
     * 得到现在时间
     * 
     * @return
     */
   public static Date getNow() {
     Date currentTime = new Date();
     return currentTime;
   }
    
   /**
     * 提取一个月中的最后一天
     * 
     * @param day
     * @return
     */
   public static Date getLastDate(long day) {
     Date date = new Date();
     long date_3_hm = date.getTime() - 3600000 * 34 * day;
     Date date_3_hm_date = new Date(date_3_hm);
     return date_3_hm_date;
   }
    
   /**
     * 得到现在时间
     * 
     * @return 字符串 yyyyMMdd HHmmss
     */
   public static String getStringToday() {
     Date currentTime = new Date();
     SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HHmmss");
     String dateString = formatter.format(currentTime);
     return dateString;
   }
    
   /**
     * 得到现在小时
     */
   public static String getHour() {
     Date currentTime = new Date();
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String dateString = formatter.format(currentTime);
     String hour;
     hour = dateString.substring(11, 13);
     return hour;
   }
    
   /**
     * 得到现在分钟
     * 
     * @return
     */
   public static String getTime() {
     Date currentTime = new Date();
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String dateString = formatter.format(currentTime);
     String min;
     min = dateString.substring(14, 16);
     return min;
   }
    
   /**
     * 根据用户传入的时间表示格式，返回当前时间的格式 如果是yyyyMMdd，注意字母y不能大写。
     * 
     * @param sformat
     *            yyyyMMddhhmmss
     * @return
     */
   public static String getUserDate(String sformat) {
     Date currentTime = new Date();
     SimpleDateFormat formatter = new SimpleDateFormat(sformat);
     String dateString = formatter.format(currentTime);
     return dateString;
   }
    
   /**
     * 二个小时时间间的差值,必须保证二个时间都是"HH:MM"的格式，返回字符型的分钟
     */
   public static String getTwoHour(String st1, String st2) {
     String[] kk = null;
     String[] jj = null;
     kk = st1.split(":");
     jj = st2.split(":");
     if (Integer.parseInt(kk[0]) < Integer.parseInt(jj[0]))
      return "0";
     else {
      double y = Double.parseDouble(kk[0]) + Double.parseDouble(kk[1]) / 60;
      double u = Double.parseDouble(jj[0]) + Double.parseDouble(jj[1]) / 60;
      if ((y - u) > 0)
       return y - u + "";
      else
       return "0";
     }
   }
    
   /**
     * 得到二个日期间的间隔天数
     */
   public static String getTwoDay(String sj1, String sj2) {
     SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
     long day = 0;
     try {
      java.util.Date date = myFormatter.parse(sj1);
      java.util.Date mydate = myFormatter.parse(sj2);
      day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
     } catch (Exception e) {
      return "";
     }
     return day + "";
   }
    
   /**
     * 时间前推或后推分钟,其中JJ表示分钟.
     */
   public static String getPreTime(String sj1, String jj) {
     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     String mydate1 = "";
     try {
      Date date1 = format.parse(sj1);
      long Time = (date1.getTime() / 1000) + Integer.parseInt(jj) * 60;
      date1.setTime(Time * 1000);
      mydate1 = format.format(date1);
     } catch (Exception e) {
     }
     return mydate1;
   }
    
   /**
     * 得到一个时间延后或前移几天的时间,nowdate为时间,delay为前移或后延的天数
     */
   public static String getNextDay(String nowdate, String delay) {
     try{
     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
     String mdate = "";
     Date d = strToDate(nowdate);
     long myTime = (d.getTime() / 1000) + Integer.parseInt(delay) * 24 * 60 * 60;
     d.setTime(myTime * 1000);
     mdate = format.format(d);
     return mdate;
     }catch(Exception e){
      return "";
     }
   }
    
   /**
     * 判断是否润年
     * 
     * @param ddate
     * @return
     */
   public static boolean isLeapYear(String ddate) {
    
     /**
      * 详细设计： 1.被400整除是闰年，否则： 2.不能被4整除则不是闰年 3.能被4整除同时不能被100整除则是闰年
      * 3.能被4整除同时能被100整除则不是闰年
      */
     Date d = strToDate(ddate);
     GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
     gc.setTime(d);
     int year = gc.get(Calendar.YEAR);
     if ((year % 400) == 0)
      return true;
     else if ((year % 4) == 0) {
      if ((year % 100) == 0)
       return false;
      else
       return true;
     } else
      return false;
   }
    
   /**
     * 返回美国时间格式 26 Apr 2006
     * 
     * @param str
     * @return
     */
   @SuppressLint("DefaultLocale")
   public static String getEDate(String str) {	
     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     ParsePosition pos = new ParsePosition(0);
     Date strtodate = formatter.parse(str, pos);
     String j = strtodate.toString();
     String[] k = j.split(" ");
     return k[2] + k[1].toUpperCase() + k[5].substring(2, 4);
   }
    
   /**
     * 获取一个月的最后一天
     * 
     * @param dat
     * @return
     */
   public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd
     String str = dat.substring(0, 8);
     String month = dat.substring(5, 7);
     int mon = Integer.parseInt(month);
     if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
      str += "31";
     } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
      str += "30";
     } else {
      if (isLeapYear(dat)) {
       str += "29";
      } else {
       str += "28";
      }
     }
     return str;
   }
    
   /**
     * 判断二个时间是否在同一个周
     * 
     * @param date1
     * @param date2
     * @return
     */
   public static boolean isSameWeekDates(Date date1, Date date2) {
     Calendar cal1 = Calendar.getInstance();
     Calendar cal2 = Calendar.getInstance();
     cal1.setTime(date1);
     cal2.setTime(date2);
     int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
     if (0 == subYear) {
      if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
       return true;
     } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
      // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
      if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
       return true;
     } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
      if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
       return true;
     }
     return false;
   }
    
   /**
     * 产生周序列,即得到当前时间所在的年度是第几周
     * 
     * @return
     */
   public static String getSeqWeek() {
     Calendar c = Calendar.getInstance(Locale.CHINA);
     String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
     if (week.length() == 1)
      week = "0" + week;
     String year = Integer.toString(c.get(Calendar.YEAR));
     return year + week;
   }
    
   /**
     * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
     * 
     * @param sdate
     * @param num
     * @return
     */
   public static String getWeek(String sdate, String num) {
     // 再转换为时间
     Date dd = strToDate(sdate);
     Calendar c = Calendar.getInstance();
     c.setTime(dd);
     if (num.equals("1")) // 返回星期一所在的日期
      c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
     else if (num.equals("2")) // 返回星期二所在的日期
      c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
     else if (num.equals("3")) // 返回星期三所在的日期
      c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
     else if (num.equals("4")) // 返回星期四所在的日期
      c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
     else if (num.equals("5")) // 返回星期五所在的日期
      c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
     else if (num.equals("6")) // 返回星期六所在的日期
      c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
     else if (num.equals("0")) // 返回星期日所在的日期
      c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
     return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
   }
    
   /**
     * 根据一个日期，返回是星期几的字符串
     * 
     * @param sdate
     * @return
     */
   public static String getWeek(String sdate) {
     // 再转换为时间
     Date date = strToDate(sdate);
     Calendar c = Calendar.getInstance();
     c.setTime(date);
     // int hour=c.get(Calendar.DAY_OF_WEEK);
     // hour中存的就是星期几了，其范围 1~7
     // 1=星期日 7=星期六，其他类推
     return new SimpleDateFormat("EEEE").format(c.getTime());
   }
   public static String getWeekStr(String sdate){
     String str = "";
     str = getWeek(sdate);
     if("1".equals(str)){
      str = "星期日";
     }else if("2".equals(str)){
      str = "星期一";
     }else if("3".equals(str)){
      str = "星期二";
     }else if("4".equals(str)){
      str = "星期三";
     }else if("5".equals(str)){
      str = "星期四";
     }else if("6".equals(str)){
      str = "星期五";
     }else if("7".equals(str)){
      str = "星期六";
     }
     return str;
   }
    
   /**
     * 两个时间之间的天数
     * 
     * @param date1
     * @param date2
     * @return
     */
   public static long getDays(String date1, String date2) {
     if (date1 == null || date1.equals(""))
      return 0;
     if (date2 == null || date2.equals(""))
      return 0;
     // 转换为标准时间
     SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
     java.util.Date date = null;
     java.util.Date mydate = null;
     try {
      date = myFormatter.parse(date1);
      mydate = myFormatter.parse(date2);
     } catch (Exception e) {
     }
     long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
     return day;
   }
    
   /**
     * 形成如下的日历 ， 根据传入的一个时间返回一个结构 星期日 星期一 星期二 星期三 星期四 星期五 星期六 下面是当月的各个时间
     * 此函数返回该日历第一行星期日所在的日期
     * 
     * @param sdate
     * @return
     */
   public static String getNowMonth(String sdate) {
     // 取该时间所在月的一号
     sdate = sdate.substring(0, 8) + "01";
    
     // 得到这个月的1号是星期几
     Date date = strToDate(sdate);
     Calendar c = Calendar.getInstance();
     c.setTime(date);
     int u = c.get(Calendar.DAY_OF_WEEK);
     String newday = getNextDay(sdate, (1 - u) + "");
     return newday;
   }
    
   /**
     * 取得数据库主键 生成格式为yyyymmddhhmmss+k位随机数
     * 
     * @param k
     *            表示是取几位随机数，可以自己定
     */
    
   public static String getNo(int k) {
    
     return getUserDate("yyyyMMddhhmmss") + getRandom(k);
   }
    
   /**
     * 返回一个随机数
     * 
     * @param i
     * @return
     */
   public static String getRandom(int i) {
     Random jjj = new Random();
     // int suiJiShu = jjj.nextInt(9);
     if (i == 0)
      return "";
     String jj = "";
     for (int k = 0; k < i; k++) {
      jj = jj + jjj.nextInt(9);
     }
     return jj;
   }
   
   public static Calendar getCalendarFromTime(String time){

	     Date date = strToDateLong(time);
	     Calendar c = Calendar.getInstance();
	     c.setTime(date);
	     
	     return c;
   }
   
   public static Calendar getCalendarFromTimeMM(String time){

	     Date date = strToDateLongMM(time);
	     Calendar c = Calendar.getInstance();
	     c.setTime(date);
	     
	     return c;
 }
   
   public static Calendar getCalendarFromDate(String date2){

	     Date date = strToDate(date2);
	     Calendar c = Calendar.getInstance();
	     c.setTime(date);
	     
	     return c;
 }
}
