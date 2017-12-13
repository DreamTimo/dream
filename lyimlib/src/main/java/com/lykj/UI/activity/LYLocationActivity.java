package com.lykj.UI.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.dlbase.Model.DLLocationModel;
import com.dlbase.base.DLBaseActivity;
import com.dlbase.util.DLDateUtils;
import com.dlbase.util.DLFolderManager;
import com.dlbase.util.DLImageUtil;
import com.luyz.lyimlib.LyImEngine;
import com.luyz.lyimlib.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LYLocationActivity extends DLBaseActivity implements OnGetPoiSearchResultListener {


    public static final String RESULT_SELETELOCATION = "selete_location";
    public static final int RESULT_CODE_LOCATION = 0x998;

    public static DLLocationModel resultLocation = new DLLocationModel();

    private MapView mMapView = null;
    private BaiduMap mBaiduMap;

    private ImageView mRequestLocation;
    private ListView mListView;

    // 搜索周边相关
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;

    /**
     * 定位SDK的核心类
     */
    public LocationClient mLocationClient = null;

    /**
     * 当前标志
     */
    private Marker mCurrentMarker;
    // 定位图标描述
    private BitmapDescriptor currentMarker = null;

    public BDLocationListener myListener = new MyLocationListener();

    private List<PoiInfo> dataList;
    private ListAdapter adapter;

    private int locType;
    private double longitude;// 精度
    private double latitude;// 维度
    private float radius;// 定位精度半径，单位是米
    private String addrStr;// 反地理编码
    private String province;// 省份信息
    private String city;// 城市信息
    private String district;// 区县信息
    private float direction;// 手机方向信息

    private int checkPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lylocation);

        initView();
        initLocation();
        initEvent();
    }

    private void initView(){

        initNavView("我的位置", TTopBackType.ETopBack_Black, false,R.id.top_view);

        dataList = new ArrayList<PoiInfo>();
        mMapView = (MapView) findViewById(R.id.bmapView);
        mRequestLocation = (ImageView) findViewById(R.id.request);
        mListView = (ListView) findViewById(R.id.lv_location_nearby);
        checkPosition=0;

        adapter = new ListAdapter(dataList);
        mListView.setAdapter(adapter);

        //去掉百度log
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }

        // 隐藏缩放控件
        mMapView.showScaleControl(false);
        mMapView.showZoomControls(false);


        mBaiduMap = mMapView.getMap();
        mBaiduMap.clear();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));   // 设置级别

        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);// 注册定位监听接口

    }

    /**
     * 事件初始化
     */
    private void initEvent(){

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                checkPosition = position;
                adapter.setCheckposition(position);
                adapter.notifyDataSetChanged();


                PoiInfo ad = (PoiInfo) adapter.getItem(position);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ad.location);
                mBaiduMap.animateMapStatus(u);
//			    	mCurrentMarker.setPosition(ad.location);

                LYLocationActivity.resultLocation.setLat(ad.location.latitude+"");
                LYLocationActivity.resultLocation.setLng(ad.location.longitude+"");

                LyImEngine.getInstance().showWaitDialog(mContext);

                screenshot();
            }
        });

        mRequestLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //DLShowDialog.show(getApplicationContext(), "正在定位。。。");
                initLocation();
            }
        });

    }

    /**
     * 定位
     */
    private void initLocation(){
        //重新设置
        //checkPosition = 0;
        adapter.setCheckposition(0);

        /**
         * 设置定位参数
         */
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
//		option.setScanSpan(5000);// 设置发起定位请求的间隔时间,ms
        option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);
        mLocationClient.start(); // 调用此方法开始定位
    }


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        if (v.getId() == R.id.top_left) {
            finish();
        }
    }


    /**
     * 定位SDK监听函数
     *
     * @author
     *
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mMapView == null) {
                return;
            }

            locType = location.getLocType();
            Log.i("mybaidumap", "当前定位的返回值是："+locType);

            longitude = location.getLongitude();
            latitude = location.getLatitude();
            if (location.hasRadius()) {// 判断是否有定位精度半径
                radius = location.getRadius();
            }

            if (locType == BDLocation.TypeNetWorkLocation) {
                addrStr = location.getAddrStr();// 获取反地理编码(文字描述的地址)
                Log.i("mybaidumap", "当前定位的地址是："+addrStr);
            }

            direction = location.getDirection();// 获取手机方向，【0~360°】,手机上面正面朝北为0°
            province = location.getProvince();// 省份
            city = location.getCity();// 城市
            district = location.getDistrict();// 区县

            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());

            //将当前位置加入List里面
            PoiInfo info = new PoiInfo();
            info.address = location.getAddrStr();
            info.city = location.getCity();
            info.location = ll;
            info.name = location.getAddrStr();
            dataList.add(info);
            adapter.notifyDataSetChanged();
            Log.i("mybaidumap", "province是："+province +" city是"+city +" 区县是: "+district);

            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(0)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(locData);

            //画标志
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(ll);
            converter.from(CoordinateConverter.CoordType.COMMON);
            LatLng convertLatLng = converter.convert();

		   /* OverlayOptions ooA = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_marka));
		    mCurrentMarker = (Marker) mBaiduMap.addOverlay(ooA);*/


            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
            mBaiduMap.animateMapStatus(u);

            //画当前定位标志
            MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(uc);

            mMapView.showZoomControls(false);
            //poi 搜索周边
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Looper.prepare();
                    searchNeayBy();
                    Looper.loop();
                }
            }).start();


        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    /**
     * 搜索周边
     */
    private void searchNeayBy(){

        // POI初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();
        poiNearbySearchOption.keyword("写字楼");
        poiNearbySearchOption.location(new LatLng(latitude, longitude));
        poiNearbySearchOption.radius(500);  // 检索半径，单位是米
        poiNearbySearchOption.pageCapacity(20);  // 默认每页10条
        mPoiSearch.searchNearby(poiNearbySearchOption);  // 发起附近检索请求
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.i("----------------", "---------------------");
                    adapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };

    /*
     * 接受周边地理位置结果
     */
    @Override
    public void onGetPoiResult(PoiResult result) {
        // 获取POI检索结果
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            Toast.makeText(LYLocationActivity.this, "未找到结果",Toast.LENGTH_LONG).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
//			mBaiduMap.clear();
            if(result != null){
                if(result.getAllPoi()!= null && result.getAllPoi().size()>0){
                    PoiInfo temItem = dataList.get(0);
                    dataList.clear();
                    dataList.add(temItem);
                    dataList.addAll(result.getAllPoi());
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
            }
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    private void screenshot(){

        mBaiduMap.snapshot(new BaiduMap.SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap bitmap) {

                LYLocationActivity.resultLocation.setImageBitmap(bitmap);

                String tempName = DLDateUtils.getCurrentTimeInString()+".jpg";
                File tempFile = new File(DLFolderManager.getTempFolder(),tempName);

                DLImageUtil.saveBitmapFile(tempFile.getAbsolutePath(), bitmap);
                LYLocationActivity.resultLocation.setImagePath(tempFile.getAbsolutePath());

                LyImEngine.getInstance().hideWaitDialog();

                setResult(LYLocationActivity.RESULT_CODE_LOCATION);

                finish();
            }
        });

    }


   /* //截图功能
   	private Bitmap screenshot(){
   		Bitmap b = null;
   		int viewWidth = mMapView.getMeasuredWidth();
   		int viewHeight = mMapView.getMeasuredHeight();
   		if (viewWidth > 0 && viewHeight > 0) {
   		    b = Bitmap.createBitmap(viewWidth, viewHeight, Config.ARGB_8888);
   		    Canvas cvs = new Canvas(b);
   		    mMapView.draw(cvs);
   		}
   		return b;
       }*/


    class ListAdapter extends BaseAdapter {

        private int checkPosition;
        List<PoiInfo> mList;

        public ListAdapter(List<PoiInfo> mList){
            this.mList = mList;
            checkPosition = 0;
        }

        /**
         * 设置第几个item被选择
         * @param checkPosition
         */

        public void setCheckposition(int checkPosition){
            this.checkPosition = checkPosition;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if(convertView == null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(LYLocationActivity.this).inflate(R.layout.adapter_locationitem, null);

                holder.textView = (TextView) convertView.findViewById(R.id.text_name);
                holder.textAddress = (TextView) convertView.findViewById(R.id.text_address);
                holder.imageLl = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);

            }else{
                holder = (ViewHolder)convertView.getTag();
            }
            Log.i("mybaidumap", "name地址是："+mList.get(position).name);
            Log.i("mybaidumap", "address地址是："+mList.get(position).address);

            holder.textView.setText(mList.get(position).name);
            holder.textAddress.setText(mList.get(position).address);
            if(checkPosition == position){
                holder.imageLl.setVisibility(View.VISIBLE);
            }else{
                holder.imageLl.setVisibility(View.GONE);
            }

            return convertView;
        }

        class ViewHolder{
            TextView textView;
            TextView textAddress;
            ImageView imageLl;
        }
    }



    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocationClient != null) {
            mLocationClient.stop();
        }

        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mPoiSearch.destroy();
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult arg0) {
        // TODO Auto-generated method stub

    }
}
