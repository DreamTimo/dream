package com.dlbase.video;

import com.dlbase.util.DLStringUtil;
import com.luyz.dlbaselib.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class VideoPlayerActivity extends Activity implements OnClickListener {

	public static final String PAGEKEY_URL = "url";
	public static final String PAGEKEY_NAME = "name";
	public static final String TAG = "VideoPlayerActivity";
	// 视频播放地址
//	 private String videoUrl =
//	 "http://v.iask.com/v_play_ipad.php?vid=138152839";
	private String videoUrl = null;
	private String videoName = null;
	/*************************************************/
	private SurfaceView video_sv;// 绘图容器对象，用于把视频显示在屏幕上
	private ProgressBar video_pb;
	private LinearLayout video_ll_title;
	private Button video_btn_back;
	private TextView video_tv_name;// 视频名称显示的view
	private LinearLayout video_ll_bottom;
	private Button video_btn_play;// 用于开始和暂停的按钮
	private TextView video_tv_otime;
	private SeekBar video_seekbar_time;// 进度条控件
	private TextView video_tv_ctime;
	private Button video_btn_full;
	private Button video_btn_lock;

	private MediaPlayer mediaPlayer; // 播放器控件
	private upDateSeekBar playingSeekBar; // 更新进度条用
	private int postSize; // 保存义播视频大小
	private boolean flag = true; // 用于判断视频是否在播放中

	private static long lastClickTime;
	private int position = 0;

	private boolean isLocked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		/* 去掉title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/* 设置屏幕常亮 *//* flag：标记 ； */
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		videoUrl = (String)getIntent().getSerializableExtra(PAGEKEY_URL);
		videoName  =  (String )getIntent().getSerializableExtra(PAGEKEY_NAME);
		
		setContentView(R.layout.activity_video); // 加载布局文件
		initView(); // 初始化数据

		playVideo();
		setListener(); // 绑定相关事件

	}
	
	@SuppressWarnings("deprecation")
	private void playVideo() {
		video_pb.setVisibility(View.VISIBLE);
		if(DLStringUtil.notEmpty(videoName)){
			video_tv_name.setText(videoName);// 视频标题
		}
		video_btn_play.setEnabled(false); // 刚进来，设置其不可点击
		video_sv.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		video_sv.getHolder().setKeepScreenOn(true); // 保持屏幕高亮
		video_sv.getHolder().addCallback(new surFaceView()); // 设置监听事件
		mHandler.sendMessageDelayed(mHandler.obtainMessage(0x124), 3000);// 隐藏控件
	}

	private void initView() {
		video_sv = (SurfaceView) findViewById(R.id.video_sv);
		video_pb = (ProgressBar) findViewById(R.id.video_pb);

		video_ll_title = (LinearLayout) findViewById(R.id.video_ll_title);
		video_btn_back = (Button) findViewById(R.id.video_btn_back);
		video_btn_back.setOnClickListener(this);
		video_tv_name = (TextView) findViewById(R.id.video_tv_name);

		video_ll_bottom = (LinearLayout) findViewById(R.id.video_ll_bottom);
		video_btn_play = (Button) findViewById(R.id.video_btn_play);
		video_btn_play.setOnClickListener(this);
		video_tv_otime = (TextView) findViewById(R.id.video_tv_otime);
		video_seekbar_time = (SeekBar) findViewById(R.id.video_seekbar_time);
		video_tv_ctime = (TextView) findViewById(R.id.video_tv_ctime);
		video_btn_full = (Button) findViewById(R.id.video_btn_full);
		video_btn_full.setOnClickListener(this);

		video_btn_lock = (Button) findViewById(R.id.video_btn_lock);
		video_btn_lock.setOnClickListener(this);

		mediaPlayer = new MediaPlayer(); // 创建一个播放器对象
		playingSeekBar = new upDateSeekBar(); // 创建更新进度条对象
	}

	@Override
	public void onClick(View v) {
		
		if(v.getId() ==  R.id.video_btn_back){
			finish();
		}else if(v.getId() == R.id.video_btn_lock){
			if (isLocked) {
				unLockScreen();// 解锁
				isLocked = false;
			} else {
				lockScreen();// 锁定
				isLocked = true;
			}
		}
	}

	private void lockScreen() {
		video_btn_lock
				.setBackgroundResource(R.drawable.player_landscape_screen_off_normal);
		video_ll_title.setVisibility(View.GONE);
		video_ll_bottom.setVisibility(View.GONE);
	}

	private void unLockScreen() {
		video_btn_lock
				.setBackgroundResource(R.drawable.player_landscape_screen_on_noraml);
		video_ll_title.setVisibility(View.VISIBLE);
		video_ll_bottom.setVisibility(View.VISIBLE);
	}

	/**
	 * 更新进度条
	 */
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0x124) {
				video_ll_title.setVisibility(View.GONE);
				video_ll_bottom.setVisibility(View.GONE);
			} else {
				if (mediaPlayer == null) {
					flag = false;
				} else if (mediaPlayer.isPlaying()) {
					flag = true;
					int position = mediaPlayer.getCurrentPosition();
					int mMax = mediaPlayer.getDuration();
					int sMax = video_seekbar_time.getMax();
					if (mMax > 0) {
						video_tv_otime.setText(change(position / 1000));
						video_tv_ctime.setText(change(mMax / 1000));
						video_seekbar_time.setProgress(position * sMax / mMax);
					} else {
						Toast.makeText(VideoPlayerActivity.this, "无法播放",
								Toast.LENGTH_LONG).show();
					}
				}
			}
		};
	};

	class upDateSeekBar implements Runnable {

		@Override
		public void run() {
			mHandler.sendMessage(Message.obtain());
			if (flag) {
				mHandler.postDelayed(playingSeekBar, 1000);
			}
		}
	}

	class PlayMovie extends Thread { // 播放视频的线程
		int post = 0;

		public PlayMovie(int post) {
			this.post = post;
		}

		@Override
		public void run() {
			super.run();
			try {
				mediaPlayer.reset(); // 回复播放器默认
				if(DLStringUtil.notEmpty(videoUrl)){
					mediaPlayer.setDataSource(videoUrl); // 设置播放路径
				}
				mediaPlayer.setDisplay(video_sv.getHolder()); // 把视频显示在SurfaceView上
				mediaPlayer.setOnPreparedListener(new PreparedListener(post)); // 设置监听事件
				mediaPlayer.prepare(); // 准备播放
//				mediaPlayer.seekTo(50000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class PreparedListener implements OnPreparedListener {
		int postSize;

		public PreparedListener(int postSize) {
			this.postSize = postSize;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			Log.e("onPrepared", "----onPrepared");
			video_pb.setVisibility(View.GONE); // 准备完成后，隐藏控件
			video_btn_play
					.setBackgroundResource(R.drawable.qiyi_sdk_play_btn_pause);
			if (mediaPlayer != null) {
				mediaPlayer.start(); // 开始播放视频
			} else {
				return;
			}
			if (postSize > 0) { // 说明中途停止过（activity调用过pase方法，不是用户点击停止按钮），跳到停止时候位置开始播放
				mediaPlayer.seekTo(postSize); // 跳到postSize大小位置处进行播放
			}
			new Thread(playingSeekBar).start(); // 启动线程，更新进度条
		}
	}

	private class surFaceView implements Callback { // 上面绑定的监听的事件

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			mediaPlayer.setDisplay(holder);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) { // 创建完成后调用
			if (postSize > 0 && videoUrl != null) { // 说明，停止过activity调用过pase方法，跳到停止位置播放
				new PlayMovie(postSize).start();
				flag = true;
				int sMax = video_seekbar_time.getMax();
				int mMax = mediaPlayer.getDuration();

				video_tv_otime
						.setText(change(mediaPlayer.getCurrentPosition() / 1000));
				video_tv_ctime
						.setText(change(mediaPlayer.getDuration() / 1000));

				video_seekbar_time.setProgress(postSize * sMax / mMax);
				postSize = 0;
				video_pb.setVisibility(View.GONE);
			} else {
				new PlayMovie(0).start();// 表明是第一次开始播放
			}
			mediaPlayer.setDisplay(holder);
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) { // activity调用过pase方法，保存当前播放位置
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				postSize = mediaPlayer.getCurrentPosition();
				mediaPlayer.stop();
				flag = false;
				video_pb.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		video_btn_play.setEnabled(true);
		video_btn_play
				.setBackgroundResource(R.drawable.qiyi_sdk_play_btn_pause);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				position = mediaPlayer.getCurrentPosition();
				mediaPlayer.stop();
				SharedPreferences share = getSharedPreferences("video_player",
						0);
				Editor editor = share.edit();
				editor.putInt("position", position);
				editor.commit();
			}
		}
	}

	@Override
	protected void onDestroy() { // activity销毁后，释放资源
		super.onDestroy();
		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
		}
		System.gc();
	}
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
	
	int mSurfaceViewWidth = 0;
	int mSurfaceViewHeight = 0;
	boolean mIsVideoSizeKnown = false;
	int mVideoHeight = 0;
	int mVideoWidth = 0;
	
	private void setListener() {
		if (mediaPlayer == null) {
			return;
		}
		mediaPlayer
				.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						video_seekbar_time.setSecondaryProgress(percent);
					}
				});

		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // 视频播放完成
					@Override
					public void onCompletion(MediaPlayer mp) {
						flag = false;
						video_tv_otime.setText(change(mediaPlayer.getDuration() / 1000));
						video_btn_play
								.setBackgroundResource(R.drawable.qiyi_sdk_play_btn_player);
						// 播放完成后需要自动播放下一集
						Log.e("mediaPlayer", "本集结束");
						finish();
					}
				});

		mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {

			}
		});
		mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
				Log.e("mediaPlayer", "无法播放：" + arg1 + "：" + arg2);
				return false;
			}
		});
		mediaPlayer.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {
			
			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				// TODO Auto-generated method stub
				if (width == 0 || height == 0) {
		            Log.e(TAG, "invalid video width(" + width + ") or height(" + height
		                    + ")");
		            return;
		        }
		        Log.d(TAG, "onVideoSizeChanged width:" + width + " height:" + height);
		        mIsVideoSizeKnown = true;
		        mVideoHeight = height;
		        mVideoWidth = width;
		        int wid = mediaPlayer.getVideoWidth();
		        int hig = mediaPlayer.getVideoHeight();
		        // 根据视频的属性调整其显示的模式

		        if (wid > hig) {
		            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
		                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		            }
		        } else {
		            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
		                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		            }
		        }
		        DisplayMetrics dm = new DisplayMetrics();
		        getWindowManager().getDefaultDisplay().getMetrics(dm);
		        mSurfaceViewWidth = dm.widthPixels;
		        mSurfaceViewHeight = dm.heightPixels;
		        if (width > height) {
		            // 竖屏录制的视频，调节其上下的空余

		            int w = mSurfaceViewHeight * width / height;
		            int margin = (mSurfaceViewWidth - w) / 2;
		            Log.d(TAG, "margin:" + margin);
		            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
		                    RelativeLayout.LayoutParams.MATCH_PARENT,
		                    RelativeLayout.LayoutParams.MATCH_PARENT);
		            lp.setMargins(margin, 0, margin, 0);
		            video_sv.setLayoutParams(lp);
		        } else {
		            // 横屏录制的视频，调节其左右的空余

		            int h = mSurfaceViewWidth * height / width;
		            int margin = (mSurfaceViewHeight - h) / 2;
		            Log.d(TAG, "margin:" + margin);
		            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
		                    RelativeLayout.LayoutParams.MATCH_PARENT,
		                    RelativeLayout.LayoutParams.MATCH_PARENT);
		            lp.setMargins(0, margin, 0, margin);
		            video_sv.setLayoutParams(lp);
		        }
			}
		});
		/**
		 * 如果视频在播放，则调用mediaPlayer.pause();，停止播放视频，反之，mediaPlayer.start()
		 * ，同时换按钮背景
		 */
		video_btn_play.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mediaPlayer.isPlaying()) {
					video_btn_play
							.setBackgroundResource(R.drawable.qiyi_sdk_play_btn_player);
					mediaPlayer.pause();
					postSize = mediaPlayer.getCurrentPosition();
				} else {
					video_btn_play
							.setBackgroundResource(R.drawable.qiyi_sdk_play_btn_pause);
					mediaPlayer.start();
					if (flag == false) {
						flag = true;
						new Thread(playingSeekBar).start();
					}
				}
			}
		});
		video_seekbar_time
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

						if (isFastDoubleClick()) {
							int value = video_seekbar_time.getProgress()
									* mediaPlayer.getDuration() // 计算进度条需要前进的位置数据大小
									/ video_seekbar_time.getMax();
							video_tv_otime.setText(change(value / 1000));
							mediaPlayer.seekTo(value);
						}
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {

					}
				});
		
		/**
		 * 点击屏幕，切换控件的显示，显示则应藏，隐藏，则显示
		 */
		video_sv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 只有全屏时才显示操作按钮
				// if (!isFullScreen) {
				// return;
				// }
				if (isLocked) {
					video_ll_title.setVisibility(View.GONE);
					video_ll_bottom.setVisibility(View.GONE);
				} else {
					video_ll_title.setVisibility(View.VISIBLE);
					video_ll_bottom.setVisibility(View.VISIBLE);
					mHandler.sendMessageDelayed(mHandler.obtainMessage(0x124),
							3000);// 隐藏控件
				}
			}
		});

	}

	// 防止用户频繁操作
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (3000 < timeD) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// 如果是锁定状态不允许使用系统后退键
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (isLocked) {
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	public static String change(int second) {
		int h = 0;
		int d = 0;
		int s = 0;
		int temp = second % 3600;
		if (second > 3600) {
			h = second / 3600;
			if (temp != 0) {
				if (temp > 60) {
					d = temp / 60;
					if (temp % 60 != 0) {
						s = temp % 60;
					}
				} else {
					s = temp;
				}
			}
		} else {
			d = second / 60;
			if (second % 60 != 0) {
				s = second % 60;
			}
		}

		return h + ":" + d + ":" + s;
	}

}
