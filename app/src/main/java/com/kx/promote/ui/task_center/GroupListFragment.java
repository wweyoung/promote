package com.kx.promote.ui.task_center;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kx.promote.R;
import com.kx.promote.bean.Group;
import com.kx.promote.bean.SerializableBean;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GroupListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupListFragment extends Fragment {
    private List<Group> groupList;
    private GroupRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    public GroupListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GroupListFragment newInstance(List<Group> groupList) {
        GroupListFragment fragment = new GroupListFragment();
        Bundle args = new Bundle();
        SerializableBean<List<Group>> bean = new SerializableBean<>(groupList);
        args.putSerializable("groupList",bean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SerializableBean<List<Group>> bean = (SerializableBean<List<Group>>) getArguments().getSerializable("groupList");
            groupList = bean.getBean();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.group_list);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        setGroupList(groupList);
        return view;
    }
    public void setGroupList(List<Group> groupList){
        this.groupList = groupList;
        adapter = new GroupRecyclerViewAdapter(groupList);
        recyclerView.setAdapter(adapter);
    }
}
