package com.kx.promote.ui.task_center;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.kx.promote.R;
import com.kx.promote.bean.Group;
import com.kx.promote.dao.TaskDao;
import com.kx.promote.ui.LoginActivity;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;
import com.kx.promote.utils.MyCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
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
public class TodayTaskFragment extends Fragment {
    private List<Group> groupList;
    private GroupListFragment groupListFragment;
    private Handler handler = new MyHandler(this);
    protected static final int SHOW_TOAST = 0;
    protected static final int UPDATE_UI = 1;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_today_task, container, false);
        groupListFragment = (GroupListFragment)getChildFragmentManager().findFragmentById(R.id.group_list_fragment);
        getTodayTask();
        updateUI();
        return view;
    }
    public void updateUI(){
        groupListFragment.setGroupList(groupList);
    }
    public void getTodayTask(){
        TaskDao taskDao = TaskDao.getInstance();
        taskDao.getGroupListByDate(new Date(), new MyCallback() {
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
