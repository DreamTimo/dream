package com.timo.timolib.base.base_adapter.animation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ScaleAndAlphaAnimationAdapter extends AnimationAdapter {

    private static final float DEFAULT_SCALE_FROM = .5f;
    private final float mFrom;

    public ScaleAndAlphaAnimationAdapter(RecyclerView.Adapter adapter) {
        this(adapter, DEFAULT_SCALE_FROM);
    }

    public ScaleAndAlphaAnimationAdapter(RecyclerView.Adapter adapter, float from) {
        super(adapter);
        mFrom = from;
    }

    @Override
    protected Animator[] getAnimators(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", mFrom, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", mFrom, 1f);
        return new Animator[] { ObjectAnimator.ofFloat(view, "alpha", 0, 1f),scaleX,scaleY};
    }
}
