package com.example.administrator.movefast.event.base;

import com.example.administrator.movefast.event.base.BaseEvent;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.utils.logger.Logger;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/4/20 0020.
 */

public class EventCenter {
    public static void post(BaseEvent event) {
        if (!Helper.isMainThread(Thread.currentThread().getName())) {
            Logger.e("EventCenter post not Main -> " + event.getClass().getSimpleName());
        }
        EventBus.getDefault().post(event);
    }

    public static void register(Object aObject) {
        EventBus.getDefault().register(aObject);
    }

    public static void unRegister(Object aObject) {
        EventBus.getDefault().unregister(aObject);
    }
}
