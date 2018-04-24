package com.example.administrator.movefast.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.widget.TopBar;

public class AboutUsActivity extends AppCompatActivity {

    private TopBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        getSupportActionBar().hide();

        topBar = findViewById(R.id.topbar);
        topBar.setTitle("关于我们");

        topBar.setOnTopClickListener(new TopBar.OnTopClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });

    }
}
