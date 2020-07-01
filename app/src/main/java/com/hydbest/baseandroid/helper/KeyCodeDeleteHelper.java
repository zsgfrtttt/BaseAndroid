package com.hydbest.baseandroid.helper;

import android.text.Selection;
import android.text.Spannable;
import android.util.Log;

import com.hydbest.baseandroid.activity.integrate.span.DataBindingSpan;

/**
 * Created by csz on 2018/12/20.
 */

public class KeyCodeDeleteHelper {

    public static boolean onDelDown(Spannable text) {
        int selectionStart = Selection.getSelectionStart(text);
        int selectionEnd = Selection.getSelectionEnd(text);

        Log.i("csz","selectionStart:"+selectionStart+"   selectionEnd:"+selectionEnd);

        DataBindingSpan[] spans = text.getSpans(selectionStart, selectionEnd, DataBindingSpan.class);
        if (spans != null && spans.length > 0) {
            int spanStart = text.getSpanStart(DataBindingSpan.class);
            int spanEnd = text.getSpanEnd(DataBindingSpan.class);
            Log.i("csz","spanStart:"+spanStart+"    spanEnd:"+spanEnd);
            Selection.setSelection(text, spanStart, spanEnd);
        }
        return selectionStart == selectionEnd;
    }
}
