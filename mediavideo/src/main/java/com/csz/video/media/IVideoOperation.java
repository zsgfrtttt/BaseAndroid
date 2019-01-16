package com.csz.video.media;

/**
 * Created by csz on 2019/1/15.
 * 对外提供的操作方法
 */

public interface IVideoOperation {
    void resume();

    void pause(boolean isRealPause);

    boolean onBackPressed();

    void playWithData(SlotValue data);
}
