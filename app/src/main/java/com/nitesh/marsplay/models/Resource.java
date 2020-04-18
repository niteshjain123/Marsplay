package com.nitesh.marsplay.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Resource implements Parcelable {

    @SerializedName("public_id")
    @Expose
    private String publicId;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("version")
    @Expose
    private Integer version;
    @SerializedName("resource_type")
    @Expose
    private String resourceType;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("bytes")
    @Expose
    private Integer bytes;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("access_mode")
    @Expose
    private String accessMode;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("secure_url")
    @Expose
    private String secureUrl;

    private long timeStamp;

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getBytes() {
        return bytes;
    }

    public void setBytes(Integer bytes) {
        this.bytes = bytes;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecureUrl() {
        return secureUrl;
    }

    public void setSecureUrl(String secureUrl) {
        this.secureUrl = secureUrl;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.publicId);
        dest.writeString(this.format);
        dest.writeValue(this.version);
        dest.writeString(this.resourceType);
        dest.writeString(this.type);
        dest.writeString(this.createdAt);
        dest.writeValue(this.bytes);
        dest.writeValue(this.width);
        dest.writeValue(this.height);
        dest.writeString(this.accessMode);
        dest.writeString(this.url);
        dest.writeString(this.secureUrl);
        dest.writeLong(this.timeStamp);
    }

    public Resource() {
    }

    protected Resource(Parcel in) {
        this.publicId = in.readString();
        this.format = in.readString();
        this.version = (Integer) in.readValue(Integer.class.getClassLoader());
        this.resourceType = in.readString();
        this.type = in.readString();
        this.createdAt = in.readString();
        this.bytes = (Integer) in.readValue(Integer.class.getClassLoader());
        this.width = (Integer) in.readValue(Integer.class.getClassLoader());
        this.height = (Integer) in.readValue(Integer.class.getClassLoader());
        this.accessMode = in.readString();
        this.url = in.readString();
        this.secureUrl = in.readString();
        this.timeStamp = in.readLong();
    }

    public static final Creator<Resource> CREATOR = new Creator<Resource>() {
        @Override
        public Resource createFromParcel(Parcel source) {
            return new Resource(source);
        }

        @Override
        public Resource[] newArray(int size) {
            return new Resource[size];
        }
    };
}