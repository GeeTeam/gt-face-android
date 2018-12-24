package com.geetest.imageidnumber;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ((TextView) findViewById(R.id.txt_score)).setText(
                getIntent().getFloatExtra(NumberActivity.VERIFICATION_SCORE, 0.1f) + ""
        );
    }
}
