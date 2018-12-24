package com.geetest.slientimageverification;

import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by 谷闹年 on 2018/4/13.
 */
public class ImageSizeUtils {




    public static void saveBitmapToFile(Bitmap bitmap, String path) {
        if (bitmap != null && !TextUtils.isEmpty(path)) {
            try {
                File file;
                if ((file = new File(path)).exists() || !file.getParentFile().mkdirs() || file.createNewFile()) {
                    BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, outputStream);
                    outputStream.close();
                }
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }
    }


}
