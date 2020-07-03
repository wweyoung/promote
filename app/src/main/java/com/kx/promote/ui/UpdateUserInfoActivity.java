package com.kx.promote.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    // 主线程创建消息处理器
    protected static final int IMAGE_UPDATE = 1;
    protected static final int ERROR = 2;
    private String appPath;
    User user;
    protected static final int SUCCESS = 0;
    protected static final int FAIL = 1;
    protected static int Click = 0;
//   private Handler handler = new UpdateUserInfoActivity().MyHandler(this);


     private Handler handler= new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS:
                    Toast.makeText(UpdateUserInfoActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                    break;
                case FAIL:
                    Toast.makeText(UpdateUserInfoActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        appPath = getString(R.string.app_path);

        userNameEdit=(EditText)findViewById(R.id.editText_userName);
        personNameEdit=(EditText)findViewById(R.id.editText_personName);
        userPhoneEdit=(EditText)findViewById(R.id.editText_userPhone);
        userMailEdit=(EditText)findViewById(R.id.editText_mail);
        newPasswordEdit=(EditText)findViewById(R.id.editText_newPassword);
        newPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        confirmPasswordEdit=(EditText)findViewById(R.id.editText_confirmPassword);
        confirmPasswordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        ImageButton btn_back = (ImageButton)findViewById(R.id.btn_back);
        Button btn_save=(Button)findViewById(R.id.btn_save);
        btn_back.setOnClickListener(this);
        btn_save.setOnClickListener(this);

//        Intent intent=getIntent();
//        user = (User) intent.getSerializableExtra("user");
//        userNameEdit.setText(user.getUser());
//        personNameEdit.setText(user.getName());
//        userPhoneEdit.setText(user.getPhone());
//        userMailEdit.setText(user.getEmail());

        user=MyApplication.getUser();
        userNameEdit.setText(user.getUser());
        personNameEdit.setText(user.getName());
        userPhoneEdit.setText(user.getPhone());
        userMailEdit.setText(user.getEmail());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_back:

                if(Click==0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateUserInfoActivity.this);
                    dialog.setTitle("提示");
                    dialog.setMessage("退出后将不会保存修改的信息，确定要退出吗");
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //返回上一个上一个界面
                            Intent intent = new Intent(UpdateUserInfoActivity.this, HomeActivity.class);
//                            intent.putExtra("user",user);
                            startActivity(intent);
                        }
                    });
                    dialog.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    dialog.show();
                }else{
                    Intent intent = new Intent(UpdateUserInfoActivity.this, HomeActivity.class);
//                intent.putExtra("user",user);//将数据返回
                    startActivity(intent);
                }

                break;

            case R.id.btn_save:
                if(!newPasswordEdit.getText().toString().equals(confirmPasswordEdit.getText().toString())){
                    Toast.makeText(UpdateUserInfoActivity.this,"密码与确认密码不一致",Toast.LENGTH_SHORT).show();
                }else {
                    MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
                    Map<String, String> userMap = new HashMap<>();
                    userMap.put("id", String.valueOf(user.getId()));
                    userMap.put("user", userNameEdit.getText().toString());
                    userMap.put("name", personNameEdit.getText().toString());
                    userMap.put("phone", userPhoneEdit.getText().toString());
                    userMap.put("email", userMailEdit.getText().toString());
                    userMap.put("password", newPasswordEdit.getText().toString());
                    RequestBody body = RequestBody.create(JSON.toJSONString(userMap), mediaType);
                    HttpUtil.post(MyApplication.getAppPath() + "/interface/user", body, new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Toast.makeText(UpdateUserInfoActivity.this, "修改失败",
                                    Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            String json = response.body().string();
                            Msg msg = JSON.parseObject(json, Msg.class);//json转Msg对象
                            Log.d("result", json);
                            if (msg.getCode() == 0) {//判断是否成功
                                user.setPassword(newPasswordEdit.getText().toString());
                                user.setPhone(userPhoneEdit.getText().toString());
                                user.setUser(userNameEdit.getText().toString());
                                user.setEmail(userMailEdit.getText().toString());
                                MyApplication.setUser(user);
                                Message message = new Message();
                                message.what = SUCCESS;
                                handler.sendMessage(message);
                            } else {//修改失败
                                Message message = new Message();
                                message.what = FAIL;
                                handler.sendMessage(message);
                            }

                        }
                    });
                    Click++;
                }
                break;
            default:
                break;
        }
    }
}
