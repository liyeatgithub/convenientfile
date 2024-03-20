package com.hal.convenientfile.lib.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPA implements Parcelable.Creator<QPH> {
    @Override // android.os.Parcelable.Creator
    public QPH createFromParcel(Parcel parcel) {
        return new QPH(parcel);
    }

    @Override // android.os.Parcelable.Creator
    public QPH[] newArray(int i2) {
        return new QPH[i2];
    }
}