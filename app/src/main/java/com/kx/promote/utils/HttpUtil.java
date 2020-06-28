package com.kx.promote.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    public static void get(String url, Callback callback, Context context){
        OkHttpClient client = new OkHttpClient().newBuilder().cookieJar(new MyCookieJar(context)).build();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }
    public static void post(String url, RequestBody requestBody, Callback callback, Context context){
        OkHttpClient client = new OkHttpClient().newBuilder().cookieJar(new MyCookieJar(context)).build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

}
