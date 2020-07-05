package com.kx.promote.ui._do;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.kx.promote.R;
import com.kx.promote.bean.Group;
import com.kx.promote.ui.HomeActivity;
import com.kx.promote.ui.task_center.GroupRecyclerViewAdapter;
import com.kx.promote.ui.task_center.OrderRecyclerViewAdapter;
import com.kx.promote.utils.HttpUtil;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;
import com.kx.promote.utils.MyCallback;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImageUploaderAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private static final int SET_IMAGE_BITMAP = 0;
    private Handler handler = new MyHandler(this);
    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
        notifyDataSetChanged();//更新数据源自动刷新
    }
    public void removeUrl(int index){
        this.urlList.remove(index);
        setUrlList(this.urlList);
    }
    private List<String> urlList;
    private Context mContext;

    public ImageUploaderAdapter(List<String> urlList){
        super();
        this.urlList = urlList;
        mInflater = (LayoutInflater) MyApplication.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if(urlList==null)
            return 0;
        return this.urlList.size();
    }

    @Override
    public Object getItem(int i) {
        if(this.urlList==null)
            return null;
        return this.urlList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        String url = urlList.get(i)+MyApplication.getImageSmall();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_image_uploader_image, null);
            holder = new ViewHolder(convertView);
            holder.setUrl(urlList.get(i));
            holder.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeUrl(i);
                }
            });
        } else {
            holder = (ViewHolder)convertView.getTag();
            if(!holder.url.equals(urlList.get(i))){
                holder.setUrl(urlList.get(i));
            }
        }
        return convertView;
    }
    class ViewHolder {
        public SimpleDraweeView imgView;
        public ImageView removeButton;
        private String url;
        ViewHolder(View view) {
            imgView =  view.findViewById(R.id.image_uploader_image);
            removeButton = view.findViewById(R.id.image_uploader_remove);
            view.setTag(this);
        }
        void setUrl(String url){
            this.url = url;
            imgView.setImageURI(url+MyApplication.getImageSmall());
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
