package com.csz.runtime;

import android.os.Bundle;

public class BundleUtil {

    public static <T> T get(Bundle bundle,String key){
        return (T) bundle.get(key);
    }

    public static <T> T get(Bundle bundle,String key,T def){
        Object o =   bundle.get(key);
        if (o == null){
            o = def;
        }
        return (T) o;
    }
}
