package com.kx.promote.ui._do;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kx.promote.R;
import com.kx.promote.bean.Group;
import com.kx.promote.bean.Order;
import com.kx.promote.utils.HttpUtil;
import com.kx.promote.utils.ViewFindUtils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {
    private Group group;

    private EditText idET;
    private EditText dateET;
    private EditText orderNumberET;
    private EditText prepriceET;
    private EditText stateET;

    public OverviewFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OverviewFragment newInstance(Group group) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        args.putSerializable("group",group);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            group = (Group) getArguments().getSerializable("group");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_do_overview, container, false);
        idET  = ViewFindUtils.find(view,R.id.group_id);
        dateET  = ViewFindUtils.find(view,R.id.group_time);
        orderNumberET  = ViewFindUtils.find(view,R.id.group_number);
        prepriceET  = ViewFindUtils.find(view,R.id.group_preprice);
        stateET  = ViewFindUtils.find(view,R.id.group_state);
        updateUI();
        return view;
    }
    public void updateUI(){
        if(group==null)
            return;
        idET.setText(""+group.getId());
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        dateET.setText(df.format(group.getTime()));
        orderNumberET.setText(group.getOrderlist().size()+"单");
        prepriceET.setText(getPrePrice().floatValue()+"元");
        stateET.setText(group.getStateString());
    }
    public BigDecimal getPrePrice(){
        BigDecimal preprice = new BigDecimal(0);
        if(group==null || group.getOrderlist()==null)
            return preprice;
        for(Order order:group.getOrderlist()){
            preprice = preprice.add(order.getNeed().getPrice());
        }
        return preprice;
    }

}
