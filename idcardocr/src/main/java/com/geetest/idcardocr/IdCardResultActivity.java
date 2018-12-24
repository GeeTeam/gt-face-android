package com.geetest.idcardocr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.geetest.oneperson.idcard.ScanSide;

public class IdCardResultActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card_result);
        if (getIntent().getStringExtra(IdCardActivity.side).equals(ScanSide.ID_CARD_FRONT)) {
            ((TextView) findViewById(R.id.txt_name)).setText(
                    getIntent().getStringExtra(IdCardActivity.name));
            ((TextView) findViewById(R.id.txt_sex)).setText(
                    getIntent().getStringExtra(IdCardActivity.gender));
            ((TextView) findViewById(R.id.txt_nation)).setText(
                    getIntent().getStringExtra(IdCardActivity.nation));
            ((TextView) findViewById(R.id.txt_birthday)).setText(
                    getIntent().getStringExtra(IdCardActivity.year) + "_" + getIntent().getStringExtra(IdCardActivity.month) + "_" + getIntent().getStringExtra(IdCardActivity.day));
            ((TextView) findViewById(R.id.txt_address)).setText(
                    getIntent().getStringExtra(IdCardActivity.address));
            ((TextView) findViewById(R.id.txt_number)).setText(
                    getIntent().getStringExtra(IdCardActivity.number));
        } else {
            ((TextView) findViewById(R.id.txt_timelimit)).setText(
                    getIntent().getStringExtra(IdCardActivity.timelimit));
            ((TextView) findViewById(R.id.txt_authority)).setText(
                    getIntent().getStringExtra(IdCardActivity.authority));
        }
    }
}
