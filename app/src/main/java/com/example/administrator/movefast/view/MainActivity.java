package com.example.administrator.movefast.view;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.adapter.MainAdapter;
import com.example.administrator.movefast.qrcode.activity.CaptureActivity;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.utils.PermissionUtility;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;

public class MainActivity extends AppCompatActivity {

    private RadioButton rbCreateQr;
    private RadioButton rbSaoQr;

    private ListView lvMain;
    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        initData();
        initView();
        initEvent();
    }


    private void initData() {
        data.add("北京");
        data.add("上海");
        data.add("广州");
        data.add("深圳");
        data.add("厦门");
        data.add("天津");
        data.add("石家庄");
        data.add("西安");
        data.add("沈阳");
        data.add("青岛");
        data.add("大连");
        data.add("郑州");
        data.add("银川");
    }

    private void initView() {
        lvMain = findViewById(R.id.main_list);
        lvMain.addHeaderView(getHeader());
        lvMain.setAdapter(new MainAdapter(this, data));
    }

    private void initEvent() {
        rbCreateQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CreateQRActivity.class));
            }
        });

        rbSaoQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PermissionUtility.getRxPermission(MainActivity.this)
//                        .request(Manifest.permission.READ_EXTERNAL_STORAGE) //读取外部存储权限
//                        .subscribe(new Action<>() {
//                            @Override
//                            public void run() throws Exception {
//
//                            }
//                        });
//                        .subscribe(new Action<Boolean>() {
//                            @Override
//                            public void call(Boolean granted) {
//                                if (granted) {
//
//                                }
//                            }
//                        });

                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 99);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99) {  //传回扫描二维码中的信息
            try {
                String s = data.getExtras().getString("result");
                String[] arr = s.split(":");
                Helper.showToast(s);
            } catch (Exception e) {
                Helper.showToast("获取物流信息失败!");
            }
        }

    }

    /**
     * 设置列表头部
     *
     * @return
     */
    public View getHeader() {
        View header = View.inflate(this, R.layout.list_header, null);
        rbCreateQr = header.findViewById(R.id.rb_1);
        rbSaoQr = header.findViewById(R.id.rb_2);
        return header;
    }
}
