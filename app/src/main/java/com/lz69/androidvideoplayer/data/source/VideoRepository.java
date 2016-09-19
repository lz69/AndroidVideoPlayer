package com.lz69.androidvideoplayer.data.source;

import com.lz69.androidvideoplayer.data.Video;
import com.lz69.androidvideoplayer.data.source.local.VideoLocalDataSource;
import com.lz69.androidvideoplayer.data.source.remote.VideoRemoteDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lz69 on 2016/9/5.
 */
public class VideoRepository implements VideoDataSource{

    private static VideoRepository INSTANCE = null;

    Map<Integer, Video> mCachedVideos;

    VideoLocalDataSource mVideoLocalDataSource;

    VideoRemoteDataSource mVideoRemoteDataSource;

    private Boolean isCacheDirty = false;

    private VideoRepository(VideoLocalDataSource videoLocalDataSource,
                            VideoRemoteDataSource videoRemoteDataSource) {
        mVideoLocalDataSource = videoLocalDataSource;
        mVideoRemoteDataSource = videoRemoteDataSource;
    }

    public static VideoRepository getInstance(VideoLocalDataSource videoLocalDataSource,
                                       VideoRemoteDataSource videoRemoteDataSource) {
        if (INSTANCE == null)
            INSTANCE = new VideoRepository(videoLocalDataSource, videoRemoteDataSource);
        return INSTANCE;
    }

    @Override
    public void getVideos(final LoadVideosCallback loadVideosCallback) {
        //如果内存中有数据
        if (mCachedVideos != null && !isCacheDirty) {
            loadVideosCallback.onVideosLoaded(new ArrayList<Video>(mCachedVideos.values()));
            return;
        }
        //加载本地数据
        mVideoLocalDataSource.getVideos(new LoadVideosCallback() {
            @Override
            public void onVideosLoaded(List<Video> videos) {
                refreshCache(videos);
                loadVideosCallback.onVideosLoaded(new ArrayList<Video>(mCachedVideos.values()));

            }

            @Override
            public void onDataNotAvailable(String tip) {
                loadVideosCallback.onDataNotAvailable(tip);
            }
        });
    }

    private void refreshCache(List<Video> videos) {
        if(mCachedVideos == null)
            mCachedVideos = new LinkedHashMap<>();
        mCachedVideos.clear();
        for (Video video : videos) {
            mCachedVideos.put(video.getId(), video);
        }
        isCacheDirty = false;
    }

    public void refreshVideos() {
        isCacheDirty = true;
    }
}
