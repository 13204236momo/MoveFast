package com.example.administrator.movefast.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Entity
public class User {
    @Id(autoincrement = true)
    private Long id;

    /**
     * 账号
     */
    @NotNull
    private String account;

    /**
     * 密码
     */
    @NotNull
    private String pass_word;

    /**
     * 是否已登录 (0:未登录  1：已登录)
     */
    @NotNull
    private int is_login;

    @Generated(hash = 947504915)
    public User(Long id, @NotNull String account, @NotNull String pass_word,
            int is_login) {
        this.id = id;
        this.account = account;
        this.pass_word = pass_word;
        this.is_login = is_login;
    }

    @Generated(hash = 586692638)
    public User( ) {
    }

    public User( @NotNull String account, @NotNull String pass_word,@NotNull int is_login) {
        this.account = account;
        this.pass_word = pass_word;
        this.is_login = is_login;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPass_word() {
        return this.pass_word;
    }

    public void setPass_word(String pass_word) {
        this.pass_word = pass_word;
    }

    public int getIs_login() {
        return this.is_login;
    }

    public void setIs_login(int is_login) {
        this.is_login = is_login;
    }

}
