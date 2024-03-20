package com.hal.convenientfile.lib.entity;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPS implements Parcelable {
    public static final Parcelable.Creator<QPS> QPP = new QPP();

    public String[] pathArr;

    public String processName;

    public Intent serviceIntent;

    public Intent dReceiverIntent;

    public Intent instruIntent;

    public QPS() {
    }

    public static final Creator<QPS> CREATOR = new Creator<QPS>() {
        @Override
        public QPS createFromParcel(Parcel in) {
            return new QPS(in);
        }

        @Override
        public QPS[] newArray(int size) {
            return new QPS[size];
        }
    };

    public static QPS createParcel(String str) {
        Parcel obtain = Parcel.obtain();
        byte[] decode = Base64.decode(str, 2);
        obtain.unmarshall(decode, 0, decode.length);
        obtain.setDataPosition(0);
        QPS createFromParcel = QPP.createFromParcel(obtain);
        obtain.recycle();
        return createFromParcel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        Parcel obtain = Parcel.obtain();
        writeToParcel(obtain, 0);
        String encodeToString = Base64.encodeToString(obtain.marshall(), 2);
        obtain.recycle();
        return encodeToString;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i2) {
        parcel.writeStringArray(this.pathArr);
        parcel.writeString(this.processName);
        writeIntentToParcel(parcel, this.serviceIntent);
        writeIntentToParcel(parcel, this.dReceiverIntent);
        writeIntentToParcel(parcel, this.instruIntent);
    }

    private void writeIntentToParcel(Parcel parcel, Intent intent) {
        if (intent == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            intent.writeToParcel(parcel, 0);
        }
    }


    public QPS(Parcel parcel) {
        this.pathArr = parcel.createStringArray();
        this.processName = parcel.readString();
        if (parcel.readInt() != 0) {
            this.serviceIntent = (Intent) Intent.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.dReceiverIntent = (Intent) Intent.CREATOR.createFromParcel(parcel);
        }
        if (parcel.readInt() != 0) {
            this.instruIntent = (Intent) Intent.CREATOR.createFromParcel(parcel);
        }
    }
}
