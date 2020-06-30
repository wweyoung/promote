package com.kx.promote.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.kx.promote.R;
import com.kx.promote.bean.Group;
import com.kx.promote.bean.Order;
import com.kx.promote.entity.TabEntity;
import com.kx.promote.ui._do.OverviewFragment;
import com.kx.promote.ui._do.TaskFragment;
import com.kx.promote.utils.HttpUtil;
import com.kx.promote.utils.Msg;
import com.kx.promote.utils.MyApplication;
import com.kx.promote.utils.ViewFindUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.internal.concurrent.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String appPath;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private CommonTabLayout header;
    private MyPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private View view;
    private Group group;
    private HomeActivity homeActivity;

    private OverviewFragment overviewFragment;
    private List<TaskFragment> taskFragmentList;
    Random mRandom = new Random();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public Handler handler = new MyHandler(this);


    public final static int UPDATE_UI = 0;

    public DoFragment() {
        // Required empty public constructor
     }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoFragment newInstance(String param1, String param2) {
        DoFragment fragment = new DoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_do, container, false);
        appPath = getString(R.string.app_path);
        this.view = view;
        this.homeActivity = (HomeActivity) getActivity();

        mViewPager = ViewFindUtils.find(view, R.id.do_body);//在view中寻找do_body
        mAdapter = new MyPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mAdapter);
        /** with ViewPager */
        header = ViewFindUtils.find(view, R.id.do_header);
        initHeaderBar();
        if(group==null){
            getGroup(3390);
        }
        updateUI();
        return view;
    }
    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabEntities.get(position).getTabTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    protected int dp2px(float dp) {
        final float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void initHeaderBar() {
        if(mTabEntities!=null && !mTabEntities.isEmpty()) {
            header.setTabData(mTabEntities);
        }
        header.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    header.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(header.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                header.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(0);

    }
    public void updateUI(){
        mFragments.clear();
        mTabEntities.clear();
        if(group==null || group.getOrderlist()==null)
            return;
        overviewFragment = OverviewFragment.newInstance(group);
        mFragments.add(overviewFragment);
        mTabEntities.add(new TabEntity("概览", 0,0));
        for(Order order:group.getOrderlist()){
            TaskFragment taskFragment = TaskFragment.newInstance(order);
            mFragments.add(taskFragment);
            TabEntity tabEntity = new TabEntity(String.valueOf(mTabEntities.size()), R.mipmap.tab_speech_select, R.mipmap.tab_speech_unselect);
            mTabEntities.add(tabEntity);
        }
        mFragments.add(new TaskFragment());
        mTabEntities.add(new TabEntity("提交", 0,0));

        header.setTabData(mTabEntities);
        mAdapter.notifyDataSetChanged();
    }
    public void getGroup(Integer groupId){
        String url = appPath+"/interface/worker/do";
        if(groupId!=null)
            url+="/"+groupId;
        HttpUtil.get(url, new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Log.d("doFragment", json);
                Msg msg = JSON.parseObject(json,Msg.class);
                group = JSONObject.toJavaObject((JSON) msg.get("group"),Group.class);
                Message message = new Message();
                message.what = homeActivity.SHOW_MESSAGE;
                message.obj = msg.getMsg();
                homeActivity.handler.sendMessage(message);
                message = new Message();
                message.what = UPDATE_UI;
                handler.sendMessage(message);
            }
        });
    }
    static class MyHandler extends Handler {
        private WeakReference<DoFragment> mOuter;

        public MyHandler(DoFragment activity) {
            mOuter = new WeakReference<DoFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DoFragment outer = mOuter.get();
            if (outer != null) {
                if (msg.what == UPDATE_UI) {
                    outer.updateUI();
                }
            }
        }
    }

}
