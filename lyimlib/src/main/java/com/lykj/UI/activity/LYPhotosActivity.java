package com.lykj.UI.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.dlbase.base.DLBaseActivity;
import com.dlbase.util.DLCapturePhotoHelper;
import com.luyz.lyimlib.R;
import com.lykj.Photos.LYAlbumHelper;
import com.lykj.Photos.LYBimp;
import com.lykj.Photos.LYImageBucket;
import com.lykj.Photos.LYPublicWay;
import com.lykj.UI.adapter.LYPhotosAdapter;
import com.lykj.model.LYImageModel;

import java.util.ArrayList;
import java.util.List;

public class LYPhotosActivity extends DLBaseActivity {

    public static final String RESULT_SELETEIMAGE = "selete_image";

    private Button bt_photos_preview;
    private TextView tv_photos_count;
    private Button bt_photos_send;
    private GridView gv_photos_gridView;
    LYPhotosAdapter adapter;

    public static List<LYImageBucket> contentList;
    private ArrayList<LYImageModel> dataList;
    public static Bitmap bitmap;
    private LYAlbumHelper helper;

    private DLCapturePhotoHelper captureHelper;
    private static final int REQUEST_CODE_GALLERY = 0x990;
    public static final int RESULT_CODE_PHOTOS = 0x605;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyphotos);

        LYBimp.tempSelectBitmap.clear();

        LYPublicWay.activityList.add(this);
        //注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_check);

        captureHelper = new DLCapturePhotoHelper((FragmentActivity) mContext);

        initView();
        initListener();
        //这个函数主要用来控制预览和完成按钮的状态
        isShowOkBt();

    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //mContext.unregisterReceiver(this);
            // TODO Auto-generated method stub
            adapter.notifyDataSetChanged();
        }
    };

    public void initView(){

        initNavView("相册", TTopBackType.ETopBack_Black, true,R.id.top_view);
        topRight.setText("系统相册");

        bt_photos_preview = (Button) findViewById(R.id.bt_photos_preview);
        bt_photos_preview.setOnClickListener(this);

        tv_photos_count = (TextView) findViewById(R.id.tv_photos_count);

        helper = LYAlbumHelper.getHelper();
        helper.init(getApplicationContext());

        contentList = helper.getImagesBucketList(false);
        dataList = new ArrayList<LYImageModel>();
        for(int i = 0; i<contentList.size(); i++){
            dataList.addAll( contentList.get(i).imageList );
        }

        gv_photos_gridView = (GridView)findViewById(R.id.gv_photos_gridView);
        adapter = new LYPhotosAdapter(this,dataList,LYBimp.tempSelectBitmap);
        gv_photos_gridView.setAdapter(adapter);

        bt_photos_send = (Button) findViewById(R.id.bt_photos_send);
        bt_photos_send.setText("发送");
        tv_photos_count.setText(""+LYBimp.tempSelectBitmap.size());
    }


    private void initListener() {

        adapter.setOnItemClickListener(new LYPhotosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final ToggleButton toggleButton,
                                    int position, boolean isChecked,Button chooseBt) {
                if (LYBimp.tempSelectBitmap.size() >= LYPublicWay.num) {
                    toggleButton.setChecked(false);
                    chooseBt.setVisibility(View.GONE);
                    if (!removeOneData(dataList.get(position))) {
                        Toast.makeText(LYPhotosActivity.this, "超出可选图片张数",Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (isChecked) {
                    chooseBt.setVisibility(View.VISIBLE);
                    LYBimp.tempSelectBitmap.add(dataList.get(position));
                    bt_photos_send.setText("发送");
                    tv_photos_count.setText(""+LYBimp.tempSelectBitmap.size());
                } else {
                    LYBimp.tempSelectBitmap.remove(dataList.get(position));
                    chooseBt.setVisibility(View.GONE);
                    bt_photos_send.setText("发送");
                    tv_photos_count.setText(""+LYBimp.tempSelectBitmap.size());
                }
                isShowOkBt();
            }
        });

        bt_photos_send.setOnClickListener(new AlbumSendListener());

        captureHelper.setListener(new DLCapturePhotoHelper.DLCapturePhotoListener() {

            @Override
            public void handleVideo(Bitmap bitmap, String videoPath, String imagePath) {
                // TODO Auto-generated method stub

            }

            @Override
            public void handleImage(Bitmap bitmap, String path) {
                // TODO Auto-generated method stub
                LYImageModel tempImage = new LYImageModel();
                tempImage.setImageBitmap(bitmap);
                tempImage.setImagePath(path);
                LYBimp.tempSelectBitmap.add(tempImage);

                send();
            }
        });
    }

    // 完成按钮的监听
    private class AlbumSendListener implements View.OnClickListener {
        public void onClick(View v) {
            overridePendingTransition(R.anim.activity_lytranslate_in, R.anim.activity_lytranslate_out);
            send();
        }
    }

    private boolean removeOneData(LYImageModel imageItem) {
        if (LYBimp.tempSelectBitmap.contains(imageItem)) {
            LYBimp.tempSelectBitmap.remove(imageItem);
            bt_photos_send.setText("发送");
            tv_photos_count.setText(""+LYBimp.tempSelectBitmap.size());
            return true;
        }
        return false;
    }

    public void isShowOkBt() {
        if (LYBimp.tempSelectBitmap.size() > 0) {
            bt_photos_send.setText("发送");
            tv_photos_count.setText(""+LYBimp.tempSelectBitmap.size());
            bt_photos_preview.setPressed(true);
            bt_photos_send.setPressed(true);
            bt_photos_preview.setClickable(true);
            bt_photos_send.setClickable(true);
            bt_photos_send.setTextColor(Color.parseColor("#03A9F4"));
            bt_photos_preview.setTextColor(Color.parseColor("#03A9F4"));
        } else {
            bt_photos_send.setText("发送");
            tv_photos_count.setText(""+LYBimp.tempSelectBitmap.size());
            bt_photos_preview.setPressed(false);
            bt_photos_preview.setClickable(false);
            bt_photos_send.setPressed(false);
            bt_photos_send.setClickable(false);
            bt_photos_send.setTextColor(Color.parseColor("#E1E0DE"));
            bt_photos_preview.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    @Override
    protected void onRestart() {
        isShowOkBt();
        super.onRestart();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.top_left) {
            finish();
        }else if(v.getId() == R.id.top_right){
            captureHelper.pickPhoto(DLCapturePhotoHelper.TCaptureType.EPhoto_Image);
        }else if (v.getId() ==  R.id.bt_photos_preview) {

            if (LYBimp.tempSelectBitmap.size() > 0) {
                Intent intent = new Intent();
                intent.putExtra("position", "1");
                intent.setClass(mContext, LYGalleryActivity.class);
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
        }else if (v.getId() ==  R.id.bt_photos_send) {
            send();
        }
    }

    private void send(){
        setResult(RESULT_CODE_PHOTOS);
        finish();
    }

    /**
     * onActivityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(captureHelper!=null){
            captureHelper.onCapturePhotoResult(requestCode, resultCode, data);
        }

        if(requestCode == REQUEST_CODE_GALLERY){
            if(resultCode == RESULT_OK){
                send();
            }
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }
}
