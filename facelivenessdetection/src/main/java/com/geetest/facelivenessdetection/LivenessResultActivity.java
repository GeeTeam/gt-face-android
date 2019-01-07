package com.geetest.facelivenessdetection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class LivenessResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liveness_result_liveness);
        ((TextView) findViewById(R.id.txt_pass)).setText(
                getIntent().getBooleanExtra(ClientActivity.PASSED, false) + "");
        ((TextView) findViewById(R.id.txt_score)).setText(
                getIntent().getFloatExtra(ClientActivity.LIVENESS_SCORE, 0.1f) + ""
        );
    }
}
