package com.hydbest.baseandroid.natived;

/**
 * Created by csz on 2018/10/18.
 * 差分包与旧包合成新包
 */

public class BsPatch {

    static {
        System.loadLibrary("bsdiff");
    }

    public static native int bspatch(String oldApk, String newApk, String patch);
}
