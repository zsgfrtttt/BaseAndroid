package com.hydbest.baseandroid.activity.anim.scene

import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hydbest.baseandroid.R

class DetailActivity : AppCompatActivity() {

    private lateinit var mHeaderImageView: ImageView
    private lateinit var mHeaderTitle: TextView
    private lateinit var mItem: Item

    companion object {
        val VIEW_NAME_HEADER_IMAGE = "detail:header:image"
        val VIEW_NAME_HEADER_TITLE = "detail:header:title"
        val EXTRA_PARAM_ID = "detail:_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mItem = Item.getItem(intent.getLongExtra(EXTRA_PARAM_ID, 0))!!
        mHeaderImageView = findViewById(R.id.imageview_header)
        mHeaderTitle = findViewById(R.id.textview_title)

        ViewCompat.setTransitionName(mHeaderImageView, VIEW_NAME_HEADER_IMAGE)
        ViewCompat.setTransitionName(mHeaderTitle, VIEW_NAME_HEADER_TITLE)
        loadItem()
    }

    private fun loadItem() {
        loadThumbnail()
     //   loadFullSizeImage()
        // Set the title TextView to the item's name and author
        mHeaderTitle.text = getString(R.string.image_header, mItem.getName(), mItem.getAuthor())
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener()) {
            // If we're running on Lollipop and we have added a listener to the shared element
            // transition, load the thumbnail. The listener will load the full-size image when
            // the transition is complete.
            loadThumbnail()
        } else {
            // If all other cases we should just load the full-size image now
            loadFullSizeImage()
        }*/
    }

    /**
     * Load the item's thumbnail image into our [ImageView].
     */
    private fun loadThumbnail() {
        Glide.with(mHeaderImageView.context)
                .load(mItem.getThumbnailUrl())
                .apply(RequestOptions().dontAnimate())
                .into(mHeaderImageView)
    }

    /**
     * Load the item's full-size image into our [ImageView].
     */
    private fun loadFullSizeImage() {
        Glide.with(mHeaderImageView.context)
                .load(mItem.getPhotoUrl())
                .apply(RequestOptions().dontAnimate().dontTransform())
                .into(mHeaderImageView)
    }

    /**
     * Try and add a [Transition.TransitionListener] to the entering shared element
     * [Transition]. We do this so that we can load the full-size image after the transition
     * has completed.
     *
     * @return true if we were successful in adding a listener to the enter transition
     */
    @RequiresApi(21)
    private fun addTransitionListener(): Boolean {
        val transition = window.sharedElementEnterTransition
        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(object : Transition.TransitionListener {
                override fun onTransitionEnd(transition: Transition) {
                    // As the transition has ended, we can now load the full-size image
                    loadFullSizeImage()

                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this)
                }

                override fun onTransitionStart(transition: Transition) {
                    // No-op
                }

                override fun onTransitionCancel(transition: Transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this)
                }

                override fun onTransitionPause(transition: Transition) {
                    // No-op
                }

                override fun onTransitionResume(transition: Transition) {
                    // No-op
                }
            })
            return true
        }

        // If we reach here then we have not added a listener
        return false
    }
}