package com.lykj.UI.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.dlbase.base.DLBaseActivity;
import com.dlbase.util.DLStringUtil;
import com.dlbase.view.CircleImageView;
import com.luyz.lyimlib.LYIMConfig;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;
import com.lykj.model.LYUserModel;

public class LYMapActivity extends DLBaseActivity {

    private TextView tv_addressName;
    private TextView tv_addressDetails;
    private RelativeLayout rl_guidance;
    private ImageView iv_location;
    public static final String PAGE_USERMODEL = "userModel1";
    public static final String PAGE_LAT = "userlat";
    public static final String PAGE_LNG = "userlng";
    private MapView mapView = null;
    private BaiduMap baiduMap = null;

    View v;
    GeoCoder mSearch;
    private LYUserModel userModel = null;
    private String userLat = null;
    private String userLng = null;

    // 定位相关声明
    public LocationClient mLocationClient = null;

    //自定义图标
    BitmapDescriptor mCurrentMarker = null;

    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(0)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            baiduMap.setMyLocationData(locData);	//设置定位数据

            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16);	//设置地图中心点以及缩放级别
            baiduMap.animateMapStatus(u);

            mLocationClient.stop();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lymap);

        userModel = (LYUserModel)getIntent().getSerializableExtra(PAGE_USERMODEL);
        userLat = (String)getIntent().getSerializableExtra(PAGE_LAT);
        userLng = (String)getIntent().getSerializableExtra(PAGE_LNG);

        initView();
        setLocationOption();	//设置定位参数
        getLocation();
    }

    private void initView(){
        initNavView("我的位置", TTopBackType.ETopBack_Black, false,R.id.top_view);

        iv_location = (ImageView) findViewById(R.id.iv_location);
        iv_location.setOnClickListener(this);

        tv_addressName = (TextView) findViewById(R.id.tv_addressName);
        tv_addressDetails = (TextView) findViewById(R.id.tv_addressDetails);
        rl_guidance = (RelativeLayout) findViewById(R.id.rl_guidance);
        rl_guidance.setOnClickListener(this);

        mapView = (MapView) this.findViewById(R.id.mapView); // 获取地图控件引用
        // 隐藏缩放控件
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        //去掉百度log
        View child = mapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }

        baiduMap = mapView.getMap();
        baiduMap.clear();
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));   // 设置级别

    }

    // 三个状态实现地图生命周期管理
    @Override
    protected void onDestroy() {
        mSearch.destroy();
        //退出时销毁定位
        mLocationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        super.onDestroy();
        mapView.onDestroy();
        mapView = null;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 设置定位参数
     */
    private void setLocationOption() {

        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);// 注册定位监听接口
        /**
         * 设置定位参数
         */
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
//			option.setScanSpan(5000);// 设置发起定位请求的间隔时间,ms
        option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向
        option.setOpenGps(true);
//			option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
//			option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);
//			mLocationClient.start(); // 调用此方法开始定位
    }

    private void getLocation(){

        LatLng point = new LatLng(Double.valueOf(userLat),Double.valueOf(userLng));
        //设定中心点坐标
//			LatLng point = new LatLng(30.663791,104.07281);

        mSearch = GeoCoder.newInstance();

        mSearch.setOnGetGeoCodeResultListener(listener);
        ReverseGeoCodeOption option = new ReverseGeoCodeOption();
        option.location(point);
        mSearch.reverseGeoCode(option);


        //自定义View设置图标
        v = LayoutInflater.from(LYMapActivity.this).inflate(R.layout.image, null);

//        CircleImageView iv_icon_map = (CircleImageView) v.findViewById(R.id.iv_icon_map);
//
//         if (userModel != null) {
//            if (DLStringUtil.notEmpty(userModel.getAvatar())) {
//                LyImEngine.getInstance().DownImage(userModel.getAvatar()+ LYIMConfig.THUMBNAIL100, iv_icon_map,R.drawable.image_default);
//            }
//        }

        BitmapDescriptor icon =  BitmapDescriptorFactory.fromView(v);

        OverlayOptions options = new MarkerOptions().icon(icon).position(point);
        baiduMap.addOverlay(options);

        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
                .zoom(17)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化

        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);
    }

    OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //没有检索到结果
            }
            //获取地理编码结果

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                //DLToastUtil.showToastShort(mContext, "");
            }
            //获取反向地理编码结果
            tv_addressName.setText(result.getBusinessCircle());
            tv_addressDetails.setText(result.getAddress());
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);

        if (v.getId() == R.id.top_left) {
            finish();
        }else if (v.getId() == R.id.iv_location) {
//				setLocationOption();
            mLocationClient.start(); // 调用此方法开始定位
        }else if (v.getId() == R.id.rl_guidance) {
            getLocation();
        }
    }
}
