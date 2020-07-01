package com.kx.promote.utils;

import android.app.Application;
import android.content.Context;
import com.kx.promote.R;
public class MyApplication extends Application {
    private static Context context;
    private static String appPath;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        appPath = context.getString(R.string.app_path);
    }
    public static Context getContext(){
        return context;
    }
    public static String getAppPath(){
        return appPath;
    }
}
