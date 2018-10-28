package com.hydbest.baseandroid.activity.other.tinyserver;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpContext {

    private Socket mUnderlySocket;
    private Map<String,String> mHeader;

    public HttpContext(){
        mHeader = new HashMap();
    }

    public void setUnderlySocket(Socket socket) {
        this.mUnderlySocket = socket;
    }

    public Socket getUnderlySocket() {
        return mUnderlySocket;
    }

    public void addRequestHeader(String key, String value) {
        mHeader.put(key,value);
    }

    public String getHeaderValue(String headName){
        return mHeader.get(headName);
    }
}
