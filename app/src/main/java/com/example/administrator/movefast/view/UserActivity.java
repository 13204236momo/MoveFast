package com.example.administrator.movefast.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.widget.TopBar;

public class UserActivity extends AppCompatActivity {
    
    private TopBar top;
    private RelativeLayout rlSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().hide();
        initView();
        initEvent();
    }


    private void initView() {
        top = findViewById(R.id.top);
        top.setTitle("个人中心");
        rlSetting = findViewById(R.id.rl_setting);
    }

    private void initEvent() {
        top.setOnTopClickListener(new TopBar.OnTopClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });

        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this,SettingActivity.class));
            }
        });
    }

}
