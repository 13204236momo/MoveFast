package com.example.administrator.movefast.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.greendao.UserDao;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();

        init();
    }

    private void init() {
        Observable<List<User>> observable = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                // 进行数据库查询  （查询数据库是费时操作，放到其他线程）
                List<User> list = DbManager.getDaoSession(SplashActivity.this).getUserDao().queryBuilder().where(UserDao.Properties.Is_login.eq(1)).limit(1).list();
                e.onNext(list);
            }
        });

        Consumer<List<User>> consumer = new Consumer<List<User>>() {
            @Override
            public void accept(List<User> list) throws Exception {
                if (list.size() > 0) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                finish();

            }
        };

        observable.subscribeOn(Schedulers.io())  //被观察者执行的线程
                .observeOn(AndroidSchedulers.mainThread())  //观察者执行的线程
                .subscribe(consumer);



    }
}
