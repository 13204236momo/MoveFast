package com.example.administrator.movefast.view;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.adapter.MainAdapter;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.entity.WayBill;
import com.example.administrator.movefast.greendao.UserDao;
import com.example.administrator.movefast.greendao.WayBillDao;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.utils.PermissionUtility;
import com.example.administrator.movefast.widget.SearchView;
import com.example.administrator.movefast.widget.TopBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HistoryActivity extends AppCompatActivity {
    private ListView lvHistory;
    private TopBar topBar;
    private EditText etSearch;
    private TextView tvSearch;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;

    private List<WayBill> data;
    private User user;

    private int searchType = 0;  //查询方式（无条件:0、按收件人：1、按运单号：2、按手机号：3）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        initView();
        getLoginUser();
        initEvent();
    }

    private void getLoginUser() {
        Observable<List<User>> observable = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                // 进行数据库查询  （查询数据库是费时操作，放到其他线程）
                List<User> list = DbManager.getDaoSession(HistoryActivity.this).getUserDao().queryBuilder().where(UserDao.Properties.Is_login.eq(1)).limit(1).list();
                e.onNext(list);
            }
        });

        Consumer<List<User>> consumer = new Consumer<List<User>>() {
            @Override
            public void accept(List<User> list) throws Exception {
                user = list.get(0);
                initData(user);
            }
        };

        observable.subscribeOn(Schedulers.io())  //被观察者执行的线程
                .observeOn(AndroidSchedulers.mainThread())  //观察者执行的线程
                .subscribe(consumer);
    }


    private void initData(final User user) {
        Observable<List<WayBill>> observable = Observable.create(new ObservableOnSubscribe<List<WayBill>>() {
            @Override
            public void subscribe(ObservableEmitter<List<WayBill>> e) throws Exception {
                // 进行数据库查询  （查询数据库是费时操作，放到其他线程）
                List<WayBill> list = new ArrayList<>();
                switch (searchType){
                    case 0:
                        list = DbManager.getDaoSession(HistoryActivity.this).getWayBillDao().queryBuilder()
                                .where(WayBillDao.Properties.Account.eq(user.getAccount()))
                                .orderDesc(WayBillDao.Properties.Id)
                                .build()
                                .list();
                        break;
                    case 1:
                        String na = etSearch.getText().toString().trim();
                        list = DbManager.getDaoSession(HistoryActivity.this).getWayBillDao().queryBuilder()
                                .where(WayBillDao.Properties.Account.eq(user.getAccount()),WayBillDao.Properties.Name.eq(na))
                                .orderDesc(WayBillDao.Properties.Id)
                                .build()
                                .list();
                        break;
                    case 2:
                        list = DbManager.getDaoSession(HistoryActivity.this).getWayBillDao().queryBuilder()
                                .where(WayBillDao.Properties.Account.eq(user.getAccount()),WayBillDao.Properties.Track_num.eq(etSearch.getText().toString().trim()))
                                .orderDesc(WayBillDao.Properties.Id)
                                .build()
                                .list();
                        break;
                    case 3:

                        list = DbManager.getDaoSession(HistoryActivity.this).getWayBillDao().queryBuilder()
                                .where(WayBillDao.Properties.Account.eq(user.getAccount()),WayBillDao.Properties.Phone.eq(etSearch.getText().toString().trim()))
                                .orderDesc(WayBillDao.Properties.Id)
                                .build()
                                .list();
                        break;
                    case 4:
                        list = DbManager.getDaoSession(HistoryActivity.this).getWayBillDao().queryBuilder()
                                .where(WayBillDao.Properties.Account.eq(user.getAccount()),WayBillDao.Properties.Create_time.like(etSearch.getText().toString().trim()+"%"))
                                .orderDesc(WayBillDao.Properties.Id)
                                .build()
                                .list();
                        break;

                }

                e.onNext(list);
            }
        });

        Consumer<List<WayBill>> consumer = new Consumer<List<WayBill>>() {
            @Override
            public void accept(List<WayBill> list) throws Exception {
                data = list;
                lvHistory.setAdapter(new MainAdapter(HistoryActivity.this, list));

            }
        };

        observable.subscribeOn(Schedulers.io())  //被观察者执行的线程
                .observeOn(AndroidSchedulers.mainThread())  //观察者执行的线程
                .subscribe(consumer);
    }

    private void initView() {
        lvHistory = findViewById(R.id.lv_history);
        topBar = findViewById(R.id.topbar);
        etSearch = findViewById(R.id.et_search);
        tvSearch = findViewById(R.id.tv_search);
        rb1 = findViewById(R.id.rb_1);
        rb2 = findViewById(R.id.rb_2);
        rb3 = findViewById(R.id.rb_3);
        rb4 = findViewById(R.id.rb_4);
        topBar.setTitle("历史定单");
    }

    private void initEvent() {
        topBar.setOnTopClickListener(new TopBar.OnTopClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

        lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        PermissionUtility.getRxPermission(HistoryActivity.this)
                                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE) //申请定位权限
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean granted) throws Exception {
                                        if (granted) {
                                            Intent intent = new Intent(HistoryActivity.this, MapActivity.class);
                                            intent.putExtra("data", data.get(position - 1));
                                            startActivity(intent);
                                        } else {
                                            Helper.showToast("请开启定位权限");
                                        }
                                    }
                                });
                    }
                });
            }
        });


        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb1.isChecked()){
                    searchType = 1;
                }else if (rb2.isChecked()){
                    searchType = 2;
                }else if (rb3.isChecked()){
                    searchType = 3;
                }else if (rb4.isChecked()){
                    searchType = 4;
                }else {
                    searchType = 0;
                }
                initData(user);
            }
        });
    }

}
