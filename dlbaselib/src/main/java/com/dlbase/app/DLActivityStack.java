package com.dlbase.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.dlbase.base.DLBaseActivity;

public class DLActivityStack {

	private ConcurrentHashMap<String, DLBaseActivity> activityMap;
    private List<DLBaseActivity> activityList;
    private int index = 0;
    
	public DLActivityStack(){
		activityMap = new ConcurrentHashMap<String, DLBaseActivity>();
        activityList = new ArrayList<DLBaseActivity>();
	}
	
	public void addActivity(DLBaseActivity mActivity) {
        activityMap.put(mActivity.getClass().getSimpleName() + index, mActivity);
        activityList.add(mActivity);
        index++;
    }

    public void removeActivity(DLBaseActivity mActivity) {
        activityMap.remove(mActivity.getClass().getSimpleName());
        activityList.remove(mActivity);
    }

    public void finishAllActivity() {
        
    	List<DLBaseActivity> activitys = new ArrayList<DLBaseActivity>(activityMap.values());
        for (DLBaseActivity activity : activitys) 
        {
            if (activity != null) {
            	activity.finish();	
            }
        }
        
    	activityList.clear();
        activityMap.clear();
    }
    
    public DLBaseActivity getLastActivity(){
    	
    	DLBaseActivity tempActivity = null;
    	
    	if(activityList.size()>0){
    		tempActivity = activityList.get(activityList.size()-1);
    	}
    	
    	return tempActivity;
    }
	
	public ConcurrentHashMap<String, DLBaseActivity> getActivityMap() {
		return activityMap;
	}

	public void setActivityMap(ConcurrentHashMap<String, DLBaseActivity> activityMap) {
		this.activityMap = activityMap;
	}

	public List<DLBaseActivity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<DLBaseActivity> activityList) {
		this.activityList = activityList;
	}
}
