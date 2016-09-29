package com.lz69.androidvideoplayer.imageloader;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by lz69 on 2016/9/28.
 */

public class PicassoImageLoaderAdapter extends ImageLoaderAdapter {

    private Context mContext;

    public PicassoImageLoaderAdapter(Context context) {
        mContext = context;
    }

    @Override
    public void loadImageFromFile(String picPath, ImageView imageView) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int width = params.width;
        int height = params.height;
        Picasso.with(mContext).load(new File(picPath)).centerCrop().resize(width, height).into(imageView);
    }

}
