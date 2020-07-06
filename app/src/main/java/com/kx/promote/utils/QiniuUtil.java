package com.kx.promote.utils;

import android.util.Log;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

public class QiniuUtil {
    Configuration config;
    // 重用uploadManager。一般地，只需要创建一个uploadManager对象
    UploadManager uploadManager;
    //配置3个线程数并发上传；不配置默认为3，只针对file.size>4M生效。线程数建议不超过5，上传速度主要取决于上行带宽，带宽很小的情况单线程和多线程没有区别
    public QiniuUtil(){
        config = new Configuration.Builder()
                .connectTimeout(10)           // 链接超时。默认10秒
                .useHttps(true)               // 是否使用https上传域名
                .responseTimeout(60)          // 服务器响应超时。默认60秒
                .zone(FixedZone.zone0)        // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
                .build();
        uploadManager = new UploadManager(config, 3);
    }
    public void upload(final String path, final Callback callback){
        HttpUtil.get(MyApplication.getAppPath()+"/interface/file/qiniu/uploadToken", new MyCallback() {
            @Override
            public void success(Msg msg) {
                if(msg.getCode()==0){
                    String key = (String) msg.get("key");
                    String uploadToken = (String) msg.get("uploadToken");
                    String domain = (String)msg.get("domain");
                    upload(path,key,uploadToken,domain,callback);
                }
                else{
                    callback.failed();
                }
            }

            @Override
            public void failed(Msg msg) {
                callback.failed();
            }
        });
    }

    public void upload(String path, String key, String token, final String domain, final Callback callback){
        uploadManager.put(path, key, token,
                new  UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res) {
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                            Log.i("qiniu", "Upload Success");
                            callback.success(domain+"/"+key);
                        } else {
                            Log.i("qiniu", "Upload Fail");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                            callback.failed();
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                },
                new UploadOptions(null, null, false,
                        new UpProgressHandler(){
                            public void progress(String key, double percent){
                                Log.i("qiniu", key + ": " + percent);
                                callback.progress(percent);
                            }
                        }, null));
    }
    public interface Callback {
        void success(String url);
        void failed();
        void progress(double percent);
    }
}
