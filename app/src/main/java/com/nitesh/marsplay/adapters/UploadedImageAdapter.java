package com.nitesh.marsplay.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nitesh.marsplay.R;
import com.nitesh.marsplay.interfaces.AssignmentConstants;
import com.nitesh.marsplay.models.EventObject;
import com.nitesh.marsplay.models.Resource;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class UploadedImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AssignmentConstants {

    private ArrayList<Resource> resourceArrayList;
    private Context context;

    public UploadedImageAdapter(ArrayList<Resource> resourceArrayList, Context context) {
        this.resourceArrayList = resourceArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            default:
                View defaultView = inflater.inflate(R.layout.item_uploaded_photo, viewGroup, false);
                viewHolder = new UploadedImageViewHolder(defaultView);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            default:
                UploadedImageViewHolder defaultViewHolder = (UploadedImageViewHolder) viewHolder;
                configureUploadedImageViewHolder(defaultViewHolder, position);
                break;
        }
    }

    private void configureUploadedImageViewHolder(final UploadedImageViewHolder uploadedImageViewHolder, int position) {
        Glide.with(context)
                .load(resourceArrayList.get(position).getSecureUrl())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        uploadedImageViewHolder.progressBar.setVisibility(View.GONE);
                        uploadedImageViewHolder.iVUploadedImage.setImageResource(R.drawable.ic_image_place_holder);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        uploadedImageViewHolder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(uploadedImageViewHolder.iVUploadedImage);
    }

    @Override
    public int getItemCount() {
        return resourceArrayList.size();
    }

    public class UploadedImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iVUploadedImage;
        ProgressBar progressBar;

        UploadedImageViewHolder(View view) {
            super(view);
            iVUploadedImage = view.findViewById(R.id.iv_uploaded_image);
            progressBar = view.findViewById(R.id.progress);
            iVUploadedImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_uploaded_image:
                    EventBus.getDefault().post(
                            new EventObject(
                                    EventCenter.ClickEvents.ON_UPLOADED_IMAGE_CLICK,
                                    resourceArrayList.get(getAdapterPosition())));
                    break;
            }
        }
    }

}