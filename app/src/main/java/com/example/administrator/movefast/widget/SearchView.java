package com.example.administrator.movefast.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.movefast.R;


/**
 * Created by DN on 2018/03/12.
 */

public class SearchView extends LinearLayout{
    private EditText edSearch;
    private TextView tvSearch;



    private Context context;
    public SearchView(Context context) {
        this(context,null);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();


    }

    private void initView() {
       // LayoutInflater.from(context).inflate(R.layout.view_search, null, false);
        View.inflate(context, R.layout.view_search, this);
        edSearch = findViewById(R.id.et_search);
        tvSearch = findViewById(R.id.tv_search);
    }


}
