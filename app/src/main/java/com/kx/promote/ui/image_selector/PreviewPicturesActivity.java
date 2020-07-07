package com.kx.promote.ui.image_selector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.kx.promote.utils.MyApplication;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.kx.promote.R;

import java.io.File;
import java.util.ArrayList;

public class PreviewPicturesActivity extends Activity {
    ViewPager pager;
    MyPagerAdapter adapter;
    private ArrayList<String> picList = new ArrayList<>();
    private TextView tvCancel, tvSend;

    //preview image
    public static void preViewSingle(Activity activity, String url) {
        Intent intent = new Intent(activity, PreviewPicturesActivity.class);
        ArrayList<String> pic = new ArrayList<>();
        pic.add(url);
        intent.putExtra("pics", pic);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_pictures);
        pager = (ViewPager) findViewById(R.id.pager);

        picList = getIntent().getStringArrayListExtra("pics");
        if (picList.size() != 0) {
            adapter = new MyPagerAdapter();
            pager.setAdapter(adapter);
        }

    }

    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return picList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view.equals(o);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(PreviewPicturesActivity.this, R.layout.item_image, null);
            SimpleDraweeView imageView = (SimpleDraweeView) view.findViewById(R.id.iv_pic);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            String path = picList.get(position);
            PipelineDraweeControllerBuilder controllerBuilder = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(ImageRequest.fromUri(path))
                    .setOldController(imageView.getController());
            if(path.indexOf("http")==0){
                controllerBuilder.setLowResImageRequest(ImageRequest.fromUri(path+ MyApplication.getImageSmall()));
            }
            DraweeController  controller = controllerBuilder.build();
            imageView.setController(controller);

            GenericDraweeHierarchy hierarchy = imageView.getHierarchy();
            hierarchy.setProgressBarImage(new ProgressBarDrawable());
            imageView.setHierarchy(hierarchy);
            container.addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            System.gc();
        }
    }

}
