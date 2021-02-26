package com.hydbest.baseandroid.activity.jetpack.binding

import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hydbest.baseandroid.R

class RecycleViewBindingAdapter {

    companion object {

        @JvmStatic
        @BindingAdapter("image")
        fun setImage(imageView: ImageView, url: String) {
            if (TextUtils.isEmpty(url)) {
                imageView.setBackgroundColor(imageView.resources.getColor(R.color.blue))
            } else {
                Glide.with(imageView).load(url).into(imageView)
            }
        }

    }

}