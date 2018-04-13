package com.example.administrator.movefast.view.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.greendao.UserDao;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.view.LoginActivity;
import com.example.administrator.movefast.view.MainActivity;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/4/13 0013.
 */

public class LoginFragment extends Fragment {

    private EditText etAccount;
    private EditText etPassword;
    private TextView tvLogin;
    private TextView tvRegister;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initView();
        initEvent();
    }

    private void initView() {
        etAccount = getActivity().findViewById(R.id.et_account);
        etPassword = getActivity().findViewById(R.id.et_pw);
        tvLogin = getActivity().findViewById(R.id.tv_login);
        tvRegister = getActivity().findViewById(R.id.tv_register);


    }

    private void initEvent() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginActivity) getActivity()).showFragment(2);
            }
        });
    }

    /**
     * 登录操作
     */
    private void login(){
        Observable<List<User>> observable = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                // 进行数据库查询  （查询数据库是费时操作，放到其他线程）
                List<User> list = DbManager.getDaoSession(getActivity()).getUserDao().queryBuilder().where(UserDao.Properties.Account.eq(etAccount.getText().toString().trim())).limit(1).list();
                e.onNext(list);
            }
        });

        Consumer<List<User>> consumer = new Consumer<List<User>>() {
            @Override
            public void accept(List<User> list) throws Exception {
                if (list.size()>0){
                    User user = list.get(0);
                    if (user.getPass_word().equals(etPassword.getText().toString().trim())){
                        user.setIs_login(1);
                        DbManager.getDaoSession(getActivity()).getUserDao().update(user);
                        Helper.showToast("登录成功");
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                }else {
                    Helper.showToast("此用户还没有注册哦！");
                }

            }
        };

        observable.subscribeOn(Schedulers.io())  //被观察者执行的线程
                .observeOn(AndroidSchedulers.mainThread())  //观察者执行的线程
                .subscribe(consumer);
    }
}
