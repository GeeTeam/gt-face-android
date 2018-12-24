package com.geetest.idcardocr;

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
    private static final int REQUEST_CODE_RESULT = 1;
    private static final int REQUEST_CODE_SCAN = 2;
    private TextView mTipsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView) findViewById(R.id.txt_app_name)).setText(
                getString(R.string.app_name_with_version_ID, CardApi.getVersion()));
        ((TextView) findViewById(R.id.txt_copyright)).setText(getString(R.string.copyright));

        findViewById(R.id.txt_front_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionToDetect();
            }
        });
        mTipsView = (TextView) findViewById(R.id.txt_tips);
    }
    private void checkPermissionToDetect() {
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
                /// Request the permission. The result will be received
                // in onRequestPermissionResult()
                requestPermissions(permissionArray, 0);
            } else {
                startDetectActivity();
            }
        } else {
            startDetectActivity();
        }
    }
    private void startDetectActivity() {

        Intent intent = new Intent(MainActivity.this, IdCardActivity.class);
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
                    intent.setClass(MainActivity.this, IdCardResultActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        dealPermissionsResult(requestCode, grantResults);

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void dealPermissionsResult(int requestCode, int[] grantResults) {
        for (int g : grantResults) {
            if (g != PackageManager.PERMISSION_GRANTED) {
                //permission denied
                mTipsView.setVisibility(View.VISIBLE);
                mTipsView.setText(R.string.error_camera);
                return;
            }
        }
        //permission allowed
        mTipsView.setVisibility(View.GONE);
        startDetectActivity();
    }
}
