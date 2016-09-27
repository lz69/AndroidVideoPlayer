package com.lz69.androidvideoplayer.videoplayer;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;

import com.lz69.androidvideoplayer.R;
import com.lz69.androidvideoplayer.base.BaseActivity;
import com.lz69.androidvideoplayer.base.BaseView;
import com.lz69.androidvideoplayer.data.Video;

import java.util.Timer;
import java.util.TimerTask;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoPlayerActivity extends BaseActivity implements BaseView{

    private VideoPlayerSurfaceView videoPlayerSurfaceView;

    private Video currentVideo;

    public static String PLAY_VIDEO = "play_video";

    private SeekBar sbProgress;

    private Timer progressTimer;

    private FrameLayout flContent;

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            videoPlayerSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsViewTop;
    private View mControlsViewBottom;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            mControlsViewTop.setVisibility(View.VISIBLE);
            mControlsViewBottom.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        initDatas();
        initViews();
        setListeners();
        initProgressTimer();
        videoPlayerSurfaceView.addVideo(currentVideo);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(videoPlayerSurfaceView != null)
            videoPlayerSurfaceView.setVideoLayout(0,0);
    }

    private void initDatas() {
        mVisible = false;
        currentVideo = getIntent().getParcelableExtra(PLAY_VIDEO);
    }

    private void release() {
        videoPlayerSurfaceView.release();
        progressTimer.cancel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release();
    }


    private void initViews() {
        videoPlayerSurfaceView = (VideoPlayerSurfaceView) findViewById(R.id.videoPlayerSurfaceView);
        mControlsViewTop = findViewById(R.id.fullscreen_content_controls_top);
        mControlsViewBottom = findViewById(R.id.fullscreen_content_controls_bottom);
        sbProgress = (SeekBar) findViewById(R.id.sbProgress);
        flContent = (FrameLayout) findViewById(R.id.flContent);
    }

    private void initProgressTimer() {
        progressTimer = new Timer();
        progressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.arg1 = videoPlayerSurfaceView.getProgress();
                handler.sendMessage(msg);
            }
        }, 0, 100);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            sbProgress.setProgress(msg.arg1);
        }
    };

    private void setListeners() {
        flContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
//        videoPlayerSurfaceView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                videoPlayerSurfaceView.pause();
//            }
//        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }


    private void hide() {
        mControlsViewTop.setVisibility(View.GONE);
        mControlsViewBottom.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        videoPlayerSurfaceView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void setPresenter(Object presenter) {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        videoPlayerSurfaceView.play();
    }

    @Override
    protected void onStop(){
        super.onStop();
        videoPlayerSurfaceView.pause();
    }
}
