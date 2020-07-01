package com.hydbest.baseandroid.adapter;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hydbest.baseandroid.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ImageWatcherAdapter extends BaseQuickAdapter<Uri, BaseViewHolder> {

    public ImageWatcherAdapter(@Nullable List<Uri> data) {
        super(R.layout.item_img_watcher, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Uri item) {
        //helper.setImageResource(R.id.iv,item);
        ImageView iv = helper.getView(R.id.iv);
        addChildClickViewIds(R.id.iv);
        addChildLongClickViewIds(R.id.iv);
        Glide.with(iv).asBitmap().load(item).into(iv);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

            }
        });
    }



    public static Uri id2Uri(Resources resources, int id) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id));
    }

    public static List<Uri> idArray2UriList(Resources resources, Integer[] ids) {
        List<Integer> ints = Arrays.asList(ids);
        List<Uri> uris=new ArrayList<>();
        for (int i=0;i<ints.size();i++){
            uris.add(id2Uri(resources,ints.get(i)));
        }
        return uris;
    }
}
