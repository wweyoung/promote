package com.kx.promote.utils;

import android.app.Application;
import android.content.Context;
import com.kx.promote.R;
public class MyApplication extends Application {
    private static Context context;
    private static String appPath;
    private static String imageSmall;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        appPath = context.getString(R.string.app_path);
        imageSmall = getString(R.string.image_small);
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
}
