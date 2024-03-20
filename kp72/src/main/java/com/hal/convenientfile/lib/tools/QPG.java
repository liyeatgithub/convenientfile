package com.hal.convenientfile.lib.tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.hal.convenientfile.lib.component.NIdd;
import com.hal.convenientfile.lib.component.Uioi;
import com.hal.convenientfile.lib.component.Moodn;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPG implements QPJ.AbstractC0188a{

    /* compiled from: AegisUtils */
    public static class a implements ServiceConnection {
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    @Override
    public boolean a(Context context, String str) {
        Class cls = null;
        try {
            QPJ a2 = QOV.a();
            cls = getClassBasedOnString(str, a2);
            if (cls != null) {
                a(context, cls);
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private Class<?> getClassBasedOnString(String str, QPJ a2) {
        if (str.equals(a2.f27345b)) {
            return NIdd.class;
        } else if (str.equals(a2.f27346c)) {
            return Uioi.class;
        } else if (str.equals(a2.f27347d)) {
            return Moodn.class;
        }
        return null;
    }


    public static void a(Context context, Class<?> cls) {
        try {
            context.startService(new Intent(context, cls));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            a(context, null, cls);
        } catch (Exception e3) {
            e3.printStackTrace();
        }
    }

    public static void a(Context context, Intent intent, Class<?> cls) {
        if (cls != null) {
            if (intent == null) {
                intent = new Intent(context, cls);
            } else {
                intent.setClass(context, cls);
            }
            context.bindService(intent, new a(), Context.BIND_AUTO_CREATE);
        }
    }
}
