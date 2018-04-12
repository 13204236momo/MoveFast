package com.example.administrator.movefast.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.movefast.R;


/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class TopBar extends LinearLayout {
    private Context context;
    private TextView tvTitle;
    private TextView tvBack;
    private OnTopClickListener listener;
    public TopBar(Context context) {
        this(context,null);
    }

    public TopBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
        initEvent();
    }

    private void initView() {
        View.inflate(context, R.layout.view_topbar, this);
        tvTitle = findViewById(R.id.tv_title);
        tvBack = findViewById(R.id.tv_back);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    private void initEvent() {
        tvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.finish();
                if (listener != null){
                    listener.onLeftClick();
                }
            }
        });
    }

    public interface OnTopClickListener{
        void onLeftClick();
    }

    public void setOnTopClickListener(OnTopClickListener l){
        this.listener = l;
    }

}
