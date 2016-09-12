package com.lz69.androidvideoplayer.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable{

    private int id;

    private String name;

    private String path;

    private String thumbPath;

    private String title;

    private String mimeType;

    private Long duration;

    public Video(){}

    protected Video(Parcel in) {
        id = in.readInt();
        name = in.readString();
        path = in.readString();
        thumbPath = in.readString();
        title = in.readString();
        mimeType = in.readString();
        duration = in.readLong();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(path);
        parcel.writeString(thumbPath);
        parcel.writeString(title);
        parcel.writeString(mimeType);
        parcel.writeLong(duration);
    }
}
