package com.hydbest.baseandroid;

import com.hydbest.annotationcompiler.annotation.Seriable;

/**
 * Created by csz on 2018/6/12.
 */

@Seriable
public class User {
    @Seriable
    private int age;
    @Seriable
    private String name;
}
