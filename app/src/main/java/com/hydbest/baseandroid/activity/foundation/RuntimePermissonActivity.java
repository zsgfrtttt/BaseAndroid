package com.hydbest.baseandroid.activity.foundation;

import android.Manifest;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hydbest.baseandroid.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    FrameLayout frame;
    Button btn;
    LinearLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_permission);
        webView = findViewById(R.id.web_1);
        webView2 = findViewById(R.id.web_2);
        webView2.loadUrl(url);
        frame = findViewById(R.id.frame);
        btn = findViewById(R.id.btn);
        root = findViewById(R.id.root);

        inset();
        start();
        drag();
    }

    private void inset() {

        root.setSystemUiVisibility(root.getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        final int paddingTop = root.getPaddingTop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            root.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
                @Override
                public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                    root.setPadding(root.getPaddingLeft(), paddingTop + insets.getSystemWindowInsetTop(), root.getPaddingRight(), root.getPaddingBottom());
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
        frame.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                //获取事件
                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        btn.setVisibility(View.INVISIBLE);
                        Log.d("csz", "开始拖拽");
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d("csz", "结束拖拽");
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d("csz", "拖拽的view进入监听的view时");
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d("csz", "拖拽的view离开监听的view时");
                        frame.setBackgroundColor(Color.BLUE);
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        float x = event.getX();
                        float y = event.getY();
                        frame.setBackgroundColor(Color.RED);
                        Log.i("csz", "拖拽的view在监听view中的位置:x =" + x + ",y=" + y);
                        break;
                    case DragEvent.ACTION_DROP:
                        Log.i("csz", "释放拖拽的view");
                        if (event.getLocalState() != null) {
                            View localState = (View) event.getLocalState();
                            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            ((ViewGroup) localState.getParent()).removeView(localState);
                            frame.addView(localState, layoutParams);
                            btn.setVisibility(View.VISIBLE);
                        }
                        break;
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
