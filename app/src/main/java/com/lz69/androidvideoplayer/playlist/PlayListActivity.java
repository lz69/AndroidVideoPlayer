package com.lz69.androidvideoplayer.playlist;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lz69.androidvideoplayer.R;
import com.lz69.androidvideoplayer.base.BaseActivity;
import com.lz69.androidvideoplayer.data.Video;
import com.lz69.androidvideoplayer.data.source.VideoRepository;
import com.lz69.androidvideoplayer.data.source.local.VideoLocalDataSource;
import com.lz69.androidvideoplayer.data.source.remote.VideoRemoteDataSource;
import com.lz69.androidvideoplayer.imageloader.ImageLoader;
import com.lz69.androidvideoplayer.utils.TimeUtils;
import com.lz69.androidvideoplayer.videoplayer.VideoPlayerActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

public class PlayListActivity extends BaseActivity implements PlayListContract.View, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView rvPlayList;

    private PlayListContract.Presenter mPresenter;

    private PlayListAdapter mPlayListAdapter;

    private SwipeRefreshLayout srlRefresh;

    private final String TAG = "lz69";

    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        initViews();
        mPresenter = new PlayListPresenter(VideoRepository.getInstance(VideoLocalDataSource.getInstance(this),
                VideoRemoteDataSource.getInstance(this)), this);
        requestSomePermissin();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requestSomePermissin() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                );

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            mPresenter.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mPresenter.start();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void initViews() {
        rvPlayList = (RecyclerView) findViewById(R.id.rvPlayList);
        srlRefresh = (SwipeRefreshLayout) findViewById(R.id.srlRefresh);
        srlRefresh.setOnRefreshListener(this);

        rvPlayList.setLayoutManager(new LinearLayoutManager(this));
        rvPlayList.setItemAnimator(new DefaultItemAnimator());
        //设置固定大小
        rvPlayList.setHasFixedSize(true);
        rvPlayList.addItemDecoration(new ItemDivider(this, R.drawable.shape_divider_playlist));
    }

    @Override
    public void setPresenter(PlayListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showNoDataNotAvailable(String tip) {
        Toast.makeText(this, tip, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPlayList(List<Video> videos) {
        Message msg = new Message();
        msg.obj = videos;
        handler.sendMessage(msg);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<Video> videos = (List<Video>) msg.obj;
            // 初始化自定义的适配器
            mPlayListAdapter = new PlayListAdapter(PlayListActivity.this, videos);
            // 为mRecyclerView设置适配器
            rvPlayList.setAdapter(mPlayListAdapter);
            srlRefresh.setRefreshing(false);
            hideRefresh();
        }
    };

    @Override
    public void showRefresh() {
        srlRefresh.setRefreshing(true);
    }

    @Override
    public void hideRefresh() {
        srlRefresh.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.getVideos(false);
    }

    class ItemDivider extends RecyclerView.ItemDecoration {

        private Drawable mDrawable;

        public ItemDivider(Context context, int resId) {
            mDrawable = context.getResources().getDrawable(resId);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDrawOver(c, parent, state);
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                //以下计算主要用来确定绘制的位置
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDrawable.getIntrinsicHeight();
                mDrawable.setBounds(left, top, right, bottom);
                mDrawable.draw(c);
            }
        }

    }

    class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder> {

        private List<Video> videos;

        private Context mContext;

        public PlayListAdapter(Context context, List<Video> videos) {
            mContext = context;
            this.videos = videos;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_playlist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Video video = videos.get(position);
            holder.tvName.setText(video.getName());
            if(video.getThumbPath() != null)
                ImageLoader.getInstance(mContext).loadImageFromFile(video.getThumbPath(), holder.ivThumb);
            holder.tvDuration.setText(TimeUtils.getHMS(video.getDuration()));
        }

        @Override
        public int getItemCount() {
            return videos == null ? 0 : videos.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            public TextView tvName;

            public ImageView ivThumb;

            public TextView tvDuration;

            public ViewHolder(View itemView) {
                super(itemView);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                ivThumb = (ImageView) itemView.findViewById(R.id.ivThumb);
                tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                        intent.putExtra(VideoPlayerActivity.PLAY_VIDEO, videos.get(getLayoutPosition()));
                        startActivity(intent);
                    }
                });
                itemView.setBackgroundResource(R.drawable.recycler_bg);
            }
        }
    }
}
