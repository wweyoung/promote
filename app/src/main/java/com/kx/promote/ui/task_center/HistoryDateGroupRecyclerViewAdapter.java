package com.kx.promote.ui.task_center;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kx.promote.R;
import com.kx.promote.bean.Group;
import com.kx.promote.bean.HistoryDateGroup;
import com.kx.promote.dao.TaskDao;
import com.kx.promote.ui.HomeActivity;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;
import com.kx.promote.utils.MyCallback;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HistoryDateGroupRecyclerViewAdapter extends RecyclerView.Adapter<HistoryDateGroupRecyclerViewAdapter.ViewHolder> {
    public List<HistoryDateGroup> getDateGroupList() {
        return dateGroupList;
    }
    private Handler handler = new MyHandler(this);
    protected static final int SHOW_TOAST = 0;
    protected static final int FRESH_GROUP_LIST = 1;

    public void setHistoryGroupList(List<HistoryDateGroup> dateGroupList) {
        this.dateGroupList = dateGroupList;
        notifyDataSetChanged();//更新数据源自动刷新
    }

    private List<HistoryDateGroup> dateGroupList = new ArrayList<>();
    private Context mContext;

    public HistoryDateGroupRecyclerViewAdapter(List<HistoryDateGroup> dateGroupList){
        this.dateGroupList = dateGroupList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_group,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final HistoryDateGroup dateGroup = dateGroupList.get(position);
        if(dateGroup==null)
            return;
        holder.orderNumberView.setText("总计"+dateGroup.getOrderNumber()+"单");
        holder.finishedOrderNumberView.setText("完成"+dateGroup.getFinishedOrderNumber()+"单");
        holder.groupNumberView.setText(dateGroup.getGroupNumber()+"组");
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        holder.dateView.setText(df.format(dateGroup.getDate()));
        holder.actpriceView.setText("实付"+dateGroup.getActPrice().toString()+"元");
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MyApplication.getContext());
        holder.groupListRecyclerView.setLayoutManager(mLayoutManager);
        final GroupRecyclerViewAdapter adapter = new GroupRecyclerViewAdapter(dateGroup.getGroupList());
        holder.groupListRecyclerView.setAdapter(adapter);
        holder.header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.body.getVisibility()==View.GONE) {
                    if(dateGroup.getGroupList()==null){
                        TaskDao dao = TaskDao.getInstance();
                        dao.getGroupListByDate(dateGroup.getDate(), new MyCallback() {
                            @Override
                            public void success(Msg msg) {
                                if(msg.getCode()==0) {
                                    List<Group> groupList = (List<Group>) msg.get("groupList");
                                    msg.add("adapter", adapter);
                                    handler.obtainMessage(FRESH_GROUP_LIST, msg).sendToTarget();
                                }
                                else{
                                    handler.obtainMessage(SHOW_TOAST,msg.getMsg()).sendToTarget();
                                }
                            }

                            @Override
                            public void failed(Msg msg) {
                                handler.obtainMessage(SHOW_TOAST,msg.getMsg()).sendToTarget();
                            }
                        });
                    }
                    holder.body.setVisibility(View.VISIBLE);
                }
                else
                    holder.body.setVisibility(View.GONE);
            }
        });
    }
    public void freshGroupList(GroupRecyclerViewAdapter adapter,List<Group> groupList){
        adapter.setGroupList(groupList);
    }
    @Override
    public int getItemCount() {
        if(dateGroupList==null)
            return 0;
        return this.dateGroupList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dateView;
        private TextView orderNumberView;
        private TextView groupNumberView;
        private TextView actpriceView;
        private TextView finishedOrderNumberView;
        private RecyclerView groupListRecyclerView;
        private View header;
        private View body;
        public ViewHolder(View itemView) {
            super(itemView);
            dateView = (TextView) itemView.findViewById(R.id.group_date);
            orderNumberView = (TextView) itemView.findViewById(R.id.order_number);
            groupNumberView = (TextView) itemView.findViewById(R.id.group_number);
            actpriceView = (TextView) itemView.findViewById(R.id.actprice);
            finishedOrderNumberView = (TextView) itemView.findViewById(R.id.order_number_finished);
            groupListRecyclerView = itemView.findViewById(R.id.group_list);
            header = itemView.findViewById(R.id.header);
            body = itemView.findViewById(R.id.body);
        }
    }
    static class MyHandler extends Handler {
        private WeakReference<HistoryDateGroupRecyclerViewAdapter> mOuter;

        public MyHandler(HistoryDateGroupRecyclerViewAdapter activity) {
            mOuter = new WeakReference<HistoryDateGroupRecyclerViewAdapter>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HistoryDateGroupRecyclerViewAdapter outer = mOuter.get();
            if (outer != null) {
                if(msg.what == SHOW_TOAST){
                    String text = (String) msg.obj;
                    Toast.makeText(MyApplication.getContext(),text,Toast.LENGTH_SHORT).show();
                }
                else if (msg.what == FRESH_GROUP_LIST) {
                    Msg m = (Msg) msg.obj;
                    GroupRecyclerViewAdapter adapter = (GroupRecyclerViewAdapter) m.get("adapter");
                    List<Group> groupList = (List<Group>) m.get("groupList");
                    outer.freshGroupList(adapter,groupList);
                }
            }
        }
    }
}
