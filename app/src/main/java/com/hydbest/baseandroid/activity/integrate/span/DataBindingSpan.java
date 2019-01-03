package com.hydbest.baseandroid.activity.integrate.span;

/**
 * Created by csz on 2018/12/20.
 */

public interface DataBindingSpan<T> {
    CharSequence spannedText();
    T bindingData();
}
