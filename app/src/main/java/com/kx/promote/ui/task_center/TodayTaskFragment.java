package com.kx.promote.ui.task_center;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kx.promote.R;
import com.kx.promote.bean.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayTaskFragment extends Fragment {
    private List<Group> groupList;
    private GroupListFragment groupListFragment;
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
        List<Group> groupList = new ArrayList<>();
        for(int i=0;i<15;i++){
            Group group = new Group();
            group.setId(i*16);
            groupList.add(group);
        }
        groupListFragment.setGroupList(groupList);
        return view;
    }
}
