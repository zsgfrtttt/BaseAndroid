package com.hydbest.baseandroid.activity.other.tinyserver;

public class WebConfiguration {

    private int port;
    private int maxParallels;

    public WebConfiguration(int port, int maxParallels) {
        this.port = port;
        this.maxParallels = maxParallels;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxParallels() {
        return maxParallels;
    }

    public void setMaxParallels(int maxParallels) {
        this.maxParallels = maxParallels;
    }
}
