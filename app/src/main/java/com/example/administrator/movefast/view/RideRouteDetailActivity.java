package com.example.administrator.movefast.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.RidePath;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.adapter.RideSegmentListAdapter;
import com.example.administrator.movefast.utils.AMapUtil;
import com.example.administrator.movefast.widget.TopBar;

/**
 * 骑行路线详情
 */
public class RideRouteDetailActivity extends Activity {
	private RidePath mRidePath;
	private TextView mTitleWalkRoute;
	private ListView mRideSegmentList;
	private RideSegmentListAdapter mRideSegmentListAdapter;
	private TopBar topBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_detail);
		getIntentData();
		initView();
	}

	private void initView(){
		topBar = findViewById(R.id.top);
		mTitleWalkRoute = findViewById(R.id.firstline);
		mRideSegmentList = findViewById(R.id.bus_segment_list);
		topBar.setTitle("骑行路线详情");
		String dur = AMapUtil.getFriendlyTime((int) mRidePath.getDuration());
		String dis = AMapUtil.getFriendlyLength((int) mRidePath.getDistance());
		mTitleWalkRoute.setText(dur + "(" + dis + ")");

		mRideSegmentListAdapter = new RideSegmentListAdapter(this, mRidePath.getSteps());
		mRideSegmentList.setAdapter(mRideSegmentListAdapter);

		topBar.setOnTopClickListener(new TopBar.OnTopClickListener() {
			@Override
			public void onLeftClick() {
				finish();
			}
		});

	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		mRidePath = intent.getParcelableExtra("ride_path");
	}



}
