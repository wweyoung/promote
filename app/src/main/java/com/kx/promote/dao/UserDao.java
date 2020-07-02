package com.kx.promote.dao;

import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.kx.promote.bean.Group;
import com.kx.promote.bean.User;
import com.kx.promote.utils.DateToolkit;
import com.kx.promote.utils.HttpUtil;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;
import com.kx.promote.utils.MyCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class UserDao {
    private static final UserDao instance = new UserDao();//直接先实例化一个，确保单例模式
    public void getLoginUser(final MyCallback callback){
        String url = MyApplication.getAppPath()+"/interface/user";
        HttpUtil.get(url, new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.failed(null);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Msg msg = JSON.parseObject(json,Msg.class);
                if(msg.getCode()==0) {
                    User user= JSONObject.toJavaObject((JSONObject) msg.get("user"), User.class);//json转User对象
                    msg.put("user", user);
                }
                callback.success(msg);
            }
        });
    }
    public static UserDao getInstance(){//单例模式，需要的时候获取已经new好的
        return instance;
    }
}
