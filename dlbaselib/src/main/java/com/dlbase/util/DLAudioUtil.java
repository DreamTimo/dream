package com.dlbase.util;

import com.dlbase.app.DLBaseApp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;

public class DLAudioUtil {

	@SuppressWarnings("deprecation")
	public static MediaRecorder recordStart(String path) {
        try {
        	
        	MediaRecorder recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置MediaRecorder的音频源为麦克风  
            recorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);  
            // 设置MediaRecorder录制的音频格式  
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  
            // 设置MediaRecorder录制音频的编码为amr
            recorder.setOutputFile(path);  
            // 设置录制好的音频文件保存路径  
            recorder.prepare();
            recorder.start();
            
            return recorder;
        } catch (Exception ex) {  
            ex.printStackTrace();
        }
        return null;
	}
	
	public static void recordStop(MediaRecorder recorder) {
		try {
			if(recorder == null) {
				return;
			}
            recorder.stop();
            recorder.release();
            recorder = null;
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } 
	}
	
	public static void playSpeaker(Context context, boolean receiver){
		try {
			AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			audioManager.setSpeakerphoneOn(receiver);
		} catch (Exception ex) {  
            ex.printStackTrace();  
        } 
	}
	
	public static void playFromReceiver(Context context, boolean receiver) {
		try {
			AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if(!receiver) {
				//听筒模式
				audioManager.setMode(AudioManager.MODE_IN_CALL);
			} else {
				//正常模式
				audioManager.setMode(AudioManager.MODE_NORMAL);
			}
			audioManager.setSpeakerphoneOn(receiver);
		} catch (Exception ex) {  
            ex.printStackTrace();  
        } 
	}
	
	private static MediaPlayer player;
	public static void play(String path, final Handler handler) {
        try {
        	if(player == null) {
        		player = new MediaPlayer();
        	} else {
        		player.stop();
        		player = new MediaPlayer();
        	}
        	player.setDataSource(path);
        	player.prepare();
        	player.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					if(handler!=null){
						Message msg = handler.obtainMessage();
						msg.what = 600;
						handler.sendMessage(msg);
					}
				}
			});
        	player.start();
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } 
	}
	
	public static void stop(){
		if(player!=null){
			player.stop();
			player.reset();
			player = null;
		}
	}
	
	public static void playAudio(String name){
		try {
        	
        	MediaPlayer tempplayer = new MediaPlayer();
        	
        	AssetFileDescriptor fileDescriptor = DLBaseApp.getContext().getAssets().openFd(name);
        	tempplayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
        	tempplayer.prepare();
        	tempplayer.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					
				}
			});
        	tempplayer.start();
        	
        } catch (Exception ex) {  
            ex.printStackTrace();  
        } 
	}
}
