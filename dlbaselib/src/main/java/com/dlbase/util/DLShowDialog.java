package com.dlbase.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.text.Html;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dlbase.app.DLBaseApp;
import com.dlbase.dateutil.DLBirthDateDialog;
import com.dlbase.dateutil.DLBirthDateSource;
import com.dlbase.pickutil.DLPickerDataSource;
import com.dlbase.pickutil.DLPickerView;
import com.dlbase.view.Dialog_ShowChatAdapter;
import com.luyz.dlbaselib.R;

import java.util.ArrayList;

/**
 * Created by luyz on 2017/4/19.
 */

public class DLShowDialog {

    public static DLShowDialog instance;
    public static DLShowDialog getInstance() {
        if (instance == null) {
            instance = new DLShowDialog();
        }
        return instance;
    }
    //分享到微信好友、朋友圈
    public  void showShareDialog(Context context, final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getApplication().getAppContext())); //设置宽度
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.dialog_share);
            TextView tv_wechat=(TextView) window.findViewById(R.id.tv_wechat);
            TextView tv_favorite=(TextView) window.findViewById(R.id.tv_favorite);
            tv_wechat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listene.onSubmitClick("wechat");
                }
            });
            tv_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listene.onSubmitClick("favorite");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //进度条显示
    public  AlertDialog showProgressDialog(Context context){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.setCanceledOnTouchOutside(false);
            ad.setCancelable(false);
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())/3); //设置宽度
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.MyLoadDialog); // 添加动画
            window.setContentView(R.layout.fragment_loading);
            @SuppressWarnings("unused")
            ProgressBar loading_bar = (ProgressBar) window.findViewById(R.id.loading_bar);
            TextView loading_text = (TextView) window.findViewById(R.id.loading_text);
            loading_text.setText("正在加载···");
            return ad;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //发照片-->从相册或拍照
    public  void showPhotoDialog(Context context,final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())); //设置宽度
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.dialog_photo);
            Button bt_takePhoto=(Button) window.findViewById(R.id.bt_takePhoto);
            Button bt_pickPhoto=(Button) window.findViewById(R.id.bt_pickPhoto);
            Button bt_cancle=(Button) window.findViewById(R.id.bt_cancle);
            bt_takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listene.onSubmitClick("takePhoto");
                }
            });
            bt_pickPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listene.onSubmitClick("pickPhoto");
                }
            });
            bt_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //聊天长按转发,删除等
    public  void showChatDialog(Context context, ArrayList<String> data, final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())*2/3); //设置宽度
            lp.height = (int)(data.size()*DLDensityUtil.dp2px(context, 60));
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.MyLoadDialog); // 添加动画
            window.setContentView(R.layout.dialog_showchat);

            ListView listview = (ListView) window.findViewById(R.id.lvstView);
            Dialog_ShowChatAdapter adapter = new Dialog_ShowChatAdapter(context, data);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    // TODO Auto-generated method stub
                    if(listene!=null){
                        listene.onSubmitClick(arg2);
                    }
                    ad.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //两项选择提示
    public  void showClewDialog(Context context,String firstValue,String secondValue,final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())); //设置宽度
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.dialog_photo);
            Button bt_takePhoto=(Button) window.findViewById(R.id.bt_takePhoto);
            Button bt_pickPhoto=(Button) window.findViewById(R.id.bt_pickPhoto);
            Button bt_cancle=(Button) window.findViewById(R.id.bt_cancle);
            if(DLStringUtil.notEmpty(firstValue)){
                bt_takePhoto.setText(firstValue);
            }else{
                bt_takePhoto.setVisibility(View.GONE);
            }
            if(DLStringUtil.notEmpty(secondValue)){
                bt_pickPhoto.setText(secondValue);
            }else{
                bt_pickPhoto.setVisibility(View.GONE);
            }

            bt_takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listene.onSubmitClick("first");
                }
            });
            bt_pickPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listene.onSubmitClick("second");
                }
            });
            bt_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //上传视频-->拍摄或从图库中选择
    public  void showVideoDialog(Context context,final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())); //设置宽度
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.dialog_photo);
            Button bt_takePhoto=(Button) window.findViewById(R.id.bt_takePhoto);
            Button bt_pickPhoto=(Button) window.findViewById(R.id.bt_pickPhoto);
            Button bt_cancle=(Button) window.findViewById(R.id.bt_cancle);

            bt_takePhoto.setText("拍摄");
            bt_pickPhoto.setText("从图库中选择");

            bt_takePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listene.onSubmitClick("takeVideo");
                }
            });
            bt_pickPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listene.onSubmitClick("pickVideo");
                }
            });
            bt_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //性别选择
    public  void showSexDialog(Context context,final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())); //设置宽度
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.dialog_sex);
            Button bt_man=(Button) window.findViewById(R.id.bt_man);
            Button bt_woman=(Button) window.findViewById(R.id.bt_woman);
            Button bt_cancle=(Button) window.findViewById(R.id.bt_cancle);
            bt_man.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listene.onSubmitClick("man");
                }
            });
            bt_woman.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    listene.onSubmitClick("woman");
                }
            });
            bt_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //电话
    public  void showTel(final Context context){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())); //设置宽度
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.dialog_tel);
            Button bt_submit=(Button) window.findViewById(R.id.bt_submit);
            Button bt_cancle=(Button) window.findViewById(R.id.bt_cancle);
            bt_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                    Intent in2 = new Intent();
                    in2.setAction(Intent.ACTION_CALL);
                    in2.setData(Uri.parse("tel:"+"025-96596"));
                    context.startActivity(in2);
                }
            });
            bt_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**时间选择
     * @param context
     * @param listene
     */
    public  void showTimeDialog(Context context, DLBirthDateSource timeSource, final DialogListener listene){

        int width = DLScreenUtil.getScreenWidth(context);
        int height = DLScreenUtil.getScreenHeight(context);

        DLBirthDateDialog birthDiolog = new DLBirthDateDialog(context,
                new DLBirthDateDialog.PriorityListener() {
                    @Override
                    public void refreshPriorityUI(String seleteValue) {
                        listene.onSubmitClick(seleteValue);
                    }
                },
                timeSource,
                width,
                height);
        Window window = birthDiolog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialogstyle); // 添加动画
        birthDiolog.setCancelable(true);
        birthDiolog.show();
    }

    /**时间选择
     * @param context
     * @param time  yyyy-MM-dd HH:mm:ss
     * @param listene
     */
    public  void showDateDialog(Context context,String time,final DialogListener listene){

        int width = DLScreenUtil.getScreenWidth(context);
        int height = DLScreenUtil.getScreenHeight(context);
        String curDate = null;
        if(DLStringUtil.isEmpty(time)){
            curDate = DLDateUtils.getCurrentTimeInString();
        }else{
            curDate = time.toString();
        }

        DLBirthDateSource dataSoure = new DLBirthDateSource();
        dataSoure.setDefaultValue(curDate);

        DLBirthDateDialog birthDiolog = new DLBirthDateDialog(context,
                new DLBirthDateDialog.PriorityListener() {
                    @Override
                    public void refreshPriorityUI(String seleteValue) {
                        listene.onSubmitClick(seleteValue);
                    }
                },
                dataSoure,
                width,
                height);
        Window window = birthDiolog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.dialogstyle); // 添加动画
        birthDiolog.setCancelable(true);
        birthDiolog.show();
    }
    //联动选择
    public  void showPickerDialog(Context context, DLPickerDataSource data, final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())); //设置宽度
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.item_picker);
            final DLPickerView temppicker = (DLPickerView) window.findViewById(R.id.citypicker);
            Button btn_sure=(Button) window.findViewById(R.id.btn_sure);
            Button btn_cancel=(Button) window.findViewById(R.id.btn_cancel);

            temppicker.setPickData(data);

            btn_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();

                    listene.onSubmitClick(temppicker.getPickData());
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //认证确认
    public  void showIdentificationDialog(Context context,final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())*2/3); //设置宽度
            window.setAttributes(lp);
            window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
            //window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.dialog_identification);
            Button btn_no = (Button) window.findViewById(R.id.btn_no);
            Button btn_sure =  (Button) window.findViewById(R.id.btn_sure);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    ad.dismiss();
                }
            });
            btn_sure.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    listene.onSubmitClick(" ");
                    ad.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //删除好友
    public  void showRemoveDialog(Context context,final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            ad.show();
            Window window = ad.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())); //设置宽度
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.dialog_removefriend);
            Button btn_rf_more = (Button) window.findViewById(R.id.btn_rf_more);
            Button btn_rf_complain = (Button) window.findViewById(R.id.btn_rf_complain);
            Button btn_rf_remove = (Button) window.findViewById(R.id.btn_rf_remove);
            Button btn_rf_cancel = (Button) window.findViewById(R.id.btn_rf_cancel);
            //更多
            btn_rf_more.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    listene.onSubmitClick("");
                    ad.dismiss();
                }
            });
            //投诉
            btn_rf_complain.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    listene.onSubmitClick("");
                    ad.dismiss();
                }
            });
            //删除好友
            btn_rf_remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    listene.onSubmitClick("");
                    ad.dismiss();
                }
            });
            //取消
            btn_rf_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    ad.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //询问提示框
    public  void showAlertDialog(Context context,String title,String content,final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();
            //点击外部区域不能取消dialog
            ad.setCanceledOnTouchOutside(false);
            ad.setOnKeyListener(keylistener);
            ad.show();

            Window window = ad.getWindow();

            window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.dialog_alert);

            TextView tv_alert_title = (TextView) window.findViewById(R.id.tv_alert_title);
            TextView tv_alert_conment = (TextView) window.findViewById(R.id.tv_alert_content);
            if(DLStringUtil.notEmpty(title)){
                tv_alert_title.setText(title);
                tv_alert_title.setVisibility(View.VISIBLE);
            }else{
                tv_alert_title.setVisibility(View.GONE);
            }
            if(DLStringUtil.notEmpty(content)){
                tv_alert_conment.setText(content);
            }
            Button btn_no = (Button) window.findViewById(R.id.btn_no);
            Button btn_sure =  (Button) window.findViewById(R.id.btn_sure);
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    listene.onSubmitClick("yes");

                    ad.dismiss();
                }
            });
            btn_sure.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    listene.onCancleClick("no");
                    ad.dismiss();
                }
            });

            TextPaint pFont = tv_alert_conment.getPaint();
            Rect rect = new Rect();
            pFont.getTextBounds(content, 0, content.length(), rect);

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())*2/3); //设置宽度
            lp.height = DLDensityUtil.dp2px(context, 120)+rect.height();
            window.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //询问提示框
    public  void showAlertOneButtonDialog(Context context,String title,String content,String btnStr,final DialogListener listene){
        try {
            final AlertDialog ad=new AlertDialog.Builder(context).create();

            //点击外部区域不能取消dialog
            ad.setCanceledOnTouchOutside(false);
            ad.setOnKeyListener(keylistener);
            ad.show();

            Window window = ad.getWindow();

            window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
            window.setWindowAnimations(R.style.dialogstyle); // 添加动画
            window.setContentView(R.layout.dialog_alert);

            TextView tv_alert_title = (TextView) window.findViewById(R.id.tv_alert_title);
            TextView tv_alert_conment = (TextView) window.findViewById(R.id.tv_alert_content);
            if(DLStringUtil.notEmpty(title)){
                tv_alert_title.setText(title);
                tv_alert_title.setVisibility(View.VISIBLE);
            }else{
                tv_alert_title.setVisibility(View.GONE);
            }
            if(DLStringUtil.notEmpty(content)){
                tv_alert_conment.setText(content);
            }
            View btn_line = (View)window.findViewById(R.id.btn_line);
            Button btn_no = (Button) window.findViewById(R.id.btn_no);
            Button btn_sure =  (Button) window.findViewById(R.id.btn_sure);
            btn_line.setVisibility(View.GONE);
            btn_sure.setVisibility(View.GONE);

            if(DLStringUtil.notEmpty(btnStr)){
                btn_no.setText(btnStr);
            }else{
                btn_no.setText("确定");
            }

            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    listene.onSubmitClick("yes");
                    ad.dismiss();
                }
            });

            TextPaint pFont = tv_alert_conment.getPaint();
            Rect rect = new Rect();
            pFont.getTextBounds(content, 0, content.length(), rect);

            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = (int)(DLScreenUtil.getScreenWidth(DLBaseApp.getContext())*2/3); //设置宽度
            lp.height = DLDensityUtil.dp2px(context, 120)+rect.height();
            window.setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showTel(String tel,final Context context){
        final AlertDialog dialog=new AlertDialog.Builder(context).create();
        //点击外部区域不能取消dialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(keylistener);
        dialog.show();
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_tel1);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
        Button bt_confirm = (Button) window.findViewById(R.id.bt_confirm);
        Button bt_cancel = (Button) window.findViewById(R.id.bt_cancel);
        tv_title.setText(tel);
        bt_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                Intent in2 = new Intent();
                in2.setAction(Intent.ACTION_CALL);
                in2.setData(Uri.parse("tel:"+"tel"));
                context.startActivity(in2);
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();

            }
        });

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    //普通确定、取消
    public void showDialog(String content,Context context,final DialogListener listene){
        final AlertDialog dialog=new AlertDialog.Builder(context).create();
        //点击外部区域不能取消dialog
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(keylistener);
        dialog.show();

        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_pay);
        TextView tv_title = (TextView) window.findViewById(R.id.tv_title);
        Button bt_confirm = (Button) window.findViewById(R.id.bt_confirm);
        Button bt_cancel = (Button) window.findViewById(R.id.bt_cancel);
        tv_title.setText(Html.fromHtml(content));
        bt_confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                listene.onSubmitClick(null);
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                listene.onCancleClick(null);
            }
        });

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }

    public static DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode== KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    } ;
    public interface DialogListener {
        void onSubmitClick(Object obj);
        void onCancleClick(Object obj);
    }
    public interface DialogPrivilegeListener {
        void onPrivilegeClick(int privilege);
    }
}
