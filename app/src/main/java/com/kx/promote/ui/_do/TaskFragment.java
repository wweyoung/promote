package com.kx.promote.ui._do;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kx.promote.R;
import com.kx.promote.bean.Order;
import com.kx.promote.ui.LoginActivity;
import com.kx.promote.utils.HttpUtil;
import com.kx.promote.utils.ViewFindUtils;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;

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
    private ImageView imageView;
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
    protected static final int IMAGE_UPDATE = 1;
    protected static final int ERROR = 2;
    private Handler handler = new MyHandler(this);

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
        imageView = ViewFindUtils.find(view,R.id.order_image);
        idET = ViewFindUtils.find(view,R.id.order_id);
        keywordET = ViewFindUtils.find(view,R.id.order_keyword);
        actpriceET  = ViewFindUtils.find(view,R.id.order_actprice);
        noET  = ViewFindUtils.find(view,R.id.order_no);
        noteET  = ViewFindUtils.find(view,R.id.order_note);
        prepriceET = ViewFindUtils.find(view,R.id.order_preprice);
        shopNameET = ViewFindUtils.find(view,R.id.order_shop);
        stateET = ViewFindUtils.find(view,R.id.order_state);
        noET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                order.setNo(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        actpriceET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();
                BigDecimal price = null;
                if(!text.isEmpty()){
                    price = new BigDecimal(text);
                }
                order.setPrice(price);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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
        HttpUtil.getImage(order.getNeed().getImage().getUrl()+getString(R.string.image_small),handler,IMAGE_UPDATE);
    }
    static class MyHandler extends Handler {
        private WeakReference<TaskFragment> mOuter;

        public MyHandler(TaskFragment activity) {
            mOuter = new WeakReference<TaskFragment>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TaskFragment outer = mOuter.get();
            if (outer != null) {
                if (msg.what == IMAGE_UPDATE) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    outer.imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
