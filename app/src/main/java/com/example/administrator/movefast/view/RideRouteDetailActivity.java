package com.example.administrator.movefast.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.route.RidePath;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.adapter.RideSegmentListAdapter;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.WayBill;
import com.example.administrator.movefast.utils.AMapUtil;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.utils.PermissionUtility;
import com.example.administrator.movefast.widget.TopBar;

import io.reactivex.functions.Consumer;

/**
 * 骑行路线详情
 */
public class RideRouteDetailActivity extends Activity {

    private TextView mTitleWalkRoute;
    private TextView tvOrder;
    private ListView mRideSegmentList;
    private TopBar topBar;
    private ImageView ivState;
    private TextView tvContact;
    private TextView tvPhone;
    private ImageView ivPhone;

    private RidePath mRidePath;
    private RideSegmentListAdapter mRideSegmentListAdapter;
    private String destination;
    private WayBill data;

    private String phone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);
        getIntentData();
        initView();
        initEvent();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mRidePath = intent.getParcelableExtra("ride_path");
        data = intent.getParcelableExtra("data");
        destination = data.getAddress();
        phone = data.getPhone();
    }

    private void initView() {
        topBar = findViewById(R.id.top);
        mTitleWalkRoute = findViewById(R.id.firstline);
        tvOrder = findViewById(R.id.secondline);
        mRideSegmentList = findViewById(R.id.bus_segment_list);
        ivState = findViewById(R.id.iv_state);


        topBar.setTitle("骑行路线详情");
        String dur = AMapUtil.getFriendlyTime((int) mRidePath.getDuration());
        String dis = AMapUtil.getFriendlyLength((int) mRidePath.getDistance());
        mTitleWalkRoute.setText(dur + "(" + dis + ")");
        tvOrder.setText(destination);
        if (data.getIs_end() == 0) {
            ivState.setBackgroundResource(R.drawable.no_finish);
        } else {
            ivState.setBackgroundResource(R.drawable.finish);
        }

        mRideSegmentListAdapter = new RideSegmentListAdapter(this, mRidePath.getSteps());
        mRideSegmentList.setAdapter(mRideSegmentListAdapter);
        mRideSegmentList.addFooterView(getFooter());
    }

    private View getFooter() {
        View footer = View.inflate(this,R.layout.detail_footer,null);
        tvContact = footer.findViewById(R.id.tv_contacts);
        tvPhone = footer.findViewById(R.id.tv_phone);
        ivPhone = footer.findViewById(R.id.iv_phone);

        tvContact.setText(data.getName());
        tvPhone.setText(phone);
        return footer;
    }

    private void initEvent() {
        topBar.setOnTopClickListener(new TopBar.OnTopClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });

        ivState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getIs_end() == 0){
                    ivState.setBackgroundResource(R.drawable.finish);
                    data.setIs_end(1);
                }else if (data.getIs_end() == 1){
                    ivState.setBackgroundResource(R.drawable.no_finish);
                    data.setIs_end(0);
                }
                upData(data);
            }
        });

        ivPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PermissionUtility.getRxPermission(RideRouteDetailActivity.this)
                        .request(Manifest.permission.CALL_PHONE) //申请打电话权限
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean granted) throws Exception {
                                if (granted) {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    Uri data = Uri.parse("tel:" + phone);
                                    intent.setData(data);
                                    startActivity(intent);
                                } else {
                                    Helper.showToast("请开启定位权限");
                                }
                            }
                        });
            }
        });


    }

    /**
     * 更新数据库
     * @param data
     */
    private void upData(WayBill data) {
        DbManager.getDaoSession(RideRouteDetailActivity.this).getWayBillDao().update(data);
            Helper.showToast("登录成功");
    }


}
