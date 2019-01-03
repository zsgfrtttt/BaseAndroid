package com.hydbest.baseandroid.activity.integrate.span.impl;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.hydbest.baseandroid.activity.integrate.span.DataBindingSpan;

/**
 * Created by csz on 2018/12/20.
 */

public class SpannableData implements DataBindingSpan<String> {
    private String spanned;

    public SpannableData(String spanned) {
        this.spanned = spanned;
    }

    @Override
    public CharSequence spannedText() {
        SpannableString span = new SpannableString(spanned);
        span.setSpan(new ForegroundColorSpan(Color.RED), 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    @Override
    public String bindingData() {
        return spanned;
    }
}
