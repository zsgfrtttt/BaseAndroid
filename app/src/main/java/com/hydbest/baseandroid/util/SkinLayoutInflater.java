package com.hydbest.baseandroid.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author csz
 */
public class SkinLayoutInflater extends LayoutInflater {

    private static final String[] sClassPrefixList = {"android.widget.", "android.webkit.", "android.app.", "android.view."};

    private static SkinLayoutInflater skinLayoutInflater;

    private SkinLayoutInflater(Context context) {
        super(context);
    }

    private SkinLayoutInflater(SkinLayoutInflater skinLayoutInflater, Context context) {
        super(skinLayoutInflater, context);
    }

    public static SkinLayoutInflater from(Context context) {
        if (skinLayoutInflater == null) {
            synchronized (SkinLayoutInflater.class) {
                if (skinLayoutInflater == null) {
                    skinLayoutInflater = new SkinLayoutInflater(context);
                }
            }
        }
        return skinLayoutInflater;
    }

    //
    @Override
    public View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
        if (-1 == name.indexOf('.')) {
            for (String prefix : sClassPrefixList) {
                try {
                    View view = createView(name, prefix, attrs);
                    if (view != null) {
                        return view;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.createView(name,"",attrs);
    }

    public LayoutInflater cloneInContext(Context newContext) {
        return new SkinLayoutInflater(this, newContext);
    }
}
