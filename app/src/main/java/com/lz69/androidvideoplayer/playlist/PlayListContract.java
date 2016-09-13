package com.lz69.androidvideoplayer.playlist;

import com.lz69.androidvideoplayer.base.BasePresenter;
import com.lz69.androidvideoplayer.base.BaseView;
import com.lz69.androidvideoplayer.data.Video;

import java.util.List;

/**
 * Created by lz69 on 2016/9/5.
 */
public interface PlayListContract {

    interface View extends BaseView<Presenter> {

        void showNoDataNotAvailable(String tip);

        void showPlayList(List<Video> videos);

        void showRefresh();

        void hideRefresh();
    }

    interface Presenter extends BasePresenter {

    }
}
