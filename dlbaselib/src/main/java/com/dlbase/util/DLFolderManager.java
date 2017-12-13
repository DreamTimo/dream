package com.dlbase.util;

import android.os.Environment;

import java.io.File;

import com.dlbase.app.DLBaseApp;

/**
 * 目录管理器
 * <p/>
 * Created by Clock on 2016/5/27.
 */
public class DLFolderManager {

    /**
     * 应用程序在SD卡上的主目录名称
     */
    private final static String APP_FOLDER_NAME = DLSystemUtil.getAppid();
    /**
     * 存放图片目录名
     */
    private final static String PHOTO_FOLDER_NAME = "photo";
    /**
     * 存放闪退日志目录名
     */
    private final static String CRASH_LOG_FOLDER_NAME = "crash";
    /**
     * 存放语音目录名
     */
    private final static String VOICE_FOLDER_NAME = "voice";
    /**
     * 存放视频目录名
     */
    private final static String VIDEO_FOLDER_NAME = "video";
    /**
     * 存放缓存目录名
     */
    private final static String CACHE_FOLDER_NAME = "cache";
    /**
     * 存放临时缓存目录名
     */
    private final static String TEMP_FOLDER_NAME = "temp";
    /**
     * 存放文件目录名
     */
    private final static String FILE_FOLDER_NAME = "file";
    /**
     * 存放http缓存目录
     */
    private final static String HTTPCACHE_FOLDER_NAME = "httpcache";
    
    private DLFolderManager() {

    }

    /**
     * 获取app在应用内的cache目录
     * @return
     */
    public static File getAppCacheFolder(){
    	File cacheDir = new File(DLBaseApp.getApplication().getCacheDir(),APP_FOLDER_NAME);
    	return createOnNotFound(cacheDir);
    }
    
    /**
     * 获取Http cache目录
     * @return
     */
    public static File getHttpCacheFolder(){
    	
    	File appFolder = getAppCacheFolder();
        if (appFolder != null) {

            File photoFolder = new File(appFolder, HTTPCACHE_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取app在sd卡上的主目录
     *
     * @return 成功则返回目录，失败则返回null
     */
    public static File getAppFolder() {
    	
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File appFolder = new File(Environment.getExternalStorageDirectory(), APP_FOLDER_NAME);
            return createOnNotFound(appFolder);
        } else {
            return getAppCacheFolder();
        }
    }

    /**
     * 获取用户文件夹
     * @param userId
     * @return
     */
    public static File getUserIdFolder(String userId){
    	
    	File appFolder = getAppFolder();
        if (appFolder != null) {

            File photoFolder = new File(appFolder, userId);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取用户应用存放图片的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getPhotoFolderForUser(String userId) {
        File appFolder = getUserIdFolder(userId);
        if (appFolder != null) {

            File photoFolder = new File(appFolder, PHOTO_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取用户应用存放音频的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getVoiceFolderForUser(String userId) {
        File appFolder = getUserIdFolder(userId);
        if (appFolder != null) {

            File photoFolder = new File(appFolder, VOICE_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取用户应用存放视频的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getVideoFolderForUser(String userId) {
        File appFolder = getUserIdFolder(userId);
        if (appFolder != null) {

            File photoFolder = new File(appFolder, VIDEO_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取用户应用存放文件的目录 根据userId
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getFileFolderForUser(String userId) {
        File appFolder = getFileFolder();
        if (appFolder != null) {
            File photoFolder = new File(appFolder, userId);
            return createOnNotFound(photoFolder);
        } else {
            return null;
        }
    }
    
    /**
     * 获取用户应用存放缓存的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getCacheFolderForUser(String userId) {
        File appFolder = getUserIdFolder(userId);
        if (appFolder != null) {

            File photoFolder = new File(appFolder, CACHE_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取应用存放图片的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getPhotoFolder() {
        File appFolder = getAppFolder();
        if (appFolder != null) {

            File photoFolder = new File(appFolder, PHOTO_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取应用存放音频的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getVoiceFolder() {
        File appFolder = getAppFolder();
        if (appFolder != null) {

            File photoFolder = new File(appFolder, VOICE_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取应用存放视频的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getVideoFolder() {
        File appFolder = getAppFolder();
        if (appFolder != null) {

            File photoFolder = new File(appFolder, VIDEO_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取应用存放缓存的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getCacheFolder() {
        File appFolder = getAppFolder();
        if (appFolder != null) {

            File photoFolder = new File(appFolder, CACHE_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取应用存临时放缓存的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getTempFolder() {
        File appFolder = getAppFolder();
        if (appFolder != null) {

            File photoFolder = new File(appFolder, TEMP_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }
    
    /**
     * 获取应用存放文件的目录
     *
     * @return 成功则返回目录名，失败则返回null
     */
    public static File getFileFolder() {
        File appFolder = getAppFolder();
        if (appFolder != null) {

            File photoFolder = new File(appFolder, FILE_FOLDER_NAME);
            return createOnNotFound(photoFolder);

        } else {
            return null;
        }
    }

    /**
     * 获取闪退日志存放目录
     *
     * @return
     */
    public static File getCrashLogFolder() {
        File appFolder = getAppFolder();
        if (appFolder != null) {

            File crashLogFolder = new File(appFolder, CRASH_LOG_FOLDER_NAME);
            return createOnNotFound(crashLogFolder);
        } else {
            return null;
        }
    }

    /**
     * 创建目录
     *
     * @param folder
     * @return 创建成功则返回目录，失败则返回null
     */
    public static File createOnNotFound(File folder) {
        if (folder == null) {
            return null;
        }

        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (folder.exists()) {
            return folder;
        } else {
            return null;
        }
    }
}
