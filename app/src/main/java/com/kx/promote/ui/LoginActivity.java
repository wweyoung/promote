package com.kx.promote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kx.promote.R;
import com.kx.promote.bean.User;
import com.kx.promote.utils.HttpUtil;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private ImageView verificationImage;
    private EditText userEdit;
    private EditText passwordEdit;
    private EditText verificationEdit;
    // 主线程创建消息处理器
    protected static final int SHOW_TOAST = 0;
    protected static final int IMAGE_UPDATE = 1;
    protected static final int CLEAR_VERIFIACTION = 3;//清空验证码输入框
    private static final int GO_HOME = 4;//去主页
    protected static final int ERROR = 2;
    private Handler handler = new MyHandler(this);
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //获取需要展示图片验证码的ImageView
        verificationImage = (ImageView) findViewById(R.id.imageView_showCode);
        //获取工具类生成的图片验证码对象
        loadVerificationImage();
        verificationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadVerificationImage();
            }
        });

        userEdit = (EditText)findViewById(R.id.editText_userName);
        passwordEdit = (EditText)findViewById(R.id.editText_password);
        passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        verificationEdit = (EditText)findViewById(R.id.editText_code);
        Button btn_login=(Button)findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        if(MyApplication.hasUserInfo()){
            User user = MyApplication.getUser();
            userEdit.setText(user.getUser());
            passwordEdit.setText(user.getPassword());
            autoLogin();
        }
    }
    private void loadVerificationImage(){
        HttpUtil.getImage(MyApplication.getAppPath()+"/verificationCode",handler,IMAGE_UPDATE);
    }
    private void login(){
        if(userEdit.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "请输入用户名",Toast.LENGTH_SHORT).show();
            return;
        }
        if(passwordEdit.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this, "请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if(verificationEdit.getText().toString().isEmpty()) {
            Toast.makeText(LoginActivity.this, "请输入验证码空", Toast.LENGTH_SHORT).show();
            return;
        }

        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        final Map<String,String> userMap = new HashMap<>();
        final String password = passwordEdit.getText().toString();
        userMap.put("user",userEdit.getText().toString());
        userMap.put("password",password);
        RequestBody body = RequestBody.create(JSON.toJSONString(userMap),mediaType);
        HttpUtil.post(MyApplication.getAppPath() +"/interface/login?rememberMe=true&verificationCode="+verificationEdit.getText().toString(),
                body, new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message.obtain(handler,SHOW_TOAST,"请求失败！").sendToTarget();
                    }
                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String json = response.body().string();
                        Log.d("login",json);
                        Msg msg =  JSON.parseObject(json,Msg.class);//json转Msg对象

                        if(msg.getCode()==0){//判断是否成功
                            user= JSONObject.toJavaObject((JSONObject) msg.get("user"),User.class);//json转User对象
                            user.setPassword(password);
                            Message.obtain(handler,SHOW_TOAST,"登录成功！").sendToTarget();
                            MyApplication.setUser(user);
                            goHome(user);
                        }
                        else{//登录失败
                            Message.obtain(handler,CLEAR_VERIFIACTION).sendToTarget();//给主线程发送信息，让主线程清空输入框
                            if(msg.getCode()==1){//需要刷新验证码
                                loadVerificationImage();//重新加载验证码
                            }
                            Message.obtain(handler,SHOW_TOAST,msg.getMsg()).sendToTarget();//给主线程发送信息，让主线程弹出Toast
                        }

                    }
                });
    }
    private void autoLogin(){
        Toast.makeText(this,"自动登录中...",Toast.LENGTH_SHORT).show();
        HttpUtil.get(MyApplication.getAppPath()+"/interface/user", new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(LoginActivity.this,"请求失败！",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Msg msg =  JSON.parseObject(json,Msg.class);//json转Msg对象

                if(msg.getCode()==0){//判断是否成功
                    user= JSONObject.toJavaObject((JSONObject) msg.get("user"),User.class);//json转User对象
                    Message.obtain(handler,GO_HOME,user).sendToTarget();
                }
                else{//登录失败
                    Message.obtain(handler,SHOW_TOAST,"自动登录失败，请手动登录").sendToTarget();//给主线程发送信息，让主线程弹出Toast
                }
            }
        });
    }
    private void goHome(User user){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user",user);

        startActivity(intent);
        finish();
    }
    static class MyHandler extends Handler {
        private WeakReference<LoginActivity> mOuter;

        public MyHandler(LoginActivity activity) {
            mOuter = new WeakReference<LoginActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LoginActivity outer = mOuter.get();
            if (outer != null) {
                if(msg.what == SHOW_TOAST){
                    String text = (String) msg.obj;
                    Toast.makeText(outer,text,Toast.LENGTH_SHORT).show();
                }
                else if (msg.what == IMAGE_UPDATE) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    outer.verificationImage.setImageBitmap(bitmap);
                }
                else if (msg.what == CLEAR_VERIFIACTION) {
                    outer.verificationEdit.setText("");
                }
                else if(msg.what == GO_HOME){
                    User user = (User)msg.obj;
                    outer.goHome(user);
                }
                else if (msg.what == ERROR) {
                    Toast.makeText(outer, "显示图片错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
