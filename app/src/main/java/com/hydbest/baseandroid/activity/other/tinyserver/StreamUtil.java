package com.hydbest.baseandroid.activity.other.tinyserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {

    public static final String readLine(InputStream inputStream) {
        StringBuffer sb = new StringBuffer();
        try {
            int c1 = 0;
            int c2 = 0;
            while (c2 != -1 && !(c1 == '\r' && c2 == '\n')) {
                c1 = c2;
                c2 = inputStream.read();
                sb.append((char) c2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static byte[] readRawFromStream(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = 0 ;
            while ((len = inputStream.read(buffer))!= -1){
                byteArrayOutputStream.write(buffer,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }
}
