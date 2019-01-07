package com.geetest.imageverification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_image_verification);
        ((TextView) findViewById(R.id.txt_score)).setText(
                getIntent().getFloatExtra(UploadImageActivity.VERIFICATION_SCORE, 0.1f) + ""
        );
    }
}
