package com.kx.promote;

import android.util.Log;

import com.kx.promote.utils.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    final static String TAG = "okhttpTest";
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void okHttpTest(){
        Log.d(TAG,"开始测试okhttp");
        System.out.println("开始测试okhttp");
        HttpUtil.sendOkHttpRequest("http://baidu.com", new okhttp3.Callback() {
             @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.d(TAG,"访问失败啦！");
                System.out.println("访问失败啦！");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d(TAG,response.body().string());
                System.out.println(response.body().string());
            }
        }, editor);
    }
}