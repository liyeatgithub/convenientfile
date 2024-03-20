package com.hal.convenientfile.lib.component;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class Liuyh extends Service {

    public static class a implements ServiceConnection {
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i2, int i3) {
        a(this, NIdd.class);
        return super.onStartCommand(intent, i2, i3);
    }

    public static void a(Context context, Class<?> cls) {
        Intent serviceIntent = new Intent(context, cls);
        try {
            context.startService(serviceIntent);
            bindService(context, serviceIntent, cls);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindService(Context context, Intent intent, Class<?> cls) {
        if (cls != null) {
            intent.setClass(context, cls);
            context.bindService(intent, new a(), Context.BIND_AUTO_CREATE);
        }
    }

}
