package com.hydbest.baseandroid.activity.foundation;

import android.content.ClipData;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.WindowInsets;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hydbest.baseandroid.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/**
 * Created by csz on 2018/7/24.
 */

public class RuntimePermissonActivity extends AutoTrackActivity {
    String mimeType = "text/html";
    String encoding = "utf-8";
    String userAgent = "Mozilla/5.0.html (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10";
    String url = "https://translate.google.cn/m?sl=auto&tl=zh-CN&hl=zh-CN&mui=sl";
    Executor mExecutor = Executors.newSingleThreadExecutor();
    WebView webView;
    WebView webView2;
    Button btn;
    FrameLayout root;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_permission);
        webView = findViewById(R.id.web_1);
        webView2 = findViewById(R.id.web_2);
        webView2.loadUrl(url);
        btn = findViewById(R.id.btn);
        root = findViewById(R.id.root);

        inset();
        start();
        drag();

        PackageManager pm = getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.hydbest.baseandroid", 0);
            Log.i("csz", "md6 :" +applicationInfo.sourceDir + "  " + getMD5Three(applicationInfo.sourceDir));
            FileInputStream inputStream =  new FileInputStream(new File(applicationInfo.sourceDir));
            File cacheDir = getCacheDir();
            File cache = new File(cacheDir + "/cache.apk");
            cache.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(cache);
            FileUtils.copy(inputStream,outputStream);
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("csz", "md5 :" + getApplicationInfo().sourceDir + "  " + getMD5Three(getApplicationInfo().sourceDir));
    }

    public static String getMD5Three(String path) {
        BigInteger bi = null;
        try {
            byte[] buffer = new byte[8192];
            int len = 0;
            MessageDigest md = MessageDigest.getInstance("MD5");
            File f = new File(path);
            FileInputStream fis = new FileInputStream(f);
            while ((len = fis.read(buffer)) != -1) {
                md.update(buffer, 0, len);
            }
            fis.close();
            byte[] b = md.digest();
            bi = new BigInteger(1, b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi.toString(16);
    }

    private void inset() {
        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        final int paddingTop = root.getPaddingTop();
        final int paddingBottom = root.getPaddingBottom();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            root.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    root.setPadding(root.getPaddingLeft(), paddingTop + insets.getSystemWindowInsetTop(), root.getPaddingRight(), paddingBottom + insets.getSystemWindowInsetBottom());
                    return insets;
                }
            });
            //这不是绝对必要的，但确实可以解决WindowInsets的分派方式
            if (root.isAttachedToWindow()) {
                root.requestApplyInsets();
            } else {
                root.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                    @Override
                    public void onViewAttachedToWindow(View v) {
                        v.removeOnAttachStateChangeListener(this);
                        v.requestApplyInsets();
                    }

                    @Override
                    public void onViewDetachedFromWindow(View v) {

                    }
                });
            }
        }
    }

    private void drag() {
        btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING);
                View.DragShadowBuilder builder = new View.DragShadowBuilder(v);
                ClipData data = ClipData.newPlainText("label", "我是文本内容！");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    v.startDragAndDrop(data, builder, v, 0);
                } else {
                    v.startDrag(data, builder, v, 0);
                }
                return true;
            }
        });

    }

    private void start() {
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = null;
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestProperty("User-Agent", userAgent);
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
            File file = new File(getFilesDir(), "test.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bufferedWriter.write(result, 0, result.length());
            bufferedWriter.flush();
            bufferedWriter.close();
            Document doc = Jsoup.parse(result);
            Element body = doc.select("div").get(2);
            body.text("");
            webView.loadDataWithBaseURL(url, doc.html(), mimeType, encoding, "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void test(View view) {

    }

    public static void main(String[] args){
        //ef0f5005431ea5e3c202f0784f76a19c
        //ef0f5005431ea5e3c202f0784f76a19c
        System.out.println("llllll" + getMD5Three("/Users/caishuzhan/Desktop/workspace/BaseAndroid/app/build/outputs/apk/all32/debug/app-all32-debug.apk"));
        System.out.println();
    }
}
