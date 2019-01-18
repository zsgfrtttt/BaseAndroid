package com.csz.video.media;

import android.util.Log;

/**
 * Created by csz on 2019/1/16.
 */

public class VideoManager {

    private static VideoManager sInstance;

    private VideoManager() {
    }

    public static synchronized VideoManager getInstance() {
        if (sInstance == null) {
            sInstance = new VideoManager();
        }
        return sInstance;
    }

    private VideoHandler mCurrentPlayHandler;

    public VideoHandler getCurrentHandler() {
        return mCurrentPlayHandler;
    }

    public void setCurrentPlayHandler(VideoHandler handler) {
        Log.i("csz","setCurrentPlayHandler");
        if (mCurrentPlayHandler != handler) {
            Log.i("csz","oooooooooooooo");
            if (mCurrentPlayHandler != null) {
                mCurrentPlayHandler.release();
            }
            mCurrentPlayHandler = handler;
        }
    }

    public void pausePlayHandler(boolean isRealPause) {
        if (mCurrentPlayHandler != null) {
            mCurrentPlayHandler.pause();
        }
    }

    public void resumePlayHandler() {
        if (mCurrentPlayHandler != null) {
            mCurrentPlayHandler.resume();
        }
    }

    public void releaseHandler() {
        if (mCurrentPlayHandler != null) {
            mCurrentPlayHandler.release();
            mCurrentPlayHandler = null;
        }
    }

    public boolean onBackPressed() {
        if (mCurrentPlayHandler != null) {
            return mCurrentPlayHandler.onBackPressed();
        }
        return false;
    }
}
