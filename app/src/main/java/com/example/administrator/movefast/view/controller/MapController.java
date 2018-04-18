package com.example.administrator.movefast.view.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RidePath;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.example.administrator.movefast.R;
import com.example.administrator.movefast.overlay.RideRouteOverlay;
import com.example.administrator.movefast.utils.AMapUtil;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.view.RideRouteDetailActivity;


/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class MapController implements LocationSource, AMapLocationListener, RouteSearch.OnRouteSearchListener, GeocodeSearch.OnGeocodeSearchListener {

    //路线规划方式 ：骑车
    private final int ROUTE_TYPE_RIDE = 4;

    private LatLonPoint latLngStart;
    private LatLonPoint latLngEnd;
    private RouteSearch mRouteSearch;
    private RideRouteResult mRideRouteResult;

    private Activity context;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    public AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private OnLocationChangedListener mListener;

    //
    private GeocodeSearch geocodeSearch;

    private TextView mRouteTimeDes, mRouteDetailDes;
    private RelativeLayout mBottomLayout;


    public MapController(Activity context, AMap aMap) {
        this.context = context;
        this.aMap = aMap;

        init();
    }

    private void init() {
        initView();
        initLocation();
        getEndData();

        mRouteSearch = new RouteSearch(context);
        mRouteSearch.setRouteSearchListener(this);
    }

    private void initView() {
        mRouteTimeDes = (TextView) context.findViewById(R.id.firstline);
        mRouteDetailDes = (TextView) context.findViewById(R.id.secondline);
        mBottomLayout = (RelativeLayout) context.findViewById(R.id.bottom_layout);
    }

    /**
     * 通过订单里的收件地址查出 经纬度，从而进行路线规划
     */
    private void getEndData() {
        geocodeSearch = new GeocodeSearch(context);
        geocodeSearch.setOnGeocodeSearchListener(this);

        String destination = context.getIntent().getStringExtra("destination"); //目的地

        String city;
        if (destination.contains("省")){
             city = destination.split("市")[0].split("省")[1];
        }else {
             city = destination.split("市")[0];
        }

        GeocodeQuery query = new GeocodeQuery(destination, city);
        geocodeSearch.getFromLocationNameAsyn(query);
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        //设置定位点
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。
        myLocationStyle.showMyLocation(true);  //设置是否显示定位小蓝点
        //myLocationStyle.anchor(0, 0f);  //设置定位蓝点图标的锚点
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)); //设置定位图标
        myLocationStyle.strokeColor(R.color.colorPrimaryDark); //设置定位蓝点精度圆圈的边框颜色的方法。
        myLocationStyle.radiusFillColor(R.color.colorPrimary);  //设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.strokeWidth(0.2f); //设置定位蓝点精度圈的边框宽度的方法

        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);  ///设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setLocationSource(this);
        //获取经纬度
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

            }
        });
    }

    //LocationSource 接口方法
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(context);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mLocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    //AMapLocationListener 接口方法
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                // mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                //  aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                setFormantToMarker(aMapLocation);
                searchRouteResult(ROUTE_TYPE_RIDE, RouteSearch.RidingDefault);

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 设置起点，终点
     *
     * @param aMapLocation
     */
    private void setFormantToMarker(AMapLocation aMapLocation) {
        latLngStart = new LatLonPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude());

        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(latLngStart))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(latLngEnd))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
    }

    /**
     * 开始搜索路径规划方案
     */
    private void searchRouteResult(int routeType, int mode) {
        if (latLngStart == null) {
            Helper.showToast("定位中，稍后再试...");
            return;
        }
        if (latLngEnd == null) {
            Helper.showToast("终点未设置");
        }
        // showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                latLngStart, latLngEnd);
        if (routeType == ROUTE_TYPE_RIDE) {// 骑行路径规划
            RouteSearch.RideRouteQuery query = new RouteSearch.RideRouteQuery(fromAndTo, mode);
            mRouteSearch.calculateRideRouteAsyn(query);// 异步路径规划骑行模式查询
        }
    }


    //OnRouteSearchListener 接口方法
    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    //骑行路线规划
    @Override
    public void onRideRouteSearched(RideRouteResult result, int errorCode) {
        // dissmissProgressDialog();
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mRideRouteResult = result;
                    final RidePath ridePath = mRideRouteResult.getPaths()
                            .get(0);
                    if (ridePath == null) {
                        return;
                    }
                    RideRouteOverlay rideRouteOverlay = new RideRouteOverlay(
                            context, aMap, ridePath,
                            mRideRouteResult.getStartPos(),
                            mRideRouteResult.getTargetPos());
                    rideRouteOverlay.removeFromMap();
                    rideRouteOverlay.addToMap();
                    rideRouteOverlay.zoomToSpan();
                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) ridePath.getDistance();
                    int dur = (int) ridePath.getDuration();
                    String des = AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    mRouteTimeDes.setText(des);
                    mRouteDetailDes.setVisibility(View.GONE);
                    mBottomLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, RideRouteDetailActivity.class);
                            intent.putExtra("ride_path", ridePath);
                            intent.putExtra("ride_result", mRideRouteResult);
                            context.startActivity(intent);
                        }
                    });
                } else if (result != null && result.getPaths() == null) {
                    Helper.showToast("对不起，没有搜索到相关数据！");
                }
            } else {
                Helper.showToast("对不起，没有搜索到相关数据！");
            }
        } else {
            Helper.showToast(errorCode + "");
        }

    }

    //OnGeocodeSearchListener 接口方法
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int reCoed) {
        if (reCoed == 1000) {
            GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
            latLngEnd = address.getLatLonPoint();
        }else {
            Helper.showToast("定位不到收件地址！");
        }
    }
}
