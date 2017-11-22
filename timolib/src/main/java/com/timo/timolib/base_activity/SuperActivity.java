package com.timo.timolib.base_activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;

import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.timo.timolib.BaseConfig;
import com.timo.timolib.R;
import com.timo.timolib.base.BaseConstancts;
import com.timo.timolib.BaseTools;
import com.timo.timolib.MyApplication;
import com.timo.timolib.Params;
import com.timo.timolib.camera.CameraActivity;
import com.timo.timolib.view.TitleBar;
import com.timo.timolib.view.search.SearchFragment;
import com.timo.timolib.view.search.custom.OnSearchClickListener;
import com.timo.timolib.utils.PermissionUtils;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;

/**
 * activity基类的参数、方法
 */

public abstract class SuperActivity extends FragmentActivity {
    private Params getParams;
    private Params setParams;
    private TitleBar baseTitleBar;
    public int cameraRequestCode = 9745;//开启相机的请求Code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
        ButterKnife.bind(this);
        setTitle();
        initEvent();
    }

    protected abstract int getContentResId();

    protected void setTitle() {
        if (findViewById(R.id.title) != null && BaseTools.isNotEmpty(setTitleName())) {
            baseTitleBar = (TitleBar) findViewById(R.id.title);
            BaseTools.setTitleBar(baseTitleBar, setTitleName());
        }
    }

    protected abstract String setTitleName();

    protected abstract void initEvent();

    public void startActivityNoFinish(Class<?> cls) {
        if (setParams == null) {
            startActivity(new Intent(MyApplication.getInstance().getContext(), cls));
        } else {
            startActivity(new Intent(MyApplication.getInstance().getContext(), cls).putExtra(BaseConstancts.BASE_PARAM, setParams));
        }
    }

    public void startActivityAddFinish(Class<?> cls) {
        if (setParams == null) {
            startActivity(new Intent(this, cls));
            finish();
        } else {
            startActivity(new Intent(this, cls).putExtra(BaseConstancts.BASE_PARAM, setParams));
            finish();
        }
    }

    public void setMyResult(Serializable object) {
        setResult(RESULT_OK, new Intent().putExtra(BaseConstancts.BASE_PARAM, object));
    }

    public void startActivityForMyResult(Class<?> cls, int code) {
        if (setParams == null) {
            startActivityForResult(new Intent(MyApplication.getInstance().getContext(), cls), code);
        } else {
            startActivityForResult(new Intent(MyApplication.getInstance().getContext(), cls).putExtra(BaseConstancts.BASE_PARAM, setParams), code);
        }
    }

    public Params getParams() {
        if (getIntent().getSerializableExtra(BaseConstancts.BASE_PARAM) != null) {
            getParams = (Params) getIntent().getSerializableExtra(BaseConstancts.BASE_PARAM);
        } else {
            getParams = new Params();
        }
        return getParams;
    }

    public Params setParams() {
        if (setParams == null) {
            setParams = new Params();
        }
        return setParams;
    }


    public Params getMyParamResult(Intent data) {
        if (data.getSerializableExtra(BaseConstancts.BASE_PARAM) != null) {
            return (Params) data.getSerializableExtra(BaseConstancts.BASE_PARAM);
        }
        return null;
    }

    public Serializable getMyResult(Intent data) {
        return data.getSerializableExtra(BaseConstancts.BASE_PARAM);
    }

    /**
     * 拨打电话
     */
    private String super_phone;

    public void callPhone(String phone) {
        this.super_phone = phone;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.getInstance().applyCallPhonePermission(this);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isMain()) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (BaseConfig.exit_to_back) {
                    moveTaskToBack(false);
                    return true;
                } else {
                    if ((System.currentTimeMillis() - mExitTime) > BaseConfig.exit_time) {
                        BaseTools.showToast("再按一次，退出程序");
                        mExitTime = System.currentTimeMillis();
                    } else {
                        stopAndFinish();
                    }
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 停止程序
     */
    protected void stopAndFinish() {
        finish();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }

    /**
     * 判断是否是首页
     */
    protected boolean isMain() {
        return false;
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                startActivityForResult(new Intent(this, CameraActivity.class), 100);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, GET_PERMISSION_REQUEST);
            }
        } else {
            startActivityForResult(new Intent(this, CameraActivity.class), cameraRequestCode);
        }
    }

    public void showSearchFragment(OnSearchClickListener listener) {
        SearchFragment searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(listener);
        searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
    }

    private final int GET_PERMISSION_REQUEST = 100; //权限申请自定义码

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.getInstance().getRequestCodeCallPhone() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + super_phone));
            startActivity(intent);
        } else if (requestCode == GET_PERMISSION_REQUEST) {
            int size = 0;
            if (grantResults.length >= 1) {
                int writeResult = grantResults[0];
                //读写内存权限
                boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;//读写内存权限
                if (!writeGranted) {
                    size++;
                }
                //录音权限
                int recordPermissionResult = grantResults[1];
                boolean recordPermissionGranted = recordPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!recordPermissionGranted) {
                    size++;
                }
                //相机权限
                int cameraPermissionResult = grantResults[2];
                boolean cameraPermissionGranted = cameraPermissionResult == PackageManager.PERMISSION_GRANTED;
                if (!cameraPermissionGranted) {
                    size++;
                }
                if (size == 0) {
                    startActivityForResult(new Intent(this, CameraActivity.class), 100);
                } else {
                    BaseTools.showToast("请到设置-权限管理中开启相机权限");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == cameraRequestCode) {
            String cameraPicture = BaseTools.getCameraPicture(resultCode, data);
        } else if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            // 图片选择结果回调
            // 例如 LocalMedia 里面返回三种path
            // 1.media.getPath(); 为原图path
            // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
            // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
            // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
            List<LocalMedia> localMedias = BaseTools.getPicturePath(data);
        }
    }
}
