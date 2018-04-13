package com.example.administrator.movefast.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.adapter.MainAdapter;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.WayBill;
import com.example.administrator.movefast.greendao.WayBillDao;
import com.example.administrator.movefast.widget.TopBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().hide();

        initData();
        initView();
        initEvent();
    }

    private void initData() {
        Observable<List<WayBill>> observable = Observable.create(new ObservableOnSubscribe<List<WayBill>>() {
            @Override
            public void subscribe(ObservableEmitter<List<WayBill>> e) throws Exception {
                // 进行数据库查询  （查询数据库是费时操作，放到其他线程）
                List<WayBill> list = DbManager.getDaoSession(HistoryActivity.this).getWayBillDao().queryBuilder().list();
                e.onNext(list);
            }
        });

        Consumer<List<WayBill>> consumer = new Consumer<List<WayBill>>() {
            @Override
            public void accept(List<WayBill> list) throws Exception {
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
        topBar.setTitle("历史定单");
    }

    private void initEvent() {
    }

}
