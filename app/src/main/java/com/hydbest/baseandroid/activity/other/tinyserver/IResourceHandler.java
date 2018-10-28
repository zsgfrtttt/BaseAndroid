package com.hydbest.baseandroid.activity.other.tinyserver;

public interface IResourceHandler {
    boolean accept(String uri);

    void handle(String uri,HttpContext httpContext);
}
