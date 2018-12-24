package com.geetest.passportocr;


import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.X509TrustManager;

/**
 * Created by 谷闹年 on 2017/8/30.
 */


public class HttpUtils {
    private static final String CHARSET = "utf-8";
    private static final String TAG = "HttpUtils";
    private static final String HTTPS_GET_TITLE = "https";


    /**
     * @param getUrl
     * @param params
     * @return
     */
    public static String postHttpOfMap(String getUrl, Map<String, Object> params, int httpTimeOut) {
        URL url = getValidateURL(getUrl);
        try {

            HttpURLConnection mSubmitConneciton = null;
                byte[] data = getRequestDataMap(params).toString().getBytes();

                try {
                    mSubmitConneciton = (HttpURLConnection) url.openConnection();


                    mSubmitConneciton.setConnectTimeout(httpTimeOut);
                    mSubmitConneciton.setReadTimeout(httpTimeOut);
                    mSubmitConneciton.setDoInput(true);
                    mSubmitConneciton.setDoOutput(false);
                    mSubmitConneciton.setRequestMethod("POST");
                    mSubmitConneciton.setUseCaches(false);


                    mSubmitConneciton.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    mSubmitConneciton.setRequestProperty("Content-Length",
                            String.valueOf(data.length));
                    mSubmitConneciton.connect();
                    OutputStream outputStream = mSubmitConneciton.getOutputStream();
                    outputStream.write(data);

                    outputStream.flush();
                    outputStream.close();
                    int responseCode = mSubmitConneciton.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inptStream = mSubmitConneciton.getInputStream();
                        return dealResponseResult(inptStream);
                    } else {
                        Log.i(TAG, "url:" + getUrl + ",responseCode:" + responseCode);
                        return "url:" + getUrl + ",responseCode:" + responseCode;
                    }

                } catch (Exception e) {
                    Log.i(TAG, "url:" + getUrl + ",error:" + e.toString());
                    return "url:" + getUrl + ",error:" + e.toString();
                } finally {
                    if (mSubmitConneciton != null) {
                        mSubmitConneciton.disconnect();
                    }
                }

        } catch (Exception e) {
            Log.i(TAG, "error:" + e.toString());
            return "";
        }
    }

    /**
     * 拼接方法
     *
     * @param params
     * @return
     */
    private static StringBuffer getRequestDataMap(Map<String, Object> params) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=")
                        .append(entry.getValue())
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /**
     * 数据流
     *
     * @param inputStream
     * @return
     */
    private static String dealResponseResult(InputStream inputStream) {
        String resultData;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }

    /**
     * X509证书
     */
    private static class TrustAllManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }
    }

    /**
     * url获取
     *
     * @param urlString
     * @return
     */
    private static URL getValidateURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (Exception e) {
            Log.i(TAG, "error:" + e.toString());
        }
        return null;
    }

}
