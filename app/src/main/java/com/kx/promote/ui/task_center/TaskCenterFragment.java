package com.kx.promote.ui.task_center;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.kx.promote.R;
import com.kx.promote.entity.TabEntity;
import com.kx.promote.utils.ViewFindUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

public class TaskCenterFragment extends Fragment {
    private static final String TAG = "TaskCenterFragment";
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private CommonTabLayout header;
    private MyPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private View mDecorView;
    Random mRandom = new Random();
    public Handler handler = new MyHandler(this);
    private View view;
    private TodayTaskFragment todayTaskFragment;
    private HistoryTaskFragment historyTaskFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        mAdapter = new MyPagerAdapter(getFragmentManager());

        if (getArguments() != null) {

        }

    }

    public TodayTaskFragment getTodayTaskFragment() {
        return todayTaskFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_center, container, false);
        this.view = view;


        mViewPager = ViewFindUtils.find(view, R.id.task_center_body);//在view中寻找do_body
        mViewPager.setAdapter(mAdapter);
        /** with ViewPager */
        header = ViewFindUtils.find(view, R.id.task_center_header);

        initHeaderBar();
        updateUI();
        return view;
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
    private void updateUI(){
        mFragments.clear();
        mTabEntities.clear();
        todayTaskFragment = new TodayTaskFragment();
        mFragments.add(todayTaskFragment);
        mTabEntities.add(new TabEntity("今日任务", 0,0));
        historyTaskFragment = HistoryTaskFragment.newInstance();
        mFragments.add(historyTaskFragment);
        mTabEntities.add(new TabEntity("历史任务", 0,0));
        header.setTabData(mTabEntities);
        mAdapter.notifyDataSetChanged();
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
        final float scale = this.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    static class MyHandler extends Handler {
        private WeakReference<TaskCenterFragment> mOuter;

        public MyHandler(TaskCenterFragment activity) {
            mOuter = new WeakReference<TaskCenterFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TaskCenterFragment outer = mOuter.get();
            if (outer != null) {
            }
        }
    }

}
