/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.journeyapps.barcodescanner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class ViewfinderView extends View {
    protected static final String TAG = ViewfinderView.class.getSimpleName();

    protected static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    protected static final long ANIMATION_DELAY = 16L;
    protected static final int CURRENT_POINT_OPACITY = 0xA0;
    protected static final int MAX_RESULT_POINTS = 20;
    protected static final int POINT_SIZE = 6;
    protected static final int SPEED_DISTANCE = 4;
    private static final int SCANNER_LINE_HEIGHT = 20;

    protected final Paint paint;
    protected Bitmap resultBitmap;
    protected int maskColor;
    protected final int resultColor;
    protected final int laserColor;
    protected final int resultPointColor;
    protected int scannerAlpha;
    protected List<ResultPoint> possibleResultPoints;
    protected List<ResultPoint> lastPossibleResultPoints;
    protected CameraPreview cameraPreview;

    // Cache the framingRect and previewFramingRect, so that we can still draw it after the preview
    // stopped.
    protected Rect framingRect;
    protected Rect previewFramingRect;

    private float density;
    private int screentRate;
    private int borderWidth;
    private int speedDistance;

    private int slideTop;
    private int slideBottom;
    private Bitmap mLineBitmap;

    // This constructor is used when the class is built from an XML resource.
    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Initialize these once for performance rather than calling them every time in onDraw().
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Resources resources = getResources();

        // Get setted attributes on view
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.zxing_finder);

        this.maskColor = attributes.getColor(R.styleable.zxing_finder_zxing_viewfinder_mask,
                resources.getColor(R.color.zxing_viewfinder_mask));
        this.resultColor = attributes.getColor(R.styleable.zxing_finder_zxing_result_view,
                resources.getColor(R.color.zxing_result_view));
        this.laserColor = attributes.getColor(R.styleable.zxing_finder_zxing_viewfinder_laser,
                resources.getColor(R.color.zxing_viewfinder_laser));
        this.resultPointColor = attributes.getColor(R.styleable.zxing_finder_zxing_possible_result_points,
                resources.getColor(R.color.zxing_possible_result_points));

        attributes.recycle();

        scannerAlpha = 0;
        possibleResultPoints = new ArrayList<>(MAX_RESULT_POINTS);
        lastPossibleResultPoints = new ArrayList<>(MAX_RESULT_POINTS);

        density =getContext().getResources().getDisplayMetrics().density;
        screentRate = (int) (24 * density);
        borderWidth = (int) (5 * density);
        speedDistance = (int) (SPEED_DISTANCE * density);
        mLineBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.scan_light)).getBitmap();
    }

    public void setCameraPreview(CameraPreview view) {
        this.cameraPreview = view;
        view.addStateListener(new CameraPreview.StateListener() {
            @Override
            public void previewSized() {
                refreshSizes();
                invalidate();
            }

            @Override
            public void previewStarted() {

            }

            @Override
            public void previewStopped() {

            }

            @Override
            public void cameraError(Exception error) {

            }

            @Override
            public void cameraClosed() {

            }
        });
    }

    protected void refreshSizes() {
        if(cameraPreview == null) {
            return;
        }
        Rect framingRect = cameraPreview.getFramingRect();
        Rect previewFramingRect = cameraPreview.getPreviewFramingRect();
        if(framingRect != null && previewFramingRect != null) {
            this.framingRect = framingRect;
            this.previewFramingRect = previewFramingRect;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        refreshSizes();
        if (framingRect == null || previewFramingRect == null) {
            return;
        }

        final Rect frame = framingRect;
        final Rect previewFrame = previewFramingRect;

        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        // Draw the exterior (i.e. outside the framing rect) darkened
        paint.setColor(resultBitmap != null ? resultColor : maskColor);
        canvas.drawRect(0, 0, width, frame.top, paint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
        canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
        canvas.drawRect(0, frame.bottom + 1, width, height, paint);

        paint.setColor(Color.BLUE);
        canvas.drawRect(frame.left,frame.top,frame.left + screentRate ,frame.top + borderWidth,paint);
        canvas.drawRect(frame.left,frame.top,frame.left + borderWidth ,frame.top + screentRate,paint);
        canvas.drawRect(frame.right - screentRate,frame.top,frame.right  ,frame.top + borderWidth,paint);
        canvas.drawRect(frame.right - borderWidth,frame.top,frame.right ,frame.top + screentRate,paint);
        canvas.drawRect(frame.left,frame.bottom - screentRate,frame.left + borderWidth ,frame.bottom,paint);
        canvas.drawRect(frame.left,frame.bottom - borderWidth,frame.left + screentRate ,frame.bottom,paint);
        canvas.drawRect(frame.right - screentRate,frame.bottom - borderWidth,frame.right ,frame.bottom ,paint);
        canvas.drawRect(frame.right - borderWidth,frame.bottom - screentRate,frame.right ,frame.bottom,paint);
        if (slideTop == 0) {
            slideTop = frame.top;
        }
        slideBottom = slideTop+mLineBitmap.getHeight();
        if (slideBottom > frame.bottom){
            slideBottom = frame.bottom;
        }
        Rect lineRect = new Rect(frame.left,slideTop,frame.right,slideBottom);
       // canvas.drawBitmap(mLineBitmap,null,lineRect,paint);
        drawLaserScanner(canvas,lineRect);
        slideTop += speedDistance;
        if (slideTop >= frame.bottom){
            slideTop = frame.top;
        }

        if (resultBitmap != null) {
            // Draw the opaque result bitmap over the scanning rectangle
            paint.setAlpha(CURRENT_POINT_OPACITY);
            canvas.drawBitmap(resultBitmap, null, frame, paint);
        } else {

            // Draw a red "laser scanner" line through the middle to show decoding is active
            paint.setColor(laserColor);
            paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
            scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
            final int middle = frame.height() / 2 + frame.top;
            canvas.drawRect(frame.left + 2, middle - 1, frame.right - 1, middle + 2, paint);

            final float scaleX = frame.width() / (float) previewFrame.width();
            final float scaleY = frame.height() / (float) previewFrame.height();

            final int frameLeft = frame.left;
            final int frameTop = frame.top;

            // draw the last possible result points
            if (!lastPossibleResultPoints.isEmpty()) {
                paint.setAlpha(CURRENT_POINT_OPACITY / 2);
                paint.setColor(resultPointColor);
                float radius = POINT_SIZE / 2.0f;
                for (final ResultPoint point : lastPossibleResultPoints) {
                    canvas.drawCircle(
                            frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            radius, paint
                    );
                }
                lastPossibleResultPoints.clear();
            }

            // draw current possible result points
            if (!possibleResultPoints.isEmpty()) {
                paint.setAlpha(CURRENT_POINT_OPACITY);
                paint.setColor(resultPointColor);
                for (final ResultPoint point : possibleResultPoints) {
                    canvas.drawCircle(
                            frameLeft + (int) (point.getX() * scaleX),
                            frameTop + (int) (point.getY() * scaleY),
                            POINT_SIZE, paint
                    );
                }

                // swap and clear buffers
                final List<ResultPoint> temp = possibleResultPoints;
                possibleResultPoints = lastPossibleResultPoints;
                lastPossibleResultPoints = temp;
                possibleResultPoints.clear();
            }

            // Request another update at the animation interval, but only repaint the laser line,
            // not the entire viewfinder mask.
            postInvalidateDelayed(ANIMATION_DELAY,
                    frame.left - POINT_SIZE,
                    frame.top - POINT_SIZE,
                    frame.right + POINT_SIZE,
                    frame.bottom + POINT_SIZE);
        }
    }

    //绘制扫描线
    private void drawLaserScanner(Canvas canvas, Rect frame) {
        paint.setColor(Color.BLUE);
        //线性渐变
        LinearGradient linearGradient = new LinearGradient(
                frame.left, frame.top,
                frame.left, frame.top + SCANNER_LINE_HEIGHT / 2,
                shadeColor(laserColor),
                laserColor,
                Shader.TileMode.MIRROR);

        RadialGradient radialGradient = new RadialGradient(
                (float)(frame.left + frame.width() / 2),
                (float)(frame.top + SCANNER_LINE_HEIGHT / 2),
                360f,
                laserColor,
                shadeColor(laserColor),
                Shader.TileMode.MIRROR);

        SweepGradient sweepGradient = new SweepGradient(
                (float)(frame.left + frame.width() / 2),
                (float)(frame.top + SCANNER_LINE_HEIGHT),
                shadeColor(laserColor),
                laserColor);

        ComposeShader composeShader = new ComposeShader(radialGradient, linearGradient, PorterDuff.Mode.ADD);
        paint.setShader(radialGradient);

        if(frame.top + SCANNER_LINE_HEIGHT<= framingRect.bottom) {
            RectF rectF = new RectF(frame.left + 2 * SCANNER_LINE_HEIGHT, frame.top, frame.right - 2 * SCANNER_LINE_HEIGHT, frame.top + SCANNER_LINE_HEIGHT);
            canvas.drawOval(rectF, paint);
        }else if (frame.top + SCANNER_LINE_HEIGHT > framingRect.bottom){
            RectF rectF = new RectF(frame.left + 2 * SCANNER_LINE_HEIGHT, frame.top, frame.right - 2 * SCANNER_LINE_HEIGHT, framingRect.bottom);
            canvas.drawOval(rectF, paint);
        }
        paint.setShader(null);
    }

    //处理颜色模糊
    public int shadeColor(int color) {
        String hax = Integer.toHexString(color);
        String result = "20"+hax.substring(2);
        return Integer.valueOf(result, 16);
    }

    public void drawViewfinder() {
        Bitmap resultBitmap = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap != null) {
            resultBitmap.recycle();
        }
        invalidate();
    }

    /**
     * Draw a bitmap with the result points highlighted instead of the live scanning display.
     *
     * @param result An image of the result.
     */
    public void drawResultBitmap(Bitmap result) {
        resultBitmap = result;
        invalidate();
    }

    /**
     * Only call from the UI thread.
     *
     * @param point a point to draw, relative to the preview frame
     */
    public void addPossibleResultPoint(ResultPoint point) {
        if (possibleResultPoints.size() < MAX_RESULT_POINTS)
            possibleResultPoints.add(point);
    }

    public void setMaskColor(int maskColor) {
        this.maskColor = maskColor;
    }

    public CameraPreview getCameraPreview() {
        return cameraPreview;
    }
}
