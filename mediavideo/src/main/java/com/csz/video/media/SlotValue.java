package com.csz.video.media;

public class SlotValue {
    private String url;
    //广告url
    private String clickUrl;

    public SlotValue(String url, String clickUrl) {
        this.url = url;
        this.clickUrl = clickUrl;
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
}
