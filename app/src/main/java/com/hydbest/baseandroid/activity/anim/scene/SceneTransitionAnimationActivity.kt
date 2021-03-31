package com.hydbest.baseandroid.activity.anim.scene

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hydbest.baseandroid.R

class SceneTransitionAnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scene_transition_anim)

        val grid = findViewById<GridView>(R.id.grid)
        grid.onItemClickListener = mOnItemClickListener
        val adapter: GridAdapter = GridAdapter()
        grid.adapter = adapter
    }

    private val mOnItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->

        val item: Item = adapterView.getItemAtPosition(position) as Item
        val intent = Intent(this@SceneTransitionAnimationActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_PARAM_ID, item.getId())

        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this@SceneTransitionAnimationActivity,
                Pair(view.findViewById(R.id.imageview_item),
                        DetailActivity.VIEW_NAME_HEADER_IMAGE),
                Pair(view.findViewById(R.id.textview_name),
                        DetailActivity.VIEW_NAME_HEADER_TITLE))

        ActivityCompat.startActivity(this@SceneTransitionAnimationActivity, intent, activityOptions.toBundle())
    }

    /**
     * [android.widget.BaseAdapter] which displays items.
     */
    private inner class GridAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return Item.ITEMS.size
        }

        override fun getItem(position: Int): Item {
            return Item.ITEMS.get(position)
        }

        override fun getItemId(position: Int): Long {
            return getItem(position).getId()
        }

        override fun getView(position: Int, holderView: View?, viewGroup: ViewGroup): View {
            var view: View? = holderView
            view = view ?: getLayoutInflater().inflate(R.layout.grid_item, viewGroup, false)
            val item: Item = getItem(position)

            val image = view!!.findViewById<ImageView>(R.id.imageview_item)
            Glide.with(image.context).load(item.getThumbnailUrl())
                    .apply(RequestOptions().dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(image)

            val name = view.findViewById<TextView>(R.id.textview_name)
            name.setText(item.getName())
            return view
        }
    }
}