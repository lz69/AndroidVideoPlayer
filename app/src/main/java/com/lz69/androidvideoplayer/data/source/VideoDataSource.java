package com.lz69.androidvideoplayer.data.source;

import com.lz69.androidvideoplayer.data.Video;

import java.util.List;

public interface VideoDataSource {

    interface LoadVideosCallback {

        void onVideosLoaded(List<Video> videos);

        void onDataNotAvailable(String tip);

    }

    void loadVideos(LoadVideosCallback loadVideosCallback);
}
