package com.geetest.slientimageverification;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.InputStream;


public class UploadActivity extends AppCompatActivity {
    public static final int CODE_REQUEST_CODE_FIRST = 0;
    private static final String FILES_PATH = Environment.getExternalStorageDirectory().getPath() + "/geetest";
    private static final String imagePath = FILES_PATH + "/image.jpg";
    public static final String IMAEG_PATH = "image_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_slient_verification);
        findViewById(R.id.btn_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CODE_REQUEST_CODE_FIRST);

            }
        });
        findViewById(R.id.btn_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ClientActivity.class);
                intent.putExtra(IMAEG_PATH, imagePath);
                startActivityForResult(intent,0);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CODE_REQUEST_CODE_FIRST == requestCode && null != data) {
            if (RESULT_CANCELED == resultCode) {
                Intent intent = new Intent(data);
                intent.setClass(UploadActivity.this, ResultActivity.class);
                startActivity(intent);
            }else {
                Uri selectImageUri = data.getData();
                Bitmap bitmap = compressBitmap(this, selectImageUri);
                ImageSizeUtils.saveBitmapToFile(bitmap, imagePath);
                Toast.makeText(getApplicationContext(), "照片选择成功", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public static Bitmap compressBitmap(Context context, Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        decodeBitmap(context, uri, options);
        options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        Bitmap bitmap = null;

        try {
            bitmap = decodeBitmap(context, uri, options);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap decodeBitmap(Context context, Uri uri, BitmapFactory.Options options) {
        Bitmap bitmap = null;

        if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            try {
                /**
                 * 将图片的Uri地址转换成一个输入流
                 */
                inputStream = cr.openInputStream(uri);

                /**
                 * 将输入流转换成Bitmap
                 */
                bitmap = BitmapFactory.decodeStream(inputStream, null, options);

                assert inputStream != null;
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
