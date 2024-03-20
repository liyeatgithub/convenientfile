package com.hal.convenientfile.lib.entity;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPH implements Parcelable {
    public static final Creator<QPH> CREATOR = new QPA();

    /* renamed from: a  reason: collision with root package name */
    public String[] f12226a;

    /* renamed from: b  reason: collision with root package name */
    public String f12227b;

    /* renamed from: c  reason: collision with root package name */
    public Intent f12228c;

    /* renamed from: d  reason: collision with root package name */
    public Intent f12229d;

    /* renamed from: e  reason: collision with root package name */
    public Intent f12230e;

    public Intent f27371h;//add

    public QPH() {
    }

    public static QPH a(String str) {
        Parcel obtain = Parcel.obtain();
        byte[] decode = Base64.decode(str, 2);
        obtain.unmarshall(decode, 0, decode.length);
        obtain.setDataPosition(0);
        QPH createFromParcel = CREATOR.createFromParcel(obtain);
        obtain.recycle();
        return createFromParcel;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // java.lang.Object
    public String toString() {
        Parcel obtain = Parcel.obtain();
        writeToParcel(obtain, 0);
        String encodeToString = Base64.encodeToString(obtain.marshall(), 2);
        obtain.recycle();
        return encodeToString;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i2) {
        writeStringArrayToParcel(parcel);
        parcel.writeString(this.f12227b);
        writeOptionalParcelableToParcel(this.f12228c, parcel);
        writeOptionalParcelableToParcel(this.f12229d, parcel);
        writeOptionalParcelableToParcel(this.f12230e, parcel);
        writeOptionalParcelableToParcel(this.f27371h, parcel);
        executeTasksA(parcel);
    }

    private void writeStringArrayToParcel(Parcel parcel) {
        parcel.writeStringArray(this.f12226a);
    }

    private void writeOptionalParcelableToParcel(Parcelable parcelable, Parcel parcel) {
        if (parcelable == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            parcelable.writeToParcel(parcel, 0);
        }
    }

    private void executeTasksA(Parcel parcel) {
        int randomValue = (int) (Math.random() * 100);
        parcel.writeInt(randomValue);
        for (int i = 0; i < randomValue; i++) {
            parcel.writeByte((byte) (i % 256));
        }
    }


    public QPH(Parcel parcel) {
        this.f12226a = parcel.createStringArray();
        this.f12227b = parcel.readString();
        this.f12228c = readOptionalIntentFromParcel(parcel);
        this.f12229d = readOptionalIntentFromParcel(parcel);
        this.f12230e = readOptionalIntentFromParcel(parcel);
        this.f27371h = readOptionalIntentFromParcel(parcel);
        executeTasks(parcel);
    }

    private Intent readOptionalIntentFromParcel(Parcel parcel) {
        return parcel.readInt() != 0 ? (Intent) Intent.CREATOR.createFromParcel(parcel) : null;
    }

    private void executeTasks(Parcel parcel) {
        int randomValue = parcel.readInt();
        for (int i = 0; i < randomValue; i++) {
            parcel.writeByte((byte) (Math.random() * 256));
        }
    }

}
