package com.lz69.androidvideoplayer.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lz69.androidvideoplayer.data.Video;

public class VideoPlayerSurfaceView extends SurfaceView implements SurfaceHolder.Callback, MediaPlayer.OnVideoSizeChangedListener {

    private MediaPlayer mMediaPlayer;

    private Video currentVideo;

    private int mVideoWidth;

    private int mVideoHeight;

    private int mSurfaceViewWidth;

    private int mSurfaceViewHeight;

    private Activity mActivty;

    private float mVideoAspectRatio = 16/9;

    public VideoPlayerSurfaceView(Context context) {
        super(context);
        init();
    }

    public VideoPlayerSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VideoPlayerSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init() {
        mActivty = (Activity)getContext();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnVideoSizeChangedListener(this);
        getHolder().setKeepScreenOn(true);
        getHolder().addCallback(this);
    }

    public void addVideo(Video currentVideo) {
        this.currentVideo = currentVideo;
    }

    private void start() {
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(this.currentVideo.getPath());
            mMediaPlayer.setDisplay(getHolder());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "该视频无法播放", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        setVideoLayout(0, 0);
    }

    public void setVideoLayout(int layout, float aspectRatio) {
        //获取视频的宽和高
        mVideoWidth = mMediaPlayer.getVideoWidth();
        mVideoHeight = mMediaPlayer.getVideoHeight();

        ViewGroup.LayoutParams lp = getLayoutParams();
        DisplayMetrics disp = getContext().getResources().getDisplayMetrics();
        //获取窗口的宽度，高度个，宽高比
        int windowWidth = disp.widthPixels;
        int windowHeight = disp.heightPixels;
        float windowRatio = windowWidth / (float) windowHeight;

        //获取视频的宽高比
        mVideoAspectRatio = mVideoWidth / (float) mVideoHeight;

        //设置变化后视频的宽高比
        float videoRatio = aspectRatio <= 0.01f ? mVideoAspectRatio : aspectRatio;

        //如果当前是横屏
        if(mActivty.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mSurfaceViewHeight = windowWidth;
            mSurfaceViewWidth = (int)(mSurfaceViewHeight * videoRatio);
        } else {
            mSurfaceViewHeight = windowHeight;
            mSurfaceViewWidth = windowWidth;
        }
        lp.width = mSurfaceViewWidth;
        lp.height = mSurfaceViewHeight;
        setLayoutParams(lp);
        getHolder().setFixedSize(mSurfaceViewHeight, mSurfaceViewWidth);
    }

    public void release() {
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }

    public void pause() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    public void play() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }
}
