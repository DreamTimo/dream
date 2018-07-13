package com.timo.timolib.base.base_activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.timo.timolib.BaseConstancts;
import com.timo.timolib.BaseTools;
import com.timo.timolib.R;
import com.timo.timolib.tools.camera.CameraActivity;
import com.timo.timolib.tools.permissions.PermissionUtils;
import com.timo.timolib.tools.utils.math.RegexUtil;
import com.timo.timolib.view.search.SearchFragment;
import com.timo.timolib.view.search.custom.OnSearchClickListener;

import java.util.List;

/**
 * 预留：设置布局
 */

public abstract class BaseActivity extends SuperActivity {
    /**
     * 拨打电话
     */
    private String tag_phone;

    public void callPhone(String phone) {
        if (TextUtils.isEmpty(phone) || !RegexUtil.getInstance().isTel(phone)) {
            BaseTools.showToast(getString(R.string.please_edit_right_phone));
            return;
        }
        this.tag_phone = phone;
        if (PermissionUtils.hasPermissions(this, Manifest.permission.CALL_PHONE)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        } else {
            PermissionUtils.requestPermissions(this, "需要拨打电话权限", BaseConstancts.requestCode_phone, Manifest.permission.CALL_PHONE);
        }

    }

    /**
     * 打开相机
     */
    public void openCamera() {
        if (PermissionUtils.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)) {
            startActivityForResult(new Intent(this, CameraActivity.class), BaseConstancts.requestCode_camera);
        } else {
            PermissionUtils.requestPermissions(this, getString(R.string.app_name), BaseConstancts.requestCode_camera, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseConstancts.requestCode_camera) {
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == BaseConstancts.requestCode_phone) {
            callPhone(tag_phone);
        } else if (requestCode == BaseConstancts.requestCode_camera) {
            openCamera();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    /**
     * 展示搜索控件
     *
     * @param listener
     */
    public void showSearchFragment(OnSearchClickListener listener) {
        SearchFragment searchFragment = SearchFragment.newInstance();
        searchFragment.setOnSearchClickListener(listener);
        searchFragment.show(getSupportFragmentManager(), SearchFragment.TAG);
    }

}
