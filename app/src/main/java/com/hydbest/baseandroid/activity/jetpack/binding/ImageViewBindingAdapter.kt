package com.hydbest.baseandroid.activity.jetpack.binding

import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.setPadding
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hydbest.baseandroid.R

class ImageViewBindingAdapter {

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

        @JvmStatic
        @BindingAdapter("image")
        fun setImage(imageView: ImageView, resId: Int) {
            imageView.setBackgroundResource(resId)
        }

        /**
         * 多参数重载
         */
        @JvmStatic
        @BindingAdapter(value = ["image_","res_"], requireAll = false)
        fun setMultiImage(imageView: ImageView, url: String?, resId: Int) {
            if (!TextUtils.isEmpty(url)) {
                Glide.with(imageView).load(url).into(imageView)
            } else {
                imageView.setBackgroundResource(resId)
            }
        }

        @JvmStatic
        @BindingAdapter("alpha")
        fun alpha(imageView: ImageView, alpha: Int) {
            imageView.drawable?.alpha = alpha
        }

        /**
         * @param oldPadding xml原来的值
         * @param newPadding xml新的值
         */
        @JvmStatic
        @BindingAdapter("pading")
        fun padding(imageView: ImageView,oldPadding: Int, newPadding: Int) {
            Log.i("csz","oldPadding   " +oldPadding   +"    newPadding   "  +newPadding+"    " + imageView.paddingLeft);
            imageView.setPadding(newPadding)
        }

    }

}