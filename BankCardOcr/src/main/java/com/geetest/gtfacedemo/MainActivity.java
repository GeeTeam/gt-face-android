package com.geetest.gtfacedemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.geetest.oneperson.CardApi;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SCAN = 2;
    public static final int CARD_ORIENTATION_VERTICAL = 1;
    public static final int CARD_ORIENTATION_HORIZONTAL = 2;
    private TextView mTipsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.txt_app_name)).setText(
                getString(R.string.app_name_with_version, CardApi.getVersion()));
        ((TextView) findViewById(R.id.txt_copyright)).setText(getString(R.string.copyright));

        findViewById(R.id.txt_vertical_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionToDetect(CARD_ORIENTATION_VERTICAL);
            }
        });
        findViewById(R.id.txt_horizontal_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionToDetect(CARD_ORIENTATION_HORIZONTAL);
            }
        });

        mTipsView = (TextView) findViewById(R.id.txt_tips);
    }

    private void checkPermissionToDetect(int card) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissions = null;
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissions = new ArrayList<>();
                permissions.add(Manifest.permission.CAMERA);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (permissions == null) {
                    permissions = new ArrayList<>();
                }
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissions != null) {
                String[] permissionArray = new String[permissions.size()];
                permissions.toArray(permissionArray);
                requestPermissions(permissionArray, card);
            } else {
                startDetectActivity(card);
            }
        } else {
            startDetectActivity(card);
        }
    }

    private void startDetectActivity(int card) {
        Intent intent = new Intent(MainActivity.this, ScanningActivity.class);
        intent.putExtra(ScanningActivity.EXTRA_CARD_ORIENTATION, card);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN) {
            switch (resultCode) {
                case RESULT_CANCELED:
                    mTipsView.setVisibility(View.VISIBLE);
                    mTipsView.setText(R.string.canceled);
                    break;
                case Activity.RESULT_OK:
                    mTipsView.setVisibility(View.INVISIBLE);
                    mTipsView.setText("");
                    Intent intent = new Intent(data);
                    intent.setClass(MainActivity.this, BankCardResultActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        dealPermissionResult(requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void dealPermissionResult(int requestCode, int[] grantResults) {
        for (int g : grantResults) {
            if (g != PackageManager.PERMISSION_GRANTED) {
                mTipsView.setVisibility(View.VISIBLE);
                mTipsView.setText(R.string.error_camera);
                return;
            }
        }
        mTipsView.setVisibility(View.GONE);
        startDetectActivity(requestCode);
    }
}
