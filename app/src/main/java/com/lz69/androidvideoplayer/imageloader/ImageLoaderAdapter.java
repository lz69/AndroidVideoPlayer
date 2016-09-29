package com.lz69.androidvideoplayer.imageloader;

import android.content.Context;
import android.widget.ImageView;

/**
 * Created by lz69 on 2016/9/28.
 */

public abstract class ImageLoaderAdapter {
    public abstract void loadImageFromFile(String picPath, ImageView imageView);
}
