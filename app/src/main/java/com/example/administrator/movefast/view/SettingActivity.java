package com.example.administrator.movefast.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.widget.TopBar;

public class SettingActivity extends AppCompatActivity {

    private TopBar top;
    private EditText etOldPassword;
    private EditText etNewPassword;
    private EditText etRePassword;

    private User currentLoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().hide();

        initData();
        initView();
        initEvent();
    }

    private void initData(){
        currentLoginUser = getIntent().getParcelableExtra("user");
    }

    private void initView() {
        top = findViewById(R.id.top);
        etOldPassword = findViewById(R.id.old_pw);
        etNewPassword = findViewById(R.id.new_pw);
        etRePassword = findViewById(R.id.re_pw);

        top.setRightVisibility();
        top.setTitle("设置");
    }

    private void initEvent() {
        top.setOnTopClickListener(new TopBar.OnTopClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                changePassword();
            }
        });


    }

    private void changePassword() {
        if (etOldPassword.getText().toString().equals("")){
            Helper.showToast("原密码不能为空！");
            return;
        }else  if (etNewPassword.getText().toString().equals("")){
            Helper.showToast("新密码不能为空！");
            return;
        }else if (etRePassword.getText().toString().equals("")){
            Helper.showToast("确认密码不能为空！");
            return;
        }

        if (!etNewPassword.getText().toString().trim().equals(etRePassword.getText().toString().trim())){
            Helper.showToast("两次输入的密码不同，请重新输入！");
            return;
        }

        currentLoginUser.setPass_word(etRePassword.getText().toString().trim());
        DbManager.getDaoSession(this).getUserDao().update(currentLoginUser);
        Helper.showToast("修改密码成功！");

    }
}
