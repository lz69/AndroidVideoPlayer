package com.lz69.androidvideoplayer.data.source;

import com.lz69.androidvideoplayer.data.Video;
import com.lz69.androidvideoplayer.data.source.local.VideoLocalDataSource;
import com.lz69.androidvideoplayer.data.source.remote.VideoRemoteDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by lz69 on 2016/9/22.
 */
public class VideoRepositoryTest {

    private VideoRepository mVideoRepository;

    @Mock
    VideoLocalDataSource mVideoLocalDataSource;

    @Mock
    VideoRemoteDataSource mVideoRemoteDataSource;

    @Mock
    VideoDataSource.LoadVideosCallback mLoadVideosCallback;

    @Before
    public void setupVideoRepository() {
        MockitoAnnotations.initMocks(this);
        mVideoRepository = VideoRepository.getInstance(mVideoLocalDataSource, mVideoRemoteDataSource);
    }

    @Test
    public void addition_isCorrect() {
        assertEquals( 2 + 2, 4);

    }

    @Test
    public void getVideos_isCorrect() {
        List<Video> videos = new ArrayList<>();
//        mVideoRepository.getVideos(videos);
    }
}