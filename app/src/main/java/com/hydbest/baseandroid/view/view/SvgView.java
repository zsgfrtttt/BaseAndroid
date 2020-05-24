package com.hydbest.baseandroid.view.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import androidx.annotation.Nullable;
import androidx.core.graphics.PathParser;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hydbest.baseandroid.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SvgView extends View {

    private int[] colors = {Color.RED, Color.GRAY, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN};
    private List<DrawItem> drawItems;
    private DrawItem select;
    private RectF totalRectF;
    private float scale = -1;

    private Paint paint;
    private Matrix matrix;

    public SvgView(Context context) {
        this(context, null);
    }

    public SvgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SvgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(false);
        new Thread(new Task()).start();
    }

    class Task implements java.lang.Runnable {

        @SuppressLint("RestrictedApi")
        @Override
        public void run() {
            List<DrawItem> list = new ArrayList<>();
            Context context = getContext();
            InputStream inputStream = context.getResources().openRawResource(R.raw.china);
            try {
                float left = 0;
                float top = 0;
                float right = 0;
                float bottom = 0;
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = builder.parse(inputStream);
                Element root = document.getDocumentElement();
                NodeList nodeList = root.getElementsByTagName("path");
                for (int i = 0; i < nodeList.getLength(); i++) {
                    Element item = (Element) nodeList.item(i);
                    String d = item.getAttribute("d");
                    Path path = PathParser.createPathFromPathData(d);

                    RectF bound = new RectF();
                    path.computeBounds(bound, true);
                    left = Math.min(left, bound.left);
                    top = Math.min(top, bound.top);
                    right = Math.max(right, bound.right);
                    bottom = Math.max(bottom, bound.bottom);
                    totalRectF = new RectF(left, top, right, bottom);

                    DrawItem drawItem = new DrawItem(path, colors[i % colors.length]);
                    list.add(drawItem);
                }
                drawItems = list;
                post(new Runnable() {
                    @Override
                    public void run() {
                        requestLayout();
                        postInvalidate();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (totalRectF != null) {
            float widthScale = width / totalRectF.width();
            float heightScale = height / totalRectF.height();
            scale = Math.min(widthScale, heightScale);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawItems != null) {
            canvas.save();
            if (scale != -1) {
                int width = getMeasuredWidth();
                int height = getMeasuredHeight();
                float widthScale = width / totalRectF.width();
                float heightScale = height / totalRectF.height();
                if (widthScale > heightScale) {
                    canvas.translate((width - totalRectF.width() * scale) / 2, 0);
                } else {
                    canvas.translate(0, (height - totalRectF.height() * scale) / 2);
                }
                canvas.scale(scale, scale);
            }
            for (DrawItem drawItem : drawItems) {
                drawItem.draw(canvas, paint, select == drawItem);
            }
            matrix = canvas.getMatrix();
            canvas.restore();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (drawItems == null) return true;
        PointF point = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (DrawItem drawItem : drawItems) {
                    if (pointInPath(drawItem.path, point) && drawItem != select) {
                        select = drawItem;
                        invalidate();
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                select = null;
                invalidate();
                break;
        }
        return true;
    }

    private boolean pointInPath(Path target, PointF point) {
        Path path;
        if (matrix != null) {
            path = new Path();
            target.transform(matrix, path);
        } else {
            path = target;
        }
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        Region region = new Region();
        region.setPath(path, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
        return region.contains((int) point.x, (int) point.y);
    }

    static class DrawItem {
        private Path path;
        private int color;

        public DrawItem(Path path, int color) {
            this.path = path;
            this.color = color;
        }

        public void draw(Canvas canvas, Paint paint, boolean b) {
            if (b) {
                paint.setColor(color);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(path, paint);

                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(1);
                canvas.drawPath(path, paint);
            } else {
                paint.setColor(color);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(path, paint);
            }
        }
    }
}
