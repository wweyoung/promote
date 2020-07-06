package com.kx.promote.ui._do;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.kx.promote.R;
import com.kx.promote.bean.SerializableBean;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ImageUploaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageUploaderFragment extends Fragment implements Serializable {
    private List<String> urlList;
    private int max = 10;
    private ImageUploaderGridView imageView;
    private ImageUploaderAdapter adapter;
    public ImageUploaderFragment() {
        // Required empty public constructor
    }

    public static ImageUploaderFragment newInstance(List<String> urlList,Integer max) {
        ImageUploaderFragment fragment = new ImageUploaderFragment();
        Bundle args = new Bundle();
        SerializableBean<List<String>> data = new SerializableBean<>(urlList);
        args.putSerializable("urlList",data);
        args.putInt("max", max);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Serializable data = getArguments().getSerializable("data");
            if(data!=null){
                urlList = ((SerializableBean<List<String>>)data).getBean();
            }
            max = getArguments().getInt("max",99);
        }

    }
    public void set(List<String> urlList,int max){
        this.urlList = urlList;
        this.max = max;
        adapter.setUrlList(this.urlList);
    }
    public void add(List<String> urlList){
        this.urlList.addAll(urlList);
        adapter.setUrlList(this.urlList);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_uploader, container, false);
        imageView = view.findViewById(R.id.image_uploader);
        adapter = new ImageUploaderAdapter(urlList,max,this);
        imageView.setAdapter(adapter);
        return view;
    }
}
