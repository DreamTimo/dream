package com.timo.gamelife.adapter;

import android.content.Context;
import android.view.View;

import com.timo.gamelife.R;
import com.timo.gamelife.bean.HouseBean;
import com.timo.timolib.BaseTools;
import com.timo.timolib.base.BaseAdapter;
import com.timo.timolib.base.base_adapter.ViewHolder;

import java.util.List;

/**
 * Created by 蔡永汪 on 2017/8/3.
 */

public class HomeLimeAdapter extends BaseAdapter<HouseBean> {
    public HomeLimeAdapter(Context context, int layoutId, List<HouseBean> datas) {
        super(context, layoutId, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final HouseBean bean, int position) {
        holder.setOnClickListener(R.id.root, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseTools.showToast("跳转");
            }
        });
        if (BaseTools.isNotEmpty(bean.getRoomName())) {
            holder.setText(R.id.recommend_name, bean.getRoomName());
        }
        if (BaseTools.isNotEmpty(bean.getNormalPrice())) {
            holder.setText(R.id.recommend_price, "¥" + bean.getNormalPrice());
        }
        if (BaseTools.isNotEmpty(bean.getEvaluateScore())) {
            holder.setText(R.id.recommend_introduction, bean.getEvaluateScore());
        }

    }
}
