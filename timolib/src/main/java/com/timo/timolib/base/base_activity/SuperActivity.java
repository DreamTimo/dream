package com.timo.timolib.base.base_activity;

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
import com.timo.timolib.Timo_BaseConfig;
import com.timo.timolib.R;
import com.timo.timolib.Timo_BaseConstancts;
import com.timo.timolib.BaseTools;
import com.timo.timolib.Timo_Application;
import com.timo.timolib.Timo_Params;
import com.timo.timolib.tools.camera.CameraActivity;
import com.timo.timolib.tools.daynightmodeutils.ChangeModeController;
import com.timo.timolib.view.TitleBar;
import com.timo.timolib.view.search.SearchFragment;
import com.timo.timolib.view.search.custom.OnSearchClickListener;
import com.timo.timolib.tools.utils.PermissionUtils;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;

/**
 * activity基类的参数、方法
 */

public abstract class SuperActivity extends FragmentActivity {
    private Timo_Params getParams;
    private Timo_Params setParams;
    private TitleBar baseTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //夜间模式
//        ChangeModeController.getInstance().init(this, R.attr.class);
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());
        ButterKnife.bind(this);
        setTitle();
        initEvent(savedInstanceState);
    }

    protected abstract int getContentResId();

    protected void setTitle() {
        if (findViewById(R.id.title) != null && BaseTools.isNotEmpty(setTitleName())) {
            baseTitleBar = (TitleBar) findViewById(R.id.title);
            BaseTools.setTitleBar(baseTitleBar, setTitleName());
        }
    }

    protected abstract String setTitleName();

    protected abstract void initEvent(Bundle savedInstanceState);

    public void startActivityNoFinish(Class<?> cls) {
        if (setParams == null) {
            startActivity(new Intent(Timo_Application.getInstance().getContext(), cls));
        } else {
            startActivity(new Intent(Timo_Application.getInstance().getContext(), cls).putExtra(Timo_BaseConstancts.BASE_PARAM, setParams));
        }
    }

    public void startActivityAddFinish(Class<?> cls) {
        if (setParams == null) {
            startActivity(new Intent(this, cls));
            finish();
        } else {
            startActivity(new Intent(this, cls).putExtra(Timo_BaseConstancts.BASE_PARAM, setParams));
            finish();
        }
    }

    public void setMyResult(Serializable object) {
        setResult(RESULT_OK, new Intent().putExtra(Timo_BaseConstancts.BASE_PARAM, object));
    }

    public void startActivityForMyResult(Class<?> cls, int code) {
        if (setParams == null) {
            startActivityForResult(new Intent(Timo_Application.getInstance().getContext(), cls), code);
        } else {
            startActivityForResult(new Intent(Timo_Application.getInstance().getContext(), cls).putExtra(Timo_BaseConstancts.BASE_PARAM, setParams), code);
        }
    }

    public Timo_Params getParams() {
        if (getIntent().getSerializableExtra(Timo_BaseConstancts.BASE_PARAM) != null) {
            getParams = (Timo_Params) getIntent().getSerializableExtra(Timo_BaseConstancts.BASE_PARAM);
        } else {
            getParams = new Timo_Params();
        }
        return getParams;
    }

    public Timo_Params setParams() {
        if (setParams == null) {
            setParams = new Timo_Params();
        }
        return setParams;
    }


    public Timo_Params getMyParamResult(Intent data) {
        if (data.getSerializableExtra(Timo_BaseConstancts.BASE_PARAM) != null) {
            return (Timo_Params) data.getSerializableExtra(Timo_BaseConstancts.BASE_PARAM);
        }
        return null;
    }

    public Serializable getMyResult(Intent data) {
        return data.getSerializableExtra(Timo_BaseConstancts.BASE_PARAM);
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
                if (Timo_BaseConfig.exit_to_back) {
                    moveTaskToBack(false);
                    return true;
                } else {
                    if ((System.currentTimeMillis() - mExitTime) > Timo_BaseConfig.exit_time) {
                        BaseTools.showToast(getString(R.string.hint_exit));
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
                        Manifest.permission.CAMERA}, Timo_BaseConstancts.get_camera_permission_request);
            }
        } else {
            startActivityForResult(new Intent(this, CameraActivity.class), Timo_BaseConstancts.cameraRequestCode);
        }
    }

    public void showSearchFragment(OnSearchClickListener listener) {
        SearchFragment searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(listener);
        searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
    }

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
        } else if (requestCode == Timo_BaseConstancts.get_camera_permission_request) {
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
                    BaseTools.showToast(getString(R.string.hint_camera_need_permission));
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Timo_BaseConstancts.cameraRequestCode) {
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
