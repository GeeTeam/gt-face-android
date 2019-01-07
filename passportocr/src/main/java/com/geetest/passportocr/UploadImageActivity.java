package com.geetest.passportocr;

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
import com.geetest.oneperson.impl.BaseCardListener;
import com.geetest.oneperson.passport.PassPortInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadImageActivity extends AppCompatActivity {
    public static final int CODE_REQUEST_CODE_PASSPORT = 0;
    private static final String FILES_PATH = Environment.getExternalStorageDirectory().getPath() + "/geetest";
    private static final String passPortImagePath = FILES_PATH + "/passport_image.jpg";

    private CardApi cardApi;

    public static final String rfidMrz = "rfidMrz";
    public static final String issueLocation = "issueLocation";
    public static final String ocrMrz = "ocrMrz";
    public static final String passportNumber = "passportNumber";
    public static final String idNumber = "idNumber";
    public static final String birthPlace = "birthPlace";
    public static final String firstName = "firstName";

    public static final String passPortNumberMrz = "passPortNumberMrz";
    public static final String lastName = "lastName";

    public static final String effectiveTime = "effectiveTime";
    public static final String mrz2 = "mrz2";
    public static final String issueLocationChina = "issueLocationChina";

    public static final String country = "country";
    public static final String englishName = "englishName";

    public static final String passPortType = "passPortType";
    public static final String issueData = "issueData";

    public static final String englishNameLast = "englishNameLast";
    public static final String sex = "sex";
    public static final String countryName = "countryName";
    public static final String birthDate = "birthDate";
    public static final String birthDateChina = "birthDateChina";

    public static final String mrz1 = "mrz1";
    public static final String issueCountry = "issueCountry";
    public static final String englishFirstName = "englishFirstName";
    public static final String height = "height";
    public static final String avatar = "avatar";

    public void onImageSuccess(PassPortInfo passPortInfo) {
        if (passPortInfo != null) {
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);

            intent.putExtra(rfidMrz, passPortInfo.getRfidMrz());
            intent.putExtra(issueLocation, passPortInfo.getIssueLocation());
            intent.putExtra(ocrMrz, passPortInfo.getOcrMrz());
            intent.putExtra(passportNumber, passPortInfo.getPassportNumber());
            intent.putExtra(idNumber, passPortInfo.getIdNumber());
            intent.putExtra(birthPlace, passPortInfo.getBirthPlace());
            intent.putExtra(firstName, passPortInfo.getFirstName());


            intent.putExtra(passPortNumberMrz, passPortInfo.getPassPortNumberMrz());
            intent.putExtra(lastName, passPortInfo.getLastName());

            intent.putExtra(effectiveTime, passPortInfo.getEffectiveTime());
            intent.putExtra(mrz2, passPortInfo.getMrz2());
            intent.putExtra(issueLocationChina, passPortInfo.getIssueLocationChina());

            intent.putExtra(country, passPortInfo.getCountry());
            intent.putExtra(englishName, passPortInfo.getEnglishName());

            intent.putExtra(passPortType, passPortInfo.getPassPortType());
            intent.putExtra(issueData, passPortInfo.getIssueData());

            intent.putExtra(englishNameLast, passPortInfo.getEnglishNameLast());
            intent.putExtra(sex, passPortInfo.getSex());
            intent.putExtra(countryName, passPortInfo.getCountryName());
            intent.putExtra(birthDate, passPortInfo.getBirthDate());
            intent.putExtra(birthDateChina, passPortInfo.getBirthDateChina());

            intent.putExtra(mrz1, passPortInfo.getMrz1());
            intent.putExtra(issueCountry, passPortInfo.getIssueCountry());
            intent.putExtra(englishFirstName, passPortInfo.getEnglishFirstName());
            intent.putExtra(height, passPortInfo.getHeight());
            intent.putExtra(avatar, passPortInfo.getAvatar());

            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image_passport);
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
                        try

                        {
                            JSONObject data = new JSONObject(httpResult);
                            String rfidMrz = data.getString("RFID MRZ");
                            String issueLocation = data.getString("签发地点");
                            String ocrMrz = data.getString("OCR MRZ");
                            String passportNumber = data.getString("护照号码");
                            String idNumber = data.getString("身份证号码");
                            String birthPlace = data.getString("出生地点");
                            String firstName = data.getString("本国姓");

                            String passPortNumberMrz = data.getString("护照号码MRZ");
                            String lastName = data.getString("本国姓名");

                            String effectiveTime = data.getString("有效期至");
                            String mrz2 = data.getString("MRZ2");
                            String issueLocationChina = data.getString("签发地点拼音");

                            String country = data.getString("持证人国籍代码");
                            String englishName = data.getString("英文姓名");

                            String passPortType = data.getString("护照类型");
                            String issueData = data.getString("签发日期");

                            String englishNameLast = data.getString("英文名");
                            String sex = data.getString("性别");
                            String countryName = data.getString("本国名");
                            String birthDate = data.getString("出生日期");
                            String birthDateChina = data.getString("出生地点拼音");

                            String mrz1 = data.getString("MRZ1");
                            String issueCountry = data.getString("签发国代码");
                            String englishFirstName = data.getString("英文姓");
                            String height = data.getString("身高");
                            String image = data.getString("头像");
                            onImageSuccess(new PassPortInfo(rfidMrz, issueLocation, ocrMrz, passportNumber, idNumber, birthPlace, firstName, passPortNumberMrz, lastName, effectiveTime, mrz2, issueLocationChina, country, englishName, passPortType, issueData, englishNameLast, sex,
                                    countryName, birthDate, birthDateChina, mrz1, issueCountry, englishFirstName, height, image));
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
                                                                 cardApi.inputPassPortImage(passPortImagePath);

                                                             }
                                                         }

        );

        findViewById(R.id.btn_passport_image).setOnClickListener(new View.OnClickListener() {
                                                                     @Override
                                                                     public void onClick(View view) {
                                                                         Intent intent = new Intent(
                                                                                 Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                                         startActivityForResult(intent, CODE_REQUEST_CODE_PASSPORT);

                                                                     }
                                                                 }

        );

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (CODE_REQUEST_CODE_PASSPORT == requestCode && null != data) {
            Uri selectImageUri = data.getData();
            Bitmap bitmap = compressBitmap(this, selectImageUri);
            ImageSizeUtils.saveBitmapToFile(bitmap, passPortImagePath);
            Toast.makeText(getApplicationContext(), "照片选择成功", Toast.LENGTH_SHORT).show();
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
