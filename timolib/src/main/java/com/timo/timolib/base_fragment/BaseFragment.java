package com.timo.timolib.base_fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.timo.timolib.BaseTools;
import com.timo.timolib.MyApplication;
import com.timo.timolib.R;
import com.timo.timolib.BaseConstancts;
import com.timo.timolib.Params;
import com.timo.timolib.camera.CameraActivity;
import com.timo.timolib.http.MyHttpParams;
import com.timo.timolib.utils.PermissionUtils;
import com.timo.timolib.view.TitleBar;

import java.io.Serializable;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by lykj on 2017/9/10.
 */

public abstract class BaseFragment extends Fragment {

    private Params setParams;
    private Params getParams;
    private TitleBar baseTitleBar;
    private Unbinder unbinder;
    private String super_phone;
    public void startActivity(Class<?> cls) {
        if (setParams == null) {
            startActivity(new Intent(getActivity(), cls));
        } else {
            startActivity(new Intent(getActivity(), cls).putExtra(BaseConstancts.BASE_PARAM, setParams));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentResId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        setTitle(view);
        initEvent(view);
        return view;

    }

    protected void setTitle(View view) {
        if (view.findViewById(R.id.title) != null && BaseTools.isNotEmpty(setTitleName())) {
            baseTitleBar = (TitleBar) view.findViewById(R.id.title);
            BaseTools.setTitleBar(baseTitleBar, setTitleName());
        }
    }

    protected abstract String setTitleName();

    protected abstract int getContentResId();

    protected abstract void initEvent(View view);

    public Params setParams() {
        if (setParams == null) {
            setParams = new Params();
        }
        return setParams;
    }

    public Params getParams() {
        try {
            if (getArguments() != null && getArguments().getSerializable(BaseConstancts.BASE_PARAM) != null) {
                getParams = (Params) getArguments().getSerializable(BaseConstancts.BASE_PARAM);
            }
            if (getParams == null) {
                getParams = new Params();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return getParams;
    }

    public MyHttpParams getHttpParams() {
        return new MyHttpParams();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    public void startActivityNoFinish(Class<?> cls) {
        if (setParams == null) {
            startActivity(new Intent(MyApplication.getInstance().getContext(), cls));
        } else {
            startActivity(new Intent(MyApplication.getInstance().getContext(), cls).putExtra(BaseConstancts.BASE_PARAM, setParams));
        }
    }

    public void startActivityForMyResult(Class<?> cls, int code) {
        if (setParams == null) {
            startActivityForResult(new Intent(MyApplication.getInstance().getContext(), cls), code);
        } else {
            startActivityForResult(new Intent(MyApplication.getInstance().getContext(), cls).putExtra(BaseConstancts.BASE_PARAM, setParams), code);
        }
    }

    public Serializable getMyResult(Intent data) {
        return data.getSerializableExtra(BaseConstancts.BASE_PARAM);
    }
    /**
     * 拨打电话
     */

    public void callPhone(String phone) {
        this.super_phone = phone;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            PermissionUtils.getInstance().applyCallPhonePermission(getActivity());
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }
    /**
     * 打开相机
     */
    public void openCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager
                    .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager
                            .PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager
                            .PERMISSION_GRANTED) {
                startActivityForResult(new Intent(getActivity(), CameraActivity.class), 100);
            } else {
                //不具有获取权限，需要进行权限申请
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA}, BaseConstancts.get_camera_permission_request);
            }
        } else {
            startActivityForResult(new Intent(getActivity(), CameraActivity.class), BaseConstancts.cameraRequestCode);
        }
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.getInstance().getRequestCodeCallPhone() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + super_phone));
            startActivity(intent);
        } else if (requestCode == BaseConstancts.get_camera_permission_request) {
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
                    startActivityForResult(new Intent(getActivity(), CameraActivity.class), 100);
                } else {
                    BaseTools.showToast(getString(R.string.hint_camera_need_permission));
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseConstancts.cameraRequestCode) {
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
}
