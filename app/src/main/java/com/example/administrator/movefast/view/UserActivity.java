package com.example.administrator.movefast.view;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.movefast.BuildConfig;
import com.example.administrator.movefast.R;
import com.example.administrator.movefast.db.DbManager;
import com.example.administrator.movefast.entity.User;
import com.example.administrator.movefast.event.UpDateUserEvent;
import com.example.administrator.movefast.event.base.EventCenter;
import com.example.administrator.movefast.utils.GalleryUtil;
import com.example.administrator.movefast.utils.Helper;
import com.example.administrator.movefast.utils.PermissionUtility;
import com.example.administrator.movefast.widget.TopBar;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.functions.Consumer;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int NAME = 0;
    public static final int SIGN = 1;
    public static final int PHONE = 2;
    public static final int ADRESS = 3;

    private static final int IMAGE = 300;
    private static final int REQUEST_CAPTURE = 400;
    private static final int REQUEST_ALBUM = 300;

    private ImageView ivHead;
    private TextView tvName;
    private TextView tvSign;
    private TextView tvSex;
    private TextView tvPhone;
    private TextView tvAddress;

    private RelativeLayout rlName;
    private RelativeLayout rlSign;
    private RelativeLayout rlSex;
    private RelativeLayout rlPhone;
    private RelativeLayout rlAddress;
    private TopBar top;

    private User currentLoginUser;
    private Uri imageUri;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().hide();

        initData();
        initView();
        setData();
        initEvent();
    }

    private void initData() {
        EventCenter.register(this);
        currentLoginUser = getIntent().getParcelableExtra("user");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setData() {
        top.setTitle("个人资料");
        if (currentLoginUser != null) {

            if (!currentLoginUser.getHead_img().equals("")){
                Bitmap bm = BitmapFactory.decodeFile(currentLoginUser.getHead_img());
                ivHead.setBackground(new BitmapDrawable(bm));
            }

            if (currentLoginUser.getSex() == 0) {
                tvSex.setText("保密");
            } else if (currentLoginUser.getSex() == 1) {
                tvSex.setText("男");
            } else if (currentLoginUser.getSex() == 2) {
                tvSex.setText("女");
            }

            if (currentLoginUser.getName().equals("")) {
                tvName.setText("未设置");
            } else {
                tvName.setText(currentLoginUser.getName());
            }

            if (currentLoginUser.getSign().equals("")) {
                tvSign.setText("未设置");
            } else {
                tvSign.setText(currentLoginUser.getSign());
            }

            if (currentLoginUser.getPhone().equals("")) {
                tvName.setText("未设置");
            } else {
                tvPhone.setText(currentLoginUser.getPhone());
            }

            if (currentLoginUser.getCurrent_address().equals("")) {
                tvName.setText("未设置");
            } else {
                tvAddress.setText(currentLoginUser.getCurrent_address());
            }

        }
    }

    private void initView() {
        top = findViewById(R.id.topbar);
        ivHead = findViewById(R.id.iv_head);
        tvName = findViewById(R.id.tv_name);
        tvSign = findViewById(R.id.tv_sign);
        tvSex = findViewById(R.id.tv_sex);
        tvPhone = findViewById(R.id.tv_phone);
        tvAddress = findViewById(R.id.tv_address);

        rlName = findViewById(R.id.rl_name);
        rlSign = findViewById(R.id.rl_sign);
        rlSex = findViewById(R.id.rl_sex);
        rlPhone = findViewById(R.id.rl_phone);
        rlAddress = findViewById(R.id.rl_address);

    }


    private void initEvent() {
        top.setOnTopClickListener(new TopBar.OnTopClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

        ivHead.setOnClickListener(this);
        rlName.setOnClickListener(this);
        rlSign.setOnClickListener(this);
        rlSex.setOnClickListener(this);
        rlPhone.setOnClickListener(this);
        rlAddress.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_name:
                changeDetails(0);
                break;
            case R.id.rl_sign:
                changeDetails(1);
                break;
            case R.id.rl_sex:
                changeDetails(4);
                break;
            case R.id.rl_phone:
                changeDetails(2);
                break;
            case R.id.rl_address:
                changeDetails(3);
                break;
            case R.id.iv_head:
                takePictureFromAlum();

                // takePictureFromCamera();
                break;
            default:
                break;
        }
    }

    /**
     * 拍照获取图片
     */
    private void takePictureFromCamera() {
        String pictureName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault()).format(new Date()) + "-" + System.currentTimeMillis() + ".jpg";
        File mOutputImage = new File(getExternalCacheDir(), pictureName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", mOutputImage);
            Log.e("zhou", imageUri.getPath());
        } else {
            imageUri = Uri.fromFile(mOutputImage);
        }
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        if (componentName != null) {
            PermissionUtility.getRxPermission(UserActivity.this)
                    .request(Manifest.permission.CAMERA) //申请拍照权限
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                startActivityForResult(intent, REQUEST_CAPTURE);
                            } else {
                                Helper.showToast("请开启拍照权限");
                            }
                        }
                    });

            currentLoginUser.setHead_img(getExternalCacheDir() + "/" + pictureName);
            DbManager.getDaoSession(UserActivity.this).getUserDao().update(currentLoginUser);
        }
    }

    /**
     * 使用隐式意图打开系统相册
     */
    private void takePictureFromAlum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        if (componentName != null) {
            startActivityForResult(intent, REQUEST_ALBUM);
        }
    }


    private void changeDetails(int type) {
        Intent intent = new Intent(UserActivity.this, ChangeUserDetailActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("user", currentLoginUser);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case REQUEST_CAPTURE: // 拍照
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    ivHead.setBackground(new BitmapDrawable(bitmap));
                    EventCenter.post(new UpDateUserEvent(currentLoginUser));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_ALBUM:
                PermissionUtility.getRxPermission(UserActivity.this)
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE) //申请读权限
                        .subscribe(new Consumer<Boolean>() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void accept(Boolean granted) throws Exception {
                                if (granted) {
                                    String path = GalleryUtil.getPath(UserActivity.this, data.getData());
                                    currentLoginUser.setHead_img(path);
                                    DbManager.getDaoSession(UserActivity.this).getUserDao().update(currentLoginUser);
                                    ivHead.setBackground(new BitmapDrawable(BitmapFactory.decodeFile(path)));
                                } else {
                                    Helper.showToast("请开启拍照权限");
                                }
                            }
                        });
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventCenter.unRegister(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void upDate(UpDateUserEvent event) {
        currentLoginUser = event.getUser();
        if (currentLoginUser != null) {
            setData();
        } else {
            Helper.showToast("123");
        }
    }

}
