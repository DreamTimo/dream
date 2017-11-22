/*
 * Copyright 2016. SHENQINCI(沈钦赐)<946736079@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.timo.timolib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timo.timolib.R;

/**
 * 购物车商品数量、增加和减少控制按钮。
 * Created by 沈钦赐 on 16/4/29.
 */
public class SelectCountView extends LinearLayout implements View.OnClickListener{
    //库存
    private int mInventory = Integer.MAX_VALUE;
    //最大购买数，默认无限制
    private int mBuyMax = Integer.MAX_VALUE;
    private TextView mCount;
    private OnWarnListener mOnWarnListener;

    public SelectCountView(Context context) {
        this(context, null);
    }

    public SelectCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
//
//    public NumberButton(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_select_count_layout, this);

        TextView addButton = (TextView) findViewById(R.id.button_add);
        addButton.setOnClickListener(this);
        TextView subButton = (TextView) findViewById(R.id.button_sub);
        subButton.setOnClickListener(this);

        mCount = ((TextView) findViewById(R.id.text_count));
//        mCount.addTextChangedListener(this);
//        mCount.setOnClickListener(this);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberButton);
        boolean editable = typedArray.getBoolean(R.styleable.NumberButton_editable, true);
        int buttonWidth = typedArray.getDimensionPixelSize(R.styleable.NumberButton_buttonWidth, -1);
        int textWidth = typedArray.getDimensionPixelSize(R.styleable.NumberButton_m_textWidth, -1);
        int textSize = typedArray.getDimensionPixelSize(R.styleable.NumberButton_m_textSize, -1);
        int textColor = typedArray.getColor(R.styleable.NumberButton_m_textColor, 0xff000000);
        typedArray.recycle();

//        setEditable(editable);
        mCount.setTextColor(textColor);
        subButton.setTextColor(textColor);
        addButton.setTextColor(textColor);

        if (textSize > 0)
            mCount.setTextSize(textSize);

        if (buttonWidth > 0) {
            LayoutParams textParams = new LayoutParams(buttonWidth, LayoutParams.MATCH_PARENT);
            subButton.setLayoutParams(textParams);
            addButton.setLayoutParams(textParams);
        }
        if (textWidth > 0) {
            LayoutParams textParams = new LayoutParams(textWidth, LayoutParams.MATCH_PARENT);
            mCount.setLayoutParams(textParams);
        }
    }

    public int getNumber() {
       return Integer.parseInt(mCount.getText().toString().trim());
    }
    public int getCurrentNumber() {
        int count = getNumber();
        return count;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        int count = getNumber();
        if (id == R.id.button_sub) {
            if (count > 1) {
                //正常减
                mCount.setText((count - 1)+"");
            }
        } else if (id == R.id.button_add) {
            if (count < Math.min(mBuyMax, mInventory)) {
                //正常添加
                mCount.setText((count + 1)+"");
            } else if (mInventory < mBuyMax) {
                //库存不足
                warningForInventory();
            } else {
                //超过最大购买数
                warningForBuyMax();
            }

        }
    }

    /**
     * 超过的库存限制
     * Warning for inventory.
     */
    private void warningForInventory() {
        if (mOnWarnListener != null) mOnWarnListener.onWarningForInventory(mInventory);
    }

    /**
     * 超过的最大购买数限制
     * Warning for buy max.
     */
    private void warningForBuyMax() {
        if (mOnWarnListener != null) mOnWarnListener.onWarningForBuyMax(mBuyMax);
    }

    public SelectCountView setCurrentNumber(int currentNumber) {
        if (currentNumber < 1) mCount.setText("1");
        mCount.setText(""+Math.min(Math.min(mBuyMax, mInventory), currentNumber));
        return this;
    }

    public int getInventory() {
        return mInventory;
    }

    public SelectCountView setInventory(int inventory) {
        mInventory = inventory;
        return this;
    }

    public int getBuyMax() {
        return mBuyMax;
    }

    public SelectCountView setBuyMax(int buyMax) {
        mBuyMax = buyMax;
        return this;
    }

    public SelectCountView setOnWarnListener(OnWarnListener onWarnListener) {
        mOnWarnListener = onWarnListener;
        return this;
    }

    public interface OnWarnListener {
        void onWarningForInventory(int inventory);

        void onWarningForBuyMax(int max);
    }
}
