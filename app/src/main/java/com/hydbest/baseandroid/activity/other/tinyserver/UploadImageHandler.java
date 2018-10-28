package com.hydbest.baseandroid.activity.other.tinyserver;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
            fileOutputStream = new FileOutputStream(tmpPath);
            inputStream = httpContext.getUnderlySocket().getInputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onImageLoaded(String imgPath){

    }
}
