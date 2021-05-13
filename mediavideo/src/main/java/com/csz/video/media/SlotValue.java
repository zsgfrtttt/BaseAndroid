package com.csz.video.media;

public class SlotValue {
    private String url;
    //广告url
    private String clickUrl;
    //是否自动播放
    private boolean autoPlay;
    //是否为列表模式
    private boolean isListMode;

    public SlotValue(String url, String clickUrl,boolean autoPlay) {
        this.url = url;
        this.clickUrl = clickUrl;
        this.autoPlay = autoPlay;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    @Override
    public String toString() {
        return "SlotValue{" + "url='" + url + '\'' + ", clickUrl='" + clickUrl + '\'' + ", autoPlay=" + autoPlay + ", isListMode=" + isListMode + '}';
    }
}
