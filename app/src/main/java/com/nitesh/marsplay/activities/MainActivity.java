package com.nitesh.marsplay.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.nitesh.marsplay.R;
import com.nitesh.marsplay.adapters.UploadedImageAdapter;
import com.nitesh.marsplay.models.EventObject;
import com.nitesh.marsplay.models.Resource;
import com.nitesh.marsplay.utilities.CloudinaryHelper;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public static final int COMPRESSION_QUALITY = 75;
    public static final String RESOURCE_LIST = "RESOURCE_LIST";
    ArrayList<Resource> resourceArrayList = new ArrayList<>();

    @BindView(R.id.rv_uploaded_images)
    RecyclerView recyclerViewUploadedImages;
    UploadedImageAdapter uploadedImageAdapter;

    @BindView(R.id.empty_container)
    LinearLayout emptyContainer;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(swipeRefreshListener);
        initUploadedImageAdapter(savedInstanceState);
    }

    SwipeRefreshLayout.OnRefreshListener swipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            performSwipeRefresh();
        }
    };

    void performSwipeRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        resourceArrayList.clear();
        CloudinaryHelper.getAllImages();
    }

    void initUploadedImageAdapter(Bundle bundle) {
        recyclerViewUploadedImages.setLayoutManager(new LinearLayoutManager(this));
        if (bundle != null && bundle.getParcelableArrayList(RESOURCE_LIST) != null) {
            resourceArrayList = bundle.getParcelableArrayList(RESOURCE_LIST);
            if (!resourceArrayList.isEmpty()) {
                emptyContainer.setVisibility(View.GONE);
            }
            uploadedImageAdapter = new UploadedImageAdapter(resourceArrayList, this);
            recyclerViewUploadedImages.setAdapter(uploadedImageAdapter);
        } else {
            uploadedImageAdapter = new UploadedImageAdapter(resourceArrayList, this);
            recyclerViewUploadedImages.setAdapter(uploadedImageAdapter);
            fetchAllImages();
        }
    }

    private void fetchAllImages() {
        resourceArrayList.clear();
        showProgress(getString(R.string.progress_text_fetching_images));
        CloudinaryHelper.getAllImages();
    }

    @Subscribe
    @Override
    public void onEvent(final EventObject eventObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case EventCenter.NO_IMAGE_UPLOADED_YET:
                        commonOperation();
                        showToast(getString(R.string.no_image_uploaded_yet));
                        break;
                    case EventCenter.GET_ALL_IMAGE_SUCCESSFUL:
                        emptyContainer.setVisibility(View.GONE);
                        resourceArrayList.clear();
                        resourceArrayList.addAll((ArrayList<Resource>) eventObject.getObject());
                        uploadedImageAdapter.notifyDataSetChanged();
                        showToast(getString(R.string.toast_get_all_image_successful));
                        swipeRefreshLayout.setRefreshing(false);
                        dismissProgress();
                        break;
                    case EventCenter.UNABLE_TO_GET_ALL_IMAGES:
                        commonOperation();
                        showToast(getString(R.string.toast_unexpected_error));
                        break;
                    case EventCenter.ClickEvents.ON_UPLOADED_IMAGE_CLICK:
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ImageDetailActivity.IMAGE_DETAIL, (Resource) eventObject.getObject());
                        Intent imageDetailIntent = new Intent();
                        imageDetailIntent.setClass(MainActivity.this, ImageDetailActivity.class);
                        imageDetailIntent.putExtras(bundle);
                        startActivity(imageDetailIntent);
                        break;

                    case EventCenter.NO_INTERNET_CONNECTION:
                        commonOperation();
                        showToast(getString(R.string.toast_no_internet_connection));
                        break;
                }
            }
        });
    }

    void commonOperation() {
        resourceArrayList.clear();
        uploadedImageAdapter.notifyDataSetChanged();
        emptyContainer.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        dismissProgress();
    }

    @OnClick(R.id.fab)
    public void onImageUploadButtonClick() {
        Pix.start(MainActivity.this, RequestCodes.PICK_IMAGE_FROM_GALLERY_OR_CAMERA);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sync) {
            performSwipeRefresh();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case RequestCodes.PICK_IMAGE_FROM_GALLERY_OR_CAMERA:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> imageList = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);
                    startCrop(Uri.fromFile(new File(imageList.get(0))));
                } else {
                    showToast(getString(R.string.toast_no_image_selected));
                }
                break;


            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    handleCropResult(data);
                } else {
                    showToast(getString(R.string.toast_no_cropping_performed));
                }
                break;

            case RequestCodes.UPLOAD_IMAGE_TO_CLOUDINARY:
                if (resultCode == RESULT_OK) {
                    performSwipeRefresh();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Pix.start(MainActivity.this, RequestCodes.PICK_IMAGE_FROM_GALLERY_OR_CAMERA);
                } else {
                    showToast(getString(R.string.toast_please_allow_permission));
                }
                break;
            }
        }
    }

    private void startCrop(@NonNull Uri uri) {
        String destinationFileName = ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = uCrop.useSourceImageAspectRatio();

        UCrop.Options options = new UCrop.Options();

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(COMPRESSION_QUALITY);

        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(true);

        uCrop = uCrop.withOptions(options);

        uCrop.start(MainActivity.this);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            Intent intent = new Intent(MainActivity.this, ImageUploadActivity.class);
            intent.setData(resultUri);
            startActivityForResult(intent, RequestCodes.UPLOAD_IMAGE_TO_CLOUDINARY);
        } else {
            showToast(getString(R.string.toast_crop_image_not_retrieved));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(RESOURCE_LIST, resourceArrayList);
        super.onSaveInstanceState(outState);
    }
}
