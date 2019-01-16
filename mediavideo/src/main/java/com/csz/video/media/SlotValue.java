package com.csz.video.media;

public class SlotValue {
    private String url;
    //广告url
    private String clickUrl;
    private boolean autoPlay;

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
}
