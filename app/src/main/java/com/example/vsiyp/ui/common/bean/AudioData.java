package com.example.vsiyp.ui.common.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class AudioData implements Parcelable {
    private String picture = "";

    private String name = "";

    private String singer = "";

    private String downloadPath = "";

    private int isFavorite = 0;

    private String path = "";

    private long size;

    private long addTime;

    private long duration;

    private boolean isPlaying = false;

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public long getDuration() {
        return duration;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isFavorite() {
        return isFavorite == 1;
    }

    public void setFavorite(int favorite) {
        isFavorite = favorite;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getAddTime() {
        return addTime;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public static Creator<AudioData> getCREATOR() {
        return CREATOR;
    }

    @Override
    public String toString() {
        return "AudioData{" + "picture='" + picture + '\'' + ", name='" + name + '\'' + ", singer='" + singer + '\''
                + ", downloadPath='" + downloadPath + '\'' + ", isFavorite=" + isFavorite + ", path='" + path + '\''
                + ", size=" + size + ", addTime=" + addTime + ", duration=" + duration + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AudioData audioData = (AudioData) o;
        return isFavorite == audioData.isFavorite && size == audioData.size && addTime == audioData.addTime
                && duration == audioData.duration && picture.equals(audioData.picture) && name.equals(audioData.name)
                && singer.equals(audioData.singer) && downloadPath.equals(audioData.downloadPath)
                && path.equals(audioData.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(picture, name, singer, downloadPath, isFavorite, path, size, addTime, duration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.picture);
        dest.writeString(this.name);
        dest.writeString(this.singer);
        dest.writeString(this.downloadPath);
        dest.writeInt(this.isFavorite);
        dest.writeString(this.path);
        dest.writeLong(this.size);
        dest.writeLong(this.addTime);
        dest.writeLong(this.duration);
    }

    public AudioData() {
    }

    protected AudioData(Parcel in) {
        this.picture = in.readString();
        this.name = in.readString();
        this.singer = in.readString();
        this.downloadPath = in.readString();
        this.isFavorite = in.readInt();
        this.path = in.readString();
        this.size = in.readLong();
        this.addTime = in.readLong();
        this.duration = in.readLong();
    }

    public static final Creator<AudioData> CREATOR = new Creator<AudioData>() {
        @Override
        public AudioData createFromParcel(Parcel source) {
            return new AudioData(source);
        }

        @Override
        public AudioData[] newArray(int size) {
            return new AudioData[size];
        }
    };
}

