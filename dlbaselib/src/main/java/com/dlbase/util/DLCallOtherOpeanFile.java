package com.dlbase.util;

import java.io.File;

import com.dlbase.util.DLWpsModel.ClassName;
import com.dlbase.util.DLWpsModel.OpenMode;
import com.dlbase.util.DLWpsModel.PackageName;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;  
import android.content.Context;
import android.content.Intent;  
import android.net.Uri;  
import android.os.Bundle;
import android.widget.Toast;  

public class DLCallOtherOpeanFile {
	
	/** 
	 * 调用第三方打开文件
	 * 检查系统应用程序，并打开 
	 */  
	  public boolean openWPSFile(String path,Context context) {
	        Intent intent = new Intent();  
	        Bundle bundle = new Bundle();  
	        bundle.putString(DLWpsModel.OPEN_MODE, OpenMode.NORMAL); // 打开模式  
	        bundle.putBoolean(DLWpsModel.SEND_CLOSE_BROAD, true); // 关闭时是否发送广播  
	        bundle.putString(DLWpsModel.THIRD_PACKAGE, context.getPackageName()); // 第三方应用的包名，用于对改应用合法性的验证  
	        bundle.putBoolean(DLWpsModel.CLEAR_TRACE, true);// 清除打开记录  
	        // bundle.putBoolean(CLEAR_FILE, true); //关闭后删除打开文件  
	        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	        intent.setAction(android.content.Intent.ACTION_VIEW);  
	        intent.setClassName(PackageName.NORMAL, ClassName.NORMAL);  
	  
	        File file = new File(path);  
	        if (file == null || !file.exists()) {  
	            System.out.println("文件为空或者不存在");  
	            return false;  
	        }  
	  
	        Uri uri = Uri.fromFile(file);  
	        intent.setData(uri);  
	        intent.putExtras(bundle);  
	        try {  
	        	context.startActivity(intent);  
	        } catch (ActivityNotFoundException e) {  
	            System.out.println("打开wps异常："+e.toString());  
	            e.printStackTrace();  
	            return false;  
	        }  
	        return true;  
	    } 
	
	public static void openFile(File file,Context context){   
        try{  
        Intent intent = new Intent();   
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        //设置intent的Action属性   
        intent.setAction(Intent.ACTION_VIEW);   
        //获取文件file的MIME类型   
        String type = getMIMEType(file);   
        //设置intent的data和Type属性。   
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);   
        //跳转   
        context.startActivity(intent);     
  //      Intent.createChooser(intent, "请选择对应的软件打开该附件！");  
        }catch (ActivityNotFoundException e) {  
            // TODO: handle exception  
            Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();  
        }  
    }   
	@SuppressLint("DefaultLocale")
	private static String getMIMEType(File file) {   
           
        String type="*/*";   
        String fName = file.getName();   
        //获取后缀名前的分隔符"."在fName中的位置。   
        int dotIndex = fName.lastIndexOf(".");   
        if(dotIndex < 0){   
            return type;   
        }   
        /* 获取文件的后缀名*/   
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();   
        if(end=="")return type;   
        //在MIME和文件类型的匹配表中找到对应的MIME类型。   
        for(int i=0;i<MIME_MapTable.length;i++){   
        	  if(end.equals(MIME_MapTable[i][0]))   
                  type = MIME_MapTable[i][1];   
          }          
          return type;   
      }   
   //可以自己随意添加  
   private static String [][]  MIME_MapTable={   
       //{后缀名，MIME类型}   
	   {".txt",    "text/plain"},   
	   {".ppt",    "application/vnd.ms-powerpoint"},   
	   {".doc",    "application/msword"},   
	   {".pdf",    "application/pdf"},   
	   {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},   
	   {".xls",    "application/vnd.ms-excel"},    
	   {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},   
	   {".mp4",    "video/mp4"},   
	   {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},   
       {".3gp",    "video/3gpp"},   
       {".apk",    "application/vnd.android.package-archive"},   
       {".asf",    "video/x-ms-asf"},   
       {".avi",    "video/x-msvideo"},   
       {".bin",    "application/octet-stream"},   
       {".bmp",    "image/bmp"},   
       {".c",  "text/plain"},   
       {".class",  "application/octet-stream"},   
       {".conf",   "text/plain"},   
       {".cpp",    "text/plain"},   
       {".exe",    "application/octet-stream"},   
       {".gif",    "image/gif"},   
       {".gtar",   "application/x-gtar"},   
       {".gz", "application/x-gzip"},   
       {".h",  "text/plain"},   
       {".htm",    "text/html"},   
       {".html",   "text/html"},   
       {".jar",    "application/java-archive"},   
       {".java",   "text/plain"},   
       {".jpeg",   "image/jpeg"},   
       {".jpg",    "image/jpeg"},   
       {".js", "application/x-javascript"},   
       {".log",    "text/plain"},   
       {".m3u",    "audio/x-mpegurl"},   
       {".m4a",    "audio/mp4a-latm"},   
       {".m4b",    "audio/mp4a-latm"},   
       {".m4p",    "audio/mp4a-latm"},   
       {".m4u",    "video/vnd.mpegurl"},   
       {".m4v",    "video/x-m4v"},    
       {".mov",    "video/quicktime"},   
       {".mp2",    "audio/x-mpeg"},   
       {".mp3",    "audio/x-mpeg"},   
       {".mpc",    "application/vnd.mpohun.certificate"},          
       {".mpe",    "video/mpeg"},     
       {".mpeg",   "video/mpeg"},     
       {".mpg",    "video/mpeg"},     
       {".mpg4",   "video/mp4"},      
       {".mpga",   "audio/mpeg"},   
       {".msg",    "application/vnd.ms-outlook"},   
       {".ogg",    "audio/ogg"},   
       {".png",    "image/png"},   
       {".pps",    "application/vnd.ms-powerpoint"},   
       {".prop",   "text/plain"},   
       {".rc", "text/plain"},   
       {".rmvb",   "audio/x-pn-realaudio"},   
       {".rtf",    "application/rtf"},   
       {".sh", "text/plain"},   
       {".tar",    "application/x-tar"},      
       {".tgz",    "application/x-compressed"},    
       {".wav",    "audio/x-wav"},   
       {".wma",    "audio/x-ms-wma"},   
       {".wmv",    "audio/x-ms-wmv"},   
       {".wps",    "application/vnd.ms-works"},   
       {".xml",    "text/plain"},   
       {".z",  "application/x-compress"},   
       {".zip",    "application/x-zip-compressed"},   
       {"",        "*/*"}     
    };   
}