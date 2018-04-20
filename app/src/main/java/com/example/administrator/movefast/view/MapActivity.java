package com.example.administrator.movefast.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.example.administrator.movefast.R;
import com.example.administrator.movefast.view.controller.MapController;
import com.example.administrator.movefast.widget.TopBar;

public class MapActivity extends AppCompatActivity {

    private MapView mMapView;
    private AMap aMap;
    private MapController mapController;
    private TopBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().hide();

        initMap(savedInstanceState);
        initView();
        initEvent();
    }

    private void initView() {
        topBar = findViewById(R.id.topbar);
        topBar.setTitle("线路规划");
    }

    private void initEvent() {
        topBar.setOnTopClickListener(new TopBar.OnTopClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }

    private void initMap(Bundle savedInstanceState) {
        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null){
            aMap = mMapView.getMap();
        }
        //地图设置相关
        mapController = new MapController(this,aMap);

    }


    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        if(null != mapController.mLocationClient){
            mapController.mLocationClient.onDestroy();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
}
