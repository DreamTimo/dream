package com.timo.timolib.base.base_fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.timo.timolib.BaseConstancts;
import com.timo.timolib.BaseTools;
import com.timo.timolib.R;
import com.timo.timolib.tools.camera.CameraActivity;
import com.timo.timolib.tools.permissions.PermissionUtils;
import com.timo.timolib.tools.utils.math.RegexUtil;

import java.util.List;

/**
 * Created by 45590 on 2018/7/13.
 */

public abstract class BaseFragment extends SuperFragment {
    /**
     * 拨打电话
     */
    private String super_phone;

    public void callPhone(String phone) {
        if (TextUtils.isEmpty(phone) || !RegexUtil.getInstance().isTel(phone)) {
            BaseTools.showToast(getString(R.string.please_edit_right_phone));
            return;
        }
        this.super_phone = phone;
        if (PermissionUtils.hasPermissions(getActivity(), Manifest.permission.CALL_PHONE)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);
        } else {
            PermissionUtils.requestPermissions(this,getString(R.string.permission_call_phone), BaseConstancts.requestCode_phone, Manifest.permission.CALL_PHONE);
        }
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        if (PermissionUtils.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)) {
            startActivityForResult(new Intent(getActivity(), CameraActivity.class), BaseConstancts.requestCode_camera);
        } else {
            PermissionUtils.requestPermissions(this, getString(R.string.app_name), BaseConstancts.requestCode_camera, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseConstancts.requestCode_camera) {
            String cameraPicture = BaseTools.getCameraPicture(resultCode, data);
        } else if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == getActivity().RESULT_OK) {
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
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == BaseConstancts.requestCode_phone) {
            callPhone(super_phone);
        }else if (requestCode==BaseConstancts.requestCode_camera){
            openCamera();
        }
    }

}
