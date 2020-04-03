package com.keepfit.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.keepfit.MainActivity;
import com.keepfit.R;
import com.keepfit.databinding.ActivityLoginBinding;
import com.keepfit.databinding.ActivityMainBinding;
import com.keepfit.service.LoginService;
import com.keepfit.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends AppCompatActivity {
    //使用viewBind从此不再需要使用findViewById()
    private ActivityLoginBinding mLoginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取布局xml
        mLoginBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this));
        setContentView(mLoginBinding.getRoot());
        EventBus.getDefault().register(this);
        //给登录按钮增加点击事件
        mLoginBinding.loignSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account=mLoginBinding.loginAccount.getText().toString();
                String password=mLoginBinding.loginPwd.getText().toString();
                LoginService loginService=new LoginService();
                loginService.selectLogin(account,password);
            }
        });
        //给注册按钮添加点击事件
        mLoginBinding.loginRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MessageEvent message) {
        if(message.getKey().equals("login_success")) {
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
        }
        if(message.getKey().equals("login_false")) {
            Toast.makeText(this,"登录失败,请重新尝试",Toast.LENGTH_SHORT).show();
        }
    }



}
