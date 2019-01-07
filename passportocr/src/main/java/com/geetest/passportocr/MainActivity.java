package com.geetest.passportocr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.geetest.oneperson.CardApi;

import java.util.ArrayList;
import java.util.List;
@Route(path = "/passportocr/activity")
public class MainActivity extends AppCompatActivity {
    private TextView mTipsView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_passport);

        ((TextView) findViewById(R.id.txt_app_name)).setText(
                getString(R.string.app_name_with_client_passport, CardApi.getVersion()));
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
                requestPermissions(permissionArray, 0);
            } else {
                startActivity(new Intent(getApplicationContext(), UploadImageActivity.class));
            }
        } else {
            startActivity(new Intent(getApplicationContext(), UploadImageActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {

            dealPermissionResult(requestCode, grantResults);
        }
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
        startActivity(new Intent(getApplicationContext(), UploadImageActivity.class));
    }
}
