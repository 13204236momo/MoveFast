package com.example.administrator.movefast.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.WayBill;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.utils.QRCodeUtil;
import com.example.administrator.movefast.widget.TopBar;

import java.util.UUID;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class CreateQRActivity extends AppCompatActivity {
    private TopBar topBar;
    private EditText etName,etAddress,etPhone,etPrice;
    private TextView tvNum,tvCreateTime;
    private Button btnCreate;
    private ImageView ivQr;
    private Bitmap qrBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);
        getSupportActionBar().hide();

        // initData();
        initView();
        initEvent();
    }

    private void initView() {
        topBar = findViewById(R.id.topbar);
        etName = findViewById(R.id.ed_name);
        etAddress = findViewById(R.id.ed_address);
        etPhone = findViewById(R.id.ed_phone);
        etPrice = findViewById(R.id.ed_price);
        tvNum = findViewById(R.id.tv_num);
        tvCreateTime = findViewById(R.id.tv_create_time);
        btnCreate = findViewById(R.id.btn_create);
        ivQr = findViewById(R.id.iv_qr);

        //生成唯一标识来代表运单号
        tvNum.setText( UUID.randomUUID().toString());
        tvCreateTime.setText(Helper.getCurrentDate());
        topBar.setTitle("生成二维码");
    }

    private void check(){
        if (etName.getText().toString().equals("")){
            Toast.makeText(this,"收件人姓名不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }else if (etAddress.getText().toString().equals("")){
            Toast.makeText(this,"收件人地址不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }else if (etPhone.getText().toString().equals("")){
            Toast.makeText(this,"收件人电话不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }else if (etPrice.getText().toString().equals("")){
            Toast.makeText(this,"运费不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Helper.isMobile(etPhone.getText().toString())){
            Toast.makeText(this,"手机号格式错误！",Toast.LENGTH_SHORT).show();
            return;
        }
        qrBitmap = QRCodeUtil.createQRCode("收件人姓名："+etName.getText().toString()+"\n收件人地址："+etAddress.getText().toString()+"\n收件人电话："+etPhone.getText().toString()+"\n运费："+etPrice.getText().toString()+"\n运单号："+tvNum.getText().toString());
        ivQr.setImageBitmap(qrBitmap);

        //插入数据库
        WayBill bean = new WayBill(etName.getText().toString(),etAddress.getText().toString(),etPhone.getText().toString(),etPrice.getText().toString(),tvNum.getText().toString(),tvCreateTime.getText().toString(),0);
        DbManager.getDaoSession(CreateQRActivity.this).getWayBillDao().insert(bean);

        Helper.HideKeyboard(btnCreate);

    }

    private void initEvent() {
        topBar.setOnTopClickListener(new TopBar.OnTopClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


    }


}
