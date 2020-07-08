package com.kx.promote.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kx.promote.R;
import com.kx.promote.bean.User;
import com.kx.promote.ui.HomeActivity;
import com.kx.promote.ui.image_selector.PreviewPicturesActivity;

import java.lang.ref.WeakReference;

public class MyApplication extends Application {
    private static Context context;
    private static HomeActivity homeActivity;
    private static QiniuUtil qiniuUtil;
    private static String appPath;
    private static String imageSmall;
    private static User user;
    private static Integer orderImageMaxNumber;
    private static Integer groupImageMaxNumber;
    private static AlertDialog alertDialog;
    public static final int SHOW_MESSAGE = 1;
    private static MyHandler handler;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new MyHandler(this);
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
    public static MyHandler getHandler(){return handler;}
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
    public static void loading(boolean show, Activity activity){
        if (null != alertDialog && alertDialog.isShowing()) {//先清掉之前的加载框
            alertDialog.dismiss();
        }
        if(show){
            alertDialog = new AlertDialog.Builder(activity).create();
            alertDialog.setCancelable(false);
            alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK)
                        return true;
                    return false;
                }
            });
            alertDialog.show();
            alertDialog.setContentView(R.layout.item_loading);
            WindowManager.LayoutParams dialogParams = alertDialog.getWindow().getAttributes();
            dialogParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            alertDialog.setCanceledOnTouchOutside(false);
        }
    }
    public static void showBigImage(String url,Activity activity){
        PreviewPicturesActivity.preViewSingle(activity,url);
    }
    static class MyHandler extends Handler {
        private WeakReference<MyApplication> mOuter;

        public MyHandler(MyApplication activity) {
            mOuter = new WeakReference<MyApplication>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MyApplication outer = mOuter.get();
            if (outer != null) {
                if (msg.what == SHOW_MESSAGE) {
                    String tip = (String) msg.obj;
                    Toast.makeText(outer, tip, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
