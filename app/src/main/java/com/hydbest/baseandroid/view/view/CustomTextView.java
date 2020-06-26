package com.hydbest.baseandroid.view.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.Hex;
import com.hydbest.baseandroid.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.net.URLEncoder;
import java.security.AlgorithmParameterGenerator;
import java.security.AlgorithmParameters;
import java.security.CodeSigner;
import java.security.DigestInputStream;
import java.security.DigestOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignedObject;
import java.security.Timestamp;
import java.security.cert.CRL;
import java.security.cert.CertPath;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class CustomTextView extends View {

    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mTextSmall;
    private int mPadding = 32;
    private int mStrokeWidth = 8;
    private int mWidth;
    private int mInnerRadio;
    private float mProgress = 1;
    private Bitmap mBitmap;

    private int mType;


    public CustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Textswitch);
        mType = typedArray.getInt(0, 1);
        typedArray.recycle();
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = Math.min(width, height);
        mInnerRadio = mWidth / 2 - mPadding;
        setMeasuredDimension(mWidth, mWidth);
    }

    private void init() {

        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_del);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.LTGRAY);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setDither(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setTextSize(40);

        mTextSmall = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextSmall.setDither(true);
        mTextSmall.setStyle(Paint.Style.FILL);
        mTextSmall.setColor(Color.BLUE);
        mTextSmall.setStrokeWidth(2);
        mTextSmall.setTextSize(20);
        mTextSmall.setFakeBoldText(false);//是否粗体
        mTextSmall.setStrikeThruText(false);//是否加删除线
        mTextSmall.setUnderlineText(false);//是否加下滑线
        mTextSmall.setTextSkewX(0);//文字倾斜率
        mTextSmall.setTextScaleX(1f);//文字横向缩放比
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTextSmall.setLetterSpacing(0);//字母间距
            //     mTextSmall.setFontFeatureSettings("smcp"); //用 CSS 的 font-feature-settings 的方式来设置文字。  大写效果
            mTextSmall.setElegantTextHeight(true);//对中文无效，恢复某些语言某些特殊字体的原始高度
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mType < 5) {
            drawInner(canvas);
            drawCircle(canvas);
        } else {
            drawRount(canvas);
        }
        switch (mType) {
            case 0:
                drawTextOnCenter(canvas);
                break;
            case 1:
                drawText4(canvas);
                break;
            case 2:
                drawTextOnPathCW(canvas);
                break;
            case 3:
                drawTextOnPathCCW(canvas);
                break;
            case 4:
                drawTextRun(canvas);
                break;
            case 5:
                drawMoreLineText(canvas);
                break;
            case 6:
                drawBreakText(canvas);
                break;
            case 7:
                drawRunAdvance(canvas);
                break;
            case 8:
                drawLinesOnStaticLayout(canvas);
                break;
        }

    }

    private void drawRount(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(new RectF(0, 0, mWidth, mWidth), mPaint);
    }

    private void drawInner(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mWidth / 2, mWidth / 2, mInnerRadio, mPaint);
    }

    private void drawCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mStrokeWidth);
        canvas.drawArc(new RectF(mStrokeWidth / 2, mStrokeWidth / 2, mWidth - mStrokeWidth / 2, mWidth - mStrokeWidth / 2), -90, mProgress * 360, false, mPaint);
    }


    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }

    private void drawText(Canvas canvas) {
        String text = "闲人/";
        String text2 = "100";
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        mTextSmall.getTextBounds(text2, 0, text2.length(), rect2);
        float width = rect.width() + rect2.width();
        float base = mWidth * 2 / 3.f;
        canvas.drawLine(0, base, mWidth, base, mTextPaint);
        canvas.drawText(text, mWidth / 2 - width / 2, base, mTextPaint);
        canvas.drawText(text2, mWidth / 2 - width / 2 + rect.width(), base, mTextSmall);
    }

    private void drawText2(Canvas canvas) {
        String text = "1000/00";
        int end = text.indexOf("/") + 1;
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        mTextPaint.getTextBounds(text, 0, end, rect);
        mTextSmall.getTextBounds(text, end, text.length(), rect2);
        float width = rect.width() + rect2.width();
        float base = mWidth * 2 / 3.f;
        canvas.drawLine(0, base, mWidth, base, mTextPaint);
        canvas.drawText(text, 0, end, mWidth / 2 - width / 2, base, mTextPaint);
        canvas.drawText(text, end, text.length(), mWidth / 2 - width / 2 + rect.width(), base, mTextSmall);
    }

    private void drawText3(Canvas canvas) {
        String text = "1000/00";
        char[] chars = text.toCharArray();
        int end = text.indexOf("/") + 1;
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        mTextPaint.getTextBounds(text, 0, end, rect);
        mTextSmall.getTextBounds(text, end, text.length(), rect2);
        float width = rect.width() + rect2.width();
        float base = mWidth * 2 / 3.f;
        canvas.drawLine(0, base, mWidth, base, mTextPaint);
        canvas.drawText(chars, 0, end, mWidth / 2 - width / 2, base, mTextPaint);
        canvas.drawText(chars, end, text.length() - end, mWidth / 2 - width / 2 + rect.width(), base, mTextSmall);
    }

    private void drawText4(Canvas canvas) {
        String text = "绘制cglfjj/g";
        char[] chars = text.toCharArray();
        int end = text.indexOf("/") + 1;
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        mTextPaint.getTextBounds(text, 0, end, rect);
        mTextSmall.getTextBounds(text, end, text.length(), rect2);
        float width = rect.width() + rect2.width();
        float base = mWidth * 2 / 3.f;
        canvas.drawLine(0, base, mWidth, base, mTextPaint);
        //如果是绘制数字不需要往上移动descent，数字本身就是在baseline上方绘制
        canvas.drawText(chars, 0, end, mWidth / 2 - width / 2, base - mTextPaint.descent(), mTextPaint);
        canvas.drawText(chars, end, text.length() - end, mWidth / 2 - width / 2 + rect.width(), base - mTextSmall.descent(), mTextSmall);
    }

    private void drawTextOnCenter(Canvas canvas) {
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        String text = "绘制cglfjj/g";
        int end = text.indexOf("/") + 1;
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, end, rect);
        float centerY = mWidth / 2;
        canvas.drawLine(0, centerY, mWidth, centerY, mTextPaint);
        canvas.drawText(text, 0, end, mWidth / 2, centerY - (mTextPaint.ascent() + mTextPaint.descent()) / 2, mTextPaint);
    }

    //vOffset 在CW >0 往里面收 ，<0  往外面扩  ,hOffset 不能为负数且不能超过半圆的大小 ，最好用canvas.rotate代替
    //vOffset 在CCW >0 往外面扩 ，<0  往里面收
    private void drawTextOnPathCW(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90, mWidth / 2, mWidth / 2);
        float length = (float) (Math.PI * mInnerRadio * 2);
        String text = "1000/00";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        Path path = new Path();
        path.addCircle(mWidth / 2, mWidth / 2, mInnerRadio, Path.Direction.CW);
        float gree = rect.width() * 360 / (2 * length);
        canvas.rotate(-gree, mWidth / 2, mWidth / 2);
        canvas.drawTextOnPath(text, path, 0, rect.height(), mTextPaint);
        canvas.restore();
    }

    private void drawTextOnPathCCW(Canvas canvas) {
        canvas.save();
        canvas.rotate(-90, mWidth / 2, mWidth / 2);
        float length = (float) (Math.PI * mInnerRadio * 2);
        String text = "1000/00";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        Path path = new Path();
        path.addCircle(mWidth / 2, mWidth / 2, mInnerRadio, Path.Direction.CCW);
        float gree = rect.width() * 360 / (2 * length);
        canvas.rotate(gree, mWidth / 2, mWidth / 2);
        canvas.drawTextOnPath(text, path, 0, rect.height(), mTextPaint);
        canvas.restore();
    }

    private void drawTextRun(Canvas canvas) {
        String text = "1000/00";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canvas.drawTextRun(text, 0, text.length(), 0, text.length(), mWidth / 2 - rect.width() / 2, mWidth / 2 + rect.height(), false, mTextPaint);
        }
    }

    private void drawMoreLineText(Canvas canvas) {
        String line1 = "大白菜很帅";
        String line2 = "大黑菜很帅";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(line1, 0, line1.length(), rect);
        canvas.drawText(line1, mWidth / 2 - rect.width() / 2, rect.height(), mTextPaint);
        //getFontSpacing获取推荐的行间距
        canvas.drawText(line2, mWidth / 2 - rect.width() / 2, rect.height() + mTextPaint.getFontSpacing(), mTextPaint);
    }

    // 根据最大的宽度获取可以绘制的字体数，可以用这个API写长文的换行算法
    private void drawBreakText(Canvas canvas) {
        String line = "大白菜很帅";
        float[] measure = {0};//保存可绘制字数的宽度
        Rect rect = new Rect();
        mTextPaint.getTextBounds(line, 0, line.length(), rect);
        int a = mTextPaint.breakText(line, true, 130, measure);
        canvas.drawText(line, 0, a, 0, rect.height(), mTextPaint);
        Log.i("csz", "130 " + a + "   " + measure[0]);
        int b = mTextPaint.breakText(line, true, 150, measure);
        canvas.drawText(line, 0, b, 0, rect.height() + mTextPaint.getFontSpacing(), mTextPaint);
        Log.i("csz", "150 " + b + "   " + measure[0]);
        int c = mTextPaint.breakText(line, true, 290, measure);
        canvas.drawText(line, 0, c, 0, rect.height() + mTextPaint.getFontSpacing() * 2, mTextPaint);
        Log.i("csz", "290 " + c + "   " + measure[0]);
    }

    //getRunAdvance 获取光标的位置，可用于图文混排的场景
    private void drawRunAdvance(Canvas canvas) {
        String line = "shuzhan \\uD83C\\uDDE8\\uD83C\\uDDF3";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(line, 0, line.length(), rect);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            float runAdvance = mTextPaint.getRunAdvance(line, 0, line.length(), 0, line.length(), false, line.length() - 1);
            canvas.drawText(line, 0, line.length(), 0, rect.height(), mTextPaint);
            canvas.drawLine(runAdvance + 2, rect.height(), runAdvance + 2, 0, mTextPaint);
        }
    }

    private void drawLinesOnStaticLayout(Canvas canvas) {
        TextPaint paint = new TextPaint();
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(2);
        paint.setTextSize(40);

        String text1 = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.";
        StaticLayout staticLayout1 = new StaticLayout(text1, paint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        String text2 = "a\nbc\ndefghi\njklm\nnopqrst\nuvwx\nyz";
        StaticLayout staticLayout2 = new StaticLayout(text2, paint, mWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, true);
        staticLayout1.draw(canvas);
        canvas.save();
        canvas.translate(0, 200);
        staticLayout2.draw(canvas);
        canvas.restore();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {

    }
}
