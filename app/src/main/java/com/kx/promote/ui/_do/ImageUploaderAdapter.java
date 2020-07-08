package com.kx.promote.ui._do;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kx.promote.R;
import com.kx.promote.ui.image_selector.MultiImageSelectorActivity;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;
import com.kx.promote.utils.QiniuUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageUploaderAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private static final int SET_IMAGE_BITMAP = 0;
    private static final int SHOW_MESSAGE = 1;
    private static final int SET_TEXTVIEW_HEIGTH = 2;
    private static final int SET_TEXTVIEW_TEXT = 3;
    private List<String> urlList;
    private ImageUploaderFragment fragment;
    private int max = 10;
    private Context mContext;
    private final static byte IMAGE = 0 ;
    private final static byte UPLOADER = 1;
    private Handler handler = new MyHandler(this);
    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
        notifyDataSetChanged();//更新数据源自动刷新
    }
    public void set(List<String> urlList,int max){
        this.max = max;
        this.setUrlList(urlList);
    }
    public void removeUrl(int index){
        this.urlList.remove(index);
        setUrlList(this.urlList);
    }


    public ImageUploaderAdapter(List<String> urlList,int max,ImageUploaderFragment fragment){
        super();
        this.urlList = urlList;
        this.max = max;
        this.fragment = fragment;
        mInflater = (LayoutInflater) MyApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(urlList==null)
            return 1;
        int size = this.urlList.size();
        size++;
        return size;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int i){
        if(urlList==null || i==urlList.size()){
            return UPLOADER;
        }
        return IMAGE;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(i==0){
            convertView = mInflater.inflate(R.layout.item_image_uploader_uploader, null);
            TextView textView = convertView.findViewById(R.id.image_uploader_number);
            textView.setText(urlList.size()+"/"+max);
            if(urlList==null || urlList.size()<max){
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    MyApplication.getHomeActivity().setImageUploader(fragment);
                    MultiImageSelectorActivity.startSelect(MyApplication.getHomeActivity(), 2, max-urlList.size(), MultiImageSelectorActivity.MODE_MULTI);
                    }
                });
            }
            else{
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    handler.obtainMessage(SHOW_MESSAGE,"已选择"+max+"张图片！").sendToTarget();
                    }
                });
            }
            convertView.setTag(null);
        }
        else {
            final int imageIndex = i - 1;
            if (convertView == null || convertView.getTag()==null) {
                convertView = mInflater.inflate(R.layout.item_image_uploader_image, null);
                holder = new ViewHolder(convertView);
                holder.setUrl(urlList.get(imageIndex));
                holder.removeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    removeUrl(imageIndex);
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
                if (!holder.url.equals(urlList.get(imageIndex))) {
                    holder.setUrl(urlList.get(imageIndex));
                }
            }
        }
        return convertView;
    }

    class ViewHolder {
        public SimpleDraweeView imgView;
        public TextView textView;
        public ImageView removeButton;
        private String url;
        ViewHolder(View view) {
            imgView =  view.findViewById(R.id.image_uploader_image);
            removeButton = view.findViewById(R.id.image_uploader_remove);
            textView = view.findViewById(R.id.image_uploader_text);
            view.setTag(this);
        }
        void setUrl(String url){
            this.url = url;
            if(url.indexOf("http")==0){
                url+=MyApplication.getImageSmall();
                textView.setVisibility(View.GONE);
                final String finalUrl = url;
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    MyApplication.showBigImage(ViewHolder.this.url,MyApplication.getHomeActivity());
                    }
                });
            }
            else{
                final String path = url;
                QiniuUtil qiniuUtil = MyApplication.getQiniuUtil();
                final int originHeight = textView.getHeight();
                Uri uri = Uri.fromFile(new File(url));
                url= uri.toString();
                final String finalUrl = url;//本地文件路径
                qiniuUtil.upload(path, new QiniuUtil.Callback() {
                    @Override
                    public void success(String newUrl) {
                        Collections.replaceAll(urlList, path, newUrl);
                        textView.setVisibility(View.GONE);
                        imgView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            MyApplication.showBigImage(finalUrl,MyApplication.getHomeActivity());
                            }
                        });
                    }

                    @Override
                    public void failed() {
                        Log.d("上传：", "failed: ");
                        handler.obtainMessage(SET_TEXTVIEW_HEIGTH,originHeight,0,textView).sendToTarget();
                        Map<String,Object> obj = new HashMap<>();
                        obj.put("textView",textView);
                        obj.put("text","上传失败");
                        handler.obtainMessage(SET_TEXTVIEW_TEXT,obj).sendToTarget();
                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                setUrl(path);//重新上传
                            }
                        });
                    }

                    @Override
                    public void progress(double percent) {
                        handler.obtainMessage(SET_TEXTVIEW_HEIGTH,(int) Math.round(percent*originHeight),0,textView).sendToTarget();
                        Map<String,Object> obj = new HashMap<>();
                        obj.put("textView",textView);
                        obj.put("text","上传中");
                        handler.obtainMessage(SET_TEXTVIEW_TEXT,obj).sendToTarget();
                    }
                });
            }
            imgView.setImageURI(url);
        }
        String getUrl(){
            return url;
        }
    }
    static class MyHandler extends Handler {
        private WeakReference<ImageUploaderAdapter> mOuter;

        public MyHandler(ImageUploaderAdapter activity) {
            mOuter = new WeakReference<ImageUploaderAdapter>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ImageUploaderAdapter outer = mOuter.get();
            if (outer != null) {
                if (msg.what == SET_IMAGE_BITMAP) {
                    Msg m = (Msg)msg.obj;
                    ImageView imageView = (ImageView) m.get("imageView");
                    Bitmap bitmap = (Bitmap) m.get("bitmap");
                    imageView.setImageBitmap(bitmap);
                }
                else if(msg.what == SHOW_MESSAGE){
                    String text = (String) msg.obj;
                    Toast.makeText(outer.fragment.getActivity(),text,Toast.LENGTH_SHORT).show();
                }
                else if(msg.what == SET_TEXTVIEW_HEIGTH){
                    int height = msg.arg1;
                    TextView textView = (TextView) msg.obj;
                    textView.setHeight(height);
                }
                else if(msg.what == SET_TEXTVIEW_TEXT){
                    Map map = (Map) msg.obj;
                    TextView textView = (TextView) map.get("textView");
                    String text = (String) map.get("text");
                    textView.setText(text);
                }
            }
        }
    }
}
