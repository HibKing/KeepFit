package com.keepfit.service;

import android.util.Log;

import com.keepfit.utils.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterService {
    public void insertUser(String account,String password)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("uaccount",account).add("upassword",password)
                .build();
        Request request = new Request.Builder()
                .url("http://wangchaoyi.gz01.bdysite.com/keepfit/user_insert.php")
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //解析php返回的json数据
                parseAllShopping(response);
            }
        });

    }

    private void parseAllShopping(Response response) throws IOException {
        //获取json数据
        String responseData=response.body().string();
        Log.d("mgl:注册信息",responseData);
        try{
            JSONArray jsonArray=new JSONArray(responseData);
            for(int i=0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String result = jsonObject.getString("check");
                if (result.equals("1")) {
                    EventBus.getDefault().post(new MessageEvent("register_success","注册成功"));
                    return;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
