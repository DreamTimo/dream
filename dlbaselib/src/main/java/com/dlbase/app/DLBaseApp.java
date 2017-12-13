package com.dlbase.app;

import android.app.Application;
import android.content.Context;

import com.dlbase.util.DLCrashHandler;
import com.dlbase.util.DLLogUtil;

/**
 * Created by luyz
 */
public class DLBaseApp extends Application {
	
    private static final String LOG_TAG = "MyApplication";
    
   	private Context ctx;
    private static DLBaseApp myApp;
   
    private DLActivityStack activityStack;
    
    private boolean isActive = true;

    @Override
    public void onCreate() {
        super.onCreate();
        
        DLLogUtil.i(LOG_TAG, "Application init");
        
        ctx = getApplicationContext();
        
        myApp = this;
        
//        Netroid.init(ctx, true, null);
        
        setActivityStack(new DLActivityStack());
        
//        //全局异常处理
//  		final DLCrashHandler customException = DLCrashHandler.getInstance();
//  		customException.init(getApplicationContext());
  		
    }

    public Context getAppContext() {
        return ctx;
    }

    public static DLBaseApp getApplication() {
        return myApp;
    }
    
    public static Context getContext() {
		return getApplication().getBaseContext();
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public DLActivityStack getActivityStack() {
		return activityStack;
	}

	public void setActivityStack(DLActivityStack activityStack) {
		this.activityStack = activityStack;
	}
	
}
