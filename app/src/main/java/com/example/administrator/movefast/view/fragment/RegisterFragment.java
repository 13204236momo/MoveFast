package com.example.administrator.movefast.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.view.LoginActivity;

/**
 * Created by Administrator on 2018/4/13 0013.
 */

public class RegisterFragment extends Fragment {
    private ImageView ivBack;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etPasswordAgain;
    private TextView tvFinish;

    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initView();
        initEvent();
    }

    private void initView() {
        ivBack = getActivity().findViewById(R.id.iv_back);
        etAccount = getActivity().findViewById(R.id.et_input_account);
        etPassword = getActivity().findViewById(R.id.et_input_pw);
        etPasswordAgain = getActivity().findViewById(R.id.et_again_pw);
        tvFinish = getActivity().findViewById(R.id.tv_finish);
    }

    private void initEvent() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity)getActivity()).showFragment(1);
            }
        });

        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
    }

    private void check(){
        if (etAccount.getText().toString().equals("")){
            Helper.showToast("用户名不能为空！");
            return;
        }else if (etPassword.getText().toString().equals("")){
            Helper.showToast("密码不能为空！");
            return;
        }else if (etPasswordAgain.getText().toString().equals("")){
            Helper.showToast("确认密码不能为空！");
            return;
        }

        if (!etPassword.getText().toString().equals(etPasswordAgain.getText().toString())){
            Helper.showToast("输入的密码不一致！");
            return;
        }

        //插入数据库
        user = new User(etAccount.getText().toString(),etPassword.getText().toString(),0,"",0,"","","");
        DbManager.getDaoSession(getActivity()).getUserDao().insert(user);
        Helper.HideKeyboard(tvFinish);
        ((LoginActivity)getActivity()).showFragment(1);
        Helper.showToast("注册成功！");
    }


}
