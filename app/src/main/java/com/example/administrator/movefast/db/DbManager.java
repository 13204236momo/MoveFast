package com.example.administrator.movefast.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.movefast.greendao.DaoMaster;
import com.example.administrator.movefast.greendao.DaoSession;


/**
 * Created by Administrator on 2018/3/28 0028.
 */

public class DbManager {

    //是否加密
    public static final boolean ENCRYPTED = true;
    //数据库名
    private static final String DB_NAME = "test.db";
    private static DbManager mDbManager;
    private static DaoMaster.DevOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private Context mContext;

    private DbManager(Context context) {
        this.mContext = context;

        //初始化数据库信息
        mDevOpenHelper = new DaoMaster.DevOpenHelper(mContext, DB_NAME);
        getDaoMaster(mContext);
        getDaoSession(mContext);
    }

    /**
     * 获取可读数据库
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase getReadableDatabase(Context context) {
        if (null == mDevOpenHelper) {
            getInstance(context);
        }
        return mDevOpenHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     *
     * @param context
     * @return
     */
    public static SQLiteDatabase getWritableDatabase(Context context) {
        if (null == mDevOpenHelper) {
            getInstance(context);
        }

        return mDevOpenHelper.getWritableDatabase();
    }

    /**
     * 获取DbManger
     *
     * @param context
     * @return
     */
    public static DbManager getInstance(Context context) {
        if (mDbManager == null) {
            synchronized (DbManager.class) {
                if (null == mDbManager) {
                    mDbManager = new DbManager(context);
                }
            }
        }
        return mDbManager;
    }

    /**
     * 获取DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (null == mDaoMaster) {
            synchronized (DaoMaster.class) {
                if (null == mDaoMaster) {
                   // mDaoMaster = new DaoMaster(getWritableDatabase(context));

                    MyOpenHelper helper = new MyOpenHelper(context,DB_NAME,null);
                    mDaoMaster = new DaoMaster(helper.getWritableDatabase());
                }
            }
        }
        return mDaoMaster;
    }

    /**
     * 获取DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (null == mDaoSession) {
            synchronized (DaoSession.class) {
                mDaoSession = getDaoMaster(context).newSession();
            }
        }
        return mDaoSession;
    }
}
