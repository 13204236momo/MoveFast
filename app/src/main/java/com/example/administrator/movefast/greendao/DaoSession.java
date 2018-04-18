package com.example.administrator.movefast.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.entity.WayBill;

import com.example.administrator.movefast.greendao.UserDao;
import com.example.administrator.movefast.greendao.WayBillDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig wayBillDaoConfig;

    private final UserDao userDao;
    private final WayBillDao wayBillDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        wayBillDaoConfig = daoConfigMap.get(WayBillDao.class).clone();
        wayBillDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        wayBillDao = new WayBillDao(wayBillDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(WayBill.class, wayBillDao);
    }
    
    public void clear() {
        userDaoConfig.clearIdentityScope();
        wayBillDaoConfig.clearIdentityScope();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public WayBillDao getWayBillDao() {
        return wayBillDao;
    }

}
