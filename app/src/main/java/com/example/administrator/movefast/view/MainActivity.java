package com.example.administrator.movefast.view;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.adapter.MainAdapter;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.entity.WayBill;
import com.example.administrator.movefast.greendao.UserDao;
import com.example.administrator.movefast.greendao.WayBillDao;
import com.example.administrator.movefast.qrcode.activity.CaptureActivity;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.utils.PermissionUtility;
import com.example.administrator.movefast.view.fragment.MapActivity1;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private RadioButton rbCreateQr;
    private RadioButton rbSaoQr;
    private RadioButton rbHistory;
    private RadioButton rbUser;
    private RadioButton rbMap;

    private ListView lvMain;
    private User currentLoginUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();

        initView();
        getLoginUser();
        initEvent();
    }

    private void getLoginUser() {
        Observable<List<User>> observable = Observable.create(new ObservableOnSubscribe<List<User>>() {
            @Override
            public void subscribe(ObservableEmitter<List<User>> e) throws Exception {
                // 进行数据库查询  （查询数据库是费时操作，放到其他线程）
                List<User> list = DbManager.getDaoSession(MainActivity.this).getUserDao().queryBuilder().where(UserDao.Properties.Is_login.eq(1)).limit(1).list();
                e.onNext(list);
            }
        });

        Consumer<List<User>> consumer = new Consumer<List<User>>() {
            @Override
            public void accept(List<User> list) throws Exception {
                if (list.size()>0){
                    initData(list.get(0));
                    currentLoginUser = list.get(0);
                }else {
                    Helper.showToast("登录失败，请重新登录！");
                }

            }
        };

        observable.subscribeOn(Schedulers.io())  //被观察者执行的线程
                .observeOn(AndroidSchedulers.mainThread())  //观察者执行的线程
                .subscribe(consumer);
    }

    /**
     * 从数据库查数据
     */
    private void initData(final User user) {
        Observable<List<WayBill>> observable = Observable.create(new ObservableOnSubscribe<List<WayBill>>() {
            @Override
            public void subscribe(ObservableEmitter<List<WayBill>> e) throws Exception {
                // 进行数据库查询  （查询数据库是费时操作，放到其他线程）
                List<WayBill> list = DbManager.getDaoSession(MainActivity.this).getWayBillDao().queryBuilder()
                        // .where(WayBillDao.Properties.Account.eq("123"))
                        .where(WayBillDao.Properties.Account.eq(user.getAccount()))
                        .orderDesc(WayBillDao.Properties.Id).limit(8)
                        .build()
                        .list();
                e.onNext(list);
            }
        });

        Consumer<List<WayBill>> consumer = new Consumer<List<WayBill>>() {
            @Override
            public void accept(final List<WayBill> list) throws Exception {
                lvMain.setAdapter(new MainAdapter(MainActivity.this, list));
                lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                        PermissionUtility.getRxPermission(MainActivity.this)
                                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE) //申请定位权限
                                .subscribe(new Consumer<Boolean>() {
                                    @Override
                                    public void accept(Boolean granted) throws Exception {
                                        if (granted) {
                                            Intent intent = new Intent(MainActivity.this, MapActivity.class);
                                           // intent.putExtra("destination", list.get(position - 1).getAddress());
                                            intent.putExtra("data", list.get(position - 1));
                                            startActivity(intent);
                                        } else {
                                            Helper.showToast("请开启定位权限");
                                        }
                                    }
                                });
                    }
                });
            }
        };

        observable.subscribeOn(Schedulers.io())  //被观察者执行的线程
                .observeOn(AndroidSchedulers.mainThread())  //观察者执行的线程
                .subscribe(consumer);

    }

    private void initView() {
        lvMain = findViewById(R.id.main_list);
        lvMain.addHeaderView(getHeader());


    }

    private void initEvent() {
        rbCreateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateQRActivity.class));
            }
        });

        rbSaoQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtility.getRxPermission(MainActivity.this)
                        .request(Manifest.permission.CAMERA) //调用相机权限
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean granted) throws Exception {
                                if (granted) {
                                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                                    startActivityForResult(intent, 99);
                                }
                            }
                        });
            }
        });

        rbHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });

        rbMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionUtility.getRxPermission(MainActivity.this)
                        .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE) //申请定位权限
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean granted) throws Exception {
                                if (granted) {
                                    startActivity(new Intent(MainActivity.this, MapActivity1.class));
                                } else {
                                    Helper.showToast("请开启定位权限");
                                }
                            }
                        });
            }
        });

        rbUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLoginUser != null){
                    Intent intent = new Intent(MainActivity.this, MoreActivity.class);
                    intent.putExtra("user",currentLoginUser);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {  //传回扫描二维码中的信息
            try {
                String s = data.getExtras().getString("result");
                String[] arr = s.split(":");
                Helper.showToast(s);
            } catch (Exception e) {
                Helper.showToast("获取物流信息失败!");
            }
        }

    }

    /**
     * 设置列表头部
     *
     * @return
     */
    private View getHeader() {
        View header = View.inflate(this, R.layout.list_header, null);
        rbCreateQr = header.findViewById(R.id.rb_1);
        rbSaoQr = header.findViewById(R.id.rb_2);
        rbHistory = header.findViewById(R.id.rb_3);
        rbMap = header.findViewById(R.id.rb_4);
        rbUser = header.findViewById(R.id.rb_8);
        return header;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getLoginUser();
    }
}
