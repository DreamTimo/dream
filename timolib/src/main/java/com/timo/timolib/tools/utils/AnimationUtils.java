package com.timo.timolib.tools.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by 蔡永汪 on 2017/7/25.
 */

public class AnimationUtils {
    private AnimationUtils() {}
    private static AnimationUtils instance;
    public static AnimationUtils getInstance(){
        if (instance==null){
            instance=new AnimationUtils();
        }
        return instance;
    }
    public Animator[] getAlphaAnimation(View view){
        return new Animator[] { ObjectAnimator.ofFloat(view, "alpha", 0, 1f)};
    }
    public Animator[] getAlphaAnimation(View view,float initValue){
        return new Animator[] { ObjectAnimator.ofFloat(view, "alpha", initValue, 1f)};
    }
    public Animator[] getScaleAnimation(View view){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f);
        return new Animator[] {scaleX,scaleY};
    }
    public Animator[] getScaleAnimation(View view,float X,float Y){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", X, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", Y, 1f);
        return new Animator[] {scaleX,scaleY};
    }
}
