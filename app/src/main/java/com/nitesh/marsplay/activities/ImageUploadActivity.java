package com.nitesh.marsplay.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nitesh.marsplay.R;
import com.nitesh.marsplay.models.EventObject;
import com.nitesh.marsplay.utilities.CloudinaryHelper;
import com.yalantis.ucrop.view.UCropView;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;

public class ImageUploadActivity extends BaseActivity {

    private static final String TAG = "ImageUploadActivity";

    @Override
    protected int getLayout() {
        return R.layout.activity_image_upload;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (uri != null) {
            try {
                UCropView uCropView = findViewById(R.id.ucrop);
                uCropView.getCropImageView().setImageUri(uri, null);
                uCropView.getOverlayView().setShowCropFrame(false);
                uCropView.getOverlayView().setShowCropGrid(false);
                uCropView.getOverlayView().setDimmedColor(Color.TRANSPARENT);
            } catch (Exception e) {
                Log.e(TAG, "setImageUri", e);
                showToast(e.getMessage());
            }
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(getIntent().getData().getPath()).getAbsolutePath(), options);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.format_crop_result_d_d, options.outWidth, options.outHeight));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_upload, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_upload) {
            uploadCroppedImage();
        } else if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            onBackPressed();
            showToast(getString(R.string.toast_no_upload_action));
        } else {
            setResult(RESULT_CANCELED);
            showToast(getString(R.string.toast_no_upload_action));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_WRITE_ACCESS_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadCroppedImage();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void uploadCroppedImage() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_needed),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Uri imageUri = getIntent().getData();
            if (imageUri != null && imageUri.getScheme().equals("file")) {
                try {
                    showProgress(getString(R.string.progress_text_uploading));
                    CloudinaryHelper.uploadResource(getIntent().getData());
                } catch (Exception e) {
                    showToast(e.getMessage());
                    Log.e(TAG, imageUri.toString(), e);
                }
            } else {
                showToast(getString(R.string.toast_unexpected_error));
            }
        }
    }


    @Subscribe
    @Override
    public void onEvent(final EventObject eventObject) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (eventObject.getId()) {
                    case EventCenter.IMAGE_UPLOADED_SUCCESSFULLY:
                        showToast(getString(R.string.toast_image_upload_successful));
                        dismissProgress();
                        setResult(RESULT_OK);
                        finish();
                        break;
                    case EventCenter.UNABLE_TO_UPLOAD_IMAGE:
                        showToast(getString(R.string.toast_image_upload_un_successful));
                        dismissProgress();
                        break;
                }
            }
        });
    }
}
