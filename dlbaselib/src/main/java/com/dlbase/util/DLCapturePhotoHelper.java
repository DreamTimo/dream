package com.dlbase.util;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dlbase.camera.DLCameraActivity;
import com.dlbase.camera.DLCameraImageActivity;
import com.dlbase.camera.DLCameraModel;
import com.luyz.dlbaselib.R;


/**
 * 拍照辅助类
 * <p/>
 * Created by Clock on luyz.
 */
public class DLCapturePhotoHelper {

    public static final String TIMESTAMP_FORMAT = "yyyy_MM_dd_HH_mm_ss";

    public static final float RATIO = 0.75f;
    
    public final static int CAPTURE_PHOTO_REQUEST_CODE = 0x1111;
    public final static int CAPTURE_VIDEO_REQUEST_CODE = 0x2222;
    public final static int CAPTURE_PHOTODATA_REQUEST_CODE = 0x3333;
    public final static int CAPTURE_VIDEODATA_REQUEST_CODE = 0x4444;
    public final static int CAPTURE_PHOTOANDVIDEODATA_REQUEST_CODE = 0x5555;
    public final static int CAPTURE_CAMERA_REQUEST_CODE = 0x9999;
    
    public static enum TCaptureType{
    	EPhoto_Image,
    	EPhoto_Video,
    	EPhoto_ImageAndVideo
    }
    
    private FragmentActivity mActivity;
    /**
     * 存放图片的目录
     */
    private File mPhotoFolder;
    /**
     * 拍照生成的图片文件
     */
    private File mPhotoFile;
    /**
     * 监听事件
     */
    private DLCapturePhotoListener listener;
    /**
     * 是否启动系统摄像头
     */
    private boolean isDefaultSystem = true;
    
    /**
     * @param activity 存放生成照片的目录，目录不存在时候会自动创建，但不允许为null;
     */
    public DLCapturePhotoHelper(FragmentActivity activity) {
        this.mActivity = activity;
        this.mPhotoFolder = DLFolderManager.getTempFolder();
    }

    /**
     * 拍照
     */
    public void captureImage() {
       
    	if(isDefaultSystem()){
    		
    		 if (hasCamera()) {
	        	createPhotoFile();
	
	            if (mPhotoFile == null) {
	                Toast.makeText(mActivity, R.string.camera_open_error, Toast.LENGTH_SHORT).show();
	                return;
	            }
	
	            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	            Uri fileUri = Uri.fromFile(mPhotoFile);
	            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	            mActivity.startActivityForResult(captureIntent, CAPTURE_PHOTO_REQUEST_CODE);
    		 } else {
	            Toast.makeText(mActivity, R.string.camera_open_error, Toast.LENGTH_SHORT).show();
	         }
    	}else{
    		capture(false,0);
    	}
        
    }
    
    public void capture(boolean isVideo,int videoMaxTime){
    	Intent intent = new Intent(mActivity,DLCameraActivity.class);
    	intent.putExtra(DLCameraActivity.PAGEKEY_VIDEO, isVideo);
    	intent.putExtra(DLCameraActivity.PAGEKEY_MAXVIDEOTIMER, videoMaxTime);
		mActivity.startActivityForResult(intent, CAPTURE_CAMERA_REQUEST_CODE);
    }
    
    /**
     * 拍摄
     */
    public void captureVideo(int videoMaxTime) {
        
    	if(isDefaultSystem()){
    		if (hasCamera()) {
	        	createVideoFile();
	        	 
	            Intent captureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	            captureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, videoMaxTime);
	            
	            Uri fileUri = Uri.fromFile(mPhotoFile);
	            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
	            captureIntent.addCategory("android.intent.category.DEFAULT");
	            
	            mActivity.startActivityForResult(captureIntent, CAPTURE_VIDEO_REQUEST_CODE);
    		} else {
	            Toast.makeText(mActivity, R.string.camera_open_error, Toast.LENGTH_SHORT).show();
	        }
    	}else{
    		capture(true,videoMaxTime);
    	}
       
    }
    
    /**
     * @param type
     * 图片库选择
     */
    public void pickPhoto(TCaptureType type){
    	
        Intent intent = new Intent();
        int tempCode = 0;
        if(type==TCaptureType.EPhoto_Image){
        	intent.setType("image/*");  // 开启Pictures画面Type设定为image
        	tempCode = CAPTURE_PHOTODATA_REQUEST_CODE;
        }else if(type==TCaptureType.EPhoto_Video){
        	intent.setType("video/*");  // 开启Pictures画面Type设定为image
        	tempCode = CAPTURE_VIDEODATA_REQUEST_CODE;
        }else if(type==TCaptureType.EPhoto_ImageAndVideo){
        	intent.setType("video/*;image/*");  // 开启Pictures画面Type设定为image
        	tempCode = CAPTURE_PHOTOANDVIDEODATA_REQUEST_CODE;
        }
        intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action 
        mActivity.startActivityForResult(intent,tempCode); //取得相片后返回到本画面
 	}

    /**
     * 创建视频文件
     */
    @SuppressLint("SimpleDateFormat")
	private void createVideoFile() {
        if (mPhotoFolder != null) {
            if (!mPhotoFolder.exists()) {//检查保存图片的目录存不存在
                mPhotoFolder.mkdirs();
            }

            String fileName = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
            mPhotoFile = new File(mPhotoFolder, fileName + ".mp4");
            if (mPhotoFile.exists()) {
                mPhotoFile.delete();
            }
            try {
                mPhotoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                mPhotoFile = null;
            }
        } else {
            mPhotoFile = null;
            Toast.makeText(mActivity, R.string.not_specify_a_directory, Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * 创建照片文件
     */
    @SuppressLint("SimpleDateFormat")
	private void createPhotoFile() {
        if (mPhotoFolder != null) {
            if (!mPhotoFolder.exists()) {//检查保存图片的目录存不存在
                mPhotoFolder.mkdirs();
            }

            String fileName = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
            mPhotoFile = new File(mPhotoFolder, fileName + ".jpg");
            if (mPhotoFile.exists()) {
                mPhotoFile.delete();
            }
            try {
                mPhotoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                mPhotoFile = null;
            }
        } else {
            mPhotoFile = null;
            Toast.makeText(mActivity, R.string.not_specify_a_directory, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断系统中是否存在可以启动的相机应用
     *
     * @return 存在返回true，不存在返回false
     */
    public boolean hasCamera() {
        PackageManager packageManager = mActivity.getPackageManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 获取当前拍到的图片文件
     *
     * @return
     */
    public File getPhoto() {
        return mPhotoFile;
    }

    /**
     * 设置照片文件
     *
     * @param photoFile
     */
    public void setPhoto(File photoFile) {
        this.mPhotoFile = photoFile;
    }
    
    @SuppressLint("SimpleDateFormat")
	public void onCapturePhotoResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == DLCapturePhotoHelper.CAPTURE_PHOTODATA_REQUEST_CODE){
			 if(resultCode == FragmentActivity.RESULT_OK) {
				 ContentResolver resolver = mActivity.getContentResolver();
                //照片的原始资源地址
                Uri originalUri = data.getData();  
                try {
                	String path = null;
                	if(originalUri.getScheme().equals("file")){
                		path = originalUri.getEncodedPath();
                	}else if(originalUri.getScheme().equals("content")){
                		path = DLImageUtil.getVideoPathFromURI(resolver,originalUri);
                	}
                	
                	int requestWidth = (int) (DLScreenUtil.getScreenWidth(mActivity) * DLCapturePhotoHelper.RATIO);
                	int requestHeight = (int) (DLScreenUtil.getScreenHeight(mActivity) * DLCapturePhotoHelper.RATIO);
                	if(path==null){
                		 //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                       	 	Bitmap bitmap = DLImageUtil.scaleImageTo(photo,requestWidth,requestHeight);
                       	 	if(bitmap!=null){
                       		 	handleImage(bitmap);
                       	 	}
                        }
                	}else{
	                	
	                	Bitmap bitmap = DLImageUtil.compressBitmap(path, requestWidth, requestHeight);//按照屏幕宽高的3/4比例进行缩放显示
	                    if (bitmap != null) {
	                       int degree = DLImageUtil.getBitmapDegree(path);//检查是否有被旋转，并进行纠正
	                       if (degree != 0) {
	                           bitmap = DLImageUtil.rotateBitmapByDegree(bitmap, degree);
	                       }
	                       handleImage(bitmap);
	                    }
                	}
                   
                }catch(Exception e){
               	 
                }
			 }
		}else if(requestCode == DLCapturePhotoHelper.CAPTURE_PHOTO_REQUEST_CODE){
			File photoFile = getPhoto();
			if(resultCode == FragmentActivity.RESULT_OK) {
               if (photoFile != null) {
               		int requestWidth = (int) (DLScreenUtil.getScreenWidth(mActivity) * DLCapturePhotoHelper.RATIO);
                    int requestHeight = (int) (DLScreenUtil.getScreenHeight(mActivity) * DLCapturePhotoHelper.RATIO);
                    Bitmap bitmap = DLImageUtil.compressBitmap(photoFile, requestWidth, requestHeight);//按照屏幕宽高的3/4比例进行缩放显示
                    if (bitmap != null) {
                       int degree = DLImageUtil.getBitmapDegree(photoFile.getAbsolutePath());//检查是否有被旋转，并进行纠正
                       if (degree != 0) {
                           bitmap = DLImageUtil.rotateBitmapByDegree(bitmap, degree);
                       }
                       handleImage(bitmap);
                    }
               }else{
            	   if(data!=null){
            		   Bitmap bm = (Bitmap) data.getExtras().get("data");
            		   String fileName = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());
            		   photoFile = new File(mPhotoFolder, fileName + ".jpg");
            		   
            		   DLImageUtil.saveBitmapFile(photoFile.getAbsolutePath(), bm);
            		   
            		   int requestWidth = (int) (DLScreenUtil.getScreenWidth(mActivity) * DLCapturePhotoHelper.RATIO);
                       int requestHeight = (int) (DLScreenUtil.getScreenHeight(mActivity) * DLCapturePhotoHelper.RATIO);
                       Bitmap bitmap = DLImageUtil.compressBitmap(photoFile, requestWidth, requestHeight);//按照屏幕宽高的3/4比例进行缩放显示
                       if (bitmap != null) {
                          int degree = DLImageUtil.getBitmapDegree(photoFile.getAbsolutePath());//检查是否有被旋转，并进行纠正
                          if (degree != 0) {
                              bitmap = DLImageUtil.rotateBitmapByDegree(bitmap, degree);
                          }
                          handleImage(bitmap);
                       }
            	   }
               }
			 }else{
				 if (photoFile.exists()) {
                     photoFile.delete();
                 }
			 }
		}else if(requestCode == DLCapturePhotoHelper.CAPTURE_VIDEO_REQUEST_CODE){
			File photoFile = getPhoto();
			if(resultCode == FragmentActivity.RESULT_OK) {
                  
               if (photoFile != null) {
            	   int requestWidth = (int) (DLScreenUtil.getScreenWidth(mActivity) * DLCapturePhotoHelper.RATIO);
                   int requestHeight = (int) (DLScreenUtil.getScreenHeight(mActivity) * DLCapturePhotoHelper.RATIO);
                   Bitmap bitmap = DLImageUtil.getVideoThumbnail(photoFile.getAbsolutePath(), requestWidth, requestHeight, MediaStore.Images.Thumbnails.MINI_KIND);
                   
                   if(bitmap!=null){
                	   handleVideo(bitmap,photoFile.getAbsolutePath());
                   }
               }
			 }else{
				 if (photoFile.exists()) {
                     photoFile.delete();
                 }
			 }
		}else if(requestCode == DLCapturePhotoHelper.CAPTURE_VIDEODATA_REQUEST_CODE){
			if(resultCode == FragmentActivity.RESULT_OK) {
                
         	   ContentResolver resolver = mActivity.getContentResolver();
                //照片的原始资源地址
         	   Uri originalUri = data.getData(); 
         	   String path = DLImageUtil.getVideoPathFromURI(resolver,originalUri);  
               Bitmap bitmap = DLImageUtil.getVideoThumbnail(resolver, originalUri);
               
               
                if(bitmap!=null && DLStringUtil.notEmpty(path)){
             	   handleVideo(bitmap,path);
                }
            }
		}else if(requestCode == DLCapturePhotoHelper.CAPTURE_CAMERA_REQUEST_CODE){
			if(resultCode == FragmentActivity.RESULT_OK){
				if(data!=null){
					DLCameraModel tempModel = (DLCameraModel)data.getSerializableExtra(DLCameraImageActivity.PAGEKEY_CAMERAMODEL);
					if(tempModel!=null){
						if(tempModel.isVideo()){
							if(listener!=null){
					        	listener.handleVideo(DLFileUtil.getBitmapFromSDCard(tempModel.getThamnialPath()), tempModel.getFilePath(), tempModel.getThamnialPath());
					        }
						}else{
							if(listener!=null){
					        	listener.handleImage(DLFileUtil.getBitmapFromSDCard(tempModel.getFilePath()), tempModel.getFilePath());
					        }
						}
					}
				}
			}
		}
	}
    
    @SuppressLint("SimpleDateFormat")
	public void handleImage(Bitmap bitmap){
    	String fileName = new SimpleDateFormat(DLCapturePhotoHelper.TIMESTAMP_FORMAT).format(new Date());
        File imagePath = new File(mPhotoFolder, fileName+".jpg");
        DLImageUtil.saveBitmapFile(imagePath.getAbsolutePath(),bitmap);
        if(listener!=null){
        	listener.handleImage(bitmap, imagePath.getAbsolutePath());
        }
    }
    
    @SuppressLint("SimpleDateFormat")
	public void handleVideo(Bitmap bitmap,String path){
    	String fileName = new SimpleDateFormat(DLCapturePhotoHelper.TIMESTAMP_FORMAT).format(new Date());
        File imagePath = new File(mPhotoFolder, fileName+".jpg");
        DLImageUtil.saveBitmapFile(imagePath.getAbsolutePath(),bitmap);
        if(listener!=null){
        	listener.handleVideo(bitmap, path, imagePath.getAbsolutePath());
        }
    }
    
    public DLCapturePhotoListener getListener() {
		return listener;
	}

	public void setListener(DLCapturePhotoListener listener) {
		this.listener = listener;
	}

	public boolean isDefaultSystem() {
		return isDefaultSystem;
	}

	public void setDefaultSystem(boolean isDefaultSystem) {
		this.isDefaultSystem = isDefaultSystem;
	}

	public interface DLCapturePhotoListener{
    	public void handleImage(Bitmap bitmap,String path);
    	public void handleVideo(Bitmap bitmap,String videoPath,String imagePath);
    }
}
