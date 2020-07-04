package com.kx.promote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.kx.promote.R;
import com.kx.promote.ui._do.ImageUploaderFragment;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ImageUploaderTestActivity extends AppCompatActivity {

    private ImageUploaderFragment imageUploaderFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_uploader_test);
        imageUploaderFragment = (ImageUploaderFragment) getSupportFragmentManager().findFragmentById(R.id.order_submit_image);
        List<String> urlList = new ArrayList<>();
        urlList.add("http://qiniu.taodongli.net/upload/20-07-04/60500477f9084cf5a78b44be30b28fa4.png");
        urlList.add("http://qiniu.taodongli.net/9f3362ab8b67408a8171b83202a356aa.png");
        urlList.add("http://qiniu.taodongli.net/7ad9b04a26b1469f9ed8786643116818.jpeg");
        urlList.add("http://qiniu.taodongli.net/088f8b216fee488fae7210ee16f5ec34.png");
        imageUploaderFragment.set(urlList,9);
    }
}
