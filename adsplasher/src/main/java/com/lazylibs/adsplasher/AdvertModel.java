package com.lazylibs.adsplasher;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

public class AdvertModel implements Parcelable {
    /**
     * id : -3.4303179225423984E7
     * title : et
     * href : deserunt consectetur
     * imageUrl : pariatur dolor ea aliqua
     * target : -4.512969719369653E7
     * interval : 6.888892027516964E7
     */

    @JSONField(name = "id")
    public String id;
    @JSONField(name = "title")
    public String title;
    @JSONField(name = "href")
    public String href;
    @JSONField(name = "imageUrl")
    public String imageUrl;
    @JSONField(name = "target")
    public int target;
    @JSONField(name = "interval")
    public int interval;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.href);
        dest.writeString(this.imageUrl);
        dest.writeInt(this.target);
        dest.writeInt(this.interval);
    }

    public AdvertModel() {
    }

    protected AdvertModel(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.href = in.readString();
        this.imageUrl = in.readString();
        this.target = in.readInt();
        this.interval = in.readInt();
    }

    public static final Creator<AdvertModel> CREATOR = new Creator<AdvertModel>() {
        @Override
        public AdvertModel createFromParcel(Parcel source) {
            return new AdvertModel(source);
        }

        @Override
        public AdvertModel[] newArray(int size) {
            return new AdvertModel[size];
        }
    };
}