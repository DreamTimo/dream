package com.dlbase.base;

import java.util.ArrayList;
import java.util.List;

import com.dlbase.app.DLBaseApp;
import com.luyz.dlbaselib.R;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class DLBaseActivity extends FragmentActivity implements OnClickListener {

	protected Context mContext;

	public Context getmContext() {
		if (mContext==null){
			return getApplicationContext();
		}
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	protected View 		topView;
	protected TextView 	topTitle;
	protected TextView  topLeft;
	protected TextView  topRight;
	protected LinearLayout ll_left;
	protected LinearLayout ll_right;
	public static int REQUEST_CODE = 0x0200;
	public static int RESULT_FINISHCURRPAGE_CODE = 0x0300;
	public static int RESULT_FINISHTOUPPAGE_CODE = 0x0301;

	public enum TTopBackType{
		ETopBack_Black,ETopBack_White,ETopBack_Transparent
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		mContext = this;
	}

	//崩溃后重新打开程序，fragment 重叠问题(这个方法更好，但是在这不适用)
	/*@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		//super.onSaveInstanceState(outState);
	}*/

	public void handleTopViewToIcon(TextView view,int resourId){
		Drawable nav_up=getResources().getDrawable(resourId);  
		nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());  
		view.setCompoundDrawables(nav_up, null, null, null);  
	}

	public void handeTopViewToText(TextView view,String text){
		view.setCompoundDrawables(null, null, null, null);
		view.setText(text);
	}
	
	public void changeNavType(TTopBackType type){
		switch (type) {
		case ETopBack_Black:
			handleTopViewToIcon(topLeft,R.drawable.back);
			topView.setBackgroundResource(R.drawable.topview_bg);
			topTitle.setTextColor(Color.parseColor("#ffffff"));
			break;
		case ETopBack_White:
			handleTopViewToIcon(topLeft,R.drawable.ic_back);
			topView.setBackgroundColor(Color.parseColor("#ffffff"));
			topTitle.setTextColor(Color.parseColor("#222328"));
			break;
		case ETopBack_Transparent:
			handleTopViewToIcon(topLeft,R.drawable.back);
			topView.setBackgroundResource(R.color.transparent);
			topTitle.setTextColor(Color.parseColor("#ffffff"));
			break;
		default:
			break;
		}
	}
	
	protected void initNavView(String title,TTopBackType type,Boolean showRight,int resourceid){
		
		topView = findViewById(resourceid);
		topTitle = (TextView) topView.findViewById(R.id.top_title);
		topTitle.setText(title);
		topLeft = (TextView) topView.findViewById(R.id.tv_left);
		topLeft.setVisibility(View.VISIBLE);
		topRight = (TextView) topView.findViewById(R.id.tv_right);
		ll_left = (LinearLayout) topView.findViewById(R.id.top_left);
		ll_right = (LinearLayout) topView.findViewById(R.id.top_right);
		if(showRight){
			ll_right.setVisibility(View.VISIBLE);
		}else{
			ll_right.setVisibility(View.GONE);
		}
		
		changeNavType(type);
		
		ll_left.setOnClickListener(this);
		ll_right.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

//		DLBaseApp.getApplication().getActivityStack().removeActivity(this);

		super.onDestroy();

//		RequestManager.cancelAll(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_CODE){
			if(resultCode == RESULT_FINISHCURRPAGE_CODE){
				finish();
			}else if(resultCode == RESULT_FINISHTOUPPAGE_CODE){
				setResult(resultCode);
				finish();
			}
		}
	}

	public void toLogin(Class<?> cls){

		Intent intent = new Intent(mContext,cls);
		startActivity(intent);

		List<DLBaseActivity> activitys = new ArrayList<DLBaseActivity>(DLBaseApp.getApplication().getActivityStack().getActivityMap().values());
        for (DLBaseActivity activity : activitys)
        {
            if (activity != null) {
            	if(!activity.getClass().equals(cls)){
            		activity.finish();
            	}
            }
        }
	}

	@Override
	public void startActivityForResult(Intent intent,int requestCode){

		super.startActivityForResult(intent, requestCode);

	}
	@Override
	public void finish(){
		super.finish();
	}

	public void startActivityForResult(Class<?> cls,int requestCode){
		startActivityForResult(new Intent(mContext, cls), requestCode);
	}

	public void showTips() {

		AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提醒")
				.setMessage("是否退出程序")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(Intent.ACTION_MAIN);
			            intent.addCategory(Intent.CATEGORY_HOME);
			            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			            startActivity(intent);
				    android.os.Process.killProcess(android.os.Process.myPid());
					}

				}).setNegativeButton("取消",

				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).create(); // 创建对话框
		alertDialog.show(); // 显示对话框
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyDown(keyCode, event);
	}

	 @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	 @Override
     protected void onStop() {
             // TODO Auto-generated method stub
         super.onStop();

         if (!isAppOnForeground()) {
             //app 进入后台

             //全局变量isActive = false 记录当前已经进入后台

        	 DLBaseApp.getApplication().setActive(false);
         }
     }

     @Override
     protected void onResume() {
             // TODO Auto-generated method stub
         super.onResume();

         if (!DLBaseApp.getApplication().isActive()) {
             //app 从后台唤醒，进入前台
        	 DLBaseApp.getApplication().setActive(true);
         }
     }

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
//		DLBaseApp.getApplication().getActivityStack().addActivity(this);
		super.onStart();
	}

	/**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
            // Returns a list of application processes that are running on the
            // device

            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            String packageName = getApplicationContext().getPackageName();

            List<RunningAppProcessInfo> appProcesses = activityManager
                            .getRunningAppProcesses();
            if (appProcesses == null)
                    return false;

            for (RunningAppProcessInfo appProcess : appProcesses) {
                    // The name of the process that this object is associated with.
                    if (appProcess.processName.equals(packageName)
                                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            return true;
                    }
            }

            return false;
    }
}
