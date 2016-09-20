package com.lz69.androidvideoplayer.playlist;


import android.os.Handler;

import com.lz69.androidvideoplayer.data.Video;
import com.lz69.androidvideoplayer.data.source.VideoDataSource;
import com.lz69.androidvideoplayer.data.source.VideoRepository;
import com.lz69.androidvideoplayer.data.source.local.VideoLocalDataSource;
import com.lz69.androidvideoplayer.data.source.remote.VideoRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lz69 on 2016/9/5.
 */
public class PlayListPresenter implements PlayListContract.Presenter {

    private PlayListContract.View mPlayListView;

    private final VideoRepository mVideoRepository;

    public PlayListPresenter(VideoRepository videoRepository, PlayListContract.View playListView) {
        mPlayListView = playListView;
        mPlayListView.setPresenter(this);
        mVideoRepository = videoRepository;
    }

    @Override
    public void start() {
        getVideos(true);
    }

    @Override
    public void getVideos(final Boolean forceUpdate) {
        mPlayListView.showRefresh();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (forceUpdate) {
                    mVideoRepository.refreshVideos();
                }
                mVideoRepository.getVideos(new VideoDataSource.LoadVideosCallback() {
                    @Override
                    public void onVideosLoaded(List<Video> videos) {
                        mPlayListView.showPlayList(videos);
                    }

                    @Override
                    public void onDataNotAvailable(String tip) {
                        mPlayListView.showNoDataNotAvailable(tip);
                    }
                });
            }
        }).start();
    }
}
