package com.geetest.idcardocr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.geetest.idcardocr.camera.OverlayView;
import com.geetest.idcardocr.camera.SenseCamera;
import com.geetest.idcardocr.camera.SenseCameraPreview;
import com.geetest.oneperson.CardApi;
import com.geetest.oneperson.code.CardType;
import com.geetest.oneperson.idcard.IdCardInfo;
import com.geetest.oneperson.idcard.ScanSide;
import com.geetest.oneperson.impl.BaseCardListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class IdCardActivity extends AppCompatActivity implements Camera.PreviewCallback {
    public static final String EXTRA_IS_ONLY_NAME = "extra_is_only_name";

    protected static final int DEFAULT_PREVIEW_WIDTH = 1280;
    protected static final int DEFAULT_PREVIEW_HEIGHT = 960;
    protected SenseCameraPreview mCameraPreview;
    protected SenseCamera mCamera;

    protected boolean mOnlyNameNumber = false;
    protected View mLoadingView = null;

    protected OverlayView mOverlayView = null;

    private CardApi cardApi;
    public static final String number = "number";
    public static final String address = "address";
    public static final String name = "name";
    public static final String year = "year";
    public static final String month = "month";
    public static final String day = "day";
    public static final String nation = "nation";
    public static final String gender = "gender";
    public static final String timelimit = "timelimit";
    public static final String authority = "authority";
    public static final String side = "side";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card);
        cardApi = new CardApi(this);
        cardApi.init(new BaseCardListener() {

            @Override
            public void onError(int code, String error) {
                Toast.makeText(getApplicationContext(), "code:" + code + "--------" + "error:" + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResult(final String token) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> map = new HashMap<>(4);
                        map.put("token", token);
                        String httpResult = HttpUtils.postHttpOfMap(API.CARD_RESULT, map, 15000);
                        try {
                            JSONObject data = new JSONObject(httpResult);
                            JSONObject info = data.getJSONObject("info");
                            JSONObject validity = data.getJSONObject("validity");
                            String side = data.getString("side");
                            if (side.equals(ScanSide.ID_CARD_FRONT)) {
                                IdCardInfo idCardInfo = new IdCardInfo(data.getString("type"), info.getString("number"), info.getString("address"), info.getString("name"), info.getString("year"),
                                        info.getString("month"), info.getString("day"), info.getString("nation"), info.getString("gender"), data.getString("side"), validity.getBoolean("birthday"), validity.getBoolean("number"),
                                        validity.getBoolean("address"), validity.getBoolean("gender"), validity.getBoolean("name"));
                                onIdCardSuccess(idCardInfo);
                            } else {
                                IdCardInfo idCardInfo = new IdCardInfo(data.getString("type"), info.getString("timelimit"), info.getString("authority"),
                                        validity.getBoolean("timelimit"), validity.getBoolean("authority"), data.getString("side"));
                                onIdCardSuccess(idCardInfo);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }, API.API_KEY, API.API_SECRET);
        findViewById(R.id.btn_play_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardApi.play(CardType.BC_CARDTYPE_IDCARD);
            }
        });
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mOnlyNameNumber = getIntent().getBooleanExtra(EXTRA_IS_ONLY_NAME, false);


        mOverlayView = ((OverlayView) findViewById(R.id.overlay));
        mOverlayView.setIsVerticalCard(false);
        mLoadingView = findViewById(R.id.pb_loading);
        mOverlayView.setText(R.string.scan_tip_auto, Color.WHITE);
        this.mCameraPreview = (SenseCameraPreview) this.findViewById(R.id.camera_preview);
        this.mCamera =
                new SenseCamera.Builder(this).setRequestedPreviewSize(DEFAULT_PREVIEW_WIDTH, DEFAULT_PREVIEW_HEIGHT)
                        .build();
    }

    public void onIdCardSuccess(IdCardInfo idCardInfo) {
        if (idCardInfo != null) {
            Intent intent = new Intent();
            intent.putExtra(side, idCardInfo.getSide());
            if (idCardInfo.getSide().equals(ScanSide.ID_CARD_FRONT)) {
                intent.putExtra(number, idCardInfo.getNumber());
                intent.putExtra(address, idCardInfo.getAddress());
                intent.putExtra(name, idCardInfo.getName());
                intent.putExtra(year, idCardInfo.getYear());
                intent.putExtra(month, idCardInfo.getMonth());
                intent.putExtra(day, idCardInfo.getDay());
                intent.putExtra(nation, idCardInfo.getNation());
                intent.putExtra(gender, idCardInfo.getGender());
            } else {
                intent.putExtra(timelimit, idCardInfo.getTimelimit());
                intent.putExtra(authority, idCardInfo.getAuthority());
            }
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        cardApi.inputScanImage(data, this.mCamera.getPreviewSize(),
                this.mCameraPreview.convertViewRectToCameraPreview(this.mOverlayView.getCardRect()),
                this.mCamera.getRotationDegrees());
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            this.mCameraPreview.start(this.mCamera);
            this.mCamera.setOnPreviewFrameCallback(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        cardApi.cancel();
        this.mCameraPreview.stop();
        this.mCameraPreview.release();

        this.mLoadingView.setVisibility(View.GONE);

        setResult(RESULT_CANCELED);

        if (!isFinishing()) {
            finish();
        }
    }

}
