package com.hydbest.baseandroid.activity.cus_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hydbest.baseandroid.R;
import com.hydbest.baseandroid.util.ImageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ZoomImageActivity extends AppCompatActivity {

    private ImageView ivOrigin;
    private ImageView ivCompress;
    private TextView tv;

    private Bitmap mOrigin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_scale);
        ivOrigin = findViewById(R.id.iv_origin);
        ivCompress = findViewById(R.id.iv_compress);

        tv = findViewById(R.id.tv);

        mOrigin = BitmapFactory.decodeResource(getResources(), R.mipmap.c);
        ivOrigin.setImageBitmap(mOrigin);
        tv.setText(String.format("原图：%d * %d = %d, %d字节 , %.2fk", mOrigin.getWidth(), mOrigin.getHeight(), mOrigin.getWidth() * mOrigin.getHeight(), mOrigin.getByteCount(), mOrigin.getByteCount() / 1024.f));
    }

    public void compress(View view) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDensity = 320;
        options.inTargetDensity = 220;
        //options.
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.c, options);
        ivCompress.setImageBitmap(bitmap);
        //320   440
        Log.i("csz", "kk " + options.inDensity + "   " + options.inTargetDensity);
        tv.append(String.format("\n压缩后：%d * %d = %d, %d字节 , %.2fk", bitmap.getWidth(), bitmap.getHeight(), bitmap.getWidth() * bitmap.getHeight(), bitmap.getByteCount(), bitmap.getByteCount() / 1024.f));
    }

    //600k
    public void reset(View view) {
        int targetByte = 600 * 1024;
        int byteCount = mOrigin.getByteCount();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.mipmap.c, options);
        // > insampleSize
        if (byteCount > targetByte) {
            options.inTargetDensity /= Math.floor(Math.sqrt(byteCount * 1.0 / targetByte));
            options.inTargetDensity *= Math.floor(Math.sqrt(byteCount * 1.0 / targetByte))  / Math.sqrt(byteCount * 1.0 / targetByte);
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.c, options);
        ivCompress.setImageBitmap(bitmap);
        tv.append(String.format("\n压缩后：%d * %d = %d, %d字节 , %.2fk", bitmap.getWidth(), bitmap.getHeight(), bitmap.getWidth() * bitmap.getHeight(), bitmap.getByteCount(), bitmap.getByteCount() / 1024.f));

    }


    public void writeSd(View view) {
        try {
            String fileName = "laji.png";
            String path = getExternalCacheDir().getAbsolutePath() + "/" + fileName;
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            Bitmap bitmap = ImageUtil.scaleBitmap(mOrigin,300);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            ivCompress.setImageBitmap(bitmap);
            fileOutputStream.close();
            tv.append(String.format("\n压缩后：%d * %d = %d, %d字节 , %.2fk", bitmap.getWidth(), bitmap.getHeight(), bitmap.getWidth() * bitmap.getHeight(), bitmap.getByteCount(), bitmap.getByteCount() / 1024.f));
            tv.append("\n本地文件大小：" + file.length()+ "=" + file.length()/1024.f+"k");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readSd(View view) {
        try {
            String fileName = "laji.png";
            String path = getExternalCacheDir().getAbsolutePath() + "/" + fileName;
            File file = new File(path);
            if (!file.exists()){
                return ;
            }
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ivCompress.setImageBitmap(bitmap);
            tv.append(String.format("\ndecode本地图片文件后：%d * %d = %d, %d字节 , %.2fk", bitmap.getWidth(), bitmap.getHeight(), bitmap.getWidth() * bitmap.getHeight(), bitmap.getByteCount(), bitmap.getByteCount() / 1024.f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void scale(View view){
        try {
            float target = 500.f;
            float scale =  mOrigin.getWidth()/ target;
            Bitmap bitmap = Bitmap.createScaledBitmap(mOrigin, (int)target, (int)(mOrigin.getHeight() / scale), false);

            String fileName = "laji.png";
            String path = getExternalCacheDir().getAbsolutePath() + "/" + fileName;
            File file = new File(path);
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
            ivCompress.setImageBitmap(bitmap);
            fileOutputStream.close();
            tv.append(String.format("\n压缩后：%d * %d = %d, %d字节 , %.2fk", bitmap.getWidth(), bitmap.getHeight(), bitmap.getWidth() * bitmap.getHeight(), bitmap.getByteCount(), bitmap.getByteCount() / 1024.f));
            tv.append("\n本地文件大小：" + file.length()+ "=" + file.length()/1024.f+"k");
        }catch (Exception e){

        }
    }

}
