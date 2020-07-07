package com.kx.promote.ui.task_center;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.kx.promote.R;
import com.kx.promote.bean.Group;
import com.kx.promote.bean.Order;
import com.kx.promote.dao.TaskDao;
import com.kx.promote.ui.LoginActivity;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;
import com.kx.promote.utils.MyCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.internal.concurrent.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayTaskFragment extends Fragment  {
    private List<Group> groupList;
    private GroupListFragment groupListFragment;
    private TextView groupNumberView;
    private TextView finishedGroupNumberView;
    private TextView finishedOrderNumberView;
    private TextView actpriceView;
    private TextView prepriceView;
    private View header;
    private Handler handler = new MyHandler(this);
    protected static final int SHOW_TOAST = 0;
    protected static final int UPDATE_UI = 1;

    private SwipeRefreshLayout swipeRefreshLayout;

    public TodayTaskFragment() {
        // Required empty public constructor
    }


    public static TodayTaskFragment newInstance(String param1, String param2) {
        TodayTaskFragment fragment = new TodayTaskFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_task, container, false);
        FragmentManager manager = getChildFragmentManager();
        groupListFragment = (GroupListFragment)manager.findFragmentById(R.id.group_list_fragment);
        groupNumberView = view.findViewById(R.id.group_number);
        finishedGroupNumberView = view.findViewById(R.id.group_finished_number);
        finishedOrderNumberView = view.findViewById(R.id.order_finished_number);
        actpriceView = view.findViewById(R.id.actprice);
        prepriceView = view.findViewById(R.id.preprice);
        header = view.findViewById(R.id.header);
        getTodayTask();
        swipeRefreshLayout= view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTodayTask();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        updateUI();
        return view;
    }

    public void updateUI(){
        if(groupList==null)
            groupList = new ArrayList<>();

        groupListFragment.setGroupList(groupList);
        groupNumberView.setText("总计"+groupList.size()+"组");
        int finishedGroupNumber = 0;
        int finishedOrderNumber = 0;
        BigDecimal allPreprice = new BigDecimal(0);
        BigDecimal allActprice = new BigDecimal(0);
        for(Group group:groupList){
            allPreprice = allPreprice.add(group.getPreprice());
            if(group.getState()==Group.FINISHED){
                allActprice = allActprice.add(group.getActprice());
                finishedGroupNumber++;
                finishedOrderNumber += group.getFinishedOrderNumber();
            }
        }
        finishedGroupNumberView.setText("完成"+finishedGroupNumber+"组");
        finishedOrderNumberView.setText("完成"+finishedOrderNumber+"单");
        actpriceView.setText("实付"+allActprice.toString()+"元");
        prepriceView.setText("预付"+allPreprice.toString()+"元");
        Resources resources = MyApplication.getContext().getResources();
        if(finishedGroupNumber==groupList.size()){
            header.setBackgroundColor(resources.getColor(R.color.success_background));
        }
    }
    public void getTodayTask(){
        TaskDao taskDao = TaskDao.getInstance();
        taskDao.getTodayGroupList(new MyCallback() {
            @Override
            public void success(Msg msg) {
                if(msg.getCode()==0){
                    groupList = (List<Group>) msg.get("groupList");
                    Message.obtain(handler,UPDATE_UI).sendToTarget();
                }
                else{
                    Message.obtain(handler,SHOW_TOAST,msg.getMsg()).sendToTarget();//提示用户
                }
            }

            @Override
            public void failed(Msg msg) {
                Message.obtain(handler,SHOW_TOAST,"请求失败！").sendToTarget();
            }
        });
    }

    static class MyHandler extends Handler {
        private WeakReference<TodayTaskFragment> mOuter;

        public MyHandler(TodayTaskFragment activity) {
            mOuter = new WeakReference<TodayTaskFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TodayTaskFragment outer = mOuter.get();
            if (outer != null) {
                if(msg.what == SHOW_TOAST){
                    String text = (String) msg.obj;
                    Toast.makeText(MyApplication.getContext(),text,Toast.LENGTH_SHORT).show();
                }
                else if (msg.what == UPDATE_UI) {
                    outer.updateUI();
                }
            }
        }
    }

}
