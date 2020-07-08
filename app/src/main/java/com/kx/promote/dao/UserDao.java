package com.kx.promote.dao;

import android.content.Intent;
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
import com.kx.promote.ui.HomeActivity;
import com.kx.promote.ui.LoginActivity;
import com.kx.promote.utils.DateToolkit;
import com.kx.promote.utils.HttpUtil;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;
import com.kx.promote.utils.MyCallback;
import com.kx.promote.utils.MyCookieJar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserDao {
    private static final UserDao instance = new UserDao();//直接先实例化一个，确保单例模式
    public void getLoginUser(final MyCallback callback){
        String url = MyApplication.getAppPath()+"/interface/user";
        HttpUtil.get(url, new MyCallback() {
            @Override
            public void success(Msg msg) {
                if(msg.getCode()==0) {
                    User user= JSONObject.toJavaObject((JSONObject) msg.get("user"), User.class);//json转User对象
                    msg.put("user", user);
                }
                callback.success(msg);
            }

            @Override
            public void failed(Msg msg) {
                callback.failed(msg);
            }
        });
    }
    public void modifyUser(User user,MyCallback callback){
        String url = MyApplication.getAppPath() + "/interface/user";
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody body = RequestBody.create(JSON.toJSONString(user), mediaType);
        HttpUtil.post(url,body,new MyCallback(){

            @Override
            public void success(Msg msg) {

            }

            @Override
            public void failed(Msg msg) {

            }
        });
    }
    public void logout(){
        MyApplication.setUser(new User());
        new MyCookieJar(MyApplication.getContext()).removeAll();//清空Cookie;
        HomeActivity homeActivity = MyApplication.getHomeActivity();
        if(homeActivity!=null){
            Intent intent = new Intent(homeActivity, LoginActivity.class);
            MyApplication.getHomeActivity().startActivity(intent);
            homeActivity.finish();
            MyApplication.setHomeActivity(null);
        }
    }
    public static UserDao getInstance(){//单例模式，需要的时候获取已经new好的
        return instance;
    }
}
