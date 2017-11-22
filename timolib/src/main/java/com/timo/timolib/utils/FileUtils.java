package com.timo.timolib.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.timo.timolib.base.AppInfo;
import com.timo.timolib.BaseTools;
import com.timo.timolib.MyApplication;
import com.timo.timolib.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

/**
 * 对文件处理的工具类
 * 
 * @author Tony
 *
 */
public class FileUtils {
	private FileUtils() {}
	private static FileUtils instance;
	public static FileUtils getInstance(){
		if (instance==null){
			instance =new FileUtils();
		}
		return instance;
	}

	public static final String ROOT_DIR = "Android/data/"
			+ AppInfo.getPackageName();
	public static final String DOWNLOAD_DIR = "download";
	public static final String CACHE_DIR = "cache";
	public static final String ICON_DIR = "icon";
	/** 获取下载目录 */
	public String getDownloadDir() {
		return getDir(DOWNLOAD_DIR);
	}

	/** 获取缓存目录 */
	public String getCacheDir() {
		return getDir(CACHE_DIR);
	}

	/** 获取icon目录 */
	public String getIconDir() {
		return getDir(ICON_DIR);
	}
	/** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录 */
	public String getDir(String name) {
		StringBuilder sb = new StringBuilder();
		if (isSDCardAvailable()) {
			sb.append(getExternalStoragePath());
		} else {
			sb.append(getCachePath());
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		if (createDirs(path)) {
			return path;
		} else {
			return null;
		}
	}
	public void setFileImage(String url,ImageView view){
		if(BaseTools.isNotEmpty(url)){//设置展示图片信息
			if (isImgUrl(url)) {
				view.setImageResource(R.drawable.file_image);
			} else if (isExcelUrl(url)){
				view.setImageResource(R.drawable.file_excel);
			}else if (isPdfUrl(url)){
				view.setImageResource(R.drawable.file_pdf);
			}else if (isPptUrl(url)){
				view.setImageResource(R.drawable.file_ppt);
			}else if (isTxtUrl(url)){
				view.setImageResource(R.drawable.file_txt);
			}else if (isWordUrl(url)){
				view.setImageResource(R.drawable.file_word);
			}else if (isZipUrl(url)){
				view.setImageResource(R.drawable.file_zip);
			}else if (isRarUrl(url)){
				view.setImageResource(R.drawable.file_zip);
			}else {
				view.setImageResource(R.drawable.file_other);
			}
		}
	}
	public boolean isImgUrl(String url){
		if(BaseTools.isNotEmpty(url)){
			if (url.endsWith("jpg")||url.endsWith("png")||url.endsWith("JPG")||url.endsWith("PNG")){
				return true;
			}
		}
		return false;
	}
	public boolean isExcelUrl(String url){
		if(BaseTools.isNotEmpty(url)){
			if (url.endsWith("xlsx")||url.endsWith("xls")){
				return true;
			}
		}
		return false;
	}
	public boolean isPdfUrl(String url){
		if(BaseTools.isNotEmpty(url)){
			if (url.endsWith("pdf")||url.endsWith("PDF")){
				return true;
			}
		}
		return false;
	}
	public boolean isPptUrl(String url){
		if(BaseTools.isNotEmpty(url)){
			if (url.endsWith("ppt")||url.endsWith("PPT")||url.endsWith("pptx")||url.endsWith("PPTX")){
				return true;
			}
		}
		return false;
	}
	public boolean isTxtUrl(String url){
		if(BaseTools.isNotEmpty(url)){
			if (url.endsWith("txt")||url.endsWith("TXT")){
				return true;
			}
		}
		return false;
	}
	public boolean isWordUrl(String url){
		if(BaseTools.isNotEmpty(url)){
			if (url.endsWith("doc")||url.endsWith("docx")||url.endsWith("DOC")||url.endsWith("DOCX")){
				return true;
			}
		}
		return false;
	}
	public boolean isZipUrl(String url){
		if(BaseTools.isNotEmpty(url)){
			if (url.endsWith("zip")||url.endsWith("ZIP")){
				return true;
			}
		}
		return false;
	}
	public boolean isRarUrl(String url){
		if(BaseTools.isNotEmpty(url)){
			if (url.endsWith("rar")||url.endsWith("RAR")){
				return true;
			}
		}
		return false;
	}
	/** 获取SD下的应用目录 */
	public String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取应用的cache目录 */
	public String getCachePath() {
		File f = MyApplication.getInstance().getContext().getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}

	/** 判断SD卡是否挂载 */
	public boolean isSDCardAvailable() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return true;
		} else {
			return false;
		}
	}
	/** 创建文件夹 */
	public boolean createDirs(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		}
		return true;
	}

	/**
	 * 判断文件是否存在
	 * @return
	 */
	public boolean fileIsExists(Context context,String fileName){
		try{
            File download = context.getExternalFilesDir("Download");
            File f=new File(download.getAbsolutePath()+"/"+fileName);
			if(!f.exists()){
				return false;
			}
		}catch (Exception e) {
			BaseTools.printErrorMessage(e);
			return false;
		}
		return true;
	}
	/** 复制文件，可以选择是否删除源文件 */
	public boolean copyFile(String srcPath, String destPath,
								   boolean deleteSrc) {
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		return copyFile(srcFile, destFile, deleteSrc);
	}

	/** 复制文件，可以选择是否删除源文件 */
	public boolean copyFile(File srcFile, File destFile,
								   boolean deleteSrc) {
		if (!srcFile.exists() || !srcFile.isFile()) {
			return false;
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = in.read(buffer)) > 0) {
				out.write(buffer, 0, i);
				out.flush();
			}
			if (deleteSrc) {
				srcFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			BaseTools.closeIo(out);
			BaseTools.closeIo(in);
		}
		return true;
	}
	//java 获取可变UUID
	public String getMyUUID(){
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		return uniqueId;
	}
	/** 判断文件是否可写 */
	public boolean isWriteable(String path) {
		try {
			if (TextUtils.isEmpty(path)) {
				return false;
			}
			File f = new File(path);
			return f.exists() && f.canWrite();
		} catch (Exception e) {
			BaseTools.printErrorMessage(e);
			return false;
		}
	}

	/** 修改文件的权限,例如"777"等 */
	public void chmod(String path, String mode) {
		try {
			String command = "chmod " + mode + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (Exception e) {
			BaseTools.printErrorMessage(e);
		}
	}

	/**
	 * 把数据写入文件
	 * @param is
	 *            数据流
	 * @param path
	 *            文件路径
	 * @param recreate
	 *            如果文件存在，是否需要删除重建
	 * @return 是否写入成功
	 */
	public boolean writeFile(InputStream is, String path,boolean recreate) {
		boolean res = false;
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			if (recreate && f.exists()) {
				f.delete();
			}
			if (!f.exists() && null != is) {
				File parentFile = new File(f.getParent());
				parentFile.mkdirs();
				int count = -1;
				byte[] buffer = new byte[1024];
				fos = new FileOutputStream(f);
				while ((count = is.read(buffer)) != -1) {
					fos.write(buffer, 0, count);
				}
				res = true;
			}
		} catch (Exception e) {
			BaseTools.printErrorMessage(e);
		} finally {
			BaseTools.closeIo(fos);
			BaseTools.closeIo(is);
		}
		return res;
	}

	/**
	 * 把字符串数据写入文件
	 *
	 * @param content
	 *            需要写入的字符串
	 * @param path
	 *            文件路径名称
	 * @param append
	 *            是否以添加的模式写入
	 * @return 是否写入成功
	 */
	public boolean writeFile(byte[] content, String path, boolean append) {
		boolean res = false;
		File f = new File(path);
		RandomAccessFile raf = null;
		try {
			if (f.exists()) {
				if (!append) {
					f.delete();
					f.createNewFile();
				}
			} else {
				f.createNewFile();
			}
			if (f.canWrite()) {
				raf = new RandomAccessFile(f, "rw");
				raf.seek(raf.length());
				raf.write(content);
				res = true;
			}
		} catch (Exception e) {
			BaseTools.printErrorMessage(e);
		} finally {
			BaseTools.closeIo(raf);
		}
		return res;
	}

	/**
	 * 把字符串数据写入文件
	 *
	 * @param content
	 *            需要写入的字符串
	 * @param path
	 *            文件路径名称
	 * @param append
	 *            是否以添加的模式写入
	 * @return 是否写入成功
	 */
	public boolean writeFile(String content, String path, boolean append) {
		return writeFile(content.getBytes(), path, append);
	}

	/**
	 * 把键值对写入文件
	 *
	 * @param filePath
	 *            文件路径
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param comment
	 *            该键值对的注释
	 */
	public void writeProperties(String filePath, String key, String value, String comment) {
		if (TextUtils.isEmpty(key) || TextUtils.isEmpty(filePath)) {
			return;
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);// 先读取文件，再把键值对追加到后面
			p.setProperty(key, value);
			fos = new FileOutputStream(f);
			p.store(fos, comment);
		} catch (Exception e) {
			BaseTools.printErrorMessage(e);
		} finally {
			BaseTools.closeIo(fis);
			BaseTools.closeIo(fos);
		}
	}

	/** 根据值读取 */
	public String readProperties(String filePath, String key,
										String defaultValue) {
		if (TextUtils.isEmpty(key) || TextUtils.isEmpty(filePath)) {
			return null;
		}
		String value = null;
		FileInputStream fis = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);
			value = p.getProperty(key, defaultValue);
		} catch (IOException e) {
			BaseTools.printErrorMessage(e);
		} finally {
			BaseTools.closeIo(fis);
		}
		return value;
	}

	/** 把字符串键值对的map写入文件 */
	public void writeMap(String filePath, Map<String, String> map,
								boolean append, String comment) {
		if (map == null || map.size() == 0 || TextUtils.isEmpty(filePath)) {
			return;
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			Properties p = new Properties();
			if (append) {
				fis = new FileInputStream(f);
				p.load(fis);// 先读取文件，再把键值对追加到后面
			}
			p.putAll(map);
			fos = new FileOutputStream(f);
			p.store(fos, comment);
		} catch (Exception e) {
			BaseTools.printErrorMessage(e);
		} finally {
			BaseTools.closeIo(fis);
			BaseTools.closeIo(fos);
		}
	}

	/** 把字符串键值对的文件读入map */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> readMap(String filePath,
											  String defaultValue) {
		if (TextUtils.isEmpty(filePath)) {
			return null;
		}
		Map<String, String> map = null;
		FileInputStream fis = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);
			map = new HashMap<String, String>((Map) p);// 因为properties继承了map，所以直接通过p来构造一个map
		} catch (Exception e) {
			BaseTools.printErrorMessage(e);
		} finally {
			BaseTools.closeIo(fis);
		}
		return map;
	}

	/** 改名 */
	public boolean copy(String src, String des, boolean delete) {
		File file = new File(src);
		if (!file.exists()) {
			return false;
		}
		File desFile = new File(des);
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = new FileOutputStream(desFile);
			byte[] buffer = new byte[1024];
			int count = -1;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
				out.flush();
			}
		} catch (Exception e) {
			BaseTools.printErrorMessage(e);
			return false;
		} finally {
			BaseTools.closeIo(in);
			BaseTools.closeIo(out);
		}
		if (delete) {
			file.delete();
		}
		return true;
	}
	/**
	 * 获取文件
	 */
	public File getFile(Context context,String fileName){
		File f=null;
		try{
			File download = context.getExternalFilesDir("Download");
			f=new File(download.getAbsolutePath()+"/"+fileName);
			if(!f.exists()){
				return null;
			}
		}catch (Exception e) {
			BaseTools.printErrorMessage(e);
			return null;
		}
		return f;
	}
     /**
 	 * 空间大小单位格式化
 	 * @param size
 	 * @return
 	 */
 	public String formatSize(long size) {
         String suffix = null;
         float fSize=0;

         if (size >= 1024) {
             suffix = "KB";
             fSize=size / 1024;
             if (fSize >= 1024) {
                 suffix = "MB";
                 fSize /= 1024;
             }
             if (fSize >= 1024) {
                 suffix = "GB";
                 fSize /= 1024;
             }
         } else {
             fSize = size;
         }
         
         java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
         StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
         if (suffix != null)
             resultBuffer.append(suffix);
         return resultBuffer.toString();
     }
    
	/**
	 * 检查SD卡是否存在
	 * 
	 * @return
	 */
	public boolean checkSDCard() {
		final String status = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(status)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取安装在用户手机上的com.youyuan.yyapp下的files目录
	 * 
	 * @return files path
	 */
	public String getAppFilesDir(Context context) {
		return context.getFilesDir().getAbsolutePath();
	}
	
	/**
	 * 获取安装在用户手机上的com.youyuan.yyapp下的cache目录
	 * 
	 * @return cache path
	 */
	public String getAppCacheDir(Context context) {
		return context.getCacheDir().getPath();
	}

	   /* @param srcPath : 源文件全路径
	     * @param destPath : 目标文件全路径
	     * @return
	     */
	    public long copyFile(String srcPath, String destPath) {
	        try {
	            int position = destPath.lastIndexOf(File.separator);
	            String dir = destPath.substring(0, position);
	            String newFileName = destPath.substring(position+1);
	            final File cacheDir = new File(dir);
	            if (!cacheDir.exists()) {
	                cacheDir.mkdirs();
	            }
	            return copyFile(new File(srcPath), new File(dir), newFileName);
	        } catch (Exception e) {
				BaseTools.printErrorMessage(e);
	            return 0;
	        }

	    }
	    /**
	     * 复制文件(以超快的速度复制文件)
	     * 
	     * @param srcFile
	     *            源文件File
	     * @param destDir
	     *            目标目录File
	     * @param newFileName
	     *            新文件名
	     * @return 实际复制的字节数，如果文件、目录不存在、文件为null或者发生IO异常，返回-1
	     */
	    @SuppressWarnings("resource")
	    public long copyFile(final File srcFile, final File destDir, String newFileName) {
	        long copySizes = 0;
	        if (!srcFile.exists()) {	          
	            copySizes = -1;
	        } else if (!destDir.exists()) {	           
	            copySizes = -1;
	        } else if (newFileName == null) {	           
	            copySizes = -1;
	        } else {
	            FileChannel fcin = null;
	            FileChannel fcout = null;
	            try {
	                fcin = new FileInputStream(srcFile).getChannel();
	                fcout = new FileOutputStream(new File(destDir, newFileName)).getChannel();
	                long size = fcin.size();
	                fcin.transferTo(0, fcin.size(), fcout);
	                copySizes = size;
	            } catch (FileNotFoundException e) {
					BaseTools.printErrorMessage(e);
	            } catch (IOException e) {
					BaseTools.printErrorMessage(e);
	            } finally {
	                try {
	                    if (fcin != null) {
	                        fcin.close();
	                    }
	                    if (fcout != null) {
	                        fcout.close();
	                    }
	                } catch (IOException e) {
						BaseTools.printErrorMessage(e);
	                }
	            }
	        }
	        return copySizes;
	    }
}
