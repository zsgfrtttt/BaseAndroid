package com.hydbest.baseandroid.activity.anim.unsplash

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.SharedElementCallback
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hydbest.baseandroid.R
import com.hydbest.baseandroid.databinding.PhotoItemBinding
import com.hydbest.baseandroid.view.decoration.GridMarginDecoration
import retrofit.Callback
import retrofit.RestAdapter
import retrofit.RetrofitError
import retrofit.client.Response
import java.util.*

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class UnSplashActivity : AppCompatActivity() {

    val PHOTO_COUNT = 12
    val TAG = "csz"

    private val grid: RecyclerView by lazy { findViewById<RecyclerView>(R.id.image_grid)  }
    private val empty: ProgressBar by lazy { findViewById<ProgressBar>(android.R.id.empty) }
    private var relevantPhotos: ArrayList<Photo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unsplash)

        Log.i("csz","onCreate")
        postponeEnterTransition()
        window.sharedElementExitTransition.addListener(object :TransitionCallback(){
            override fun onTransitionEnd(transition: Transition?) {
                val callback: SharedElementCallback? = null
                //需要在return动画完成后置空，否则打开新的activity会动画异常
                setExitSharedElementCallback(callback)
            }
        })

        setupRecyclerView()

        if (savedInstanceState != null) {
            relevantPhotos = savedInstanceState.getParcelableArrayList(IntentUtil.RELEVANT_PHOTOS)
        }
        displayData()
    }

    private fun displayData() {
        if (relevantPhotos != null) {
            populateGrid()
        } else {
            val unsplashApi: UnsplashService = RestAdapter.Builder()
                    .setEndpoint(UnsplashService.ENDPOINT)
                    .build()
                    .create(UnsplashService::class.java)
            unsplashApi.getFeed(object : Callback<List<Photo>> {
                override fun failure(error: RetrofitError?) {
                    Log.e(TAG, "Error retrieving Unsplash feed:", error)
                }

                override fun success(photos: List<Photo>, response: Response?) {
                    // the first items not interesting to us, get the last <n>
                    relevantPhotos = ArrayList<Photo>(photos.subList(photos.size - PHOTO_COUNT, photos.size))
                    populateGrid()
                }
            })
        }
    }

    private fun populateGrid() {
        grid.adapter = PhotoAdapter(this, relevantPhotos)
        grid.addOnItemTouchListener(object : OnItemSelectedListener(this) {
            override fun onItemSelected(holder: RecyclerView.ViewHolder, position: Int) {
                if (holder !is PhotoViewHolder) {
                    return
                }
                val binding: PhotoItemBinding = (holder as PhotoViewHolder).getBinding()
                val intent: Intent = getDetailActivityStartIntent(this@UnSplashActivity,
                        relevantPhotos, position, binding)
                val activityOptions = getActivityOptions(binding)
                this@UnSplashActivity.startActivityForResult(intent, IntentUtil.REQUEST_CODE,
                        activityOptions!!.toBundle())
            }
        })
        empty.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(IntentUtil.RELEVANT_PHOTOS, relevantPhotos)
        super.onSaveInstanceState(outState)
    }

    /**
     * makeSceneTransitionAnimation跳转后再return会触发
     */
    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        Log.i("csz","onActivityReenter")
        postponeEnterTransition()
        // Start the postponed transition when the recycler view is ready to be drawn.
        grid.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                grid.viewTreeObserver.removeOnPreDrawListener(this)
                startPostponedEnterTransition()
                Log.i("csz","onPreDraw")
                return true
            }
        })
        if (data == null) {
            return
        }
        //处理ui相关属性
        val selectedItem = data.getIntExtra(IntentUtil.SELECTED_ITEM_POSITION, 0)
        grid.scrollToPosition(selectedItem)
        val holder: PhotoViewHolder? = grid.findViewHolderForAdapterPosition(selectedItem) as PhotoViewHolder?
        if (holder == null) {
            Log.w(TAG, "onActivityReenter: Holder is null, remapping cancelled.")
            return
        }
        val callback = DetailSharedElementEnterCallback(intent)
        callback.setBinding(holder.getBinding())
        setExitSharedElementCallback(callback)

    }

    private fun setupRecyclerView() {
        val gridLayoutManager = grid.layoutManager as GridLayoutManager?
        gridLayoutManager!!.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                /* emulating https://material-design.storage.googleapis.com/publish/material_v_4/material_ext_publish/0B6Okdz75tqQsck9lUkgxNVZza1U/style_imagery_integration_scale1.png */
                return when (position % 6) {
                    5 -> 3
                    3 -> 2
                    else -> 1
                }
            }
        }
        grid.addItemDecoration(GridMarginDecoration(
                resources.getDimensionPixelSize(R.dimen.grid_item_spacing)))
        grid.setHasFixedSize(true)
    }

    private fun getDetailActivityStartIntent(host: Activity, photos: ArrayList<Photo>?,
                                             position: Int, binding: PhotoItemBinding): Intent {
        val intent = Intent(host, DetailActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        intent.putParcelableArrayListExtra(IntentUtil.PHOTO, photos)
        intent.putExtra(IntentUtil.SELECTED_ITEM_POSITION, position)
        intent.putExtra(IntentUtil.FONT_SIZE, binding.author.getTextSize())
        intent.putExtra(IntentUtil.PADDING,
                Rect(binding.author.getPaddingLeft(),
                        binding.author.getPaddingTop(),
                        binding.author.getPaddingRight(),
                        binding.author.getPaddingBottom()))
        intent.putExtra(IntentUtil.TEXT_COLOR, binding.author.getCurrentTextColor())
        return intent
    }

    private fun getActivityOptions(binding: PhotoItemBinding): ActivityOptions? {
        val authorPair:Pair<View,String>  = Pair.create(binding.author, binding.author.getTransitionName())
        val photoPair:Pair<View,String>  = Pair.create(binding.photo, binding.photo.getTransitionName())
        val decorView = window.decorView
        val statusBackground = decorView.findViewById<View>(android.R.id.statusBarBackground)
        val navBackground = decorView.findViewById<View>(android.R.id.navigationBarBackground)
        val statusPair = Pair.create(statusBackground,
                statusBackground.transitionName)
        val options: ActivityOptions
        //避免状态栏或者导航栏闪烁
        options = if (navBackground == null) {
            ActivityOptions.makeSceneTransitionAnimation(this, authorPair, photoPair, statusPair)
        } else {
            val navPair = Pair.create(navBackground, navBackground.transitionName)
            ActivityOptions.makeSceneTransitionAnimation(this, authorPair, photoPair, statusPair, navPair)
        }
        return options
    }
}