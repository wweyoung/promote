package com.kx.promote.ui._do;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kx.promote.R;
import com.kx.promote.bean.Group;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubmitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubmitFragment extends Fragment {
    private EditText actpriceET;
    private EditText customerET;
    private EditText noteET;
    private Button submitButton;

    private Group group;

    public SubmitFragment() {
        // Required empty public constructor
    }

    public static SubmitFragment newInstance(Group group) {
        SubmitFragment fragment = new SubmitFragment();
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
        View view = inflater.inflate(R.layout.fragment_do_submit, container, false);
        actpriceET = view.findViewById(R.id.group_actprice);
        customerET = view.findViewById(R.id.group_customer);
        noteET = view.findViewById(R.id.group_note);
        submitButton = view.findViewById(R.id.group_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        updateUI();
        return view;
    }
    private void updateUI(){
        actpriceET.setText(group.getActprice()+"元");
        customerET.setText(group.getCustomer());
        noteET.setText(group.getNote());
    }
    private void submit(){

    }
}
