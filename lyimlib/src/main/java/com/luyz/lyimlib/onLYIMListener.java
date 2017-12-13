package com.luyz.lyimlib;

import android.content.Context;
import android.widget.ImageView;

import com.dlbase.Model.DLFileModel;
import com.dlbase.Model.DLImageModel;
import com.dlbase.Model.DLLocationModel;
import com.dlbase.Model.DLVoiceModel;
import com.lykj.model.LYProjectModel;
import com.lykj.model.LYUserModel;

/**
 * Created by luyz on 2017/4/20.
 */

public interface onLYIMListener {

    public void onUploadFile(Context context,String path, LyImEngine.onUploadFileListener listener);
    public void onDownImage(String url, ImageView view);
    public void onDownVoice(DLVoiceModel model,LyImEngine.onDownListener listener);
    public void onDownFile(DLFileModel model,String name,LyImEngine.onDownListener listener);
    public void onToUser(Context content,LYUserModel model);
    public void onToProject(Context content,LYUserModel user,LYProjectModel model);
    public void onGetUserInfo(String userId,LyImEngine.onUserInfoListener listener);
    public void onGetShipList(String userId,LyImEngine.onShipListListener listener);
    public void onUnReadMsgCount(String userId,LyImEngine.onUnReadMsgCountListener listener);
    public void onPostAddShip(String userId,String phone,String targetid,LyImEngine.onAddShipListener listener);
    public void onGetOfflineMessage(String userId,String limit,String max,LyImEngine.onOffLineMsgListener listener);
    public void onGetUserContants(String userId,LyImEngine.onUserContastListener listener);
    public void onLogout();
    public void loadSDCARDImage(String uri,final ImageView imageView);
}
