package com.hydbest.baseandroid.activity.other.tinyserver;

import android.text.TextUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class UploadImageHandler implements IResourceHandler {

    private static final String ACCEPT_PREFIX = "/upload/";

    @Override
    public boolean accept(String uri) {
        if (TextUtils.isEmpty(uri)) return false;
        return uri.startsWith(ACCEPT_PREFIX);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) {
        String tmpPath = "/sdcard/upload.png";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            long totalLength = Long.parseLong(httpContext.getHeaderValue("content-length").trim());
            int count = 0;
            fileOutputStream = new FileOutputStream(tmpPath);
            inputStream = httpContext.getUnderlySocket().getInputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while (count < totalLength) {
                len = inputStream.read(buffer);
                count += len;
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            //inputStream.close();  会直接关闭当前管道，导致后续数据无法写入
            outputStream = httpContext.getUnderlySocket().getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            writer.println("HTTP/1.1 200 OK");
            writer.println();
            writer.flush();
            onImageLoaded(tmpPath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) fileOutputStream.close();
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
                httpContext.getUnderlySocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onImageLoaded(String imgPath) {

    }
}
