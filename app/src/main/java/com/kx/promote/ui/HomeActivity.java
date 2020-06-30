package com.kx.promote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.kx.promote.R;
import com.kx.promote.entity.TabEntity;
import com.kx.promote.ui._do.OverviewFragment;
import com.kx.promote.ui._do.TaskFragment;
import com.kx.promote.utils.ViewFindUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    private DoFragment doFragment;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private String[] mTitles = {"任务中心", "当前任务", "我"};
    private final static int TASK_LIST = 0;
    private final static int DO_TASK = 1;
    private final static int USER_CENTER = 2;

    private int[] mIconUnselectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select};

    private CommonTabLayout header;
    private HomeActivity.MyPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private View mDecorView;
    Random mRandom = new Random();
    public Handler handler = new MyHandler(this);


    public final static int SHOW_MESSAGE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFragments.add(new OverviewFragment());
        doFragment = new DoFragment();
        mFragments.add(doFragment);
        for (int i =2;i<mTitles.length;i++) {
            mFragments.add(new TaskFragment());
//            mFragments.add(SimpleCardFragment.getInstance("Switch ViewPager " + title));
        }


        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }


        mDecorView = getWindow().getDecorView();
        mViewPager = ViewFindUtils.find(mDecorView, R.id.home_body);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        /** with ViewPager */
        header = ViewFindUtils.find(mDecorView, R.id.home_footer);
        initFooterBar();

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
            return mTitles[position];
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
    private void initFooterBar() {
        header.setTabData(mTabEntities);
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
    static class MyHandler extends Handler {
        private WeakReference<HomeActivity> mOuter;

        public MyHandler(HomeActivity activity) {
            mOuter = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HomeActivity outer = mOuter.get();
            if (outer != null) {
                if (msg.what == SHOW_MESSAGE) {
                    String tip = (String) msg.obj;
                    Toast.makeText(outer, tip, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
