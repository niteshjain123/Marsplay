package com.nitesh.marsplay.utilities;

import android.net.Uri;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.UploadRequest;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;
import com.nitesh.marsplay.MarsPlayAssignmentApplication;
import com.nitesh.marsplay.interfaces.AssignmentConstants;
import com.nitesh.marsplay.models.EventObject;
import com.nitesh.marsplay.models.ImagesUploaded;
import com.nitesh.marsplay.models.Resource;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CloudinaryHelper implements AssignmentConstants {


    public static void uploadResource(Uri resource) {
        UploadRequest request = MediaManager.get().upload(resource)
                .unsigned("jzeasvuk")
                .constrain(TimeWindow.getDefault())
                .option("resource_type", "auto")
                .maxFileSize(10 * 1024 * 1024)
                .policy(MediaManager.get().getGlobalUploadPolicy().newBuilder().maxRetries(2).build())
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        EventBus.getDefault().post(new EventObject(AssignmentConstants.EventCenter.IMAGE_UPLOADED_SUCCESSFULLY, null));
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        EventBus.getDefault().post(new EventObject(AssignmentConstants.EventCenter.UNABLE_TO_UPLOAD_IMAGE, null));
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                });

        request.dispatch(MarsPlayAssignmentApplication.getInstance());
    }

    public static void getAllImages() {
        BackgroundExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url(Api.CLOUDINARY_MARSPLAY_FOLDER_IMAGES_ENDPOINT)
                        .get()
                        .addHeader(Api.AUTHORIZATION, Api.BASIC_AUTHORIZATION_SECRET_KEY)
                        .build();

                try {
                    Response apiResponse = client.newCall(request).execute();
                    if (apiResponse != null) {
                        if (apiResponse.code() == HttpURLConnection.HTTP_OK) {
                            String json = apiResponse.body().string();
                            ImagesUploaded imagesUploaded = (ImagesUploaded) AssignmentUtil.fromJson(json, ImagesUploaded.class);
                            if (!imagesUploaded.getResources().isEmpty()) {
                                List<Resource> resourceArrayList = imagesUploaded.getResources();
                                for (Resource resource : resourceArrayList) {
                                    resource.setTimeStamp(AssignmentUtil
                                            .getTimeStamp(resource.getCreatedAt()
                                                            .replaceAll("Z", "")
                                                            .replaceAll("T", " "),
                                                    RESOURCE_DATE_FORMAT));
                                }
                                Collections.sort(resourceArrayList, new ResourceSort(ResourceSort.SORT_ORDER_DESC));
                                EventBus.getDefault().post(new EventObject(EventCenter.GET_ALL_IMAGE_SUCCESSFUL, resourceArrayList));
                            } else {
                                EventBus.getDefault().post(new EventObject(EventCenter.NO_IMAGE_UPLOADED_YET, null));
                            }
                        } else {
                            EventBus.getDefault().post(new EventObject(EventCenter.UNABLE_TO_GET_ALL_IMAGES, null));
                        }
                    } else {
                        EventBus.getDefault().post(new EventObject(EventCenter.NO_INTERNET_CONNECTION, null));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    EventBus.getDefault().post(new EventObject(EventCenter.NO_INTERNET_CONNECTION, null));
                }
            }
        });
    }
}
