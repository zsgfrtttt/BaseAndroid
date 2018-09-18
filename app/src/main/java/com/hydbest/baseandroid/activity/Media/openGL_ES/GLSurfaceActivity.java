package com.hydbest.baseandroid.activity.Media.openGL_ES;

import android.content.Context;
import android.opengl.EGLConfig;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by csz on 2018/9/18.
 */

public class GLSurfaceActivity extends AppCompatActivity {

    private GLSurfaceView mGLView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new CusGLSurfaceView(this);
        setContentView(mGLView);
    }


    class CusGLSurfaceView extends GLSurfaceView {

        private final CusGLRenderer mRenderer;

        public CusGLSurfaceView(Context context) {
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            mRenderer = new CusGLRenderer();

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(mRenderer);
        }
    }

    public class CusGLRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
            // Set the background frame color
            GLES20.glClearColor(1f, 1f, 0.0f, 1.0f);
        }

        public void onDrawFrame(GL10 unused) {
            // Redraw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        }

        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }
    }
}
