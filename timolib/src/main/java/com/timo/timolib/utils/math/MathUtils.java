package com.timo.timolib.utils.math;
import android.os.Build;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import com.timo.timolib.MyApplication;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MathUtils {
    /**
     * ldpi : {density=0.75, width=240, height=320, scaledDensity=0.75, xdpi=120.0, ydpi=120.0}
     * mdpi : {density=1.0, width=320, height=480, scaledDensity=1.0, xdpi=160.0, ydpi=160.0}
     * hdpi : {density=1.5, width=480, height=800, scaledDensity=1.5, xdpi=240.0, ydpi=240.0}
     * xdpi : {density=2, width=720, height=1280, scaledDensity=2, xdpi=320.0, ydpi=320.0}
     */
    private MathUtils() {}
    private static MathUtils instance;
    public static MathUtils getInstance(){
        if (instance==null){
            instance=new MathUtils();
        }
        return instance;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     */
    public int px2dp(float pxValue) {
        final float fontScale = MyApplication.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public int dp2px(float dipValue) {
        final float scale = MyApplication.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public int px2sp(float pxValue) {
        final float fontScale = MyApplication.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public int sp2px(float spValue) {
        final float fontScale = MyApplication.getInstance().getContext().getResources().getDisplayMetrics().density;
        return (int) (spValue * fontScale + 0.5f);
    }


    /**
     * 在给定的范围中随机取1个数<br>
     */
    public int getRandomInt(int start, int end) {
        List<Integer> readomWords = new ArrayList<Integer>();
        for (int i = start; i <= end; i++) {
            readomWords.add(i);
        }
        return getRandom(readomWords);
    }

    /**
     * 在给定的数组中随机取1个值
     */
    public <T> T getRandom(List<T> readomWords) {
         int idx = (int) (Math.random() * readomWords.size());
        return readomWords.get(idx);
    }
    /**
     * 保留小数点后两位的值
     */
    public String getPrice(Double price) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(price);
    }
    /**
     * 判断对象是否为null或长度数量为0
     */
    public boolean isNotEmpty(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String && (obj.toString().length() == 0||obj.toString().trim().length() == 0)) {
            return false;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return false;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return false;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return false;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return false;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return false;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return false;
            }
        }
        return true;
    }

}
