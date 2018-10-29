package com.hydbest.baseandroid.activity.other.tinyserver;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ResourceInAssetsHandler implements IResourceHandler{

    private static final String ACCEPT_PREFIX = "/static/";

    private Context context;

    public ResourceInAssetsHandler(Context context) {
        this.context = context;
    }

    @Override
    public boolean accept(String uri) {
        if (TextUtils.isEmpty(uri)) return false;
        return uri.startsWith(ACCEPT_PREFIX);
    }

    @Override
    public void handle(String uri, HttpContext httpContext) {
        //test(httpContext);
        try {
            String assetsPath = uri.substring(ACCEPT_PREFIX.length());
            if (!TextUtils.isEmpty(assetsPath)) {
                InputStream assetsInputStream = context.getAssets().open(assetsPath);
                byte[] raw = StreamUtil.readRawFromStream(assetsInputStream);
                assetsInputStream.close();

                OutputStream socketOutStream = httpContext.getUnderlySocket().getOutputStream();
                PrintStream writer = new PrintStream(socketOutStream);
                writer.println("HTTP/1.1 200 OK");
                writer.println("Content-Length: "+raw.length);
                if (assetsPath.endsWith(".html")){
                    writer.println("Content-Type: text/html");
                }else if (assetsPath.endsWith(".htm")){
                    writer.println("Content-Type: text/html");
                }else if (assetsPath.endsWith(".js")){
                    writer.println("Content-Type: text/js");
                }else if (assetsPath.endsWith(".css")){
                    writer.println("Content-Type: text/css");
                }else if (assetsPath.endsWith(".jpg")){
                    writer.println("Content-Type: text/jpg");
                }else if (assetsPath.endsWith(".png")){
                    writer.println("Content-Type: text/png");
                }
                writer.println();
                writer.write(raw);
                writer.println();
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpContext.getUnderlySocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void test(HttpContext httpContext) {
        try {
            OutputStream outputStream = httpContext.getUnderlySocket().getOutputStream();
            //PrintWriter
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            writer.write("HTTP/1.1 200 OK\r\n");
            writer.write("\r\n");
            writer.write("assets is OK!\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
