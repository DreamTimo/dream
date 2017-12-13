package com.dlbase.view;

import com.luyz.dlbaselib.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

@SuppressLint("RtlHardcoded")
public class IconButton extends Button {
    protected int drawableWidth;
    protected int drawableHeigth;
    protected DrawablePositions drawablePosition;
    protected int iconPadding;

    // Cached to prevent allocation during onLayout
    Rect bounds;

    private enum DrawablePositions {
        NONE,
        LEFT_AND_RIGHT,
        LEFT,
        TOP,
        RIGHT
    }

    public IconButton(Context context) {
        super(context);
        bounds = new Rect();
    }

    public IconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        bounds = new Rect();
        applyAttributes(attrs);
    }

    public IconButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        bounds = new Rect();
        applyAttributes(attrs);
    }

    protected void applyAttributes(AttributeSet attrs) {
        // Slight contortion to prevent allocating in onLayout
        if (null == bounds) {
            bounds = new Rect();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.IconButton);
        int paddingId = typedArray.getDimensionPixelSize(R.styleable.IconButton_iconPadding, 0);
        setIconPadding(paddingId);
        typedArray.recycle();
    }

    public void setIconPadding(int padding) {
        iconPadding = padding;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        Paint textPaint = getPaint();
        String text = getText().toString();
        textPaint.getTextBounds(text, 0, text.length(), bounds);

        int textWidth = bounds.width();
        int textHeight = bounds.height();
        int factor = (drawablePosition == DrawablePositions.TOP) ? 2 : 1;
        int contentWidth = drawableWidth + iconPadding * factor + textWidth;
        int horizontalPadding = (int) ((getWidth() / 2.0) - (contentWidth / 2.0));
        
        int contentHeight = drawableHeigth + iconPadding * factor + textHeight;
        int verticalPadding = (int) ((getHeight() / 2.0) - (contentHeight / 2.0));

        setCompoundDrawablePadding(-verticalPadding + iconPadding);

        switch (drawablePosition) {
            case LEFT:
                setPadding(horizontalPadding, getPaddingTop(), 0, getPaddingBottom());
                break;

            case RIGHT:
                setPadding(0, getPaddingTop(), horizontalPadding, getPaddingBottom());
                break;

            case LEFT_AND_RIGHT:
                setPadding(horizontalPadding, getPaddingTop(), horizontalPadding, getPaddingBottom());
                break;
            case TOP:
                setPadding(getPaddingLeft(), verticalPadding, getPaddingRight(), 0);
                break;
            default:
                setPadding(0, getPaddingTop(), 0, getPaddingBottom());
        }
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);

        
        if(top!=null){
        	drawableHeigth=top.getIntrinsicHeight();
        	drawablePosition = DrawablePositions.TOP;
        }else{
        	drawablePosition = DrawablePositions.NONE;
        }
        requestLayout();
    }
}