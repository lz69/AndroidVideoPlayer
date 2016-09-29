package com.lz69.androidvideoplayer.imageloader;

import android.content.Context;
import android.widget.ImageView;

public class ImageLoader {

    private static ImageLoader INSTANCE = null;

    private Context mContext = null;

    private ImageLoaderAdapter mAdapter;

    public static synchronized ImageLoader getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new ImageLoader(context);
        return INSTANCE;
    }

    public ImageLoader(Context context) {
        if(mContext == null)
            this.mContext = context;
        mAdapter = new PicassoImageLoaderAdapter(mContext);
    }

    private ImageLoader() {
    }

    public void loadImageFromFile(String picPath, ImageView imageView) {
        mAdapter.loadImageFromFile(picPath, imageView);
    }
}
