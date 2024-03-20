package com.hal.convenientfile.lib.component;

import android.app.Application;
import android.app.Instrumentation;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import com.hal.convenientfile.lib.tools.QQH;
import com.hal.convenientfile.lib.tools.QQJ;
import com.hal.convenientfile.lib.tools.QQM;

/**
 * @author Create by Payne on 2021/5/26
 * Description:
 **/
public class QPI extends BroadcastReceiver {

    /* renamed from: a  reason: collision with root package name */
    public static QPI f12221a;

    /* renamed from: b  reason: collision with root package name */
    public a f12222b;

    public interface a {
        void a(Context context);
    }

    public static void a(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction("com.android.redditDeadIntent.MAIN_PROCESS_START");
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent);
        } catch (Exception unused) {
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        a aVar = this.f12222b;
        if (aVar != null) {
            aVar.a(context);
        }
    }

    public static synchronized void a(Context context, a aVar) {
        synchronized (QPI.class) {
            synchronized (QPI.class) {
                if (f12221a == null) {
                    f12221a = new QPI();
                    f12221a.f12222b = aVar;
                    IntentFilter intentFilter = new IntentFilter("com.android.redditDeadIntent.MAIN_PROCESS_START");
                    intentFilter.setPriority(1000);
                    context.registerReceiver(f12221a, intentFilter);
                }
            }
        }
    }

    /**
     * @author Create by Payne on 2021/5/26
     * Description:
     **/
    public static class QPR extends Instrumentation {

        @Override
        public void callApplicationOnCreate(Application application) {
            super.callApplicationOnCreate(application);
            ////Log.e("DaemonLog", "MyInstrumentation callApplicationOnCreate");
        }

        @Override
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            ////Log.e("DaemonLog", "MyInstrumentation onCreate");
            QQM.startService(getTargetContext(), QPK.QQD.class);
        }
    }

    /**
     * @author Create by Payne on 2021/5/26
     * Description:
     **/
    public static class QPY extends BroadcastReceiver {

        public static QPY receiver;

        public OnMainProcessStartCallback callback;

        public interface OnMainProcessStartCallback {
            void onMainStart(Context context);
        }

        public static void send(Context context) {
            try {
                Intent intent = new Intent();
                intent.setAction("com.android.kachem.action.MAIN_PROCESS_START");
                intent.setPackage(context.getPackageName());
                context.sendBroadcast(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (callback != null) {
                callback.onMainStart(context);
            }
        }

        public static synchronized void registerMainStartReceiver(Context context, OnMainProcessStartCallback aVar) {
            synchronized (QPY.class) {
                synchronized (QPY.class) {
                    if (receiver == null) {
                        receiver = new QPY();
                        receiver.callback = aVar;
                        IntentFilter intentFilter = new IntentFilter("com.android.kachem.action.MAIN_PROCESS_START");
                        intentFilter.setPriority(1000);
                        context.registerReceiver(receiver, intentFilter);
                    }
                }
            }
        }
    }

    /**
     * @author Create by Payne on 2021/5/26
     * Description:
     **/
    public static class QPZ extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            QQM.startService(context.getApplicationContext(), QPK.QQD.class);
        }
    }

    /**
     * @author Create by Payne on 2021/5/26
     * Description:
     **/
    public static class QQF extends Service {

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int i2, int i3) {
            QQM.startService(this, QPK.QQD.class);
            return super.onStartCommand(intent, i2, i3);
        }
    }

    /**
     * @author Create by Payne on 2021/5/26
     * Description:
     **/
    public static class QQG extends Service {

        @Override
        public IBinder onBind(Intent intent) {
            return new QQH(this);
        }

        @Override
        public void onCreate() {
            super.onCreate();
            QQL.send(this, QQG.class.getName());
            QPY.registerMainStartReceiver(this, new QQJ(this));
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
    public static class QQL extends BroadcastReceiver {

        public static QQL receiver;

        public static synchronized void register(Context context) {
            synchronized (QQL.class) {
                synchronized (QQL.class) {
                    if (receiver == null) {
                        receiver = new QQL();
                        IntentFilter intentFilter = new IntentFilter("com.android.kachem.action.SERVICE_START");
                        intentFilter.setPriority(1000);
                        context.registerReceiver(receiver, intentFilter);
                    }
                }
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String stringExtra = intent.getStringExtra("service_class");
            if (!TextUtils.isEmpty(stringExtra)) {
                QQM.bindService(context, stringExtra);
                ////Log.e("DaemonLog", "QQL bind:" + stringExtra);
            }
        }

        public static void send(Context context, String str) {
            Intent intent = new Intent();
            intent.setAction("com.android.kachem.action.SERVICE_START");
            intent.putExtra("service_class", str);
            intent.setPackage(context.getPackageName());
            context.sendBroadcast(intent);
        }
    }
}