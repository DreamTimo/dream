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
import com.timo.timolib.tools.permissions.permission_interface.BasePermissionInterface;
import com.timo.timolib.tools.permissions.permission_interface.PermissionGrantedListener;
import com.timo.timolib.tools.camera.CameraActivity;
import com.timo.timolib.tools.permissions.PermissionUtils;
import com.timo.timolib.tools.utils.math.RegexUtil;

import java.util.List;

/**
 * Created by 45590 on 2018/7/13.
 */

public abstract class BaseFragment extends SuperFragment implements BasePermissionInterface {
    private String tag_phone;
    private PermissionGrantedListener mStorageListener;
    private PermissionGrantedListener mCalendarListener;
    private PermissionGrantedListener mContactsListener;
    private PermissionGrantedListener mLocationListener;
    private PermissionGrantedListener mAudioListener;
    private PermissionGrantedListener mSensorsListener;

    @Override
    public void toOpenCamera() {
        if (PermissionUtils.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)) {
            startActivityForResult(new Intent(getActivity(), CameraActivity.class), BaseConstancts.requestCode_camera);
        } else {
            PermissionUtils.requestPermissions(this, getString(R.string.app_name), BaseConstancts.requestCode_camera, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA);
        }
    }

    @Override
    public void toOpenCallPhone(String phone) {
        if (TextUtils.isEmpty(phone) || !RegexUtil.getInstance().isTel(phone)) {
            BaseTools.showToast(getString(R.string.please_edit_right_phone));
            return;
        }
        this.tag_phone = phone;
        if (PermissionUtils.hasPermissions(getActivity(), Manifest.permission.CALL_PHONE)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);
        } else {
            PermissionUtils.requestPermissions(this, getString(R.string.permission_call_phone), BaseConstancts.requestCode_phone, Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    public void toOpenSendSms(String phone) {
        if (TextUtils.isEmpty(phone) || !RegexUtil.getInstance().isTel(phone)) {
            BaseTools.showToast(getString(R.string.please_edit_right_phone));
            return;
        }
        this.tag_phone = phone;
        if (PermissionUtils.hasPermissions(getActivity(), Manifest.permission.SEND_SMS)) {
            Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + tag_phone));
            startActivity(intent);
        } else {
            PermissionUtils.requestPermissions(this, getString(R.string.permission_sms), BaseConstancts.requestCode_sms, Manifest.permission.SEND_SMS);
        }
    }

    @Override
    public void toOpenStorage(PermissionGrantedListener grantedListener) {
        if (!PermissionUtils.hasPermissions(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtils.requestPermissions(this, getString(R.string.permission_storage), BaseConstancts.requestCode_storage, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            this.mStorageListener = grantedListener;
        } else {
            if (grantedListener != null) {
                grantedListener.onGranted();
            }
        }
    }

    @Override
    public void toOpenStorage() {
        toOpenStorage(null);
    }

    @Override
    public void toOpenCalendar(PermissionGrantedListener grantedListener) {
        if (!PermissionUtils.hasPermissions(getActivity(), Manifest.permission.WRITE_CALENDAR)) {
            PermissionUtils.requestPermissions(this, getString(R.string.permission_calendar), BaseConstancts.requestCode_calendar, Manifest.permission.WRITE_CALENDAR);
            this.mCalendarListener = grantedListener;
        } else {
            if (grantedListener != null) {
                grantedListener.onGranted();
            }
        }
    }

    @Override
    public void toOpenCalendar() {
        toOpenCalendar(null);
    }

    @Override
    public void toOpenContacts(PermissionGrantedListener grantedListener) {
        if (!PermissionUtils.hasPermissions(getActivity(), Manifest.permission.WRITE_CONTACTS)) {
            PermissionUtils.requestPermissions(this, getString(R.string.permission_contacts), BaseConstancts.requestCode_contacts, Manifest.permission.WRITE_CONTACTS);
            this.mContactsListener = grantedListener;
        } else {
            if (grantedListener != null) {
                grantedListener.onGranted();
            }
        }
    }

    @Override
    public void toOpenContacts() {
        toOpenContacts(null);
    }

    @Override
    public void toOpenLocation(PermissionGrantedListener grantedListener) {
        if (!PermissionUtils.hasPermissions(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            PermissionUtils.requestPermissions(this, getString(R.string.permission_location), BaseConstancts.requestCode_location, Manifest.permission.ACCESS_FINE_LOCATION);
            this.mLocationListener = grantedListener;
        } else {
            if (grantedListener != null) {
                grantedListener.onGranted();
            }
        }
    }

    @Override
    public void toOpenLocation() {
        toOpenLocation(null);
    }

    @Override
    public void toOpenAudio(PermissionGrantedListener grantedListener) {
        if (!PermissionUtils.hasPermissions(getActivity(), Manifest.permission.RECORD_AUDIO)) {
            PermissionUtils.requestPermissions(this, getString(R.string.permission_audio), BaseConstancts.requestCode_audio, Manifest.permission.RECORD_AUDIO);
            this.mAudioListener = grantedListener;
        } else {
            if (grantedListener != null) {
                grantedListener.onGranted();
            }
        }
    }

    @Override
    public void toOpenAudio() {
        toOpenAudio(null);
    }

    @Override
    public void toOpenSensors(PermissionGrantedListener grantedListener) {
        if (!PermissionUtils.hasPermissions(getActivity(), Manifest.permission.BODY_SENSORS)) {
            PermissionUtils.requestPermissions(this, getString(R.string.permission_sensors), BaseConstancts.requestCode_sensors, Manifest.permission.BODY_SENSORS);
            this.mSensorsListener = grantedListener;
        } else {
            if (grantedListener != null) {
                grantedListener.onGranted();
            }
        }
    }

    @Override
    public void toOpenSensors() {
        toOpenSensors(null);
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
            toOpenCallPhone(tag_phone);
        } else if (requestCode == BaseConstancts.requestCode_camera) {
            toOpenCamera();
        } else if (requestCode == BaseConstancts.requestCode_sms) {
            toOpenSendSms(tag_phone);
        } else if (requestCode == BaseConstancts.requestCode_audio) {
            toOpenAudio(mAudioListener);
        } else if (requestCode == BaseConstancts.requestCode_calendar) {
            toOpenCalendar(mCalendarListener);
        } else if (requestCode == BaseConstancts.requestCode_location) {
            toOpenLocation(mLocationListener);
        } else if (requestCode == BaseConstancts.requestCode_contacts) {
            toOpenContacts(mContactsListener);
        } else if (requestCode == BaseConstancts.requestCode_sensors) {
            toOpenSensors(mSensorsListener);
        } else if (requestCode == BaseConstancts.requestCode_storage) {
            toOpenStorage(mStorageListener);
        }
    }
}
