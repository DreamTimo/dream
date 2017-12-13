package com.dlbase.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

public class DLImageUtil {
	
	private DLImageUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	public static Bitmap getImageByFilePath(String filePath, int scale) {
        Bitmap res = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        return res;
    }

    public static Bitmap getImageByFilePath(String filePath, int Towidth, int ToHeight) {
        Bitmap res = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (!new File(filePath).exists())
            return res;
        BitmapFactory.decodeFile(filePath, options);

        int origionalWidth = options.outHeight;
        int origionalHeight = options.outWidth;
        options.inJustDecodeBounds = false;
        int scale = Math.max(origionalWidth / Towidth, origionalHeight / ToHeight);
        options.inSampleSize = scale;
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        try {
            res = BitmapFactory.decodeFile(filePath, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }
    
    /**
     * convert Bitmap to byte array
     * 
     * @param b
     * @return
     */
    public static byte[] bitmapToByte(Bitmap b) {
        if (b == null) {
            return null;
        }

        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return o.toByteArray();
    }

    /**
     * convert byte array to Bitmap
     * 
     * @param b
     * @return
     */
    public static Bitmap byteToBitmap(byte[] b) {
        return (b == null || b.length == 0) ? null : BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    /**
     * convert Drawable to Bitmap
     * 
     * @param d
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable d) {
        return d == null ? null : ((BitmapDrawable)d).getBitmap();
    }

    /**
     * convert Bitmap to Drawable
     * 
     * @param b
     * @return
     */
    @SuppressWarnings("deprecation")
	public static Drawable bitmapToDrawable(Bitmap b) {
        return b == null ? null : new BitmapDrawable(b);
    }

    /**
     * convert Drawable to byte array
     * 
     * @param d
     * @return
     */
    public static byte[] drawableToByte(Drawable d) {
        return bitmapToByte(drawableToBitmap(d));
    }

    /**
     * convert byte array to Drawable
     * 
     * @param b
     * @return
     */
    public static Drawable byteToDrawable(byte[] b) {
        return bitmapToDrawable(byteToBitmap(b));
    }

    /**
     * scale image
     * 
     * @param org
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
        return scaleImage(org, (float) newWidth / org.getWidth(), (float) newHeight / org.getHeight());
    }

    /**
     * scale image
     * 
     * @param org
     * @param scaleWidth sacle of width
     * @param scaleHeight scale of height
     * @return
     */
    public static Bitmap scaleImage(Bitmap org, float scaleWidth, float scaleHeight) {
        if (org == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(), matrix, true);
    }
    
    public static int getBitmapDegree(String path) {
        short degree = 0;

        try {
            ExifInterface e = new ExifInterface(path);
            int orientation = e.getAttributeInt("Orientation", 1);
            switch(orientation) {
            case 3:
                degree = 180;
                break;
            case 6:
                degree = 90;
                break;
            case 8:
                degree = 270;
            }
        } catch (IOException var4) {
            //var4.printStackTrace();
        }

        return degree;
    }
    
    public static Bitmap rotateBitmapByDegree(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float)degree);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if(bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

        return newBitmap;
    }
    
    /**
     * caculate the bitmap sampleSize
     * @param path
     * @return
     */
    public final static int caculateInSampleSize(BitmapFactory.Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0) return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height/ (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
    
    public final static Bitmap compressBitmap(File path, int rqsW, int rqsH) {
    	return path!=null?compressBitmap(path.getAbsolutePath(), rqsW, rqsH):null;
    }
    
    /**
     * 压缩指定路径的图片，并得到图片对象
     * @param context
     * @param path bitmap source path
     * @return Bitmap {@link android.graphics.Bitmap}
     */
    public final static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
    
    /**
     * 获取真实路径
     * @param cr
     * @param uri
     * @return
     */
    public static String getVideoPathFromURI(ContentResolver cr,Uri uri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = cr.query(uri,proj, null, null, null);     
        
        if (cursor == null || cursor.getCount() == 0) {    
            return null;    
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    /** 
     * 视频截图
     * @param context 
     * @param cr
     * @param Videopath
     * @return 
     */ 
    public static Bitmap getVideoThumbnail(ContentResolver cr, Uri uri) {    
        Bitmap bitmap = null;    
        BitmapFactory.Options options = new BitmapFactory.Options();    
        options.inDither = false;    
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;    
        Cursor cursor = cr.query(uri,new String[] { MediaStore.Video.Media._ID }, null, null, null);     
        
        if (cursor == null || cursor.getCount() == 0) {    
            return null;    
        }    
        cursor.moveToFirst();    
        String videoId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));  //image id in image table.s    
    
        if (videoId == null) {    
        	return null;    
        }    
        cursor.close();    
        long videoIdLong = Long.parseLong(videoId);    
        bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, videoIdLong,Images.Thumbnails.MICRO_KIND, options);    
    
        return bitmap;    
     }    
    
    /** 
     * 获取视频的缩略图 
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。 
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。 
     * @param videoPath 视频的路径 
     * @param width 指定输出视频缩略图的宽度 
     * @param height 指定输出视频缩略图的高度度 
     * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。 
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96 
     * @return 指定大小的视频缩略图 
     */  
    public static Bitmap getVideoThumbnail(String videoPath, int width, int height,  int kind) {  
	     Bitmap bitmap = null;  
	     // 获取视频的缩略图  
	     bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
	     System.out.println("w"+bitmap.getWidth());  
	     System.out.println("h"+bitmap.getHeight());  
	     bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
	       ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
	     return bitmap;  
    }  
    
    /** 
     * 根据指定的图像路径和大小来获取缩略图 
     * 此方法有两点好处： 
     *     1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度， 
     *        第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 
     *     2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 
     *        用这个工具生成的图像不会被拉伸。 
     * @param imagePath 图像的路径 
     * @param width 指定输出图像的宽度 
     * @param height 指定输出图像的高度 
     * @return 生成的缩略图 
     */  
    public static Bitmap getImageThumbnail(String imagePath, int width, int height) {  
	     Bitmap bitmap = null;  
	     BitmapFactory.Options options = new BitmapFactory.Options();  
	     options.inJustDecodeBounds = true;  
	     // 获取这个图片的宽和高，注意此处的bitmap为null  
	     bitmap = BitmapFactory.decodeFile(imagePath, options);  
	     options.inJustDecodeBounds = false; // 设为 false  
	     // 计算缩放比  
	     int h = options.outHeight;  
	     int w = options.outWidth;  
	     int beWidth = w / width;  
	     int beHeight = h / height;  
	     int be = 1;  
	     if (beWidth < beHeight) {  
	    	 be = beWidth;  
	     } else {  
	    	 be = beHeight;  
	     }  
	     if (be <= 0) {  
	    	 be = 1;  
	     }  
	     options.inSampleSize = be;  
	     // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false  
	     bitmap = BitmapFactory.decodeFile(imagePath, options);  
	     // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象  
	     bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
	     return bitmap;  
    }  
    
    
    /**
     * 保存bitmap到file
     * @param path 完整路径 jpg
     * @param bitmap
     */
    public static void saveBitmapFile(String path,Bitmap bitmap){
        File file=new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static byte[] toBytes(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();

        return buffer.toByteArray();
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
   	
  	public static Bitmap getMaskImage(Bitmap bitmap_bg,Bitmap bitmap_in,int width,int height)
	{
		Bitmap roundConcerImage = Bitmap.createBitmap(width,height, Config.ARGB_8888);
		Canvas canvas = new Canvas(roundConcerImage);
		Paint paint = new Paint();
		Rect rect = new Rect(0,0,width,height);
		Rect rectF = new Rect(0, 0, bitmap_in.getWidth(), bitmap_in.getHeight());
		paint.setAntiAlias(true);
		NinePatch patch = new NinePatch(bitmap_bg, bitmap_bg.getNinePatchChunk(), null);
		patch.draw(canvas, rect);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap_in, rectF, rect, paint);
		return roundConcerImage;
	}
  	
  	public static boolean isImageUrl(String url){
    	boolean result = false;
    	
    	if(DLStringUtil.isURL(url)){
    		String tempEx = getExecitFormUrl(url);
    		if(DLStringUtil.notEmpty(tempEx)){
    			if(tempEx.equals("jpg") || tempEx.equals("png") || tempEx.equals("jpeg")){
    				result = true;
    			}
    		}
    	}
    	
    	return result ;
    }
    
    //处理Url的后缀
  	@SuppressLint("DefaultLocale")
	public static String getExecitFormUrl(String url){
  		String ex = null;
  		
  		int pos = url.lastIndexOf(".");
  		if(pos != -1){
  			ex = url.substring(pos+1);
  			if(DLStringUtil.notEmpty(ex)){
  				pos = ex.indexOf("!");
  				if(pos!=-1){
  					ex = ex.substring(0, pos);
  				}
  			}
  		}
  		return ex.toLowerCase();
  	}
}
