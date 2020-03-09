package com.hydbest.baseandroid.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtil {
    /**
     * 压缩图片
     *
     * @param src 源图片数组
     * @param kb  目标图片大小，单位kb
     */
    public static Bitmap scaleBitmap(Bitmap src, int kb) {
        if (src == null) return null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        src.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        Log.i("csz",String.format("\n压缩前：%d * %d = %d, %d字节 , %.2fk", src.getWidth(), src.getHeight(), src.getWidth() * src.getHeight(), src.getByteCount(), src.getByteCount() / 1024.f));
        Bitmap dest = scaleBitmap(bytes,kb);
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dest;
    }

    /**
     * 压缩图片
     *
     * @param src 源图片数组
     * @param kb  目标图片大小，单位kb
     */
    public static Bitmap scaleBitmap(byte[] src, int kb) {
        if (src == null) return null;
        int targetByte = kb * 1024;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(src, 0, src.length, options);
        int byteCount = options.outHeight * options.outWidth * 4;
        // > insampleSize
        if (byteCount > targetByte) {
            int scale = (int) Math.floor(Math.sqrt(byteCount * 1.0 / targetByte));
            if (options.inDensity == 0 || options.inTargetDensity == 0) {
                options.inDensity = 100;
                options.inTargetDensity = 100;
            }
            options.inTargetDensity /= scale;
            options.inTargetDensity *= scale / Math.sqrt(byteCount * 1.0 / targetByte);
        }
        options.inJustDecodeBounds = false;
        Bitmap dest = BitmapFactory.decodeByteArray(src, 0, src.length, options);
        Log.i("csz",String.format("\n%d压缩后：%d * %d = %d, %d字节 , %.2fk",byteCount, dest.getWidth(), dest.getHeight(), dest.getWidth() * dest.getHeight(), dest.getByteCount(), dest.getByteCount() / 1024.f));
        return dest;
    }

}
