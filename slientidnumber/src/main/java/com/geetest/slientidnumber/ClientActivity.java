package com.geetest.slientidnumber;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.geetest.oneperson.CardApi;
import com.geetest.oneperson.idnumber.SilentIdNumberInfo;
import com.geetest.oneperson.impl.BaseCardListener;
import com.geetest.slientidnumber.widget.MediaUtils;
import com.geetest.slientidnumber.widget.SendView;
import com.geetest.slientidnumber.widget.VideoProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ClientActivity extends AppCompatActivity {

    private MediaUtils mediaUtils;
    private boolean isCancel;
    private VideoProgressBar progressBar;
    private int mProgress;
    private TextView btnInfo, btn;
    private TextView view;
    private SendView send;
    private RelativeLayout recordLayout, switchLayout;
    private CardApi cardApi;
    public static final String LIVENESS_SCORE = "liveness_score";
    public static final String PASSED = "passed";
    public static final String VERIFICATION_SCORE = "verification_score";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_slient_idnumber);
        cardApi = new CardApi(getApplicationContext());
        cardApi.init(new BaseCardListener() {
            @Override
            public void onError(int code, String error) {
                Toast.makeText(getApplicationContext(), code + error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResult(final String token) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> map = new HashMap<>(4);
                        map.put("token", token);
                        String httpResult = HttpUtils.postHttpOfMap(API.CARD_RESULT, map, 5000);
                        try {
                            JSONObject data = new JSONObject(httpResult);
                            boolean passed = data.getBoolean("passed");
                            if (passed) {
                                float liveness_score = Float.parseFloat(data.get("liveness_score").toString());
                                float verification_score = Float.parseFloat(data.get("verification_score").toString());
                                onSilentIdNumberSuccess(new SilentIdNumberInfo(true, liveness_score, verification_score));
                            } else {
                                onSilentIdNumberSuccess(new SilentIdNumberInfo(false, 0.0f, 0.0f));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }, API.API_KEY, API.API_SECRET);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.main_surface_view);
        // setting
        mediaUtils = new MediaUtils(this);
        mediaUtils.setRecorderType(MediaUtils.MEDIA_VIDEO);
        File file = Environment.getExternalStorageDirectory(); // SD卡根目录
        file = new File(file.getPath() + "/recorder");
        if (!file.exists()) {
            file.mkdirs();
        }
        mediaUtils.setTargetDir(file);
        mediaUtils.setTargetName("video_geetest" + ".mp4");
        mediaUtils.setSurfaceView(surfaceView);
        // btn
        send = (SendView) findViewById(R.id.view_send);
//        view = (TextView) findViewById(R.id.view);
        btnInfo = (TextView) findViewById(R.id.tv_info);
        btn = (TextView) findViewById(R.id.main_press_control);
        btn.setOnTouchListener(btnTouch);
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.backLayout.setOnClickListener(backClick);
        send.selectLayout.setOnClickListener(selectClick);
        recordLayout = (RelativeLayout) findViewById(R.id.record_layout);
        switchLayout = (RelativeLayout) findViewById(R.id.btn_switch);
        switchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaUtils.switchCamera();
            }
        });
        // progress
        progressBar = (VideoProgressBar) findViewById(R.id.main_progress_bar);
        progressBar.setOnProgressEndListener(listener);
        progressBar.setCancel(true);


    }

    public void onSilentIdNumberSuccess(SilentIdNumberInfo silentIdNumberInfo) {
        if (silentIdNumberInfo != null) {
            Intent intent = new Intent();

            intent.putExtra(LIVENESS_SCORE, silentIdNumberInfo.getLiveness_score());
            intent.putExtra(PASSED, silentIdNumberInfo.isPassed());
            intent.putExtra(VERIFICATION_SCORE, silentIdNumberInfo.getVerification_score());

            setResult(RESULT_OK, intent);
        }
        finish();
    }

    View.OnTouchListener btnTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            boolean ret = false;
            float downY = 0;
            int action = event.getAction();

            int i = v.getId();
            if (i == R.id.main_press_control) {
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        mediaUtils.record();
                        startView();
                        ret = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!isCancel) {
                            if (mProgress == 0) {
                                stopView(false);
                                break;
                            }
                            if (mProgress < 10) {
                                //时间太短不保存
                                mediaUtils.stopRecordUnSave();
                                Toast.makeText(ClientActivity.this, "时间太短", Toast.LENGTH_SHORT).show();
                                stopView(false);
                                break;
                            }
                            //停止录制
                            mediaUtils.stopRecordSave();
                            stopView(true);
                        } else {
                            //现在是取消状态,不保存
                            mediaUtils.stopRecordUnSave();
                            Toast.makeText(ClientActivity.this, "取消保存", Toast.LENGTH_SHORT).show();
                            stopView(false);
                        }
                        ret = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float currentY = event.getY();
                        isCancel = downY - currentY > 10;
                        moveView();
                        break;
                }
            }
            return ret;
        }
    };

    VideoProgressBar.OnProgressEndListener listener = new VideoProgressBar.OnProgressEndListener() {
        @Override
        public void onProgressEndListener() {
            progressBar.setCancel(true);
            mediaUtils.stopRecordSave();
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    progressBar.setProgress(mProgress);
                    if (mediaUtils.isRecording()) {
                        mProgress = mProgress + 1;
                        sendMessageDelayed(handler.obtainMessage(0), 100);
                    }
                    break;
            }
        }
    };

    private void startView() {
        startAnim();
        mProgress = 0;
        handler.removeMessages(0);
        handler.sendMessage(handler.obtainMessage(0));
    }

    private void moveView() {
        if (isCancel) {
            btnInfo.setText("松手取消");
        } else {
            btnInfo.setText("上滑取消");
        }
    }

    private void stopView(boolean isSave) {
        stopAnim();
        progressBar.setCancel(true);
        mProgress = 0;
        handler.removeMessages(0);
        btnInfo.setText("双击放大");
        if (isSave) {
            recordLayout.setVisibility(View.GONE);
            send.startAnim();
        }
    }

    private void startAnim() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(btn, "scaleX", 1, 0.5f),
                ObjectAnimator.ofFloat(btn, "scaleY", 1, 0.5f),
                ObjectAnimator.ofFloat(progressBar, "scaleX", 1, 1.3f),
                ObjectAnimator.ofFloat(progressBar, "scaleY", 1, 1.3f)
        );
        set.setDuration(250).start();
    }

    private void stopAnim() {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(btn, "scaleX", 0.5f, 1f),
                ObjectAnimator.ofFloat(btn, "scaleY", 0.5f, 1f),
                ObjectAnimator.ofFloat(progressBar, "scaleX", 1.3f, 1f),
                ObjectAnimator.ofFloat(progressBar, "scaleY", 1.3f, 1f)
        );
        set.setDuration(250).start();
    }

    private View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            send.stopAnim();
            recordLayout.setVisibility(View.VISIBLE);
            mediaUtils.deleteTargetFile();
        }
    };

    private View.OnClickListener selectClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String path = mediaUtils.getTargetFilePath();
            cardApi.inputSilentIdNumber(path, getIntent().getStringExtra("name"), getIntent().getStringExtra("idcard"));
//            cardApi.inputSlientLiveness(path);
//            Toast.makeText(ClientActivity.this, "文件以保存至：" + path, Toast.LENGTH_SHORT).show();
            send.stopAnim();
            recordLayout.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setCancel(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cardApi.cancel();
    }


}
