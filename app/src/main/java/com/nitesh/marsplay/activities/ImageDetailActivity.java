package com.nitesh.marsplay.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.nitesh.marsplay.R;
import com.nitesh.marsplay.models.EventObject;
import com.nitesh.marsplay.models.Resource;

import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

public class ImageDetailActivity extends BaseActivity {

    public static final String IMAGE_DETAIL = "IMAGE_DETAIL";

    @BindView(R.id.photo_view)
    PhotoView photoView;
    @BindView(R.id.progress)
    ProgressBar progressBar;

    @Override
    protected int getLayout() {
        return R.layout.activity_image_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Resource resource = getIntent().getParcelableExtra(IMAGE_DETAIL);
        Glide.with(this)
                .load(resource.getSecureUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        photoView.setImageResource(R.drawable.ic_image_place_holder);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(photoView);

    }

    @Subscribe
    @Override
    public void onEvent(final EventObject eventObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }
}
