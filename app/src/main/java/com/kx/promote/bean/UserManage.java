package com.kx.promote.bean;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class UserManage {
    private static UserManage userManage;
    private static  User user;

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    private UserManage() {
        user=new User();
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
    public void saveUserInfo(Context context, User user) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);//Context.MODE_PRIVATE表示SharePrefences的数据只有自己应用程序能访问。
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user", user.getUser());
        editor.putString("name", user.getName());
        editor.putString("password", user.getPassword());
        editor.putString("phone", user.getPhone());
        editor.putString("mail", user.getEmail());
//        editor.putString("UserInfo", String.valueOf(user));
        editor.commit();
    }

    public User getUserInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        User userInfo = new User();
        userInfo.setUser(sp.getString("user",""));
        userInfo.setName(sp.getString("name", ""));
        userInfo.setPassword(sp.getString("password", ""));
        userInfo.setPhone(sp.getString("phone",""));
        userInfo.setEmail(sp.getString("mail",""));

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

