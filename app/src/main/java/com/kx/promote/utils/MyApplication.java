package com.kx.promote.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.kx.promote.R;
import com.kx.promote.bean.User;
import com.kx.promote.ui.HomeActivity;

public class MyApplication extends Application {
    private static Context context;
    private static HomeActivity homeActivity;
    private static QiniuUtil qiniuUtil;
    private static String appPath;
    private static String imageSmall;
    private static User user;
    private static Integer orderImageMaxNumber;
    private static Integer groupImageMaxNumber;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        qiniuUtil = new QiniuUtil();
        appPath = context.getString(R.string.app_path);
        imageSmall = getString(R.string.image_small);
        orderImageMaxNumber = Integer.valueOf(getString(R.string.order_image_max_number));
        groupImageMaxNumber = Integer.valueOf(getString(R.string.group_image_max_number));
        Fresco.initialize(this);//图片加载插件初始化
    }
    public static Context getContext(){
        return context;
    }
    public static String getAppPath(){
        return appPath;
    }
    public static String getImageSmall(){
        return imageSmall;
    }
    public static Integer getOrderImageMaxNumber(){return orderImageMaxNumber;}
    public static Integer getGroupImageMaxNumber(){return groupImageMaxNumber;}
    public static QiniuUtil getQiniuUtil(){return qiniuUtil;}
    public static HomeActivity getHomeActivity() {
        return homeActivity;
    }

    public static void setHomeActivity(HomeActivity homeActivity) {
        MyApplication.homeActivity = homeActivity;
    }
    public static User getUser() {
        if(user!=null)
            return user;
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        if(sp!=null){
            User user = new User();
            user.setUser(sp.getString("user",""));
            user.setName(sp.getString("name", ""));
            user.setPassword(sp.getString("password", ""));
            user.setPhone(sp.getString("phone",""));
            user.setEmail(sp.getString("mail",""));
            return user;
        }
        return null;
    }
    public static void setUser(User user) {
        MyApplication.user = user;
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user", user.getUser());
        editor.putString("name", user.getName());
        editor.putString("password", user.getPassword());
        editor.putString("phone", user.getPhone());
        editor.putString("mail", user.getEmail());
        editor.commit();
    }


    /**
     * userInfo中是否有数据
     */
    public static boolean hasUserInfo() {
        user = getUser();
        if (user != null) {
            if ((!TextUtils.isEmpty(user.getUser())) && (!TextUtils.isEmpty(user.getPassword()))) {//有数据
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
