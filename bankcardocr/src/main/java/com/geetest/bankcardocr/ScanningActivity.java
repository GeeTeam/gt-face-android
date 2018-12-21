package com.geetest.bankcardocr;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.geetest.bankcardocr.camera.OverlayView;
import com.geetest.bankcardocr.camera.SenseCamera;
import com.geetest.bankcardocr.camera.SenseCameraPreview;
import com.geetest.oneperson.CardApi;
import com.geetest.oneperson.bankcard.BankCardInfo;
import com.geetest.oneperson.code.CardType;
import com.geetest.oneperson.impl.BaseCardListener;

import org.json.JSONException;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScanningActivity extends AppCompatActivity implements Camera.PreviewCallback {
    protected static final int DEFAULT_PREVIEW_WIDTH = 1280;
    protected static final int DEFAULT_PREVIEW_HEIGHT = 960;
    public static final String EXTRA_CARD_ORIENTATION = "extra_card_orientation";
    protected SenseCameraPreview mCameraPreview;
    public static final int CARD_ORIENTATION_VERTICAL = 1;
    public static final int CARD_ORIENTATION_HORIZONTAL = 2;
    protected SenseCamera mCamera;
    /**
     * 结果验证
     * 需要网站主配置该接口进行结果校验
     */
    private static final String CARD_RESULT = "";
    protected View mLoadingView = null;

    protected OverlayView mOverlayView = null;

    private CardApi cardApi;
    public static final String EXTRA_CARD_NUMBER = "card_number";
    public static final String EXTRA_BANK_NAME = "bank_name";
    public static final String EXTRA_BANK_ID = "bank_id";
    public static final String EXTRA_CARD_NAME = "card_name";
    public static final String EXTRA_CARD_TYPE = "card_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        int cardOrientation = getIntent().getIntExtra(EXTRA_CARD_ORIENTATION, CARD_ORIENTATION_HORIZONTAL);
        cardApi = new CardApi(this);
        cardApi.init(new BaseCardListener() {

            @Override
            public void onError(int code, String error) {
                Toast.makeText(getApplicationContext(), "error:" + code, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResult(final String token) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> map = new HashMap<>(4);
                        map.put("token", token);
                        String httpResult = HttpUtils.postHttpOfMap(CARD_RESULT, map, 5000);
                        try {
                            JSONObject jsonObject1 = new JSONObject(httpResult);
                            BankCardInfo bankCardInfo = new BankCardInfo(jsonObject1.getString("bank_name"), jsonObject1.getString("bank_identification_number"), jsonObject1.getString("card_number"), jsonObject1.getString("card_type"), jsonObject1.getString("card_name"));
                            onBankCardSuccess(bankCardInfo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }, API.API_KEY, API.API_SECRET);
        findViewById(R.id.img_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardApi.play(CardType.BC_CARDTYPE_BANKCARD);
            }
        });
        mOverlayView = ((OverlayView) findViewById(R.id.overlay));
        mOverlayView.setIsVerticalCard(cardOrientation == CARD_ORIENTATION_VERTICAL);
        mLoadingView = findViewById(R.id.pb_loading);
        mOverlayView.setText(R.string.scan_tip, Color.WHITE);
        this.mCameraPreview = (SenseCameraPreview) this.findViewById(R.id.camera_preview);
        this.mCamera =
                new SenseCamera.Builder(this).setRequestedPreviewSize(DEFAULT_PREVIEW_WIDTH, DEFAULT_PREVIEW_HEIGHT)
                        .build();
    }


    public void onBankCardSuccess(BankCardInfo bankCardInfo) {
        if (bankCardInfo != null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_CARD_NUMBER, bankCardInfo.getCard_number());
            intent.putExtra(EXTRA_BANK_NAME, bankCardInfo.getBank_name());
            intent.putExtra(EXTRA_BANK_ID, bankCardInfo.getBank_identification_number());
            intent.putExtra(EXTRA_CARD_NAME, bankCardInfo.getCard_name());
            intent.putExtra(EXTRA_CARD_TYPE, bankCardInfo.getCard_type());
            setResult(RESULT_OK, intent);
        }
        finish();
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

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        cardApi.inputScanImage(bytes, this.mCamera.getPreviewSize(),
                this.mCameraPreview.convertViewRectToCameraPreview(this.mOverlayView.getCardRect()),
                this.mCamera.getRotationDegrees());
    }
}
