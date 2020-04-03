package com.keepfit.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.keepfit.MainActivity;
import com.keepfit.R;
import com.keepfit.databinding.ActivityLoginBinding;
import com.keepfit.databinding.ActivityRegisterBinding;
import com.keepfit.service.RegisterService;
import com.keepfit.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RegisterActivity extends AppCompatActivity {
    //使用viewBind从此不再需要使用findViewById()
    private ActivityRegisterBinding mRegisterBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegisterBinding = ActivityRegisterBinding.inflate(LayoutInflater.from(this));
        setContentView(mRegisterBinding.getRoot());
        EventBus.getDefault().register(this);
        mRegisterBinding.registerSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account=mRegisterBinding.registerAccount.getText().toString();
                String password=mRegisterBinding.registerPwd.getText().toString();
                RegisterService registerService=new RegisterService();
                registerService.insertUser(account,password);
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
        if(message.getKey().equals("register_success")) {
            Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
        }

    }
}
