package com.example.administrator.movefast.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/4/13 0013.
 */
@Entity
public class User implements Parcelable {
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

    /**
     * 用户名
     */
    private String name;

    /**
     * 签名
     */
    private String sign;

    /**
     * 性别 （0：保密 1：男 2：女）
     */
    private int sex;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像路径
     */

    private String head_img;

    /**
     * 现住址
     */
    private String current_address;


    @Generated(hash = 1537026936)
    public User(Long id, @NotNull String account, @NotNull String pass_word, int is_login, String name,
            String sign, int sex, String phone, String head_img, String current_address) {
        this.id = id;
        this.account = account;
        this.pass_word = pass_word;
        this.is_login = is_login;
        this.name = name;
        this.sign = sign;
        this.sex = sex;
        this.phone = phone;
        this.head_img = head_img;
        this.current_address = current_address;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public User( @NotNull String account, @NotNull String pass_word,@NotNull int is_login,
                 String name,String sign, int sex, String phone, String head_img, String current_address) {
        this.account = account;
        this.pass_word = pass_word;
        this.is_login = is_login;
        this.name = name;
        this.sign = sign;
        this.sex = sex;
        this.phone = phone;
        this.head_img = head_img;
        this.current_address = current_address;

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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHead_img() {
        return this.head_img;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public String getCurrent_address() {
        return this.current_address;
    }

    public void setCurrent_address(String current_address) {
        this.current_address = current_address;
    }

    public String getSign() {
        return this.sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.account);
        dest.writeString(this.pass_word);
        dest.writeInt(this.is_login);
        dest.writeString(this.name);
        dest.writeString(this.sign);
        dest.writeInt(this.sex);
        dest.writeString(this.phone);
        dest.writeString(this.head_img);
        dest.writeString(this.current_address);

    }


    protected User(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.account = in.readString();
        this.pass_word = in.readString();
        this.is_login = in.readInt();
        this.name = in.readString();
        this.sign = in.readString();
        this.sex = in.readInt();
        this.phone = in.readString();
        this.head_img = in.readString();
        this.current_address = in.readString();

    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
