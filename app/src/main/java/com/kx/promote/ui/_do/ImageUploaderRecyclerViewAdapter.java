package com.kx.promote.ui._do;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kx.promote.R;
import com.kx.promote.bean.Group;
import com.kx.promote.ui.HomeActivity;
import com.kx.promote.ui.task_center.GroupRecyclerViewAdapter;
import com.kx.promote.ui.task_center.OrderRecyclerViewAdapter;
import com.kx.promote.utils.MyApplication;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImageUploaderRecyclerViewAdapter extends RecyclerView.Adapter<ImageUploaderRecyclerViewAdapter.ViewHolder>{
    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
        notifyDataSetChanged();//更新数据源自动刷新
    }

    private List<Group> groupList = new ArrayList<>();
    private Context mContext;

    public ImageUploaderRecyclerViewAdapter(List<Group> dataList){
        this.groupList = dataList;
    }
    @NonNull
    @Override
    public ImageUploaderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group,parent,false);
        ImageUploaderRecyclerViewAdapter.ViewHolder holder = new ImageUploaderRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageUploaderRecyclerViewAdapter.ViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        if(groupList==null)
            return 0;
        return this.groupList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
;
        private View body;
        public ViewHolder(View itemView) {
            super(itemView);

        }
    }

}
