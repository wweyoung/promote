package com.kx.promote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kx.promote.R;
import com.kx.promote.utils.HttpUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private String code;
    private ImageView verificationImage;
    private EditText userEdit;
    private EditText passwordEdit;
    private EditText verificationEdit;
    // 主线程创建消息处理器
    protected static final int IMAGE_UPDATE = 1;
    protected static final int ERROR = 2;
    private Handler handler = new MyHandler(this);
    private String appPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        verificationEdit = (EditText)findViewById(R.id.editText_code);
        Button btn_login=(Button)findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userEdit.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "请输入用户名",Toast.LENGTH_SHORT).show();
                }else if(passwordEdit.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "请输入密码",Toast.LENGTH_SHORT).show();
                }else if(verificationEdit.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "请输入验证码空", Toast.LENGTH_SHORT).show();
                }else{
                        final MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
                        Map<String, String> user = new HashMap<>();
                        user.put("user", userEdit.getText().toString());
                        user.put("password", passwordEdit.getText().toString());
                        RequestBody body = RequestBody.create(JSON.toJSONString(user), mediaType);
                        HttpUtil.post(appPath + "/interface/login?rememberMe=true&verificationCode=" + verificationEdit.getText().toString(),
                                body, new okhttp3.Callback() {
                                    @Override
                                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                        Toast.makeText(LoginActivity.this, "请求失败",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                        String result = response.body().string();
                                        Log.d("login+++", result);
//                                        Message message=new Message(JSON.format(result,Message.class));
//                                        Message jsonObject= JSON.format(result, Message.class);

//                                        parseJSONWithJSONObject(result);
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);

                                    }
                                });
                    }

                }
        });
    }

//    private void parseJSONWithJSONObject(String result) {
//        try {
//            JSONObject jsonObject = new JSONObject(result);
//            Message message=new Message();
//            message.obj=jsonObject;
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }




    private void loadVerificationImage(){
        HttpUtil.get(appPath+"/verificationCode",new okhttp3.Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                byte[] bytes = response.body().bytes();
                //使用BitmapFactory工厂，把字节数组转化为bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Message message = handler.obtainMessage();
                message.what = IMAGE_UPDATE;
                message.obj = bitmap;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Toast.makeText(LoginActivity.this, "加载验证码失败",
                        Toast.LENGTH_SHORT).show();
            }
        });
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
                if (msg.what == IMAGE_UPDATE) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    outer.verificationImage.setImageBitmap(bitmap);
                } else if (msg.what == ERROR) {
                    Toast.makeText(outer, "显示图片错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
