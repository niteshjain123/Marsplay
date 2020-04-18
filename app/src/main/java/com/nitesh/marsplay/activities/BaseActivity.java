package com.nitesh.marsplay.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nitesh.marsplay.R;
import com.nitesh.marsplay.interfaces.AssignmentConstants;
import com.nitesh.marsplay.models.EventObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Vector;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity implements AssignmentConstants {
    public static final int SHOW_TOAST = 0;
    public static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    public static final int REQUEST_STORAGE_WRITE_ACCESS_PERMISSION = 102;

    private AlertDialog mAlertDialog;

    private PauseHandler pauseHandler = new PauseHandler();
    private ProgressDialog progressDialog;

    protected abstract
    @LayoutRes
    int getLayout();

    @Subscribe
    public abstract void onEvent(EventObject eventObject);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        injectViews();
        EventBus.getDefault().register(this);
        initProgressDialog(getString(R.string.progress_text_uploading));
    }

    private void initProgressDialog(String msg) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
    }

    public void showProgress(String msg) {
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    public void dismissProgress() {
        progressDialog.dismiss();
    }

    private void injectViews() {
        ButterKnife.bind(this);
    }

    public void sendMessageToHandler(int what, int arg1, int arg2, Object response) {
        Message message = pauseHandler.obtainMessage();
        message.obj = response;
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        pauseHandler.sendMessage(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pauseHandler.setBaseActivity(this);
        pauseHandler.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseHandler.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        progressDialog.dismiss();
        super.onDestroy();
    }

    public boolean isActivityPaused() {
        return pauseHandler.isPaused();
    }

    public void processMessage(Message message) {
        switch (message.what) {
            case SHOW_TOAST:
                String msg = (String) message.obj;
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void showToast(String toastMessage) {
        sendMessageToHandler(SHOW_TOAST, -1, 1, toastMessage);
    }

    private class PauseHandler extends Handler {

        /**
         * Message Queue Buffer
         */
        final Vector<Message> messageQueueBuffer = new Vector<>();
        BaseActivity baseActivity;
        /**
         * Flag indicating the pause state
         */
        private boolean paused;

        /**
         * Resume the handler
         */
        final void resume() {
            paused = false;

            while (messageQueueBuffer.size() > 0) {
                final Message msg = messageQueueBuffer.elementAt(0);
                messageQueueBuffer.removeElementAt(0);
                sendMessage(msg);
            }
        }

        /**
         * Pause the handler
         */
        final void pause() {
            paused = true;
        }

        boolean isPaused() {
            return paused;
        }

        final void setBaseActivity(BaseActivity baseActivity) {
            this.baseActivity = baseActivity;
        }

        boolean storeMessage(Message message) {
            return true;
        }


        @Override
        final public void handleMessage(Message msg) {
            if (paused) {
                if (storeMessage(msg)) {
                    Message msgCopy = new Message();
                    msgCopy.copyFrom(msg);
                    messageQueueBuffer.add(msgCopy);
                }
            } else {
                processMessage(msg);
            }
        }
    }

    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            showAlertDialog(getString(R.string.permission_needed), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseActivity.this,
                                    new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.ok), null, getString(R.string.cancel));
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }


}
