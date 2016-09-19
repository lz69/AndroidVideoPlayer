package com.lz69.androidvideoplayer.data.source.local;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.lz69.androidvideoplayer.data.Video;
import com.lz69.androidvideoplayer.data.source.VideoDataSource;
import com.lz69.androidvideoplayer.utils.MD5Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public void getVideos(final LoadVideosCallback loadVideosCallback) {
        getVideoFile(loadVideosCallback, Environment.getExternalStorageDirectory());
    }

    private void getVideoFile(LoadVideosCallback loadVideosCallback, File file) {// 获得视频文件

        List<Video> list = new ArrayList<>();
        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };

        // MediaStore.Video.Media.DATA：视频文件路径；
        // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
        // MediaStore.Video.Media.TITLE: 视频标题 : testVideo
        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION };

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
//                Cursor thumbCursor =  mContext.getContentResolver().query(
//                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
//                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
//                                + "=" + id, null, null);
//                if (thumbCursor.moveToFirst()) {
//                    video.setThumbPath(thumbCursor.getString(thumbCursor
//                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
//                }
                video.setId(id);

                video.setPath(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                video.setTitle(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
                video.setName(cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                video.setMimeType(cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
                video.setDuration(cursor.getLong(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));

                //生称缩略图
                video.setThumbPath(getVideoThumbnail(video.getPath()));
                list.add(video);
            } while (cursor.moveToNext());
        }
        loadVideosCallback.onVideosLoaded(list);
    }

    private String getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        }
        catch(IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            try {
                retriever.release();
            }
            catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return saveBitmap(bitmap, filePath);
    }

    public String saveBitmap(Bitmap bm, String picName) {
        File f = new File(mContext.getCacheDir(), MD5Utils.getMD5(picName));
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return f.getAbsolutePath();
    }
}
