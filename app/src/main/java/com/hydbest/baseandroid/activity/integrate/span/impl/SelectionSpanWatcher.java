package com.hydbest.baseandroid.activity.integrate.span.impl;

import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;

/**
 * Created by csz on 2018/12/20.
 */

public class SelectionSpanWatcher implements SpanWatcher {

    private Class kClass;
    private int mStartSelection;
    private int mEndSelection;

    public SelectionSpanWatcher(Class kClass) {
        this.kClass = kClass;
    }

    @Override
    public void onSpanAdded(Spannable text, Object what, int start, int end) {

    }

    @Override
    public void onSpanRemoved(Spannable text, Object what, int start, int end) {

    }

    @Override
    public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart, int nend) {
        if (what == Selection.SELECTION_END && nend != nstart) {
            mEndSelection = nstart;
            Object[] spans = text.getSpans(nstart, nend, kClass);
            if (spans != null && spans.length > 0) {
                int spanStart = text.getSpanStart(this);
                int spanEnd = text.getSpanEnd(this);
                int index = (Math.abs(mEndSelection - spanEnd) > Math.abs(mEndSelection - spanStart)) ? spanStart : spanEnd;
                Selection.setSelection(text, Selection.getSelectionStart(text), index);
            }
        }

        if (what == Selection.SELECTION_START && mStartSelection != nstart) {
            mStartSelection = nstart;
            Object[] spans = text.getSpans(nstart, nend, kClass);
            if (spans != null && spans.length > 0) {
                int spanStart = text.getSpanStart(this);
                int spanEnd = text.getSpanEnd(this);
                int index = (Math.abs(mStartSelection - spanEnd) > Math.abs(mStartSelection - spanStart)) ? spanStart : spanEnd;
                Selection.setSelection(text, index, Selection.getSelectionEnd(text));
            }
        }
    }

}

