package com.geetest.imageverification;

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

import com.geetest.oneperson.CardApi;
import com.geetest.oneperson.image.ImageInfo;
import com.geetest.oneperson.impl.BaseCardListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadImageActivity extends AppCompatActivity {
    public static final int CODE_REQUEST_CODE_FIRST = 0;
    public static final int CODE_REQUEST_CODE_SECOND = 1;
    private static final String FILES_PATH = Environment.getExternalStorageDirectory().getPath() + "/geetest";
    private static final String firstImagePath = FILES_PATH + "/first_image.jpg";
    private static final String secondImagePath = FILES_PATH + "/second_image.jpg";
    private CardApi cardApi;
    public static final String VERIFICATION_SCORE = "verification_score";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        cardApi = new CardApi(getApplicationContext());
        cardApi.init(new BaseCardListener() {
            @Override
            public void onError(int code, String error) {
                Toast.makeText(getApplicationContext(), code + error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResult(final String token) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> map = new HashMap<>(4);
                        map.put("token", token);
                        String httpResult = HttpUtils.postHttpOfMap(API.CARD_RESULT, map, 5000);
                        try {
                            JSONObject data = new JSONObject(httpResult);
                            float score = Float.parseFloat(data.get("verification_score").toString());
                            onImageSuccess(new ImageInfo(score));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }, API.API_KEY, API.API_SECRET);
        File dir = new File(FILES_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardApi.inputImageVerification(firstImagePath, secondImagePath);

            }
        });
        findViewById(R.id.btn_first_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CODE_REQUEST_CODE_FIRST);

            }
        });
        findViewById(R.id.btn_second_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CODE_REQUEST_CODE_SECOND);

            }
        });
    }

    public void onImageSuccess(ImageInfo imageInfo) {
        if (imageInfo != null) {
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);

            intent.putExtra(VERIFICATION_SCORE, imageInfo.getVerification_score());

            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CODE_REQUEST_CODE_FIRST == requestCode && null != data) {
            Uri selectImageUri = data.getData();
            Bitmap bitmap = compressBitmap(this, selectImageUri);
            ImageSizeUtils.saveBitmapToFile(bitmap, firstImagePath);
            Toast.makeText(getApplicationContext(),"第一张照片选择成功",Toast.LENGTH_SHORT).show();
        } else if (CODE_REQUEST_CODE_SECOND == requestCode && null != data) {
            Uri selectImageUri = data.getData();
            Bitmap bitmap = compressBitmap(this, selectImageUri);
            ImageSizeUtils.saveBitmapToFile(bitmap, secondImagePath);
            Toast.makeText(getApplicationContext(),"第二张照片选择成功",Toast.LENGTH_SHORT).show();
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
