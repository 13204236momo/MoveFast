package com.example.administrator.movefast.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2018/4/11 0011.
 * 运单实体类
 */

@Entity
public class WayBill {
    @Id(autoincrement = true)
    private Long id;

    /**
     * 收件人姓名
     */
    @NotNull
    private String name;

    /**
     * 收件人地址
     */
    @NotNull
    private String address;

    /**
     * 收件人手机号
     */
    @NotNull
    private String phone;

    /**
     * 运费
     */
    @NotNull
    private String price;

    /**
     * 运单号
     */
    @NotNull
    @Unique
    private String track_num;

    /**
     * 创建时间
     */
    @NotNull
    private String create_time;

    /**
     * 是否派件完成 （0：未完成  1：已完成）
     */
    private int is_end;



    @Generated(hash = 515627515)
    public WayBill(Long id, @NotNull String name, @NotNull String address,
            @NotNull String phone, @NotNull String price, @NotNull String track_num,
            @NotNull String create_time, int is_end) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.price = price;
        this.track_num = track_num;
        this.create_time = create_time;
        this.is_end = is_end;
    }

    public WayBill( @NotNull String name, @NotNull String address,
                   @NotNull String phone, @NotNull String price, @NotNull String track_num,
                   @NotNull String create_time,@NotNull int is_end) {

        this.name = name;
        this.address = address;
        this.phone = phone;
        this.price = price;
        this.track_num = track_num;
        this.create_time = create_time;
        this.is_end = is_end;
    }

    @Generated(hash = 257623155)
    public WayBill() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTrack_num() {
        return this.track_num;
    }

    public void setTrack_num(String track_num) {
        this.track_num = track_num;
    }

    public String getCreate_time() {
        return this.create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getIs_end() {
        return this.is_end;
    }

    public void setIs_end(int is_end) {
        this.is_end = is_end;
    }

   
}
