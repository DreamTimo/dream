package com.timo.timolib.tools.utils;//package com.timo.timolib.utils;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import com.google.vr.sdk.widgets.pano.VrPanoramaEventListener;
//import com.google.vr.sdk.widgets.pano.VrPanoramaView;
//import com.timo.timolib.manager.MyApplication;
//
//import java.io.IOException;
//import java.io.InputStream;
//
///**
// * Created by 蔡永汪 on 2017/7/25.
// */
//
//public class VRUtils {
//    private VRUtils() {
//    }
//
//    private static VRUtils instance;
//
//    public static VRUtils getInstance() {
//        if (instance == null) {
//            instance = new VRUtils();
//        }
//        return instance;
//    }
//
//    public void setVRImage(VrPanoramaView vrImageView,String url) {
//        /**获取assets文件夹下的图片**/
//        InputStream open = null;
//        try {
//            open = MyApplication.getInstance().getContext().getAssets().open(url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Bitmap bitmap = BitmapFactory.decodeStream(open);
//        /**设置加载VR图片的相关设置**/
//        VrPanoramaView.Options options = new VrPanoramaView.Options();
//        options.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;
//        /**设置加载VR图片监听**/
//        vrImageView.setEventListener(new VrPanoramaEventListener() {
//            /**
//             * 显示模式改变回调
//             * 1.默认
//             * 2.全屏模式
//             * 3.VR观看模式，即横屏分屏模式
//             * @param newDisplayMode 模式
//             */
//            @Override
//            public void onDisplayModeChanged(int newDisplayMode) {
//                super.onDisplayModeChanged(newDisplayMode);
//                LogUtil.getInstance().e("onDisplayModeChanged()->newDisplayMode=" + newDisplayMode);
//            }
//
//            /**
//             * 加载VR图片失败回调
//             * @param errorMessage
//             */
//            @Override
//            public void onLoadError(String errorMessage) {
//                super.onLoadError(errorMessage);
//                LogUtil.getInstance().e("onLoadError()->errorMessage=" + errorMessage);
//            }
//
//            /**
//             * 加载VR图片成功回调
//             */
//            @Override
//            public void onLoadSuccess() {
//                super.onLoadSuccess();
//                LogUtil.getInstance().e("onLoadSuccess->图片加载成功");
//            }
//
//            /**
//             * 点击VR图片回调
//             */
//            @Override
//            public void onClick() {
//                super.onClick();
//                LogUtil.getInstance().e("onClick()");
//            }
//        });
//        /**加载VR图片**/
//        vrImageView.loadImageFromBitmap(bitmap, options);
//        //TODO 页面销毁时需要释放资源:
////        vrImageView.pauseRendering();
////        vrImageView.shutdown();
//    }
//}
