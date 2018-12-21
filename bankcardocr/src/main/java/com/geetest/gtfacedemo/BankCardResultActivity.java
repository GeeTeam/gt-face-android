package com.geetest.gtfacedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class BankCardResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_result);
        ((TextView) findViewById(R.id.txt_card_number)).setText(
                getIntent().getStringExtra(ScanningActivity.EXTRA_CARD_NUMBER));
        ((TextView) findViewById(R.id.txt_bank_name)).setText(
                getIntent().getStringExtra(ScanningActivity.EXTRA_BANK_NAME));
        ((TextView) findViewById(R.id.txt_bank_id)).setText(getIntent().getStringExtra(ScanningActivity.EXTRA_BANK_ID));
        ((TextView) findViewById(R.id.txt_card_name)).setText(
                getIntent().getStringExtra(ScanningActivity.EXTRA_CARD_NAME));
        ((TextView) findViewById(R.id.txt_card_type)).setText(
                getIntent().getStringExtra(ScanningActivity.EXTRA_CARD_TYPE));
    }
}
