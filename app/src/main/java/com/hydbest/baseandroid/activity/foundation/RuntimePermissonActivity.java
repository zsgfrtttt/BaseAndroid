package com.hydbest.baseandroid.activity.foundation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.oned.rss.RSSUtils;
import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.activity.other.tinyserver.HttpContext;
import com.hydbest.baseandroid.util.network.NetworkObserver;
import com.hydbest.baseandroid.util.network.NetworkStateManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import static android.net.ConnectivityManager.TYPE_VPN;

/**
 * Created by csz on 2018/7/24.
 */

public class RuntimePermissonActivity extends AppCompatActivity {
    String mimeType = "text/html";
    String encoding = "utf-8";
    String userAgent = "Mozilla/5.0.html (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10";
    String url = "https://translate.google.cn/m?sl=auto&tl=zh-CN&hl=zh-CN&mui=sl";
    Executor mExecutor = Executors.newSingleThreadExecutor();
    WebView webView;
    WebView webView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.hydbest.baseandroid.R.layout.activity_runtime_permission);
        webView = findViewById(R.id.web_1);
        webView2 = findViewById(R.id.web_2);
        webView2.loadUrl(url);
        start();
    }

    private void start() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = null;
                try {
                    HttpURLConnection connection= (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestProperty("User-Agent",userAgent);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder builder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    final String result = builder.toString();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadData(result);
                        }
                    });
                } catch (Exception e) {
                    Log.i("csz", "e :" + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadData(String result) {
        try {
            File file = new File(getFilesDir(),"test.txt");
            if (!file.exists()){
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bufferedWriter.write(result,0,result.length());
            bufferedWriter.flush();
            bufferedWriter.close();
            Document doc = Jsoup.parse(result);
            Element body = doc.select("div").get(2);
            body.text("");
            webView.loadDataWithBaseURL(url,doc.html(), mimeType, encoding,"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void test(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                Log.i("csz", "shouldShowRequestPermissionRationale");
                new AlertDialog.Builder(this).setMessage("需要获取联系人权限").setPositiveButton("获取", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(RuntimePermissonActivity.this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
                    }
                }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 1);
            }
        }

    }


}
