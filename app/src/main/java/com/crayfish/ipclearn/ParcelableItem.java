package com.crayfish.ipclearn;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ============================
 * 作    者：crayfish(徐杰)
 * 创建日期：2017/12/13.
 * 描    述：
 * 修改历史：
 * ===========================
 */

public class ParcelableItem implements Parcelable {

    public int id;
    public String name;

    public ParcelableItem(int id,String name){
        this.id = id;
        this.name = name;
    }

    protected ParcelableItem(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ParcelableItem> CREATOR = new Creator<ParcelableItem>() {
        @Override
        public ParcelableItem createFromParcel(Parcel in) {
            return new ParcelableItem(in);
        }

        @Override
        public ParcelableItem[] newArray(int size) {
            return new ParcelableItem[size];
        }
    };
}
