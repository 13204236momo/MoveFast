package com.example.administrator.movefast;

import android.app.Application;
import android.content.Context;

import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.utils.Helper;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class App extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        mContext = getApplicationContext();
        Helper.context = mContext;
        //初始化数据库相关
        DbManager.getInstance(mContext);
    }
}
