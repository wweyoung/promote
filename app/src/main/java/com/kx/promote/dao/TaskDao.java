package com.kx.promote.dao;

import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.kx.promote.bean.Group;
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

public class TaskDao {
    private static final TaskDao instance = new TaskDao();//直接先实例化一个，确保单例模式
    public void getGroupListByDate(Date date, final MyCallback callback){
        date = DateToolkit.getZeroOfDay(date);//把日期转为0点
        String url = MyApplication.getAppPath()+"/interface/worker/groupListByTime/"+date.getTime();
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
                    JSONArray groupJsonArray = (JSONArray) msg.get("groupList");
                    String answerString = groupJsonArray.toJSONString();//将array数组转换成字符串
                    GsonBuilder builder = new GsonBuilder();
                    builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                            return new Date(json.getAsJsonPrimitive().getAsLong());//时间戳自动转时间
                        }
                    });
                    Gson gson = builder.create();
                    List<Group> groupList = (List<Group>) gson.fromJson(answerString, new TypeToken<List<Group>>() {
                    }.getType());//把字符串转换成集合
                    msg.put("groupList", groupList);
                    callback.success(msg);
                }

            }
        });
    }
    public static TaskDao getInstance(){//单例模式，需要的时候获取已经new好的
        return instance;
    }
}
