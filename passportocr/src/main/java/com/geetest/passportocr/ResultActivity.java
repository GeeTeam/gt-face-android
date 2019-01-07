package com.geetest.passportocr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_passport);
        ((TextView) findViewById(R.id.txt_tv1)).setText(
                getIntent().getStringExtra(UploadImageActivity.rfidMrz)
        );
        ((TextView) findViewById(R.id.txt_tv2)).setText(
                getIntent().getStringExtra(UploadImageActivity.issueLocation)
        );
        ((TextView) findViewById(R.id.txt_tv3)).setText(
                getIntent().getStringExtra(UploadImageActivity.ocrMrz)
        );
        ((TextView) findViewById(R.id.txt_tv4)).setText(
                getIntent().getStringExtra(UploadImageActivity.passportNumber)
        );
        ((TextView) findViewById(R.id.txt_tv5)).setText(
                getIntent().getStringExtra(UploadImageActivity.idNumber)
        );
        ((TextView) findViewById(R.id.txt_tv6)).setText(
                getIntent().getStringExtra(UploadImageActivity.birthPlace)
        );
        ((TextView) findViewById(R.id.txt_tv7)).setText(
                getIntent().getStringExtra(UploadImageActivity.firstName)
        );
        ((TextView) findViewById(R.id.txt_tv8)).setText(
                getIntent().getStringExtra(UploadImageActivity.passPortNumberMrz)
        );
        ((TextView) findViewById(R.id.txt_tv9)).setText(
                getIntent().getStringExtra(UploadImageActivity.lastName)
        );
        ((TextView) findViewById(R.id.txt_tv10)).setText(
                getIntent().getStringExtra(UploadImageActivity.effectiveTime)
        );
        ((TextView) findViewById(R.id.txt_tv11)).setText(
                getIntent().getStringExtra(UploadImageActivity.mrz2)
        );
        ((TextView) findViewById(R.id.txt_tv12)).setText(
                getIntent().getStringExtra(UploadImageActivity.issueLocationChina)
        );
        ((TextView) findViewById(R.id.txt_tv13)).setText(
                getIntent().getStringExtra(UploadImageActivity.country)
        );
        ((TextView) findViewById(R.id.txt_tv14)).setText(
                getIntent().getStringExtra(UploadImageActivity.englishName)
        );
        ((TextView) findViewById(R.id.txt_tv15)).setText(
                getIntent().getStringExtra(UploadImageActivity.passPortType)
        );
        ((TextView) findViewById(R.id.txt_tv16)).setText(
                getIntent().getStringExtra(UploadImageActivity.issueData)
        );
        ((TextView) findViewById(R.id.txt_tv17)).setText(
                getIntent().getStringExtra(UploadImageActivity.englishNameLast)
        );
        ((TextView) findViewById(R.id.txt_tv18)).setText(
                getIntent().getStringExtra(UploadImageActivity.sex)
        );
        ((TextView) findViewById(R.id.txt_tv19)).setText(
                getIntent().getStringExtra(UploadImageActivity.countryName)
        );
        ((TextView) findViewById(R.id.txt_tv20)).setText(
                getIntent().getStringExtra(UploadImageActivity.birthDate)
        );
        ((TextView) findViewById(R.id.txt_tv21)).setText(
                getIntent().getStringExtra(UploadImageActivity.birthDateChina)
        );
        ((TextView) findViewById(R.id.txt_tv22)).setText(
                getIntent().getStringExtra(UploadImageActivity.mrz1)
        );
        ((TextView) findViewById(R.id.txt_tv23)).setText(
                getIntent().getStringExtra(UploadImageActivity.issueCountry)
        );
        ((TextView) findViewById(R.id.txt_tv24)).setText(
                getIntent().getStringExtra(UploadImageActivity.englishFirstName)
        );
        ((TextView) findViewById(R.id.txt_tv25)).setText(
                getIntent().getStringExtra(UploadImageActivity.height)
        );

        ImageView imageView = (ImageView) findViewById(R.id.iv1);
        imageView.setImageBitmap(base64ToBitmap(getIntent().getStringExtra(UploadImageActivity.avatar)));
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }
}
