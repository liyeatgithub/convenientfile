package com.hal.convenientfile.lib.component;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import com.hal.convenientfile.lib.tools.QQB;
import com.hal.convenientfile.lib.tools.QQC;
import com.hal.convenientfile.lib.tools.QQJ;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPK extends BroadcastReceiver {

    /* renamed from: a  reason: collision with root package name */
    public static QPK f12223a;

    public class a implements ServiceConnection {
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
        }
    }

    public static synchronized void a(Context context) {
        synchronized (QPK.class) {
            synchronized (QPK.class) {
                if (f12223a == null) {
                    f12223a = new QPK();
                    IntentFilter intentFilter = new IntentFilter("com.android.jikealiveintent.action.SERVICE_START");
                    intentFilter.setPriority(1000);
                    context.registerReceiver(f12223a, intentFilter);
                }
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra("service_name");
        if (!TextUtils.isEmpty(stringExtra)) {
            a1(context, stringExtra);
            //Log.e("DaemonLog", "ServiceStartReceiver bind:" + stringExtra);
        }
    }

    public static void a(Context context, String str) {
        Intent intent = new Intent();
        intent.setAction("com.android.jikealiveintent.action.SERVICE_START");
        intent.putExtra("service_name", str);
        intent.setPackage(context.getPackageName());
        context.sendBroadcast(intent);
    }

    private void a1(Context context, String str) {
        try {
            Intent intent = new Intent();
            intent.setClassName(context.getPackageName(), str);
            context.bindService(intent, new a(), 65);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /**
     * @author Create by Payne on 2021/5/26
     * Description:
     **/
    public static class QQD extends Service {

        @Override
        public IBinder onBind(Intent intent) {
            return new QQC(this);
        }

        @Override
        public void onCreate() {
            super.onCreate();
            QPI.QQL.send(this, QQD.class.getName());
            QPI.QPY.registerMainStartReceiver(this, new QQJ(this));
        }

        @Override
        public int onStartCommand(Intent intent, int i2, int i3) {
            return START_NOT_STICKY;
        }
    }

    /**
     * @author Create by Payne on 2021/5/26
     * Description:
     **/
    public static class QQA extends Service {

        @Override
        public IBinder onBind(Intent intent) {
            return new QQB(this);
        }

        @Override
        public void onCreate() {
            super.onCreate();
            QPI.QQL.send(this, QQA.class.getName());
            QPI.QPY.registerMainStartReceiver(this, new QQJ(this));
        }

        @Override
        public int onStartCommand(Intent intent, int i2, int i3) {
            return START_NOT_STICKY;
        }
    }
}
