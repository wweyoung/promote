package com.kx.promote.ui.task_center;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kx.promote.R;
import com.kx.promote.bean.Group;
import com.kx.promote.bean.Order;
import com.kx.promote.utils.HttpUtil;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;
import com.kx.promote.utils.MyCallback;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> {
    private List<Order> orderList = new ArrayList<>();
    private Context mContext;
    private Handler handler = new MyHandler(this);
    private static final int SHOW_TOAST = 0;
    private static final int SET_IMAGE = 1;

    public OrderRecyclerViewAdapter(List<Order> dataList){
        this.orderList = dataList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Order order = orderList.get(position);
        if(order==null)
            return;
        HttpUtil.getImage(order.getNeed().getImage().getUrl() + MyApplication.getImageSmall(), new MyCallback() {
            @Override
            public void success(Msg msg) {
                msg.add("holder",holder);
                Message.obtain(handler,SET_IMAGE,msg).sendToTarget();
            }

            @Override
            public void failed(Msg msg) {
                Message.obtain(handler,SHOW_TOAST,msg.getMsg()).sendToTarget();
            }
        });
        holder.keywordView.setText(order.getNeed().getKeyword());
        holder.shopView.setText(order.getNeed().getShop().getName());
        holder.prepriceView.setText("预付"+order.getNeed().getPrice().toString()+"元");
        holder.stateView.setText(order.getStateString());
        if(order.getState()==Order.FINISHED || order.getState()==Order.FILLIN){
            holder.actpriceView.setText("实付"+order.getPrice().toString()+"元");
        }
    }

    @Override
    public int getItemCount() {
        if(orderList==null)
            return 0;
        return this.orderList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView keywordView;
        private TextView shopView;
        private TextView prepriceView;
        private TextView actpriceView;
        private TextView stateView;
        private ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            keywordView = itemView.findViewById(R.id.order_keyword);
            shopView =  itemView.findViewById(R.id.order_shop);
            prepriceView =  itemView.findViewById(R.id.order_preprice);
            actpriceView =  itemView.findViewById(R.id.order_actprice);
            stateView = itemView.findViewById(R.id.order_state);
            imageView =  itemView.findViewById(R.id.order_image);
        }
    }
    public void setImage(){

    }
    static class MyHandler extends Handler {
        private WeakReference<OrderRecyclerViewAdapter> mOuter;

        public MyHandler(OrderRecyclerViewAdapter activity) {
            mOuter = new WeakReference<OrderRecyclerViewAdapter>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            OrderRecyclerViewAdapter outer = mOuter.get();
            if (outer != null) {
                if(msg.what == SHOW_TOAST){
                    String text = (String) msg.obj;
                    Toast.makeText(MyApplication.getContext(),text,Toast.LENGTH_SHORT).show();
                }
                else if (msg.what == SET_IMAGE) {
                    Msg m = (Msg) msg.obj;
                    Bitmap bitmap = (Bitmap) m.get("bitmap");
                    ViewHolder holder = (ViewHolder) m.get("holder");
                    holder.imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
