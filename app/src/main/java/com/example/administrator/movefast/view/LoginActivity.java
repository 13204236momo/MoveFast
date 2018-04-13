package com.example.administrator.movefast.view;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.administrator.movefast.R;
import com.example.administrator.movefast.view.fragment.LoginFragment;
import com.example.administrator.movefast.view.fragment.RegisterFragment;

public class LoginActivity extends AppCompatActivity {

    private FrameLayout flContainer;
    private LoginFragment fgLogin;
    private RegisterFragment fgRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        init();
    }

    private void init() {
        flContainer = findViewById(R.id.fl_container);
        fgLogin = new LoginFragment();
        fgRegister = new RegisterFragment();
        showFragment(1);
    }

    public  void showFragment(int fragment){
        if (fragment == 1){
            getFragmentManager().beginTransaction().replace(R.id.fl_container, fgLogin).commit();
            getFragmentManager().beginTransaction().show(fgLogin);
        }else {
            getFragmentManager().beginTransaction().replace(R.id.fl_container, fgRegister).commit();
            getFragmentManager().beginTransaction().show(fgRegister);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fgLogin = null;
        fgRegister = null;
    }
}
