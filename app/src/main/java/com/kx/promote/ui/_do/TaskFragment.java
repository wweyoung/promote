package com.kx.promote.ui._do;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.kx.promote.R;
import com.kx.promote.bean.Order;
import com.kx.promote.utils.ViewFindUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText idET;
    private EditText keywordET;
    private EditText shopNameET;
    private EditText prepriceET;
    private EditText noteET;
    private EditText stateET;
    private EditText noET;
    private EditText actpriceET;

    // TODO: Rename and change types of parameters
    private Order order;

    public TaskFragment() {
        // Required empty public constructor
    }

    public static TaskFragment newInstance(Order order) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putSerializable("order",order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.order = (Order)getArguments().getSerializable("order");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_do_task, container, false);
        idET = ViewFindUtils.find(view,R.id.order_id);
        keywordET = ViewFindUtils.find(view,R.id.order_keyword);
        actpriceET  = ViewFindUtils.find(view,R.id.order_actprice);
        noET  = ViewFindUtils.find(view,R.id.order_no);
        noteET  = ViewFindUtils.find(view,R.id.order_note);
        prepriceET = ViewFindUtils.find(view,R.id.order_preprice);
        shopNameET = ViewFindUtils.find(view,R.id.order_shop);
        stateET = ViewFindUtils.find(view,R.id.order_state);
        this.updateUI();
        return view;
    }
    public void updateUI(){
        if(order==null)
            return;
        idET.setText(""+order.getId());
        keywordET.setText(order.getNeed().getKeyword());
        actpriceET.setText(order.getPrice().toString());
        noET.setText(order.getNo());
        noteET.setText(order.getNeed().getNote());
        prepriceET.setText(""+order.getNeed().getPrice());
        shopNameET.setText(order.getNeed().getShop().getName());
        stateET.setText(order.getStateString());
    }
}
