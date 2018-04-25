package com.example.administrator.movefast.widget;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.utils.Helper;

/**
 * Created by Administrator on 2018/4/25 0025.
 */

public class ToastDialog extends Dialog {
    private Context context;
    private TextView tvMessage;
    private TextView tvCopy;

    private String message;
    public ToastDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_dialog);
        initView();
        initEvent();
    }

    private void initView() {
        tvMessage = findViewById(R.id.tv_message);
        tvCopy = findViewById(R.id.tv_copy);

        tvMessage.setText(message);
    }

    public void setMessage(String s){
        message = s;
    }

    private void initEvent() {
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(tvMessage.getText());
                Helper.showToast("复制成功");
                dismiss();
            }
        });
    }
}
