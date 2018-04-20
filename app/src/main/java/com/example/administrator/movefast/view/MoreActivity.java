package com.example.administrator.movefast.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.event.UpDateUserEvent;
import com.example.administrator.movefast.greendao.UserDao;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.widget.TopBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoreActivity extends AppCompatActivity {
    
    private TopBar top;
    private RelativeLayout rlSetting;
    private RelativeLayout rlUser;
    private TextView tvQuit;
    private TextView tvName;
    private ImageView ivSex;

    private User currentLoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        getSupportActionBar().hide();

        initData();
        initView();
        setData();
        initEvent();
    }


    private void initData(){
        currentLoginUser = getIntent().getParcelableExtra("user");
    }

    private void initView() {
        top = findViewById(R.id.top);
        top.setTitle("更多");
        rlSetting = findViewById(R.id.rl_setting);
        rlUser = findViewById(R.id.rl_user);
        tvQuit = findViewById(R.id.tv_quit);
        tvName = findViewById(R.id.tv_account);
        ivSex = findViewById(R.id.iv_sex);

    }

    private void setData(){
        if (!currentLoginUser.getName().toString().equals("")){
            tvName.setText(currentLoginUser.getName());
        }

        if (currentLoginUser.getSex() == 1){
            ivSex.setBackgroundResource(R.drawable.man);
        }else if (currentLoginUser.getSex() == 2){
            ivSex.setBackgroundResource(R.drawable.woman);
        }



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

        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLoginUser != null){
                    Intent intent = new Intent(MoreActivity.this, MoreActivity.class);
                    intent.putExtra("user",currentLoginUser);
                    startActivity(intent);
                }
            }
        });

        tvQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        rlUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLoginUser != null){
                    Intent intent = new Intent(MoreActivity.this, UserActivity.class);
                    intent.putExtra("user",currentLoginUser);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 退出操作
     */
    private void logout() {
        Observable<List<User>> observable = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                // 进行数据库查询  （查询数据库是费时操作，放到其他线程）
                List<User> list = DbManager.getDaoSession(MoreActivity.this).getUserDao().queryBuilder().where(UserDao.Properties.Is_login.eq(1)).limit(1).list();
                e.onNext(list);
            }
        });

        Consumer<List<User>> consumer = new Consumer<List<User>>() {
            @Override
            public void accept(List<User> list) throws Exception {
                if (list.size() > 0) {
                    User user = list.get(0);
                    user.setIs_login(0);
                    DbManager.getDaoSession(MoreActivity.this).getUserDao().update(user);
                    startActivity(new Intent(MoreActivity.this,LoginActivity.class));
                    Helper.showToast("成功退出登录！");
                } else {
                    Helper.showToast("您还没有登录！");
                }

            }
        };

        observable.subscribeOn(Schedulers.io())  //被观察者执行的线程
                .observeOn(AndroidSchedulers.mainThread())  //观察者执行的线程
                .subscribe(consumer);
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
