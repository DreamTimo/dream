package com.timo.gamelife;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.timo.gamelife.adapter.HomeLimeAdapter;

import java.util.List;

/**
 * Created by 蔡永汪 on 2017/7/25.
 */

public class RecyclerViewUtils {
    private RecyclerViewUtils() {
    }

    private static RecyclerViewUtils instance;

    public static RecyclerViewUtils getInstance() {
        if (instance == null) {
            instance = new RecyclerViewUtils();
        }
        return instance;
    }

    /**
     * 设置民宿信息
     */
    public void setHomeLime(Context context, RecyclerView recyclerView, List data) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        HomeLimeAdapter limeAdapter = new HomeLimeAdapter(context, R.layout.recommend_item_1, data);
        recyclerView.setAdapter(limeAdapter);
    }


}
