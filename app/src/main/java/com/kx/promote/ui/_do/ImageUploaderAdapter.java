package com.kx.promote.ui._do;

import android.content.Context;
import android.content.Intent;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.kx.promote.R;
import com.kx.promote.ui.MainActivity;
import com.kx.promote.ui.image_selector.MultiImageSelectorActivity;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;
import com.kx.promote.utils.QiniuUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

public class ImageUploaderAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private static final int SET_IMAGE_BITMAP = 0;
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
                convertView.setVisibility(View.VISIBLE);
            }
            else{
                convertView.setVisibility(View.GONE);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                MyApplication.getHomeActivity().setImageUploader(fragment);
                MultiImageSelectorActivity.startSelect(MyApplication.getHomeActivity(), 2, max-urlList.size(), MultiImageSelectorActivity.MODE_MULTI);
                }
            });
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
            Log.d("----", url);
            this.url = url;
            if(url.indexOf("http")==0){
                url+=MyApplication.getImageSmall();
                textView.setVisibility(View.GONE);
                imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
            else{
                QiniuUtil qiniuUtil = MyApplication.getQiniuUtil();
                final String finalUrl = url;
                final int originHeight = textView.getHeight();
                qiniuUtil.upload(url, new QiniuUtil.Callback() {
                    @Override
                    public void success(String newUrl) {
                        Collections.replaceAll(urlList, finalUrl, newUrl);
                        textView.setVisibility(View.GONE);
                    }

                    @Override
                    public void failed() {
                        Log.d("上传：", "failed: ");
                        textView.setHeight(originHeight);
                        textView.setText("上传失败");
                    }

                    @Override
                    public void progress(double percent) {
                        textView.setHeight((int) Math.round(percent*originHeight));
                    }
                });
                Uri uri = Uri.fromFile(new File(url));
                url= uri.toString();
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
            }
        }
    }
}
