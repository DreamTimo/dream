package com.luyz.lyimlib;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.dlbase.Model.DLFileModel;
import com.dlbase.Model.DLImageModel;
import com.dlbase.Model.DLLocationModel;
import com.dlbase.Model.DLVoiceModel;
import com.dlbase.util.DLImageUtil;
import com.dlbase.util.DLShowDialog;
import com.dlbase.util.DLStringUtil;
import com.lykj.Bean.LYChatBean;
import com.lykj.MQTT.LYMQTTHelper;
import com.lykj.UI.activity.LYChatActivity;
import com.lykj.db.LYDBAdapter;
import com.lykj.model.LYChatItemModel;
import com.lykj.model.LYProjectModel;
import com.lykj.model.LYTopisItemModel;
import com.lykj.model.LYUserModel;
import com.lykj.util.LYSharedUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by luyz on 2017/4/19.
 */

public class LyImEngine {

    private String userName;
    private String userPwd;
    private String userId;

    private LYDBAdapter dbHelper;

    private LYUserModel userModel;
    private ArrayList<LYTopisItemModel> topicArray;

    private Context mContext;
    private AlertDialog tempdialog = null;

    private onLYIMListener lyimListener;

    private boolean isVibrator;
    private boolean isAudio;
    private String audioName;

    private LYChatActivity currChatActivity;

    public static LyImEngine instance;
    public static LyImEngine getInstance() {
        if (instance == null) {
            instance = new LyImEngine();
        }
        return instance;
    }

    public void LyImEngine(){

    }

    public void init(Context context,onLYIMListener listener){

        setmContext(context);
        setLyimListener(listener);
        SDKInitializer.initialize(context);
    }

    public void connect(String clientId,LYMQTTHelper.onChatConnectListener listener){
        LYMQTTHelper.getInstance().connect(clientId,listener);
    }

    public void disconnect(){
        LYMQTTHelper.getInstance().disconnect();
    }

    /**
     * 打开数据库  根据登录账户打开不同的数据库文件
     * @param userKey
     */
    public void OpenDB(String userKey){
        setDbHelper(LYDBAdapter.createDBAdapter(mContext, userKey));
    }

    /**
     * 关闭数据库
     */
    public void closeDB(){
        LYDBAdapter.close();
    }

    public void DownImage(String url,ImageView view,int resourceId){
//        view.setImageResource(resourceId);
        Picasso.with(mContext).load(resourceId).into(view);
        if(DLImageUtil.isImageUrl(url)) {
            if (lyimListener != null) {
                lyimListener.onDownImage(url, view);
            }
        }
    }

    public void  loadSDCARDImage(String uri,final ImageView imageView){
        if(DLImageUtil.isImageUrl(uri)) {
            if (lyimListener != null) {
                lyimListener.loadSDCARDImage(uri, imageView);
            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LYDBAdapter getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(LYDBAdapter dbHelper) {
        this.dbHelper = dbHelper;
    }

    public ArrayList<LYTopisItemModel> getTopicArray() {
        return topicArray;
    }

    public void setTopicArray(ArrayList<LYTopisItemModel> topicArray) {

        if(LyImEngine.getInstance().getDbHelper()!=null){
            LyImEngine.getInstance().getDbHelper().getMqttTable().deleteAllMQTT();
        }

        for (int i=0;i<topicArray.size();i++){
            LYTopisItemModel tempItem = topicArray.get(i);
            if(tempItem!=null) {
                if (LyImEngine.getInstance().getDbHelper() != null) {
                    LyImEngine.getInstance().getDbHelper().getMqttTable().checkAndInsertMQTT(tempItem);
                }
            }
        }

        this.topicArray = topicArray;
    }

    public LYUserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(LYUserModel userModel) {
        this.userModel = userModel;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public onLYIMListener getLyimListener() {
        return lyimListener;
    }

    public void setLyimListener(onLYIMListener lyimListener) {
        this.lyimListener = lyimListener;
    }

    public void showWaitDialog(Context context){
        if(tempdialog == null){
            tempdialog = DLShowDialog.getInstance().showProgressDialog(context);
        }
    }

    public void hideWaitDialog(){
        if(tempdialog!=null){
            tempdialog.dismiss();
            tempdialog = null;
        }
    }

    public boolean isVibrator() {
        return isVibrator;
    }

    public void setVibrator(boolean vibrator) {
        isVibrator = vibrator;
    }

    public boolean isAudio() {
        return isAudio;
    }

    public void setAudio(boolean audio) {
        isAudio = audio;
    }

    public String getAudioName() {
        return audioName;
    }

    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    public LYChatActivity getCurrChatActivity() {
        return currChatActivity;
    }

    public void setCurrChatActivity(LYChatActivity currChatActivity) {
        this.currChatActivity = currChatActivity;
    }

    public interface onUploadFileListener{
        public void onFile(String url,String path);
        public void onFail();
    }

    public  interface onDownListener{
        public  void  onDown(Object model, LYChatItemModel.TChatType type);
        public void onProgressChange(long fileSize, long downloadedSize) ;
    }

    public interface onUserInfoListener{
        public void onGetUserInfo(LYUserModel model);
    }

    public interface onShipListListener{
        public void onShipList(ArrayList<LYUserModel> array);
    }

    public interface onAddShipListener{
        public void onSuccess();
    }

    public interface onOffLineMsgListener{
        public void onOffLineMsg(String data);
    }

    public interface onUserContastListener{
        public void onUserContast(ArrayList<LYUserModel> data);
    }

    public interface onUnReadMsgCountListener{
        public void onUnReadMsgCount(String count);
    }
}
