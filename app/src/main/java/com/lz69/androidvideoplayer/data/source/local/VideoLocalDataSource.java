package com.lz69.androidvideoplayer.data.source.local;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.lz69.androidvideoplayer.data.Video;
import com.lz69.androidvideoplayer.data.source.VideoDataSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoLocalDataSource implements VideoDataSource{

    private static VideoLocalDataSource INSTANCE = null;

    private Context mContext;

    private VideoLocalDataSource(Context context){
        mContext = context;
    }

    public static VideoLocalDataSource getInstance(Context context) {
        if (INSTANCE == null)
            INSTANCE = new VideoLocalDataSource(context);
        return INSTANCE;
    }

    @Override
    public void loadVideos(LoadVideosCallback loadVideosCallback) {
//        List<Video> videos = new ArrayList<>();
//        for(int i = 0; i < 10; i++) {
//            Video video = new Video();
//            video.setId(i);
//            video.setName("测试" + i);
//            videos.add(video);
//        }
        List<Video> videos = null;// 视频信息集合
        videos = new ArrayList<>();
        getVideoFile(videos, Environment.getExternalStorageDirectory());
        loadVideosCallback.onVideosLoaded(videos);
    }

    private void getVideoFile(final List<Video> list, File file) {// 获得视频文件

        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };

        // MediaStore.Video.Media.DATA：视频文件路径；
        // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
        // MediaStore.Video.Media.TITLE: 视频标题 : testVideo
        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME };

        Cursor cursor = mContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);

        if(cursor==null){
            Toast.makeText(mContext, "没有找到可播放视频文件", Toast.LENGTH_LONG).show();
            return;
        }
        if (cursor.moveToFirst()) {
            do {
                Video video = new Video();
                int id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                Cursor thumbCursor =  mContext.getContentResolver().query(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + id, null, null);
//                if (thumbCursor.moveToFirst()) {
//                    info.setThumbPath(thumbCursor.getString(thumbCursor
//                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
//                }
                video.setPath(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
//                info.setTitle(cursor.getString(cursor
//                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));

                video.setName(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
//                LogUtil.log(TAG, "DisplayName:"+info.getDisplayName());
//                info.setMimeType(cursor
//                        .getString(cursor
//                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));

                list.add(video);
            } while (cursor.moveToNext());
        }
    }
}
