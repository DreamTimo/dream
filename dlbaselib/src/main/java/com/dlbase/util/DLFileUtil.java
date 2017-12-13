package com.dlbase.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.MimeTypeMap;

/**
 * @author luyz
 * 文件操作统一管理
 */
@SuppressLint("Recycle")
public class DLFileUtil {
	
	private static String SDPATH = DLSDCardUtil.getSDCardPath();// 得到当前外部存储设备的根目录
	
	public DLFileUtil() {
		SDPATH = DLSDCardUtil.getSDCardPath();
	}
	
	/**
	* 判断SD卡上的文件夹是否存在
	*/
	public static boolean isFileExist(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
	/**
	* 在SD卡上创建目录
	* 
	* @param dirName
	*/
	public static File creatSDDir(String filePath) {
		File dir = new File(filePath);
		dir.mkdirs();
		return dir;
	}
	
	/**
	* 在SD卡上创建文件
	* 
	* @throws IOException
	*/
	public static File creatSDFile(String filePath) throws IOException {
		File file = new File(filePath);
		file.createNewFile();
		return file;
	}
	
	/**
	* 在SD卡上删除文件
	* 
	* @param filePath
	* @return
	*/
	public static boolean delFile(String filePath) {
		File file = new File(filePath);
		boolean isExist = file.exists();
		if (isExist) {
			return file.delete();
		}
		return false;
	}
	/**
	 * 删除文件夹所有内容
	 *
	 */
	public static void deleteFile(File file) {

		if (file!=null && file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			//
		}
	}
	
	/**
	* 将一个InputStream里面的数据写入到SD卡中
	*/
	public File write2SDFromInput(String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			int len = 0;
			while ((len = input.read(buffer)) != -1) {
				output.write(buffer,0,len);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	 public static boolean renameFile(File oldFile, File newName) {

		boolean result = false;
		 
	    if (oldFile == null || newName == null) {
	        return result;
        }

        final boolean needScan = oldFile.isFile();
        try {
        	if(needScan){
	            boolean ret = oldFile.renameTo(newName);
	            if (ret) {
	            	result = true;
	            }
        	}
        } catch (SecurityException e) {
            DLLogUtil.e("renameFile", "Fail to rename file," + e.toString());
        }
        return result;
    }
	
	/**
	* 复制文件
	* 
	* @param sourcePath
	* @param targetPath
	* @throws IOException
	*/
	public static void copyFile(String sourcePath, String targetPath) throws IOException {
		File oldFile = new File(sourcePath);
		File targetFile = new File(targetPath);
		if (!oldFile.exists())
			return;
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			inBuff = new BufferedInputStream(new FileInputStream(oldFile));
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			outBuff.flush();
		} catch (Exception e) {
		
		} finally {
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}
	
	/**
	* 保存文件到SD卡
	* 
	* @param data数组
	* @param filename
	* @return
	*/
	public static boolean saveFileToSDCard(byte[] data, String filename) {
		BufferedOutputStream bos = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(SDPATH);
			if (!file.exists()) {
				file.mkdirs();
			}
			try {
				bos = new BufferedOutputStream(new FileOutputStream(new File(filename)));
				bos.write(data, 0, data.length);
				bos.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	* 从SD卡上载入文件
	* 
	* @param 文件路径filepath
	* @return
	*/
	public static byte[] loadFileFromSDCard(String filepath) {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(filepath);
			if (file.exists()) {
				try {
					baos = new ByteArrayOutputStream();
					bis = new BufferedInputStream(new FileInputStream(file));
					byte[] buffer = new byte[1024 * 8];
					int c = 0;
					while ((c = bis.read(buffer)) != -1) {
						baos.write(buffer, 0, c);
						baos.flush();
					}
					return baos.toByteArray();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (bis != null) {
							bis.close();
							baos.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	
	/**
	* 从指定路径读取指定文件
	* 
	* @param 指定路径path
	* @param 指定fileName
	* @return
	*/
	public static String readSDCardFile(String path, String fileName) {
		File file = new File(path, fileName);
		try {
			StringBuffer buffer = new StringBuffer();
			FileInputStream in = new FileInputStream(file);
			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = in.read(buf)) != -1) {
				String strContent = new String(buf, 0, len);
				buffer.append(strContent.toString());
			}
			in.close();
			return buffer.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static Bitmap getBitmapFromSDCard(String filepath) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filepath, options);
		return bitmap;
	}
	
	/**
	* 从SD卡上获取图片到Bitmap
	* 
	* @param filepath
	* @param viewWidth
	* @param viewHeight
	* @return
	*/
	public static Bitmap getBitmapFromSDCard(String filepath, int viewWidth, int viewHeight) {
		Bitmap bitmap = null;
		int decodedWidth = 0;
		int decodedHeight = 0;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);
		decodedWidth = options.outWidth;
		decodedHeight = options.outHeight;
		int heightRate = Math.round((float) decodedHeight / (float) viewHeight);
		int widthRate = Math.round((float) decodedWidth / (float) viewWidth);
		options.inSampleSize = widthRate > heightRate ? heightRate : widthRate;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(filepath, options);
		return bitmap;
	}
	
	public static String getExtensionNameFromName(String filename){
		 if (DLStringUtil.notEmpty(filename)) {    
            int dot = filename.lastIndexOf('.');    
            if ((dot >-1) && (dot < (filename.length() - 1))) {    
                return filename.substring(dot);    
            }    
        }    
        return filename;   
	}
	
	public static String getExtensionNameFromPath(String filepath){
		if(DLStringUtil.notEmpty(filepath)){
			int dot = filepath.lastIndexOf('/');
			if ((dot >-1) && (dot < (filepath.length() - 1))) {    
                String  filename = filepath.substring(dot + 1);   
                return DLFileUtil.getExtensionNameFromName(filename);
            } 
		}
		return filepath;
	}
	
	/**
     * 删除文件
     * 
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名，要在系统内保持唯一
     * @return boolean 存储成功的标志
     */
    public static boolean deleteFile(Context context, String fileName)
    {
        return context.deleteFile(fileName);
    }

    /**
     * 文件是否存在
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static boolean exists(Context context, String fileName)
    {
        return new File(context.getFilesDir(), fileName).exists();
    }

    /**
     * 存储文本数据
     * 
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名，要在系统内保持唯一
     * @param content
     *            文本内容
     * @return boolean 存储成功的标志
     */
    public static boolean writeFile(Context context, String fileName, String content)
    {
        boolean success = false;
        FileOutputStream fos = null;
        try
        {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] byteContent = content.getBytes();
            fos.write(byteContent);

            success = true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null) fos.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }

        return success;
    }

    /**
     * 存储文本数据
     * 
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名，要在系统内保持唯一
     * @param content
     *            文本内容
     * @return boolean 存储成功的标志
     */
    public static boolean writeFile(String filePath, String content)
    {
        boolean success = false;
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(filePath);
            byte[] byteContent = content.getBytes();
            fos.write(byteContent);

            success = true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (fos != null) fos.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }

        return success;
    }

    /**
     * 读取文本数据
     * 
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readFile(Context context, String fileName)
    {
        if (!exists(context, fileName)) { return null; }
        FileInputStream fis = null;
        String content = null;
        try
        {
            fis = context.openFileInput(fileName);
            if (fis != null)
            {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true)
                {
                    int readLength = fis.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                fis.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            content = null;
        }
        finally
        {
            try
            {
                if (fis != null) fis.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 读取文本数据
     * 
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readFile(String filePath)
    {
        if (filePath == null || !new File(filePath).exists()) { return null; }
        FileInputStream fis = null;
        String content = null;
        try
        {
            fis = new FileInputStream(filePath);
            if (fis != null)
            {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true)
                {
                    int readLength = fis.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                fis.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            content = null;
        }
        finally
        {
            try
            {
                if (fis != null) fis.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 读取文本数据
     * 
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readAssets(Context context, String fileName)
    {
        InputStream is = null;
        String content = null;
        try
        {
            is = context.getAssets().open(fileName);
            if (is != null)
            {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true)
                {
                    int readLength = is.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            content = null;
        }
        finally
        {
            try
            {
                if (is != null) is.close();
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        return content;
    }
    
    /**
     * 存储单个Parcelable对象
     * 
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名，要在系统内保持唯一
     * @param parcelObject
     *            对象必须实现Parcelable
     * @return boolean 存储成功的标志
     */
    @SuppressLint("Recycle")
	public static boolean writeParcelable(Context context, String fileName, Parcelable parcelObject)
    {
        boolean success = false;
        FileOutputStream fos = null;
        try
        {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            Parcel parcel = Parcel.obtain();
            parcel.writeParcelable(parcelObject, Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            byte[] data = parcel.marshall();
            fos.write(data);

            success = true;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * 存储List对象
     * 
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名，要在系统内保持唯一
     * @param list
     *            对象数组集合，对象必须实现Parcelable
     * @return boolean 存储成功的标志
     */
    public static boolean writeParcelableList(Context context, String fileName, List<Parcelable> list)
    {
        boolean success = false;
        FileOutputStream fos = null;
        try
        {
            if (list instanceof List)
            {
                fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                Parcel parcel = Parcel.obtain();
                parcel.writeList(list);
                byte[] data = parcel.marshall();
                fos.write(data);

                success = true;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
        }

        return success;
    }

    /**
     * 读取单个数据对象
     * 
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名
     * @return Parcelable, 读取到的Parcelable对象，失败返回null
     */
    public static Parcelable readParcelable(Context context, String fileName, ClassLoader classLoader)
    {
        Parcelable parcelable = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try
        {
            fis = context.openFileInput(fileName);
            if (fis != null)
            {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(b)) != -1)
                {
                    bos.write(b, 0, bytesRead);
                }

                byte[] data = bos.toByteArray();

                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                parcelable = parcel.readParcelable(classLoader);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            parcelable = null;
        }
        finally
        {
            if (fis != null) try
            {
                fis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (bos != null) try
            {
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return parcelable;
    }
    
    /**
     * 读取数据对象列表
     * 
     * @param context
     *            程序上下文
     * @param fileName
     *            文件名
     * @return List, 读取到的对象数组，失败返回null
     */
    @SuppressWarnings("unchecked")
    public static List<Parcelable> readParcelableList(Context context, String fileName, ClassLoader classLoader)
    {
        List<Parcelable> results = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try
        {
            fis = context.openFileInput(fileName);
            if (fis != null)
            {
                bos = new ByteArrayOutputStream();
                byte[] b = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(b)) != -1)
                {
                    bos.write(b, 0, bytesRead);
                }

                byte[] data = bos.toByteArray();

                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(data, 0, data.length);
                parcel.setDataPosition(0);
                results = parcel.readArrayList(classLoader);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            results = null;
        }
        finally
        {
            if (fis != null) try
            {
                fis.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if (bos != null) try
            {
                bos.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return results;
    }

    public static boolean saveSerializable(Context context, String fileName, Serializable data)
    {
        boolean success = false;
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE));
            oos.writeObject(data);
            success = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (oos != null)
            {
                try
                {
                    oos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return success;
    }

    public static Serializable readSerialLizable(Context context, String fileName)
    {
        Serializable data = null;
        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(context.openFileInput(fileName));
            data = (Serializable) ois.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (ois != null)
            {
                try
                {
                    ois.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return data;
    }

    /**
     * 从assets里边读取字符串
     * 
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName)
    {
        try
        {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制文件
     * 
     * @param srcFile
     * @param dstFile
     * @return
     */
    public static boolean copy(String srcFile, String dstFile)
    {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try
        {

            File dst = new File(dstFile);
            if (!dst.getParentFile().exists())
            {
                dst.getParentFile().mkdirs();
            }

            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(dstFile);

            byte[] buffer = new byte[1024];
            int len = 0;

            while ((len = fis.read(buffer)) != -1)
            {
                fos.write(buffer, 0, len);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            if (fis != null)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            if (fos != null)
            {
                try
                {
                    fos.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
        return true;
    }
    
    /**
     * 获取文件后缀名
     * @param file
     * @return
     */
    @SuppressLint("DefaultLocale")
	public static String getExtension( String fileName) {  
        String suffix = "";  
        String name = fileName;  
        final int idx = name.lastIndexOf(".");  
        if (idx > 0) {  
            suffix = name.substring(idx + 1);  
        }  
        return suffix.toLowerCase();  
    } 
    
    /**
     * 获取文件后缀名
     * @param file
     * @return
     */
    @SuppressLint("DefaultLocale")
	public static String getExtension(final File file) {  
        String suffix = "";  
        String name = file.getName();  
        final int idx = name.lastIndexOf(".");  
        if (idx > 0) {  
            suffix = name.substring(idx + 1);  
        }  
        return suffix.toLowerCase();  
    }  
    
    public static String getMimeType(final File file) {  
        String extension = getExtension(file);  
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);  
    } 
    
  //获取文件头信息
    public static String getFileHeader(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[3];  
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if(null != is) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return value;
    }
    @SuppressLint("DefaultLocale")
	private static String bytesToHexString(byte[] src){     
         StringBuilder builder = new StringBuilder();     
         if (src == null || src.length <= 0) {     
             return null;     
         }
         String hv;
         for (int i = 0; i < src.length; i++) {     
             hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();     
             if (hv.length() < 2) {     
                 builder.append(0);     
             }     
             builder.append(hv);
         }
         return builder.toString();     
     }  
}
