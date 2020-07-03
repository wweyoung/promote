package com.kx.promote.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kx.promote.R;
import com.kx.promote.bean.User;
import com.kx.promote.utils.MyApplication;

import okhttp3.internal.concurrent.Task;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserCenterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserCenterFragment extends Fragment {
    private User user;
    private TextView userInfo;
    private TextView userPhone;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserCenterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserCenterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserCenterFragment newInstance(String param1, String param2) {
        UserCenterFragment fragment = new UserCenterFragment();
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
        View view = inflater.inflate(R.layout.fragment_user_center, container, false);

        //设置用户信息
        user = MyApplication.getUser();
        String userInfoString=user.getName();
        String userPhoneString = user.getPhone();
        userInfo = view.findViewById(R.id.userInfo);
        userPhone = view.findViewById(R.id.userPhone);
        userInfo.setText(userInfoString);
        userPhone.setText(userPhoneString);

        Button btn_finishLogin = (Button)view.findViewById(R.id.btn_finish);
        btn_finishLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String a = user.getName();
                if (a ==null){
                    a ="空";
                }
                Toast.makeText(getActivity(),a,Toast.LENGTH_SHORT).show();
            }
        });
        Button btn_modifyMyInfo = (Button)view.findViewById(R.id.modifyMyInfo);
        btn_modifyMyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity homeActivity = (HomeActivity)getActivity();
                Intent intent = new Intent(homeActivity,UpdateUserInfoActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(),"点击了修改信息按钮",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void OutLogin(){
        if(user!=null){
            user = null;
        }
    }
}
