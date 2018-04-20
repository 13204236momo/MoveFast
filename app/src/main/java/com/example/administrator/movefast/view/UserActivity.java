package com.example.administrator.movefast.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.event.UpDateUserEvent;
import com.example.administrator.movefast.event.base.EventCenter;
import com.example.administrator.movefast.utils.DataCenter;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.widget.TopBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int NAME = 0;
    public static final int SIGN = 1;
    public static final int PHONE = 2;
    public static final int ADRESS = 3;

    private TextView tvName;
    private TextView tvSign;
    private TextView tvSex;
    private TextView tvPhone;
    private TextView tvAddress;

    private RelativeLayout rlName;
    private RelativeLayout rlSign;
    private RelativeLayout rlSex;
    private RelativeLayout rlPhone;
    private RelativeLayout rlAddress;
    private TopBar top;

    private User currentLoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().hide();

        initData();
        initView();
        setData();
        initEvent();
    }

    private void initData() {
        EventCenter.register(this);
        currentLoginUser = getIntent().getParcelableExtra("user");
    }

    private void setData() {
        top.setTitle("个人资料");
        if (currentLoginUser != null) {
            tvName.setText(currentLoginUser.getName());
            tvSign.setText(currentLoginUser.getSign());
            if (currentLoginUser.getSex() == 0) {
                tvSex.setText("保密");
            } else if (currentLoginUser.getSex() == 1) {
                tvSex.setText("男");
            } else if (currentLoginUser.getSex() == 21) {
                tvSex.setText("女");
            }

            tvPhone.setText(currentLoginUser.getPhone());
            tvAddress.setText(currentLoginUser.getCurrent_address());
        }
    }

    private void initView() {
        top = findViewById(R.id.topbar);
        tvName = findViewById(R.id.tv_name);
        tvSign = findViewById(R.id.tv_sign);
        tvSex = findViewById(R.id.tv_sex);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);

        rlName = findViewById(R.id.rl_name);
        rlSign = findViewById(R.id.rl_sign);
        rlSex = findViewById(R.id.rl_sex);
        rlPhone = findViewById(R.id.rl_phone);
        rlAddress = findViewById(R.id.rl_address);

    }


    private void initEvent() {
        top.setOnTopClickListener(new TopBar.OnTopClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

        rlName.setOnClickListener(this);
        rlSign.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_name:
                changeDetails(0);
                break;
            case R.id.rl_sign:
                changeDetails(1);
                break;
            case R.id.rl_sex:
                break;
            case R.id.rl_phone:
                changeDetails(2);
                break;
            case R.id.rl_address:
                changeDetails(3);
                break;
            default:
                break;
        }
    }

    private void changeDetails(int type){
        Intent intent = new Intent(UserActivity.this,ChangeUserDetailActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("user",currentLoginUser);
        startActivity(intent);
    }

    private void upDate(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenter.unRegister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upDate(UpDateUserEvent event){
        currentLoginUser = event.getUser();
        if (currentLoginUser != null){
            setData();
        }else {
            Helper.showToast("123");
        }
    }
}
