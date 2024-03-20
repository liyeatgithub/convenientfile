package com.hal.convenientfile.lib.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPP implements Parcelable.Creator<QPS> {
    @Override
    public QPS createFromParcel(Parcel parcel) {
        return new QPS(parcel);
    }

    @Override
    public QPS[] newArray(int i2) {
        return new QPS[i2];
    }
}