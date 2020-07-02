package com.kx.promote.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.os.UserManager;
import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kx.promote.R;
import com.kx.promote.bean.User;
import com.kx.promote.bean.UserManage;
import com.kx.promote.utils.HttpUtil;
import com.kx.promote.utils.Msg;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
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
    protected static final int ERROR = 2;
    private Handler handler = new MyHandler(this);
    private String appPath;

    User user;
    UserManage userManage;

    private static final int GO_HOME = 0;//去主页
    private static final int GO_LOGIN = 1;//去登录页
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent();
            switch (msg.what) {
                case GO_HOME://去主页
                    intent = new Intent(LoginActivity.this, UpdateUserInfoActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case GO_LOGIN://去登录页
                    intent = new Intent(LoginActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (userManage.getInstance().hasUserInfo(this))//自动登录判断，SharePrefences中有数据，则跳转到主页，没数据则跳转到登录页
        {
            mHandler.sendEmptyMessageDelayed(GO_HOME, 500);
            Toast.makeText(LoginActivity.this,"正在登录",Toast.LENGTH_SHORT).show();

        } else {
            mHandler.sendEmptyMessageAtTime(GO_LOGIN, 2000);
        }

        setContentView(R.layout.activity_login);
        appPath = getString(R.string.app_path);
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
                userMap.put("user",userEdit.getText().toString());
                userMap.put("password",passwordEdit.getText().toString());
                RequestBody body = RequestBody.create(JSON.toJSONString(userMap),mediaType);
                HttpUtil.post(appPath+"/interface/login?rememberMe=true&verificationCode="+verificationEdit.getText().toString(),
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

                                Map<String,Object> data=msg.getData();
                                String userString= String.valueOf(data.get("user"));
                                user= JSON.parseObject(userString,User.class);//json转User对象

                                if(msg.getCode()==0){//判断是否成功
//                                    UserManage.getInstance().saveUserInfo(LoginActivity.this, userEdit.getText().toString(), passwordEdit.getText().toString());
                                    userManage.getInstance().saveUserInfo(LoginActivity.this, user);
                                    Intent intent2=new Intent();
                                    intent2.setClass(getApplicationContext(), UpdateUserInfoActivity.class);
                                    intent2.putExtra("User",user);
                                    startActivityForResult(intent2,1);
                                    startActivity(intent2);
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
        });

    }
    private void loadVerificationImage(){
        HttpUtil.getImage(appPath+"/verificationCode",handler,IMAGE_UPDATE);
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
                else if (msg.what == ERROR) {
                    Toast.makeText(outer, "显示图片错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
