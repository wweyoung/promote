package com.kx.promote.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class UserManage {
    private static UserManage userManage;

    private UserManage() {
    }

    public static UserManage getInstance() {
        if (userManage == null) {
            userManage = new UserManage();
        }
        return userManage;
    }


    /**
     * 保存自动登录的用户信息
     */
    public void saveUserInfo(Context context, String username, String password) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("USER_NAME", username);
        editor.putString("PASSWORD", password);
        editor.commit();
    }

    public User getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        User userInfo = new User();
        userInfo.setName(sp.getString("USER_NAME", ""));
        userInfo.setPassword(sp.getString("PASSWORD", ""));
        return userInfo;
    }


    /**
     * userInfo中是否有数据
     */
    public boolean hasUserInfo(Context context) {
        User userInfo = getUserInfo(context);
        if (userInfo != null) {
            if ((!TextUtils.isEmpty(userInfo.getName())) && (!TextUtils.isEmpty(userInfo.getPassword()))) {//有数据
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}

