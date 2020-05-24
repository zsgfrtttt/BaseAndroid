package com.hydbest.baseandroid.activity.cus_view.shadow.sample;

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.ViewTarget
import com.bumptech.glide.request.transition.Transition
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.activity.cus_view.shadow.sample.data.DataSource
import com.hydbest.baseandroid.activity.cus_view.shadow.sample.data.Item
import io.github.armcha.coloredshadow.ShadowImageView

/**
 * let: it访问，返回最后一行
 * with: this访问 ，返回最后一行
 * run : this访问，返回最后一行
 * apply:this访问，返回自身
 * also :it访问，返回自身
 */

class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private val items by lazy {
        DataSource.items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_shadow, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val shadowView by lazy { itemView.findViewById<ShadowImageView>(R.id.shadowView) }
        private val text by lazy { itemView.findViewById<TextView>(R.id.text) }

        fun bind(item: Item) {
            text.text = item.name
            shadowView.radiusOffset = 0.7f
            //shadowView.shadowColor = Color.GRAY
            Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .apply(RequestOptions().placeholder(R.drawable.place_holder).error(R.drawable.place_holder))
                    //.transform(CircleCrop())
                    .into(object : ViewTarget<ImageView, Drawable>(shadowView) {
                        override fun onLoadStarted(placeholder: Drawable?) {
                            super.onLoadStarted(placeholder)
                            shadowView.setImageDrawable(placeholder, withShadow = false)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            super.onLoadCleared(placeholder)
                            shadowView.setImageDrawable(placeholder, withShadow = false)
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            shadowView.setImageDrawable(errorDrawable, withShadow = false)
                        }

                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            shadowView.setImageDrawable(resource)
                        }
                    })
        }
    }
}
