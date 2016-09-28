package com.lz69.androidvideoplayer.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class ImageLoader {

    private static ImageLoader INSTANCE = null;

    private Context mContext = null;

    public static synchronized ImageLoader getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new ImageLoader(context);
        return INSTANCE;
    }

    public ImageLoader(Context context) {
        if(mContext == null)
            this.mContext = context;
    }

    private ImageLoader() {
    }

    public void loadImageFromFile(String picPath, ImageView imageView, int bitmapWidth, int bitmapHeight) {
        Picasso.with(mContext).load(new File(picPath)).centerCrop().resize(bitmapWidth, bitmapHeight).into(imageView);
    }
}
