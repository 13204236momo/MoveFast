package com.example.administrator.movefast.event;

import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.event.base.BaseEvent;

/**
 * Created by Administrator on 2018/4/20 0020.
 */

public class UpDateUserEvent extends BaseEvent{
    public User user;

    public UpDateUserEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
