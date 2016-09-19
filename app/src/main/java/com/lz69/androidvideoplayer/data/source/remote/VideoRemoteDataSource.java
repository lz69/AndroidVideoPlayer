package com.lz69.androidvideoplayer.data.source.remote;

import android.content.Context;

import com.lz69.androidvideoplayer.data.source.VideoDataSource;

/**
 * Created by lz69 on 2016/9/5.
 */
public class VideoRemoteDataSource implements VideoDataSource {

    private static VideoRemoteDataSource INSTANCE = null;

    private Context mContext;

    private VideoRemoteDataSource(Context context){
        mContext = context;
    }

    public static VideoRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new VideoRemoteDataSource(context);
        return INSTANCE;
    }

    @Override
    public void getVideos(LoadVideosCallback loadVideosCallback) {

    }
}
