package com.example.e1.a4galeryglide;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ViewPhoto implements Parcelable {

    private String mUrl;
    private String mTitle;

    public ViewPhoto(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected ViewPhoto(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<ViewPhoto> CREATOR = new Creator<ViewPhoto>() {
        @Override
        public ViewPhoto createFromParcel(Parcel in) {
            return new ViewPhoto(in);
        }

        @Override
        public ViewPhoto[] newArray(int size) {
            return new ViewPhoto[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    // получаем список файлов в альбоме
    public static ArrayList<ViewPhoto> getViewPhotos(String pathName) {

        ArrayList<ViewPhoto> sp = new ArrayList<>();
        File file = new File(pathName);
        File[] files = file.listFiles();
        if(files != null) {
            for(File f : files) {
                sp.add(new ViewPhoto(f.getAbsolutePath(), f.getName()));
            }
        }
        return sp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }

}