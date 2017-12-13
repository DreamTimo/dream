package com.dlbase.util;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * @author luyz
 *   动态权限管理
 */
public class DLPermissionHelper {

	private final static int RUNTIME_PERMISSION_REQUEST_CODE = 0x1;
	
	private String[] permissionList;
	private Context mContext;
	
	public DLPermissionHelper(){
		
	}
	
	public void requestPermission(Context context,String[] permission){
		permissionList = permission;
		mContext = context;
		
//		if (Build.VERSION.SDK_INT >= 23) { //Android M 处理Runtime Permission
//			
//			for (int i = 0; i < permission.length; i++) {
//				String tempPermission = permission[i];
//			}
//			
//			int hasPermission = ActivityCompat.checkSelfPermission((Activity) mContext,Manifest.permission.WRITE_CONTACTS);
//			
//            if (hasPermission == PackageManager.PERMISSION_GRANTED) {
//            	turnOn();
//            } else {
////                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
////
////                }
//                requestPermission();
//            }
//        } else {
//        	turnOn();
//        }
	}
	
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        
        if (requestCode == RUNTIME_PERMISSION_REQUEST_CODE) {
            for (int index = 0; index < permissions.length; index++) {
                String permission = permissions[index];
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
                    if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    	turnOn();
                    } else {
                        showMissingPermissionDialog();
                    }
                }
            }
        }
    }

    /**
     * 显示打开权限提示的对话框
     */
    private void showMissingPermissionDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle(R.string.help);
//        builder.setMessage(R.string.help_content);
//
//        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(mContext, R.string.camera_open_error, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                turnOnSettings();
//            }
//        });
//
//        builder.show();
    }

    /**
     * 启动系统权限设置界面
     */
    @SuppressLint("InlinedApi")
	private void turnOnSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mContext.getPackageName()));
        mContext.startActivity(intent);
    }
    
    private void turnOn(){
    	
    }
	
    /**
     * 申请权限
     */
    public void requestPermission() {

        ActivityCompat.requestPermissions((Activity) mContext, permissionList, RUNTIME_PERMISSION_REQUEST_CODE);
    }
	
	public interface DLPermissionListener{
		
	}
}
