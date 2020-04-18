package com.nitesh.marsplay.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ImagesUploaded implements Parcelable {

    @SerializedName("resources")
    @Expose
    private List<Resource> resources = null;

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.resources);
    }

    public ImagesUploaded() {
    }

    protected ImagesUploaded(Parcel in) {
        this.resources = new ArrayList<Resource>();
        in.readList(this.resources, Resource.class.getClassLoader());
    }

    public static final Parcelable.Creator<ImagesUploaded> CREATOR = new Parcelable.Creator<ImagesUploaded>() {
        @Override
        public ImagesUploaded createFromParcel(Parcel source) {
            return new ImagesUploaded(source);
        }

        @Override
        public ImagesUploaded[] newArray(int size) {
            return new ImagesUploaded[size];
        }
    };
}