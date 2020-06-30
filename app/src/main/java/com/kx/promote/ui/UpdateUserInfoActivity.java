package com.kx.promote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.kx.promote.R;
import com.kx.promote.utils.HttpUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdateUserInfoActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText userNameEdit;
    private EditText personNameEdit;
    private EditText userPhoneEdit;
    private EditText userMailEdit;
    private EditText newPasswordEdit;
    private EditText confirmPasswordEdit;
    private String appPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        appPath = getString(R.string.app_path);

        userNameEdit=(EditText)findViewById(R.id.editText_userName);
        personNameEdit=(EditText)findViewById(R.id.editText_personName);
        personNameEdit.setFocusableInTouchMode(false);
        userPhoneEdit=(EditText)findViewById(R.id.editText_userPhone);
        userMailEdit=(EditText)findViewById(R.id.editText_mail);
        newPasswordEdit=(EditText)findViewById(R.id.editText_newPassword);
        confirmPasswordEdit=(EditText)findViewById(R.id.editText_confirmPassword);



        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        Button btn_save=(Button)findViewById(R.id.btn_save);
        btn_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:
                Toast.makeText(UpdateUserInfoActivity.this, "点击了返回", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_save:
                Toast.makeText(UpdateUserInfoActivity.this, "点击了完成", Toast.LENGTH_SHORT).show();
                //获取登录用户信息
                HttpUtil.get(appPath+"/interface/user",new okhttp3.Callback(){

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        String result = response.body().string();
                        Log.d("获取成功",result);
                    }
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Toast.makeText(UpdateUserInfoActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                    }
                });

                MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
                Map<String,String> user = new HashMap<>();
                user.put("userNameEdit",userNameEdit.getText().toString());
                user.put("userPhoneEdit",userPhoneEdit.getText().toString());
                user.put("userMailEdit",userMailEdit.getText().toString());
                user.put("newPasswordEdit",newPasswordEdit.getText().toString());
                user.put("confirmPasswordEdit",confirmPasswordEdit.getText().toString());
                RequestBody body = RequestBody.create(JSON.toJSONString(user),mediaType);

                HttpUtil.post(appPath+"/interface/user",body, new okhttp3.Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                Toast.makeText(UpdateUserInfoActivity.this, "修改失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                String result = response.body().string();
                                Log.d("修改成功",result);
                            }
                        });
                break;
            default:
                break;
        }
    }
}
