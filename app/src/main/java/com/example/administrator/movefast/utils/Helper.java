package com.example.administrator.movefast.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class Helper {
    public static Context context;
    private static Toast mToast;

    /**
     * 全局Toast
     *
     * @param str
     */
    public static void showToast(String str) {
        if (!isMainThread(Thread.currentThread().getName())) {
            return;
        }
        if (context == null) return;
        if (TextUtils.isEmpty(str)) return;
        if (mToast == null) {
            mToast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(str);
        }
        mToast.show();
    }

    /**
     * 是否在主线程
     *
     * @param aThreadName
     * @return
     */
    public static boolean isMainThread(String aThreadName) {
        return aThreadName.equals("main");
    }

    /**
     * 获取当前系统时间
     * @return
     */
    public Long getSystemCurrentTime(){
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间  格式：yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String getCurrentDate(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            return number.matches(num);
        }
    }

    /**
     *
     * @param v
     */
    public static void HideKeyboard(View v) {
        if (v == null) return;
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int dip2px(float dipValue) {
        if (context == null) return 0;
        return dip2px(context, dipValue);
    }

    public static int getDisplayHeight(Activity activity) {
        if (activity == null) return 600;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getDisplayWidth(Activity activity) {
        if (activity == null) return 800;
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        if (context == null) return 0;
        return px2dip(context, pxValue);
    }



}
