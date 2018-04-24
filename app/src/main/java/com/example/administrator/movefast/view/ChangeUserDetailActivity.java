package com.example.administrator.movefast.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.event.UpDateUserEvent;
import com.example.administrator.movefast.event.base.EventCenter;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.widget.TopBar;

public class ChangeUserDetailActivity extends AppCompatActivity {

    private TopBar topBar;
    private EditText etContent;

    private int type;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_detail);
        getSupportActionBar().hide();

        initView();
        initData();
        initEvent();

    }

    private void initView() {
        topBar = findViewById(R.id.topbar);
        etContent = findViewById(R.id.content);
    }

    private void initData() {
        type = getIntent().getIntExtra("type", -1);
        currentUser = getIntent().getParcelableExtra("user");
        topBar.setRightVisibility();


        switch (type) {
            case 0:
                topBar.setTitle("修改昵称");
                etContent.setHint(currentUser.getName());
                break;
            case 1:
                topBar.setTitle("修改签名");
                etContent.setHint(currentUser.getSign());
                break;
            case 2:
                topBar.setTitle("修改手机号");
                etContent.setHint(currentUser.getPhone());
                break;
            case 3:
                topBar.setTitle("修改所在地");
                etContent.setHint(currentUser.getCurrent_address());
                break;
            case 4:
                topBar.setTitle("修改性别");
                if (currentUser.getSex() == 0){
                    etContent.setHint("保密");
                }else if (currentUser.getSex() == 1){
                    etContent.setHint("男");
                }else if (currentUser.getSex() == 2){
                    etContent.setHint("女");
                }

                break;
            default:
                topBar.setTitle("标题");
                break;
        }

    }

    private void initEvent() {
        topBar.setOnTopClickListener(new TopBar.OnTopClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                commit();
                finish();
            }
        });
    }

    private void commit() {
        int sex;
        switch (type) {
            case 0:
                if (etContent.getText().toString().length() > 10) {
                    Helper.showToast("昵称最多10个字");
                    return;
                }
                currentUser.setName(etContent.getText().toString().trim());
                break;
            case 1:
                if (etContent.getText().toString().length() > 70) {
                    Helper.showToast("签名最多70个字");
                    return;
                }
                currentUser.setSign(etContent.getText().toString().trim());
                break;
            case 2:
                if (!Helper.isMobile(etContent.getText().toString().trim())) {
                    Helper.showToast("手机号格式错误！");
                    return;
                }
                currentUser.setPhone(etContent.getText().toString().trim());
                break;
            case 3:
                if (etContent.getText().toString().length() > 70) {
                    Helper.showToast("签名最多70个字");
                    return;
                }
                currentUser.setCurrent_address(etContent.getText().toString().trim());
                break;
            case 4:
                if (etContent.getText().toString().trim().equals("男")) {
                    sex = 1;
                } else if (etContent.getText().toString().trim().equals("女")) {
                    sex = 2;
                } else if (etContent.getText().toString().trim().equals("保密")) {
                    sex = 0;
                } else {
                    Helper.showToast("男 / 女 / 保密");
                    return;
                }
                currentUser.setSex(sex);
                break;
            default:

                break;
        }

        DbManager.getDaoSession(this).getUserDao().update(currentUser);
        EventCenter.post(new UpDateUserEvent(currentUser));
    }
}
