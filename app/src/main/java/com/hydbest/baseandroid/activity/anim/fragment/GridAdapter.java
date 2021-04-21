/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hydbest.baseandroid.activity.anim.fragment;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hydbest.baseandroid.R;

import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A fragment for displaying a grid of images.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ImageViewHolder> {

  @DrawableRes
  static final int[] IMAGE_DRAWABLES = {
          R.drawable.animal_2024172,
          R.drawable.beetle_562035,
          R.drawable.bug_189903,
          R.drawable.butterfly_417971,
          R.drawable.butterfly_dolls_363342,
          R.drawable.dragonfly_122787,
          R.drawable.dragonfly_274059,
          R.drawable.dragonfly_689626,
          R.drawable.grasshopper_279532,
          R.drawable.hover_fly_61682,
          R.drawable.hoverfly_546692,
          R.drawable.insect_278083,
          R.drawable.morpho_43483,
          R.drawable.nature_95365
  };


  /**
   * A listener that is attached to all ViewHolders to handle image loading events and clicks.
   */
  private interface ViewHolderListener {

    void onLoadCompleted(ImageView view, int adapterPosition);

    void onItemClicked(View view, int adapterPosition);

    boolean getBoolean();

    Fragment getFragment();
  }

  private final RequestManager requestManager;
  private final ViewHolderListener viewHolderListener;

  /**
   * Constructs a new grid adapter for the given {@link Fragment}.
   */
  public GridAdapter(Fragment fragment) {
    this.requestManager = Glide.with(fragment);
    this.viewHolderListener = new ViewHolderListenerImpl(fragment);
  }

  @NonNull
  @Override
  public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.image_card, parent, false);
    return new ImageViewHolder(view, requestManager, viewHolderListener);
  }


  @Override
  public void onBindViewHolder(ImageViewHolder holder, int position) {
    holder.onBind();
  }

  @Override
  public int getItemCount() {
    return IMAGE_DRAWABLES.length;
  }


  /**
   * Default {@link ViewHolderListener} implementation.
   */
  private static class ViewHolderListenerImpl implements ViewHolderListener {

    private Fragment fragment;
    private AtomicBoolean enterTransitionStarted;

    ViewHolderListenerImpl(Fragment fragment) {
      this.fragment = fragment;
      this.enterTransitionStarted = new AtomicBoolean();
    }

    @Override
    public void onLoadCompleted(ImageView view, int position) {
      // Call startPostponedEnterTransition only when the 'selected' image loading is completed.

    }

    /**
     * Handles a view click by setting the current position to the given {@code position} and
     * starting a {@link  ImagePagerFragment} which displays the image at the position.
     *
     * @param view the clicked {@link ImageView} (the shared element view will be re-mapped at the
     * GridFragment's SharedElementCallback)
     * @param position the selected view position
     */
    @Override
    public void onItemClicked(View view, int position) {
      // Update the position.
      FragmentTransitionActivity.Companion.setCurPosition(position);

      //排除点击的item的view的退出动画
      ((TransitionSet) fragment.getExitTransition()).excludeTarget(view, true);

      ImageView transitioningView = view.findViewById(R.id.card_image);
      Log.i("csz","transitioningView.getTransitionName() " +transitioningView.getTransitionName());
      fragment.getFragmentManager()
          .beginTransaction()
          .setReorderingAllowed(true) // Optimize for shared element transition
          .addSharedElement(transitioningView, transitioningView.getTransitionName())
          .replace(R.id.fragment_container, new ImagePagerFragment(), ImagePagerFragment.class
              .getSimpleName())
          .addToBackStack(null)
          .commit();
    }

    @Override
    public boolean getBoolean() {
      return enterTransitionStarted.getAndSet(true);
    }

    @Override
    public Fragment getFragment() {
      return fragment;
    }
  }

  /**
   * ViewHolder for the grid's images.
   */
  static class ImageViewHolder extends RecyclerView.ViewHolder implements
      View.OnClickListener {

    private final ImageView image;
    private final RequestManager requestManager;
    private final ViewHolderListener viewHolderListener;

    ImageViewHolder(View itemView, RequestManager requestManager,
        ViewHolderListener viewHolderListener) {
      super(itemView);
      this.image = itemView.findViewById(R.id.card_image);
      this.requestManager = requestManager;
      this.viewHolderListener = viewHolderListener;
      itemView.findViewById(R.id.card_view).setOnClickListener(this);
    }

    /**
     * Binds this view holder to the given adapter position.
     *
     * The binding will load the image into the image view, as well as set its transition name for
     * later.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void onBind() {
      int adapterPosition = getAdapterPosition();
      setImage(adapterPosition);
      // Set the string value of the image resource as the unique transition name for the view.
      image.setTransitionName(String.valueOf(IMAGE_DRAWABLES[adapterPosition]));

      if (FragmentTransitionActivity.Companion.getCurPosition() != adapterPosition) {
        return;
      }
      if (viewHolderListener.getBoolean()) {
        return;
      }
      viewHolderListener.getFragment().startPostponedEnterTransition();
    }

    void setImage(final int adapterPosition) {
      // Load the image with Glide to prevent OOM error when the image drawables are very large.
      requestManager
          .load(IMAGE_DRAWABLES[adapterPosition])
          .listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                        Target<Drawable> target, boolean isFirstResource) {
              viewHolderListener.onLoadCompleted(image, adapterPosition);
              return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable>
                target, DataSource dataSource, boolean isFirstResource) {
              viewHolderListener.onLoadCompleted(image, adapterPosition);
              return false;
            }
          })
          .into(image);
    }

    @Override
    public void onClick(View view) {
      // Let the listener start the ImagePagerFragment.
      viewHolderListener.onItemClicked(view, getAdapterPosition());
    }
  }

}