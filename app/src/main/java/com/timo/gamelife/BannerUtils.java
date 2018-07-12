package com.timo.gamelife;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import com.timo.timolib.view.banner.Banner;
import com.timo.timolib.view.banner.internal.BannerData;
import com.timo.timolib.view.banner.internal.BaseBannerAdapter;
import com.timo.timolib.view.banner.internal.ItemData;
import com.timo.timolib.view.banner.pagerstyle.ScaleInTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蔡永汪 on 2017/7/22.
 */

public class BannerUtils {
    private BannerUtils() {
    }

    private static BannerUtils instance;

    public static BannerUtils getInstance() {
        if (instance == null) {
            instance = new BannerUtils();
        }
        return instance;
    }

    /**
     * 房源信息的轮播图
     */
    public void setHouseProfiltBanner(final Context context, Banner banner, final List<String> data) {
        final List<ItemData> dataList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ItemData itemData = new ItemData();
            itemData.setImgUrl(data.get(i));
            dataList.add(itemData);
        }
        BannerData bannerData = new BannerData();
        bannerData.setDatas(dataList);
        banner.setData(bannerData, new ScaleInTransformer());
        banner.setOnClickListener(new BaseBannerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PagerAdapter parent, View view, int position, int realPosition) {

            }
        });
    }
}
