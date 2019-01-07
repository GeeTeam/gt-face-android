package com.geetest.g4project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;

public class MainActivity extends AppCompatActivity {

    private TextView bankCardTextView;
    private TextView idCardTextView;
    private TextView passPortTextView;
    private TextView livenessTextView;
    private TextView imageVerificationTextView;
    private TextView slientImageVerificationTextView;
    private TextView imageIdNumberTextView;
    private TextView slientImageIdNumberTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bankCardTextView = (TextView) findViewById(R.id.bankcardocr_tv);
        bankCardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/bankcardocr/activity").navigation();
            }
        });

        idCardTextView = (TextView) findViewById(R.id.idcardocr_tv);
        idCardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/idcardocr/activity").navigation();
            }
        });

        passPortTextView = (TextView) findViewById(R.id.passportocr_tv);
        passPortTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/passportocr/activity").navigation();
            }
        });

        livenessTextView = (TextView) findViewById(R.id.liveness_tv);
        livenessTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/liveness/activity").navigation();
            }
        });

        imageVerificationTextView = (TextView) findViewById(R.id.imageverification_tv);
        imageVerificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/imageverification/activity").navigation();
            }
        });


        slientImageVerificationTextView = (TextView) findViewById(R.id.slientimageverification_tv);
        slientImageVerificationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/slientverification/activity").navigation();
            }
        });

        imageIdNumberTextView = (TextView) findViewById(R.id.imageidnumber_tv);
        imageIdNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/imageidnumber/activity").navigation();
            }
        });

        slientImageIdNumberTextView = (TextView) findViewById(R.id.slientimageidnumber_tv);
        slientImageIdNumberTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build("/slientidnumber/activity").navigation();
            }
        });
    }
}
